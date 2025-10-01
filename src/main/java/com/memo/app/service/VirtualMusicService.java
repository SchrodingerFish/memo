package com.memo.app.service;

import com.memo.app.entity.Song;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VirtualMusicService {

    /**
     * 获取初始歌单数据
     * 注意：这里提供的是示例数据，因为直接爬取网易云音乐需要处理反爬虫机制
     * 实际生产环境中建议使用网易云音乐API或者第三方音乐服务
     */
    public List<Song> fetchPopularSongs() {
        List<Song> songs = new ArrayList<>();

        // 示例歌单数据（10首热门歌曲）
        songs.add(createSong("1", "晴天", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/s3s94/93/211513640.jpg",
            "https://er-sycdn.kuwo.cn/29920c43703f5461c3f3b4df6ab0ff32/68dcd7ec/resource/30106/trackmedia/M8000039MnYb0qxYhV.mp3"));

        songs.add(createSong("2", "说好不哭", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/12/37/4156270827.jpg",
            "https://er-sycdn.kuwo.cn/2e186b213907255b7ce2533c923ffa92/68dcd83a/resource/30106/trackmedia/M800000SOnCR1nyjIV.mp3"));

        songs.add(createSong("3", "稻香", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/s4s0/93/1794217775.jpg",
            "https://er-sycdn.kuwo.cn/54e2b7cf36106371f7651c083129be6f/68dcd8a8/resource/30106/trackmedia/M800003aAYrm3GE0Ac.mp3"));

        songs.add(createSong("4", "七里香", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/s4s81/2/3200337129.jpg",
            "https://lv-sycdn.kuwo.cn/d000a6d6298a4615d9ded056e0ce308a/68dcda9d/resource/30106/trackmedia/M800004Z8Ihr0JIu5s.mp3"));

        songs.add(createSong("5", "夜曲", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/s4s11/89/774616642.jpg",
            "https://er-sycdn.kuwo.cn/1fee4caf337c3413a90880011a0bdf4b/68dcdac6/resource/30106/trackmedia/M800001zMQr71F1Qo8.mp3"));

        songs.add(createSong("6", "告白气球", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/64/39/3540704654.jpg",
            "https://lw-sycdn.kuwo.cn/7fd44130937835be9576c6d6d0f1813f/68dcdaeb/resource/30106/trackmedia/M800002QE4Dt4Gkrgd.mp3"));

        songs.add(createSong("7", "简单爱", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/s4s36/70/1529234453.jpg",
            "https://er-sycdn.kuwo.cn/940e7b7ed839ba9e0673534394b0522f/68dcdb0b/resource/30106/trackmedia/M8000009BCJK1nRaad.mp3"));

        songs.add(createSong("8", "枫", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/s4s11/89/774616642.jpg",
            "https://er-sycdn.kuwo.cn/d9eee8340b3cec292a7133a65e117ec3/68dcdb33/resource/30106/trackmedia/M800003KtYhg4frNXC.mp3"));

        songs.add(createSong("9", "青花瓷", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/7/83/4087363627.jpg",
            "https://er-sycdn.kuwo.cn/a96001d6bcf5995b44640d057fe8b333/68dcdb55/resource/30106/trackmedia/M800002qU5aY3Qu24y.mp3"));

        songs.add(createSong("10", "东风破", "周杰伦",
            "https://img1.kuwo.cn/star/albumcover/{x}/s3s94/93/211513640.jpg",
            "https://er-sycdn.kuwo.cn/90115ecc374c8cf34f82428d905e6c78/68dcdb78/resource/30106/trackmedia/M800003uEbEr0jcW7c.mp3"));

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
