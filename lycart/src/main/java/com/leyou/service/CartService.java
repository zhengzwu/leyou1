package com.leyou.service;

import com.leyou.auth.entity.UserInfo;
import com.leyou.common.enums.ExceptionEnum;
import com.leyou.common.exception.LyException;
import com.leyou.common.utils.JsonUtils;
import com.leyou.interceptor.UserInterceptor;
import com.leyou.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    private final  static String KEY_PREFIX = "cart:uid:";
    public void addCart(Cart cart) {
        UserInfo user = UserInterceptor.getLoginUser();
        Integer num = cart.getNum();
        String key = KEY_PREFIX + user.getId();
        //redis中，用户id已经确定，根据购物车里商品skuId查询的hashKey
        System.out.println(key);
        String hashKey = cart.getSkuId().toString();
        //绑定用户id 得到redis中该用户存储的购物车map集合
        BoundHashOperations<String,Object,Object> operations = stringRedisTemplate.boundHashOps(key);
        if (operations.hasKey(hashKey)){
            String json = operations.get(hashKey).toString();
            System.out.print(json);
          cart = JsonUtils.toBean(json, Cart.class);
            cart.setNum(cart.getNum()+num);
        }
       operations.put(hashKey,JsonUtils.toString(cart));
    }

    public List<Cart> queryCart() {
        UserInfo user = UserInterceptor.getLoginUser();
        String key = KEY_PREFIX + user.getId();
        if (!stringRedisTemplate.hasKey(key)){
            throw  new LyException(ExceptionEnum.SKU_NOT_FOUND);
        }
        BoundHashOperations<String,Object,Object> operations = stringRedisTemplate.boundHashOps(key);
    List<Cart> carts  = operations.values().stream()
            .map(o -> JsonUtils.toBean(o.toString(), Cart.class)).collect(Collectors.toList());
    return carts;
    }

    public void updateNum(Long skuId, Integer num) {
        UserInfo userInfo = UserInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();
        if (!stringRedisTemplate.hasKey(key)){
            throw  new LyException(ExceptionEnum.SKU_NOT_FOUND);
        }
        BoundHashOperations<String,Object,Object> operations = stringRedisTemplate.boundHashOps(key);
        String hashKey = skuId.toString();
        String json = operations.get(hashKey).toString();
        Cart cart = JsonUtils.toBean(json, Cart.class);
        cart.setNum(num);
        String s = JsonUtils.toString(cart);
        operations.put(hashKey,s);
    }

    public void deleteCart(Long skuId) {
        UserInfo userInfo = UserInterceptor.getLoginUser();
        String key = KEY_PREFIX + userInfo.getId();
       stringRedisTemplate.opsForHash().delete(key,skuId.toString());
    }
}
