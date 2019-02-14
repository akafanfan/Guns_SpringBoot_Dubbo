package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class HallTypeVo implements Serializable {
    //获取影厅类型
    private String halltypeId;
    private String halltypeName;
    private Boolean isActive;
}
