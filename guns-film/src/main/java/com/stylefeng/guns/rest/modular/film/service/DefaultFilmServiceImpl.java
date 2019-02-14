package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = FilmServiceAPI.class)
public class DefaultFilmServiceImpl implements FilmServiceAPI {
    @Autowired
    private FBannerTMapper fBannerTMapper;
    @Autowired
    private FFilmTMapper fFilmTMapper;
    @Autowired
    private FCatDictTMapper fCatDictTMapper;
    @Autowired
    private FYearDictTMapper fYearDictTMapper;
    @Autowired
    private FSourceDictTMapper fSourceDictTMapper;
    @Autowired
    private FFilmInfoTMapper fFilmInfoTMapper;
    @Autowired
    private FActorTMapper fActorTMapper;

    @Override
    public List<BannerVo> getBanners() {
        List<BannerVo> result = new ArrayList<>();
        List<FBannerT> banners = fBannerTMapper.selectList(null);
        for (FBannerT fBannerT: banners) {
            BannerVo bannerVo = new BannerVo();
            bannerVo.setBannerId(fBannerT.getUuid()+"");
            bannerVo.setBannerUrl(fBannerT.getBannerUrl());
            bannerVo.setBannerAddress(fBannerT.getBannerAddress());
            result.add(bannerVo);
        }
        return result;
    }

    @Override
    public FilmVo getHotFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVo filmVo = new FilmVo();
        List<FilmInfo> filmInfos = new ArrayList<>();
        // 热映影片的限制条件
        EntityWrapper<FFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","1");
        // 判断是否是首页需要的内容
        if (isLimit){
            // 如果是，则限制条数、限制内容为热映影片
            Page<FFilmT> page = new Page<>(1, nums);
            List<FFilmT> fFilms = fFilmTMapper.selectPage(page, entityWrapper);
            // 组织filmInfos
            filmInfos = getFilmInfo(fFilms);
            filmVo.setFilmNum(fFilms.size());
            filmVo.setFilmInfo(filmInfos);
        }else {
            //如果不是则是列表页，同样需要限制内容位热映影片
            Page<FFilmT> page = null;
            // 1-按热门搜索，2-按时间搜索，3-按评价搜索
            switch (sortId){
                case 1:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_score");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_box_office");
                    break;
            }

            //如果sourceId,yearId ,catId不为99 则表示要按照对应编号进行查找
            if (sourceId!=99){
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_year", yearId);
            }
            if (catId != 99) {
                //#2#4#24
                String strCat = "%#"+catId+"#%";
                entityWrapper.like("film_cats", strCat);
            }
            List<FFilmT> fFilms = fFilmTMapper.selectPage(page, entityWrapper);
            //组织filmInfos
            filmInfos = getFilmInfo(fFilms);
            filmVo.setFilmNum(fFilms.size());

