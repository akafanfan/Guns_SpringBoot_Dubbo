package com.stylefeng.guns.api.cinema.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class CinemaVo implements Serializable {
    //1、查询影院列表
    //	接口：根据CinemaQueryVO，查询影院列表
    //	入参：CinemaQueryVO
    //	出参：
    //			CinemaVO
    //					“uuid”: 1231,
    //					“cinemaName”: “大地影院”,
    //					“address”:”东城区滨河路乙1号雍和航星园74-76号楼”,
    //					“minimumPrice”: 48.5
    //
    private String uuid;
    private String cinemaName;
    private String address;
    private String minimumPrice;
}
