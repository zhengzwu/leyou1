package com.leyou.pojo;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "tb_spu_detail")
public class SpuDetail {
    @Id
    private Long SpuId;
    private String packingList;
    private String afterService;
    private String description;
    private String genericSpec;
    private String specialSpec;
}