            //总页数 = totalCounts/nums +1 
            int totalCounts = fFilmTMapper.selectCount(entityWrapper);
            int totalPage = (totalCounts/nums)+1;
            filmVo.setFilmInfo(filmInfos);
            filmVo.setNowPage(nowPage);
            filmVo.setTotalPage(totalPage);

        }

        return filmVo;
    }

    @Override
    public FilmVo getSoonFilms(boolean isLimit, int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVo filmVo = new FilmVo();
        List<FilmInfo> filmInfos = new ArrayList<>();
        // 热映影片的限制条件
        EntityWrapper<FFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","2");
        // 判断是否是首页需要的内容
        if (isLimit) {
            // 如果是，则限制条数、限制内容为热映影片
            Page<FFilmT> page = new Page<>(1, nums);
            List<FFilmT> fFilms = fFilmTMapper.selectPage(page, entityWrapper);
            // 组织filmInfos
            filmInfos = getFilmInfo(fFilms);
            filmVo.setFilmNum(fFilms.size());
            filmVo.setFilmInfo(filmInfos);
        }else {
            //如果不是则是列表页，同样需要限制内容位即将上映影片
            Page<FFilmT> page = null;
            switch (sortId){
                case 1:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                case 2:
                    page = new Page<>(nowPage, nums, "film_time");
                    break;
                case 3:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
                default:
                    page = new Page<>(nowPage, nums, "film_preSaleNum");
                    break;
            }
            //如果sourceId,yearId ,catId不为99 则表示要按照对应编号进行查找
            if (sourceId!=99){
                entityWrapper.eq("film_source", sourceId);
            }
            if (yearId != 99) {
                entityWrapper.eq("film_year", yearId);
            }
            if (catId != 99) {
                //#2#4#24
                String strCat = "%#"+catId+"#%";
                entityWrapper.like("film_cats", strCat);
            }
            List<FFilmT> fFilms = fFilmTMapper.selectPage(page, entityWrapper);
            //组织filmInfos
            filmInfos = getFilmInfo(fFilms);
            filmVo.setFilmNum(fFilms.size());

            //总页数 = totalCounts/nums +1
            int totalCounts = fFilmTMapper.selectCount(entityWrapper);
            int totalPage = (totalCounts/nums)+1;
            filmVo.setFilmInfo(filmInfos);
            filmVo.setNowPage(nowPage);
            filmVo.setTotalPage(totalPage);

        }
        return filmVo;
    }

    @Override
    public FilmVo getClassicFilms(int nums, int nowPage, int sortId, int sourceId, int yearId, int catId) {
        FilmVo filmVo = new FilmVo();
        List<FilmInfo> filmInfos = new ArrayList<>();
        // 热映影片的限制条件
        EntityWrapper<FFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status","3");
        // 判断是否是首页需要的内容

        //如果不是则是列表页，同样需要限制内容位即将上映影片
        Page<FFilmT> page = null;
        // 1-按热门搜索，2-按时间搜索，3-按评价搜索
        switch (sortId){
            case 1:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
            case 2:
                page = new Page<>(nowPage, nums, "film_time");
                break;
            case 3:
                page = new Page<>(nowPage, nums, "film_score");
                break;
            default:
                page = new Page<>(nowPage, nums, "film_box_office");
                break;
        }
        //如果sourceId,yearId ,catId不为99 则表示要按照对应编号进行查找
        if (sourceId!=99){
            entityWrapper.eq("film_source", sourceId);
        }
        if (yearId != 99) {
            entityWrapper.eq("film_year", yearId);
        }
        if (catId != 99) {
            //#2#4#24
            String strCat = "%#"+catId+"#%";
            entityWrapper.like("film_cats", strCat);
        }
        List<FFilmT> fFilms = fFilmTMapper.selectPage(page, entityWrapper);
        //组织filmInfos
        filmInfos = getFilmInfo(fFilms);
        filmVo.setFilmNum(fFilms.size());

        //总页数 = totalCounts/nums +1
        int totalCounts = fFilmTMapper.selectCount(entityWrapper);
        int totalPage = (totalCounts/nums)+1;
        filmVo.setFilmInfo(filmInfos);
        filmVo.setNowPage(nowPage);
        filmVo.setTotalPage(totalPage);

        return filmVo;
    }



    //公共方法
    private List<FilmInfo> getFilmInfo(List<FFilmT> fFilms){
        List<FilmInfo> filmInfos = new ArrayList<>();
        for (FFilmT fFilmT : fFilms ) {
            FilmInfo filmInfo = new FilmInfo();
            filmInfo.setScore(fFilmT.getFilmScore());
            filmInfo.setImgAddress(fFilmT.getImgAddress());
            filmInfo.setFilmType(fFilmT.getFilmType());
            filmInfo.setFilmScore(fFilmT.getFilmScore());
            filmInfo.setFilmName(fFilmT.getFilmName());
            filmInfo.setFilmId(fFilmT.getUuid()+"");
            filmInfo.setExpectNum(fFilmT.getFilmPresalenum());
            filmInfo.setBoxNum(fFilmT.getFilmBoxOffice());
            filmInfo.setShowTime(DateUtil.getDay(fFilmT.getFilmTime()));

            filmInfos.add(filmInfo);
        }
        
        return filmInfos;
        
    }


    // 获取票房排行榜
    @Override
    public List<FilmInfo> getBoxRanking() {
        //正在上映的，票房前10名
        EntityWrapper<FFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", 1);

        Page<FFilmT> page = new Page<>(1, 10, "film_box_office");
        List<FFilmT> fFilmTS = fFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfo(fFilmTS);

        return filmInfos;
    }

    @Override
    public List<FilmInfo> getExpectRanking() {
        // 条件 -> 即将上映的，预售前10名
        EntityWrapper<FFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", 1);

        Page<FFilmT> page = new Page<>(1, 10, "film_preSaleNum");
        List<FFilmT> fFilmTS = fFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfo(fFilmTS);

        return filmInfos;
    }


    @Override
    public List<FilmInfo> getTop() {
        // 条件 -> 正在上映的，评分前10名
        EntityWrapper<FFilmT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("film_status", 1);

        Page<FFilmT> page = new Page<>(1, 10, "film_score");
        List<FFilmT> fFilmTS = fFilmTMapper.selectPage(page, entityWrapper);
        List<FilmInfo> filmInfos = getFilmInfo(fFilmTS);

        return filmInfos;
    }

    @Override
    public List<CatVo> getCats() {
        List<CatVo> cats = new ArrayList<>();
        //查询实体对象
        List<FCatDictT> fCatDictTS = fCatDictTMapper.selectList(null);
        //将实体对象转换为业务对象
        for (FCatDictT fCatDictT:fCatDictTS) {
            CatVo catVo = new CatVo();
            catVo.setCatId(fCatDictT.getUuid()+"");
            catVo.setCatName(fCatDictT.getShowName());
            cats.add(catVo);
        }

        return cats;
    }

    @Override
    public List<SourceVo> getSources() {
        List<SourceVo> sources = new ArrayList<>();
        List<FSourceDictT> source = fSourceDictTMapper.selectList(null);
        for (FSourceDictT fSourceDictT : source) {
            SourceVo sourceVo = new SourceVo();
            sourceVo.setSourceId(fSourceDictT.getUuid()+"");
            sourceVo.setSourceName(fSourceDictT.getShowName());
            sources.add(sourceVo);
        }
        return sources;
    }

    @Override
    public List<YearVo> getYears() {
        List<YearVo> years = new ArrayList<>();
        List<FYearDictT> year = fYearDictTMapper.selectList(null);
        for (FYearDictT fYearDictT : year) {
            YearVo yearVo = new YearVo();
            yearVo.setYearId(fYearDictT.getUuid()+"");
            yearVo.setYearName(fYearDictT.getShowName());
            years.add(yearVo);
        }
        return years;
    }

    @Override
    public FilmDetailVo getFilmDetail(int searchType, String searchParam) {
        FilmDetailVo filmDetailVo = null;
        //searchType 1- 按名称 2- 按ID
        if(searchType==1){//有模糊匹配
            filmDetailVo = fFilmTMapper.getFilmDetailByName("%"+searchParam+"%");
        }else {
            filmDetailVo = fFilmTMapper.getFilmDetailById(searchParam);
        }
        return filmDetailVo;
    }


}
