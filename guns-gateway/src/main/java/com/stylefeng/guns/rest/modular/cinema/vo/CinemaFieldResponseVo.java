package com.stylefeng.guns.rest.modular.cinema.vo;

import com.stylefeng.guns.api.cinema.vo.CinemaInfoVo;
import com.stylefeng.guns.api.cinema.vo.FilmInfoVo;
import com.stylefeng.guns.api.cinema.vo.HallInfoVo;
import lombok.Data;
//查单个
@Data
public class CinemaFieldResponseVo {
    private FilmInfoVo filmInfo;
    private CinemaInfoVo cinemaInfo;
    private HallInfoVo hallInfo;
}
