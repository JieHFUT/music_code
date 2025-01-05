package com.jiehfut.music_code.controller;

import com.jiehfut.music_code.mapper.LoveMusicMapper;
import com.jiehfut.music_code.mapper.MusicMapper;
import com.jiehfut.music_code.model.Music;
import com.jiehfut.music_code.model.User;
import com.jiehfut.music_code.utils.Constant;
import com.jiehfut.music_code.utils.ResponseBodyMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/music")
public class MusicCntroller {

    @Autowired
    private MusicMapper musicMapper;

    @Autowired
    private LoveMusicMapper loveMusicMapper;

    @Value("${music.local.path}")
    private String SAVE_PATH;

    @RequestMapping("/upload")
    public ResponseBodyMessage<Boolean> insertMusic(@RequestParam String singer,
                                                    @RequestParam("filename") MultipartFile file,
                                                    HttpServletRequest request) {
        // 1.检查是否已经登陆
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(Constant.USERINFO_SESSION_KEY) == null) {
            // 没有登陆
            return new ResponseBodyMessage<>(-1, "请登陆后上传！", false);
        }

        String fileName = file.getOriginalFilename(); // xxx.mp3
        int index = fileName.lastIndexOf(".");   // 最后一个 . 的下标
        String title = fileName.substring(0, index);  // xxx


        // 指定上传的音乐文件的位置
        String path = SAVE_PATH + "/" + title + "-" + singer; //  E:/code/music_code/music/xxx-xxx
        File fileSave = new File(path);
        if (!fileSave.exists()) {
            fileSave.mkdir();
        }

        // 2.检查数据库中是否已经有当前音乐（音乐名 + 歌手）
        List<Music> music = musicMapper.selectMusicByTitleAndSinger(title, singer);
        if (music != null && music.size() != 0) {
            // 有该歌曲
            return new ResponseBodyMessage<>(-1, "已经有该歌曲！", false);
        }

        try {
            // 3.保存文件到对应目录
            file.transferTo(fileSave);
            // return new ResponseBodyMessage<>(0, "文件上传成功", true);
        } catch (IOException e) {
            System.out.println("文件上传失败：" + e.getMessage());
            return new ResponseBodyMessage<>(-1, "服务器上传失败", false);
        }

        // 4.下面进行数据库的 mp3 文件上传
        User user = (User) session.getAttribute(Constant.USERINFO_SESSION_KEY);
        int userid = user.getId();

        /**
         * url
         * 1.播放音乐的时候，客户端发送请求
         *
         */
        String url = "/music/get?path=" + path;  // => /music/get?path=E:/code/music_code/music/xxx-xxx
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String time = sdf.format(new Date()); // 2025/01/03

        int insert = musicMapper.insert(title, singer, time, url, userid);
        if (insert == 1) {
            // 数据库插入成功
            return new ResponseBodyMessage<>(0, "服务器 && 数据库文件上传成功", true);
        } else {
            // 数据库插入失败
            // 数据库上传失败的话，把服务器中的文件也删除
            fileSave.delete();
            return new ResponseBodyMessage<>(-1, "数据库上传失败", false);
        }
    }


    /**
     * ResponseEntity
     *
     * @param path xxx-xxx.mp3
     * @return 该音频文件的字节流
     */
    @RequestMapping("/get") // 响应音乐的字节流
    public ResponseEntity<byte[]> getStream(String path) {
        File file = new File(SAVE_PATH + "/" + path); // E:/code/music_code/music + / + xxx-xxx
        try {
            byte[] bytes = Files.readAllBytes(file.toPath());
            if (bytes != null) {
                return ResponseEntity.ok(bytes);
            }
        } catch (IOException e) {
            System.out.println("读取路径下文件为字节数组异常：" + e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * 删除单首音乐
     * @param id
     * @return ResponseBodyMessage
     */
    public ResponseBodyMessage<Boolean> deleteMusicById(int id) {
        // 1.检查音乐是否存在
        Music music = musicMapper.findMusicById(id);
        if (music == null) {
            // 数据库中没有该首音乐
            return new ResponseBodyMessage<>(-1, "无此音乐！", false);
        }
        // 2.如果音乐存在，删除（数据库 服务器）中该音乐信息
        int deleted = musicMapper.deleteMusicById(id);
        if (deleted == 1) {
            // 数据库操作成功
            String url = music.getUrl(); // => /music/get?path=E:/code/music_code/music/三国恋-Tank
            String path = url.substring(url.indexOf("=") + 1); // E:/code/music_code/music/三国恋-Tank
            System.out.println("path = " + path);

            File file = new File(path);
            if (file.delete()){
                // todo: 数据库已经删除完成，如果服务器删除失败，能否回退数据库删除操作
                // 删除某一首音乐以后，同步删除收藏表中的关于这首音乐的所有信息
                int i = loveMusicMapper.deleteLoveMusicByMusicId(id);
                
                return new ResponseBodyMessage<>(0, "删除成功", true);
            } else {
                return new ResponseBodyMessage<>(-1, "服务器操作失败", false);
            }
        } else {
            // 数据库操作失败
            return new ResponseBodyMessage<>(-1, "数据库操作失败", false);
        }
    }


    /**
     * 删除单首音乐
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    public ResponseBodyMessage<Boolean> deleteById(@RequestParam int id) {
        return deleteMusicById(id);
    }

    /**
     * 进行批量删除音乐信息 list => [1, 2, 3, 4, 5]
     * @param id
     * @return
     */
    @RequestMapping("/deleteSel")
    public ResponseBodyMessage<Boolean> deleteList(@RequestParam("ids") List<Integer> id) {
        System.out.println(id);
        for (Integer i : id) {
            // 对每一个 id 都要进行一遍删除
            ResponseBodyMessage<Boolean> booleanResponseBodyMessage = this.deleteMusicById(i);
            // 对返回结果进行判断
            if (booleanResponseBodyMessage.getStatus() == 0) {
                // 说明这首音乐删除成功，继续判断下一首
            } else {
                // 说明这首音乐删除失败
                // todo: 有没有办法如果遇到删除失败的情况，之前删除的歌曲不作数

                return booleanResponseBodyMessage;
            }
        }
        // 如果能走到这个位置，说明所有的音乐全部成功删除
        return new ResponseBodyMessage<>(0, "删除成功", true);
    }



    /**
     * 用户搜索信息，返回音乐集合
     * @param title 歌曲名称 => 去模糊匹配歌曲信息
     *
     * @return ResponseBodyMessage<List<Music>>
     */
    @RequestMapping("/findmusic")
    public ResponseBodyMessage<List<Music>> findMusicByTitle(@RequestParam String title) {

        if (null == title || "".equals(title)) {
            // 说明没有传递信息，查询全部歌曲
            List<Music> allMusic = musicMapper.findAllMusic();
            if (null == allMusic || allMusic.size() == 0) {
                // 数据库中没有此歌曲信息
                return new ResponseBodyMessage<>(-1, "数据库中无音乐", null);
            } else {
                return new ResponseBodyMessage<>(0, "查询歌曲信息如下：", allMusic);
            }
        }


        List<Music> musicByTitle = musicMapper.findMusicByTitle(title);
        if (null == musicByTitle || musicByTitle.size() == 0) {
            // 数据库中没有此歌曲信息
            return new ResponseBodyMessage<>(-1, "无此音乐", null);
        } else {
            return new ResponseBodyMessage<>(0, "查询歌曲信息如下：", musicByTitle);
        }
    }







}
