package com.summary.dao;

import com.alibaba.fastjson.JSONObject;
import com.summary.domain.RefreshTokenDetail;
import com.summary.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface UserDao {

    User getUserByPhone(String phone);

    Integer addUser(User user);

    User getUserById(Long id);

    Integer updateUsers(User user);

    User getUserByPhoneOrEmail(@Param("phone") String phone, @Param("email") String email);

    Integer deleteRefreshToken(@Param("refreshToken") String refreshToken,
                               @Param("userId") Long userId);

    Integer addRefreshToken(@Param("refreshToken")String refreshToken,
                            @Param("userId") Long userId,
                            @Param("createTime") Date createTime);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);


}
