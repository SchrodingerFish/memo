// 音乐播放器类
class MusicPlayer {
    constructor() {
        this.audio = document.getElementById('audioPlayer');
        this.playlist = []; // 原始播放列表
        this.shuffledPlaylist = []; // 随机播放列表
        this.currentIndex = 0;
        this.playMode = 'shuffle'; // 'shuffle', 'loop', 'single'
        this.isRestoringState = false; // 标记是否正在恢复状态

        this.initElements();
        this.loadPlaylist();
        this.attachEventListeners();
        this.startAutoSave(); // 开始自动保存状态
    }

    initElements() {
        // 播放器控制元素
        this.playBtn = document.getElementById('playBtn');
        this.prevBtn = document.getElementById('prevBtn');
        this.nextBtn = document.getElementById('nextBtn');
        this.modeBtn = document.getElementById('modeBtn');
        this.volumeBtn = document.getElementById('volumeBtn');
        this.playlistToggle = document.getElementById('playlistToggle');

        // 信息显示元素
        this.musicCover = document.getElementById('musicCover');
        this.musicTitle = document.getElementById('musicTitle');
        this.musicArtist = document.getElementById('musicArtist');
        this.currentTime = document.getElementById('currentTime');
        this.totalTime = document.getElementById('totalTime');

        // 进度和音量控制
        this.progressBar = document.getElementById('progressBar');
        this.volumeBar = document.getElementById('volumeBar');

        // 播放列表
        this.playlistDiv = document.getElementById('playlist');
        this.playlistItems = document.getElementById('playlistItems');
        this.closePlaylist = document.getElementById('closePlaylist');
    }

    async loadPlaylist() {
        try {
            const response = await fetch('/api/music/songs');
            if (response.ok) {
                this.playlist = await response.json();
                this.shuffledPlaylist = [...this.playlist]; // 复制原始列表
                this.renderPlaylist();
                if (this.playlist.length > 0) {
                    // 尝试恢复之前的状态
                    const restored = this.restoreState();
                    if (!restored) {
                        // 如果没有保存的状态，则随机播放并自动开始
                        this.shuffleCurrentPlaylist();
                        this.loadSong(0);
                        this.play();
                    }
                    // 如果恢复了状态且之前在播放，会在 restoreState() 中自动播放
                }
            }
        } catch (error) {
            console.error('加载歌单失败:', error);
        }
    }

    shuffleCurrentPlaylist() {
        // Fisher-Yates 洗牌算法
        for (let i = this.shuffledPlaylist.length - 1; i > 0; i--) {
            const j = Math.floor(Math.random() * (i + 1));
            [this.shuffledPlaylist[i], this.shuffledPlaylist[j]] = [this.shuffledPlaylist[j], this.shuffledPlaylist[i]];
        }
    }

    renderPlaylist() {
        this.playlistItems.innerHTML = '';
        // 播放列表始终显示原始顺序
        this.playlist.forEach((song, index) => {
            const li = document.createElement('li');
            li.className = 'playlist-item';
            li.dataset.songId = song.id; // 使用歌曲ID而不是索引
            li.innerHTML = `
                <img src="${song.coverUrl}" alt="${song.title}" class="playlist-item-cover">
                <div class="playlist-item-info">
                    <div class="playlist-item-title">${song.title}</div>
                    <div class="playlist-item-artist">${song.artist}</div>
                </div>
            `;
            li.addEventListener('click', () => this.playSongById(song.id));
            this.playlistItems.appendChild(li);
        });
    }

    loadSong(index) {
        const currentPlaylist = this.playMode === 'shuffle' ? this.shuffledPlaylist : this.playlist;
        if (index < 0 || index >= currentPlaylist.length) return;

        this.currentIndex = index;
        const song = currentPlaylist[index];

        this.audio.src = song.musicUrl;
        this.musicCover.src = song.coverUrl;
        this.musicTitle.textContent = song.title;
        this.musicArtist.textContent = song.artist;

        this.updatePlaylistActiveItem(song.id);
    }

    updatePlaylistActiveItem(songId) {
        const items = this.playlistItems.querySelectorAll('.playlist-item');
        items.forEach((item) => {
            item.classList.toggle('active', item.dataset.songId == songId);
        });
    }

    playSongById(songId) {
        const currentPlaylist = this.playMode === 'shuffle' ? this.shuffledPlaylist : this.playlist;
        const index = currentPlaylist.findIndex(song => song.id === songId);
        if (index !== -1) {
            this.loadSong(index);
            this.play();
        }
    }

