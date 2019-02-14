package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class AreaVo implements Serializable {
    //获取行政区域
    private Integer areaId;
    private String  areaName;
    private Boolean isActive;
}
