package com.stylefeng.guns.api.film;

import com.stylefeng.guns.api.film.vo.*;

import java.util.List;

public interface FilmServiceAPI {
    //============获取首页信息接口=========================
    //获取banner信息
    List<BannerVo> getBanners();
    //获取正在热映的电影
    FilmVo getHotFilms(boolean isLimit,int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);
    //即将上映
    FilmVo getSoonFilms(boolean isLimit,int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);
    //经典影片
    FilmVo getClassicFilms(int nums,int nowPage,int sortId,int sourceId,int yearId,int catId);
    //票房排行
    List<FilmInfo> getBoxRanking();
    //最受欢迎的榜单
    List<FilmInfo> getExpectRanking();
    //获取前100
    List<FilmInfo> getTop();

    //============获取影片条件接口========================
    //分类条件
    List<CatVo> getCats();
    //片源条件
    List<SourceVo> getSources();
    //年代条件
    List<YearVo> getYears();

    //============影片详情查询功能=====================
    //根据影片ID或者名称获取影片信息
    FilmDetailVo getFilmDetail(int searchType, String searchParam);
    //获取影片相关的其他信息【演员表。。。】

}
