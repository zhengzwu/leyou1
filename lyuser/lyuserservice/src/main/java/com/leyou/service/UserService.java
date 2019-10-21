package com.leyou.service;

import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.NumberUtils;
import com.leyou.mapper.UserMapper;
import com.leyou.pojo.User;
import com.leyou.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private static final String KEY_PREFIX = "user:verify:phone:";
    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    public Boolean check(String data, Integer type){
        User user = new User();
        switch (type){
            case 1:user.setUsername(data);
            break;
            case  2: user.setPhone(data);
            break;
            default:
                throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        return userMapper.selectCount(user)==0;
    }

    public void sendcode(String phone) {
        String key= KEY_PREFIX + phone;
        String code = NumberUtils.generateCode(6);
        redisTemplate.opsForValue().set(key,code,5, TimeUnit.MINUTES);
        System.out.print(code);
    }

    public void register(@Valid User user, String code) {
        String  cachCode = (String) redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.equals(cachCode,code)){
            throw new LyException(ExceptionEnum.INVALID_USER_DATA_TYPE);
        }
        user.setCreated(new Date());
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);
        user.setPassword(CodecUtils.md5Hex(user.getPassword() , salt));
        userMapper.insert(user);
    }

    public User queryUsernameAndPassword(String username, String password) {
        User record = new User();
        record.setUsername(username);
        // 根据用户名查询用户
        User user = userMapper.selectOne(record);
        // 校验用户名
        if(user == null){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        // 校验密码
        if(! StringUtils.equals(user.getPassword(), CodecUtils.md5Hex(password , user.getSalt()))){
            throw new LyException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }

        return user;
    }
    }