    playSongByIndex(index) {
        this.loadSong(index);
        this.play();
    }

    play() {
        this.audio.play();
        this.playBtn.textContent = '⏸';
    }

    pause() {
        this.audio.pause();
        this.playBtn.textContent = '▶';
    }

    togglePlay() {
        if (this.audio.paused) {
            this.play();
        } else {
            this.pause();
        }
    }

    playNext() {
        const currentPlaylist = this.playMode === 'shuffle' ? this.shuffledPlaylist : this.playlist;
        if (this.playMode === 'shuffle' || this.playMode === 'loop') {
            this.currentIndex = (this.currentIndex + 1) % currentPlaylist.length;
        }
        this.loadSong(this.currentIndex);
        this.play();
    }

    playPrev() {
        const currentPlaylist = this.playMode === 'shuffle' ? this.shuffledPlaylist : this.playlist;
        if (this.playMode === 'shuffle' || this.playMode === 'loop') {
            this.currentIndex = (this.currentIndex - 1 + currentPlaylist.length) % currentPlaylist.length;
        }
        this.loadSong(this.currentIndex);
        this.play();
    }

    togglePlayMode() {
        const modes = ['shuffle', 'loop', 'single'];
        const icons = {
            'shuffle': '🔀',
            'loop': '🔁',
            'single': '🔂'
        };

        const currentModeIndex = modes.indexOf(this.playMode);
        const oldMode = this.playMode;
        this.playMode = modes[(currentModeIndex + 1) % modes.length];

        // 切换模式时，如果从shuffle切换到其他模式，需要找到当前歌曲在原始列表中的位置
        if (oldMode === 'shuffle' && this.playMode !== 'shuffle') {
            const currentSong = this.shuffledPlaylist[this.currentIndex];
            this.currentIndex = this.playlist.findIndex(song => song.id === currentSong.id);
        } else if (oldMode !== 'shuffle' && this.playMode === 'shuffle') {
            // 从其他模式切换到shuffle，重新洗牌并找到当前歌曲位置
            const currentSong = this.playlist[this.currentIndex];
            this.shuffledPlaylist = [...this.playlist];
            this.shuffleCurrentPlaylist();
            this.currentIndex = this.shuffledPlaylist.findIndex(song => song.id === currentSong.id);
        }

        this.modeBtn.textContent = icons[this.playMode];
        this.modeBtn.title = this.getPlayModeTitle();
    }

    getPlayModeTitle() {
        const titles = {
            'shuffle': '随机播放',
            'loop': '列表循环',
            'single': '单曲循环'
        };
        return titles[this.playMode];
    }

    updateProgress() {
        if (this.audio.duration) {
            const progress = (this.audio.currentTime / this.audio.duration) * 100;
            this.progressBar.value = progress;
            this.currentTime.textContent = this.formatTime(this.audio.currentTime);
            this.totalTime.textContent = this.formatTime(this.audio.duration);
        }
    }

    formatTime(seconds) {
        if (isNaN(seconds)) return '0:00';
        const mins = Math.floor(seconds / 60);
        const secs = Math.floor(seconds % 60);
        return `${mins}:${secs.toString().padStart(2, '0')}`;
    }

    setProgress(e) {
        const progress = e.target.value;
        if (this.audio.duration) {
            this.audio.currentTime = (progress / 100) * this.audio.duration;
        }
    }

    setVolume(e) {
        const volume = e.target.value / 100;
        this.audio.volume = volume;
        this.updateVolumeIcon(volume);
    }

    updateVolumeIcon(volume) {
        if (volume === 0) {
            this.volumeBtn.textContent = '🔇';
        } else if (volume < 0.5) {
            this.volumeBtn.textContent = '🔉';
        } else {
            this.volumeBtn.textContent = '🔊';
        }
    }

    toggleMute() {
        if (this.audio.volume > 0) {
            this.lastVolume = this.audio.volume;
            this.audio.volume = 0;
            this.volumeBar.value = 0;
            this.updateVolumeIcon(0);
        } else {
            this.audio.volume = this.lastVolume || 0.5;
            this.volumeBar.value = this.audio.volume * 100;
            this.updateVolumeIcon(this.audio.volume);
        }
    }

    togglePlaylist() {
        const isHidden = this.playlistDiv.style.display === 'none';
        this.playlistDiv.style.display = isHidden ? 'block' : 'none';
    }

