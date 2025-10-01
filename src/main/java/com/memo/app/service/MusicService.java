package com.memo.app.service;

import com.memo.app.entity.Song;
import com.memo.app.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MusicService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private VirtualMusicService virtualMusicService;

    /**
     * 初始化歌单：从网易云音乐获取并保存到数据库
     */
    @Transactional
    public void initializeSongs() {
        // 如果数据库已有歌曲，则不重复初始化
        if (songRepository.count() > 0) {
            return;
        }

        List<Song> songs = virtualMusicService.fetchPopularSongs();
        for (Song song : songs) {
            if (!songRepository.existsBySongId(song.getSongId())) {
                songRepository.save(song);
            }
        }
    }

    /**
     * 获取所有歌曲（按ID升序）
     */
    public List<Song> getAllSongs() {
        List<Song> songs = songRepository.findAllByOrderByIdAsc();
        if (songs.isEmpty()) {
            initializeSongs();
            songs = songRepository.findAllByOrderByIdAsc();
        }
        return songs;
    }

    /**
     * 根据ID获取歌曲
     */
    public Song getSongById(Long id) {
        return songRepository.findById(id).orElse(null);
    }
}
