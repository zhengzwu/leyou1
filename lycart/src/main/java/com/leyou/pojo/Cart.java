package com.leyou.pojo;

import lombok.Data;

@Data
public class Cart {
    private String image;
    private Long skuId;
    private String title;
    private Integer num;
    private Long price;
    private String ownSpec;
}
