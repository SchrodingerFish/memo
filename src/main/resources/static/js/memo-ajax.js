// AJAX 表单处理，防止页面刷新影响音乐播放

document.addEventListener('DOMContentLoaded', () => {
    // 获取 CSRF token
    function getCsrfToken() {
        const meta = document.querySelector('meta[name="_csrf"]');
        return meta ? meta.getAttribute('content') : '';
    }

    function getCsrfHeader() {
        const meta = document.querySelector('meta[name="_csrf_header"]');
        return meta ? meta.getAttribute('content') : 'X-CSRF-TOKEN';
    }

    // 确保音乐继续播放的辅助函数
    function ensureMusicPlaying() {
        // 延迟一点以确保播放器已初始化
        setTimeout(() => {
            const audioPlayer = document.getElementById('audioPlayer');
            if (audioPlayer && audioPlayer.paused && audioPlayer.src) {
                // 如果有音乐但暂停了，尝试播放
                audioPlayer.play().catch(err => {
                    console.log('自动播放被阻止，需要用户交互');
                });
            }
        }, 500);
    }

    // 处理删除操作
    document.querySelectorAll('.delete-memo-form').forEach(form => {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const confirmMessage = form.getAttribute('data-confirm');
            if (confirmMessage && !confirm(confirmMessage)) {
                return;
            }

            const url = form.action.replace('/memos/delete/', '/memos/api/delete/');

            try {
                const headers = {
                    'Content-Type': 'application/json'
                };
                headers[getCsrfHeader()] = getCsrfToken();

                const response = await fetch(url, {
                    method: 'POST',
                    headers: headers
                });

                const data = await response.json();

                if (data.success) {
                    // 移除对应的 memo 卡片
                    const memoCard = form.closest('.memo-card');
                    memoCard.style.transition = 'opacity 0.3s';
                    memoCard.style.opacity = '0';
                    setTimeout(() => {
                        memoCard.remove();

                        // 检查是否还有 memo
                        const memoGrid = document.querySelector('.memo-grid');
                        if (memoGrid && memoGrid.children.length === 0) {
                            location.reload(); // 如果没有 memo 了，刷新显示空状态
                        }
                    }, 300);

                    // 确保音乐继续播放
                    ensureMusicPlaying();
                } else {
                    alert('删除失败: ' + (data.message || '未知错误'));
                }
            } catch (error) {
                console.error('删除失败:', error);
                alert('删除失败，请重试');
            }
        });
    });

    // 处理重要标记切换
    document.querySelectorAll('.toggle-important-form').forEach(form => {
        form.addEventListener('submit', async (e) => {
            e.preventDefault();

            const url = form.action.replace('/memos/toggle-important/', '/memos/api/toggle-important/');

            try {
                const headers = {
                    'Content-Type': 'application/json'
                };
                headers[getCsrfHeader()] = getCsrfToken();

                const response = await fetch(url, {
                    method: 'POST',
                    headers: headers
                });

                const data = await response.json();

                if (data.success) {
                    // 更新星标图标
                    const button = form.querySelector('button');
                    const span = button.querySelector('span');
                    span.textContent = data.isImportant ? '⭐' : '☆';

                    // 更新 title
                    const titleKey = data.isImportant ? 'memo.remove.important' : 'memo.mark.important';
                    // 注意：这里简化处理，实际应该根据语言显示不同文本
                    button.title = data.isImportant ? 'Remove from important' : 'Mark as important';

                    // 确保音乐继续播放
                    ensureMusicPlaying();
                } else {
                    alert('操作失败: ' + (data.message || '未知错误'));
                }
            } catch (error) {
                console.error('操作失败:', error);
                alert('操作失败，请重试');
            }
        });
    });

    // 处理 memo 表单提交（新建和编辑）
    const memoForm = document.querySelector('.memo-form');
    if (memoForm) {
        memoForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const formData = new FormData(memoForm);

            // 获取 checkbox 的值
            const checkboxElement = memoForm.querySelector('input[type="checkbox"]');
            const isImportantValue = checkboxElement ? checkboxElement.checked : false;

            const memoData = {
                title: formData.get('title'),
                content: formData.get('content'),
                isImportant: isImportantValue
            };

            console.log('Checkbox element:', checkboxElement);
            console.log('Checkbox checked:', checkboxElement ? checkboxElement.checked : 'not found');
            console.log('提交的数据:', memoData);

            // 判断是新建还是编辑
            const actionUrl = memoForm.getAttribute('action');
            let url;

            if (actionUrl.includes('/edit/')) {
                // 编辑模式：从 /memos/edit/123 提取ID并构建API URL
                const match = actionUrl.match(/\/memos\/edit\/(\d+)/);
                if (match && match[1]) {
                    url = `/memos/api/edit/${match[1]}`;
                } else {
                    console.error('无法解析编辑URL:', actionUrl);
                    alert('URL解析失败');
                    return;
                }
            } else {
                // 新建模式
                url = '/memos/api/new';
            }

            console.log('请求URL:', url);

            try {
                const headers = {
                    'Content-Type': 'application/json'
                };
                headers[getCsrfHeader()] = getCsrfToken();

                const response = await fetch(url, {
                    method: 'POST',
                    headers: headers,
                    body: JSON.stringify(memoData)
                });

                console.log('响应状态:', response.status);

                if (!response.ok) {
                    const errorText = await response.text();
                    console.error('服务器响应错误:', errorText);
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                const data = await response.json();
                console.log('响应数据:', data);

                if (data.success) {
                    // 获取当前语言参数
                    const urlParams = new URLSearchParams(window.location.search);
                    const lang = urlParams.get('lang') || 'zh_CN';
                    // 成功后跳转到列表页
                    window.location.href = `/memos?lang=${lang}`;
                } else {
                    alert('保存失败: ' + (data.message || '未知错误'));
                }
            } catch (error) {
                console.error('保存失败:', error);
                alert('保存失败，请重试。详情请查看控制台。');
            }
        });
    }

    // 页面加载完成后确保音乐继续播放
    ensureMusicPlaying();
});
