package com.memo.app.service;

import com.memo.app.entity.Song;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class NeteaseMusicService {

    /**
     * 获取初始歌单数据
     * 注意：这里提供的是示例数据，因为直接爬取网易云音乐需要处理反爬虫机制
     * 实际生产环境中建议使用网易云音乐API或者第三方音乐服务
     */
    public List<Song> fetchPopularSongs() {
        List<Song> songs = new ArrayList<>();

        // 示例歌单数据（10首热门歌曲）
        songs.add(createSong("1", "晴天", "周杰伦",
            "https://p1.music.126.net/M2dCjzN4nQZ_Tc4p-W5GUQ==/109951163533171539.jpg",
            "https://music.163.com/song/media/outer/url?id=186016.mp3"));

        songs.add(createSong("2", "说好不哭", "周杰伦",
            "https://p1.music.126.net/VDn1p3j4g2z4EVq0vY1Bmw==/109951164271814630.jpg",
            "https://music.163.com/song/media/outer/url?id=1416767593.mp3"));

        songs.add(createSong("3", "稻香", "周杰伦",
            "https://p1.music.126.net/7JfWWAcY5Xk4xHSvZsN_Sw==/109951163076032248.jpg",
            "https://music.163.com/song/media/outer/url?id=185809.mp3"));

        songs.add(createSong("4", "七里香", "周杰伦",
            "https://p1.music.126.net/p5p3MsPPCOJ3wPCRWFUE-A==/109951165357233888.jpg",
            "https://music.163.com/song/media/outer/url?id=185868.mp3"));

        songs.add(createSong("5", "夜曲", "周杰伦",
            "https://p1.music.126.net/XKFMXCfXbFGI2O43dXgUlg==/109951163627318332.jpg",
            "https://music.163.com/song/media/outer/url?id=185924.mp3"));

        songs.add(createSong("6", "告白气球", "周杰伦",
            "https://p1.music.126.net/f-2GKA7G_ZUQ6Vb_L23lhA==/109951165321069503.jpg",
            "https://music.163.com/song/media/outer/url?id=436514312.mp3"));

        songs.add(createSong("7", "简单爱", "周杰伦",
            "https://p1.music.126.net/JZTQTNJiZMFsEPzMKzhKcQ==/109951163369221470.jpg",
            "https://music.163.com/song/media/outer/url?id=185877.mp3"));

        songs.add(createSong("8", "枫", "周杰伦",
            "https://p1.music.126.net/7JfWWAcY5Xk4xHSvZsN_Sw==/109951163076032248.jpg",
            "https://music.163.com/song/media/outer/url?id=185819.mp3"));

        songs.add(createSong("9", "青花瓷", "周杰伦",
            "https://p1.music.126.net/J5wf32V3aQDpLZfN3I1y4w==/109951163071337163.jpg",
            "https://music.163.com/song/media/outer/url?id=185925.mp3"));

        songs.add(createSong("10", "东风破", "周杰伦",
            "https://p1.music.126.net/p5p3MsPPCOJ3wPCRWFUE-A==/109951165357233888.jpg",
            "https://music.163.com/song/media/outer/url?id=185778.mp3"));

        return songs;
    }

    private Song createSong(String songId, String title, String artist, String coverUrl, String musicUrl) {
        Song song = new Song();
        song.setSongId(songId);
        song.setTitle(title);
        song.setArtist(artist);
        song.setCoverUrl(coverUrl);
        song.setMusicUrl(musicUrl);
        song.setDuration(240); // 默认4分钟
        return song;
    }
}
