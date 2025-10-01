package com.memo.app.service;

import com.memo.app.entity.Memo;
import com.memo.app.entity.User;
import com.memo.app.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemoService {

    private final MemoRepository memoRepository;

    public List<Memo> getAllMemosByUser(User user) {
        return memoRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public List<Memo> getImportantMemosByUser(User user) {
        return memoRepository.findByUserAndIsImportantOrderByCreatedAtDesc(user, true);
    }

    public Memo getMemoById(Long id) {
        return memoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Memo not found"));
    }

    @Transactional
    public Memo createMemo(Memo memo, User user) {
        memo.setUser(user);
        // 确保 isImportant 字段有默认值
        if (memo.getIsImportant() == null) {
            memo.setIsImportant(false);
        }
        System.out.println("Service 层 - 准备保存 Memo, IsImportant: " + memo.getIsImportant());
        Memo savedMemo = memoRepository.save(memo);
        System.out.println("Service 层 - 保存后的 Memo, IsImportant: " + savedMemo.getIsImportant());
        return savedMemo;
    }

    @Transactional
    public Memo updateMemo(Long id, Memo memoDetails, User user) {
        Memo memo = getMemoById(id);
        if (!memo.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this memo");
        }
        System.out.println("Service 层 - 更新前 IsImportant: " + memo.getIsImportant());
        System.out.println("Service 层 - 新数据 IsImportant: " + memoDetails.getIsImportant());
        memo.setTitle(memoDetails.getTitle());
        memo.setContent(memoDetails.getContent());
        memo.setIsImportant(memoDetails.getIsImportant() != null ? memoDetails.getIsImportant() : false);
        Memo savedMemo = memoRepository.save(memo);
        System.out.println("Service 层 - 更新后 IsImportant: " + savedMemo.getIsImportant());
        return savedMemo;
    }

    @Transactional
    public void deleteMemo(Long id, User user) {
        Memo memo = getMemoById(id);
        if (!memo.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to delete this memo");
        }
        memoRepository.delete(memo);
    }

    @Transactional
    public Memo toggleImportant(Long id, User user) {
        Memo memo = getMemoById(id);
        if (!memo.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You don't have permission to update this memo");
        }
        memo.setIsImportant(!memo.getIsImportant());
        return memoRepository.save(memo);
    }
}