    attachEventListeners() {
        // 播放控制
        this.playBtn.addEventListener('click', () => this.togglePlay());
        this.prevBtn.addEventListener('click', () => this.playPrev());
        this.nextBtn.addEventListener('click', () => this.playNext());
        this.modeBtn.addEventListener('click', () => this.togglePlayMode());
        this.volumeBtn.addEventListener('click', () => this.toggleMute());
        this.playlistToggle.addEventListener('click', () => this.togglePlaylist());
        this.closePlaylist.addEventListener('click', () => this.togglePlaylist());

        // 进度和音量
        this.progressBar.addEventListener('input', (e) => this.setProgress(e));
        this.volumeBar.addEventListener('input', (e) => this.setVolume(e));

        // 音频事件
        this.audio.addEventListener('timeupdate', () => this.updateProgress());
        this.audio.addEventListener('ended', () => {
            if (this.playMode === 'single') {
                this.audio.currentTime = 0;
                this.play();
            } else {
                this.playNext();
            }
        });

        this.audio.addEventListener('loadedmetadata', () => {
            this.totalTime.textContent = this.formatTime(this.audio.duration);
        });

        // 初始化音量
        this.audio.volume = 0.5;
        this.volumeBar.value = 50;
    }

    // 保存播放器状态到 localStorage
    saveState() {
        if (this.isRestoringState) return; // 恢复状态时不保存

        const currentPlaylist = this.playMode === 'shuffle' ? this.shuffledPlaylist : this.playlist;
        const currentSong = currentPlaylist[this.currentIndex];

        const state = {
            songId: currentSong?.id,
            currentTime: this.audio.currentTime,
            playMode: this.playMode,
            volume: this.audio.volume,
            isPaused: this.audio.paused,
            shuffledPlaylist: this.shuffledPlaylist.map(s => s.id), // 保存随机列表顺序
            timestamp: Date.now()
        };

        localStorage.setItem('musicPlayerState', JSON.stringify(state));
    }

    // 从 localStorage 恢复播放器状态
    restoreState() {
        const stateStr = localStorage.getItem('musicPlayerState');
        if (!stateStr) return false;

        try {
            this.isRestoringState = true;
            const state = JSON.parse(stateStr);

            // 检查状态是否过期（超过24小时）
            if (Date.now() - state.timestamp > 24 * 60 * 60 * 1000) {
                localStorage.removeItem('musicPlayerState');
                this.isRestoringState = false;
                return false;
            }

            // 恢复播放模式
            this.playMode = state.playMode || 'shuffle';
            this.modeBtn.textContent = { 'shuffle': '🔀', 'loop': '🔁', 'single': '🔂' }[this.playMode];
            this.modeBtn.title = this.getPlayModeTitle();

            // 恢复随机播放列表
            if (state.shuffledPlaylist && state.shuffledPlaylist.length > 0) {
                this.shuffledPlaylist = state.shuffledPlaylist
                    .map(id => this.playlist.find(s => s.id === id))
                    .filter(s => s); // 过滤掉不存在的歌曲
            } else {
                this.shuffledPlaylist = [...this.playlist];
                this.shuffleCurrentPlaylist();
            }

            // 恢复当前歌曲
            const currentPlaylist = this.playMode === 'shuffle' ? this.shuffledPlaylist : this.playlist;
            const songIndex = currentPlaylist.findIndex(s => s.id === state.songId);

            if (songIndex !== -1) {
                this.currentIndex = songIndex;
                this.loadSong(this.currentIndex);

                // 恢复播放位置
                if (state.currentTime) {
                    this.audio.currentTime = state.currentTime;
                }

                // 恢复音量
                if (state.volume !== undefined) {
                    this.audio.volume = state.volume;
                    this.volumeBar.value = state.volume * 100;
                    this.updateVolumeIcon(state.volume);
                }

                // 恢复播放状态
                if (!state.isPaused) {
                    this.play();
                }

                this.isRestoringState = false;
                return true;
            }

            this.isRestoringState = false;
            return false;
        } catch (error) {
            console.error('恢复播放器状态失败:', error);
            localStorage.removeItem('musicPlayerState');
            this.isRestoringState = false;
            return false;
        }
    }

    // 开始自动保存状态
    startAutoSave() {
        // 每5秒自动保存一次状态
        setInterval(() => {
            if (!this.audio.paused || this.audio.currentTime > 0) {
                this.saveState();
            }
        }, 5000);

        // 页面关闭前保存状态
        window.addEventListener('beforeunload', () => {
            this.saveState();
        });
    }
}

// 页面加载完成后初始化播放器
document.addEventListener('DOMContentLoaded', () => {
    const player = new MusicPlayer();
});
