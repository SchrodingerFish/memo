package com.memo.app.controller;

import com.memo.app.entity.Song;
import com.memo.app.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/music")
public class MusicController {

    @Autowired
    private MusicService musicService;

    /**
     * 获取所有歌曲列表
     */
    @GetMapping("/songs")
    public ResponseEntity<List<Song>> getAllSongs() {
        List<Song> songs = musicService.getAllSongs();
        return ResponseEntity.ok(songs);
    }

    /**
     * 根据ID获取单首歌曲
     */
    @GetMapping("/songs/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable Long id) {
        Song song = musicService.getSongById(id);
        if (song != null) {
            return ResponseEntity.ok(song);
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 初始化歌单（手动触发）
     */
    @PostMapping("/initialize")
    public ResponseEntity<String> initializeSongs() {
        musicService.initializeSongs();
        return ResponseEntity.ok("Songs initialized successfully");
    }
}
