package com.memo.app.repository;

import com.memo.app.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findBySongId(String songId);
    boolean existsBySongId(String songId);
    List<Song> findAllByOrderByIdAsc();
}
