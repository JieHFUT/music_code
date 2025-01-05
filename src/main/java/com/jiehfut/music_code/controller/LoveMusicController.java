package com.jiehfut.music_code.controller;


import com.jiehfut.music_code.mapper.LoveMusicMapper;
import com.jiehfut.music_code.model.Music;
import com.jiehfut.music_code.model.User;
import com.jiehfut.music_code.utils.Constant;
import com.jiehfut.music_code.utils.ResponseBodyMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/lovemusic")
public class LoveMusicController {

    @Autowired
    private LoveMusicMapper loveMusicMapper;

    /**
     * 用户点击收藏音乐进行收藏操作
     * @param musicId 某一个用户传递需要收藏的音乐 ID
     * @return 是否收藏成功
     */
    @RequestMapping("/likeMusic")
    public ResponseBodyMessage<Boolean> likeMusic(int musicId, HttpServletRequest request) {

        HttpSession session = request.getSession();
        // 获取用户信息
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userId = user.getId();

        Music music = loveMusicMapper.findLoveMusicByUserId(userId, musicId);
        // 可以预见，查询的结果是 lovemusic(id, user_id, music_id) 对象，将其赋值给 music 对象
        // 应该是只有对象中的 id 属性有值，有值就说明收藏过了。为 null 就说明没有收藏过
        System.out.println(music); // Music(id=1, title=null, singer=null, url=null, time=null, userid=0)
        if (null != music) {
            // 说明已经收藏过了
            return new ResponseBodyMessage<>(-1, "该歌曲已经收藏过了", false);
        } else {
            // 没有收藏，如果收藏进行插入成功， 返回 1 => true
            //                   插入失败，返回 0 => false
            boolean isInserted = loveMusicMapper.insertLoveMusic(userId, musicId);
            if (isInserted) {
                return new ResponseBodyMessage<>(0, "收藏成功", true);
            } else {
                return new ResponseBodyMessage<>(-1, "收藏失败", false);
            }
        }
    }


    @RequestMapping("/findlovemusic")
    public ResponseBodyMessage<List<Music>> findLoveMusic(@RequestParam(required = false) String title,
                                                          HttpServletRequest request) {
        // 1.检查是否已经登陆
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            // 没有登陆
            return new ResponseBodyMessage<>(-1, "请登陆后查询！", null);
        }
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);

        List<Music> allLoveMusic = null;
        if (null == title || title.trim().length() == 0) {
            // 没有传递参数，查询收藏的全部信息
            allLoveMusic = loveMusicMapper.findAllLoveMusicByUserId(user.getId());
        } else {
            // 传递了歌曲名称
            allLoveMusic = loveMusicMapper.findLoveMusicByUserIdAndTitle(user.getId(), title);
        }
        return new ResponseBodyMessage<>(0, "查询收藏歌曲信息如下", allLoveMusic);
    }



    @RequestMapping("/deletelovemusic")
    public ResponseBodyMessage<Boolean> deleteLoveMusic(int musicId, HttpServletRequest request) {
        // 1.检查是否已经登陆
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            // 没有登陆
            return new ResponseBodyMessage<>(-1, "请登陆后查询！", null);
        }
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);

        Integer deleted = loveMusicMapper.deleteLoveMusicByUserIdAndMusicId(user.getId(), musicId);
        if (null == deleted || deleted == 0) {
            return new ResponseBodyMessage<>(-1, "取消收藏失败", false);
        }
        return new ResponseBodyMessage<>(0, "取消收藏成功", true);
    }


    /**
     * 完善删除行为，如果某一首音乐被删除了
     * 在收藏表里涉及到这首音乐的记录全部删除
     */







}
