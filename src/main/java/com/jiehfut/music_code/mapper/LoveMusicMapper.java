package com.jiehfut.music_code.mapper;


import com.jiehfut.music_code.model.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LoveMusicMapper {


    /**
     * 用户进行音乐收藏的时候，判断是否已经收藏
     * @param userId
     * @param musicId
     * @return
     */
    Music findLoveMusicByUserId(int userId, int musicId);

    /**
     * 进行收藏音乐的操作
     * @param userId
     * @param musicId
     * @return
     */
    boolean insertLoveMusic(int userId, int musicId);

    /**
     * 查询该用户的所有收藏列表
     * @param userId
     * @return
     */
    List<Music> findAllLoveMusicByUserId(int userId);

    /**
     * 查询当前用户指定为 title 的音乐
     * @param userId
     * @param title
     * @return
     */
    List<Music> findLoveMusicByUserIdAndTitle(int userId, String title);


    /**
     * 删除收藏的歌曲信息
     * @param userId
     * @param musicId
     * @return
     */
    Integer deleteLoveMusicByUserIdAndMusicId(int userId, int musicId);


    /**
     * 删除某一首音乐，关于这首音乐的全部收藏信息删除
     * @param musicId
     * @return
     */
    int deleteLoveMusicByMusicId(int musicId);


}
