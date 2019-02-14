package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmAsyncServiceAPI {

    //获取影片描述信息
    FilmDescVo getFilmDesc(String uuid);
    //获取图片信息
    ImgVo getImgs(String uuid);
    //获取导演信息
    ActorVo getDecInfo(String uuid);
    //获取演员信息
    List<ActorVo> getActors(String filmId);
}
