package com.leyou.auth.auth.service;

import com.leyou.auth.auth.client.UserClient;
import com.leyou.auth.config.JwtProperties;
import com.leyou.auth.entity.UserInfo;
import com.leyou.auth.utils.JwtUtils;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthService {
    @Autowired
    private UserClient userClient;
    @Autowired
    private JwtProperties prop;
    public String login(String username, String password) {
        User user = userClient.queryUsernameAndPassword(username, password);
        if (user==null)
        {
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        try {
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), username), prop.getPrivateKey(), prop.getExpire());
        return token;
        } catch (Exception e) {
            throw new LyException(ExceptionEnum.CREATE_TOKEN_ERROR);
        }
    }



}
