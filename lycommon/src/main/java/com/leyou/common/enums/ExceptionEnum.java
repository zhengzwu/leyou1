package com.leyou.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ExceptionEnum {

    CATEGORY_NOT_FOUND(400,"没有这个商品"),
    BRANDS_NOT_FOUND(400,"么有这个品牌"),
    GOODS_NOT_FOUND(404,"商品不存在"),
    SPU_NOT_FOUND(404,"商品不存在"),
    SKU_NOT_FOUND(404,"商品不存在"),
    STOCK_NOT_FOUND(404,"商品不存在"),
    UPDATE_SPU_ERROR(404,"更新出错"),
    INVALID_USER_DATA_TYPE(400,"用户已被注册"),
    INVALID_USERNAME_PASSWORD(400,"用户名或密码错误"),
    CREATE_TOKEN_ERROR(400,"TOKEN生成失败" ),
    STOCK_NOT_ENOUGH(500,"库存不足"),
    CREATE_ORDER_ERROR(500,"创建订单失败");
    private int code;
    private String msg;

}
