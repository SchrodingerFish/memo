package com.memo.app.repository;

import com.memo.app.entity.Memo;
import com.memo.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByUserOrderByCreatedAtDesc(User user);
    List<Memo> findByUserAndIsImportantOrderByCreatedAtDesc(User user, Boolean isImportant);
}
