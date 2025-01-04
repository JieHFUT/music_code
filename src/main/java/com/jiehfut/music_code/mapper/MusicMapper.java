package com.jiehfut.music_code.mapper;

import com.jiehfut.music_code.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MusicMapper {

    /**
     * 客户端上传音乐文件 xxx.mp3
     * 将其存储在数据库中
     * @param title
     * @param singer
     * @param time
     * @param url
     * @param userid
     * @return
     */
    int insert(String title, String singer, String time, String url, int userid);

    /**
     * 用户点击播放音乐，向客户端传递音频的字节流
     * @param title 音乐名称
     * @param singer 歌手
     * @return
     */
    List<Music> selectMusicByTitleAndSinger(String title, String singer);


    /**
     * 用户点击删除某一首音乐的时候，检查该音乐是否存在
     * @param id
     * @return
     */
    Music findMusicById(int id);

    /**
     *
     * @param id
     * @return
     */
    int deleteMusicById(int id);


    /**
     * 根据用户传入的歌曲名字来搜索歌曲集合
     * @param title
     * @return
     */
    List<Music> findMusicByTitle(String title);

    /**
     * 根据用户传入的歌手名字来搜索歌曲集合
     * @param singer
     * @return
     */
    List<Music> findMusicBySinger(String singer);

    /**
     * 用户不传入任何信息，搜索全部歌曲信息集合
     * @return
     */
    List<Music> findAllMusic();
}
