package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class BrandVo implements Serializable {
    //获取品牌
    private String brandId;
    private String brandName;
    private Boolean isActive;

}
