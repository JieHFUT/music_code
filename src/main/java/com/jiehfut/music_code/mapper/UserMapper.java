package com.jiehfut.music_code.mapper;

import com.jiehfut.music_code.model.User;
import org.apache.ibatis.annotations.Mapper;



@Mapper
public interface UserMapper {

    /**
     * 用户进行登陆传递的 User 对象
     * @param loginUser
     * @return
     */
    User login(User loginUser);

    /**
     * 根据用户名字查询用户对象
     * @param username
     * @return
     */
    User findUserByUsername(String username);

}
