package com.stylefeng.guns.rest.modular.cinema.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;

import java.util.List;

public class DefaultCinemaServiceImpl implements CinemaServiceAPI {
    @Override
    public Page<CinemaVo> getCinemas(CinemaQueryVo cinemaQueryVo) {
        return null;
    }

    @Override
    public List<BrandVo> getBrand(int brandId) {
        return null;
    }

    @Override
    public List<AreaVo> getArea(int areaId) {
        return null;
    }

    @Override
    public List<HallTypeVo> HallType(int halltypeId) {
        return null;
    }

    @Override
    public CinemaInfoVo getCinemaInfoById(int cinemaId) {
        return null;
    }

    @Override
    public FilmInfoVo getFilmInfoByCinemaId(int cinemaId) {
        return null;
    }

    @Override
    public FilmFields getFilmFields(int fieldId) {
        return null;
    }

    @Override
    public FilmInfoVo getFilmInfoByFieldId(int fieldId) {
        return null;
    }
}
