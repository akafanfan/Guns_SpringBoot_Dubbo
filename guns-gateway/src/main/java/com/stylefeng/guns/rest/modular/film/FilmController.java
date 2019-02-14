package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.rpc.RpcContext;
import com.stylefeng.guns.api.film.FilmAsyncServiceAPI;
import com.stylefeng.guns.api.film.FilmServiceAPI;
import com.stylefeng.guns.api.film.vo.*;
import com.stylefeng.guns.rest.modular.film.vo.FilmConditionVo;
import com.stylefeng.guns.rest.modular.film.vo.FilmRequestVo;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/film/")
public class FilmController {

    private static final String IMG_PRE = "http://img.meetingshop.cn/";

    @Reference(interfaceClass = FilmServiceAPI.class)
    private FilmServiceAPI filmServiceAPI;

    @Reference(interfaceClass = FilmAsyncServiceAPI.class, async = true)
    private FilmAsyncServiceAPI filmAsyncServiceAPI;

    // 1.获取首页信息接口
    /*
        API网关：
            1、功能聚合【API聚合】
            好处：
                1、六个接口，一次请求，同一时刻节省了五次HTTP请求
                2、同一个接口对外暴漏，降低了前后端分离开发的难度和复杂度
            坏处：
                1、一次获取数据过多，容易出现问题
     */
    @RequestMapping(value = "getIndex",method = RequestMethod.GET)
    public ResponseVo getIndex(){
                            //测试Lombok
                    //        BannerVo b = new BannerVo();
                    //        b.getBannerAddress();

        FilmIndexVo filmIndexVo = new FilmIndexVo();
        //获取banner信息
        filmIndexVo.setBanners(filmServiceAPI.getBanners());
        //获取正在热映的电影
        filmIndexVo.setHotFilms(filmServiceAPI.getHotFilms(true, 8,1,1,99,99,99));
        //即将上映
        filmIndexVo.setSoonFilms(filmServiceAPI.getSoonFilms(true, 8,1,1,99,99,99));
        //票房排行
        filmIndexVo.setBoxRanking(filmServiceAPI.getBoxRanking());
        //最受欢迎的榜单
        filmIndexVo.setExpectRanking(filmServiceAPI.getExpectRanking());
        //获取前100
        filmIndexVo.setTop100(filmServiceAPI.getTop());
        return ResponseVo.success(IMG_PRE, filmIndexVo);
    }

    //2.影片条件列表查询接口
    @RequestMapping(value = "getConditionList",method = RequestMethod.GET)
    public ResponseVo getConditionList(@RequestParam(name = "catId",required = false,defaultValue = "99") String catId,
                                       @RequestParam(name = "sourceId",required = false,defaultValue = "99") String sourceId,
                                       @RequestParam(name = "yearId",required = false,defaultValue = "99") String yearId){
        FilmConditionVo filmConditionVo = new FilmConditionVo();
        //标志位
        boolean flag=false;
        //类型集合
        List<CatVo> cats = filmServiceAPI.getCats();
        List<CatVo> catReslut = new ArrayList<>();
        CatVo cat = null;
        for (CatVo catVo:cats){
            //判断集合是否存在catId，如果存在则将对应的实体变成active; 默认为99
            //6
            //1 2 3 99 4 5
            //->优化
            // 1.数据层查询按id进行排序【有序集合->有序数组】
            // 2.二分查找
            if (catVo.getCatId().equals("99")){
                cat=catVo;
                continue;
            }
            if(catVo.getCatId().equals(catId)){
                flag =true;
                catVo.setActive(true);
            }
            catReslut.add(catVo);
        }
        //如果不存在则默认将全部变为Active状态;
        if(!flag){
            cat.setActive(true);
            catReslut.add(cat);
        }else {
            cat.setActive(false);
            catReslut.add(cat);
        }

        //片源集合
        flag=false;
        List<SourceVo> sources = filmServiceAPI.getSources();
        List<SourceVo> sourceReslut = new ArrayList<>();
        SourceVo source = null;
        for (SourceVo sourceVo : sources){
            if(sourceVo.getSourceId().equals("99")){
                source = sourceVo;
                continue;
            }
            if (sourceVo.getSourceId().equals(sourceId)){
                flag = true;
                sourceVo.setActive(true);
            }
            sourceReslut.add(sourceVo);
        }
        if(!flag){
            source.setActive(true);
            sourceReslut.add(source);
        }else {
            source.setActive(false);
            sourceReslut.add(source);
        }
        //年代集合
        flag =false;
        List<YearVo> years = filmServiceAPI.getYears();
        List<YearVo> yearResult = new ArrayList<>();
        YearVo year = null;
        for (YearVo yearVo : years){
            if (yearVo.getYearId().equals("99")){
                year = yearVo;
                continue;
            }
            if (yearVo.getYearId().equals(yearId)){
                flag = true;
                year = yearVo;
            }
            yearResult.add(yearVo);
        }
        if (!flag){
            year.setActive(true);
            yearResult.add(year);
        }else {
            year.setActive(false);
            yearResult.add(year);
        }


        filmConditionVo.setCatInfo(catReslut);
        filmConditionVo.setSourceInfo(sourceReslut);
        filmConditionVo.setYearInfo(yearResult);

        return ResponseVo.success(filmConditionVo);
    }


    @RequestMapping(value = "getFilms",method = RequestMethod.GET)
    public ResponseVo getFilms(FilmRequestVo filmRequestVo){
        String img_pre = "http://img.meetingshop.cn/";
        FilmVo filmVo = null;
        //根据showType判断影片查询类型
        switch (filmRequestVo.getShowType()){
            case 1:
                filmVo = filmServiceAPI.getHotFilms(false,
                        filmRequestVo.getPageSize(),
                        filmRequestVo.getNowPage(),
                        filmRequestVo.getSortId(),
                        filmRequestVo.getSourceId(),
                        filmRequestVo.getYearId(),
                        filmRequestVo.getCatId());
                break;
            case 2:
                filmVo = filmServiceAPI.getSoonFilms(false,
                        filmRequestVo.getPageSize(),
                        filmRequestVo.getNowPage(),
                        filmRequestVo.getSortId(),
                        filmRequestVo.getSourceId(),
                        filmRequestVo.getYearId(),
                        filmRequestVo.getCatId());
                break;
            case 3:
                filmVo = filmServiceAPI.getClassicFilms(
                        filmRequestVo.getPageSize(),
                        filmRequestVo.getNowPage(),
                        filmRequestVo.getSortId(),
                        filmRequestVo.getSourceId(),
                        filmRequestVo.getYearId(),
                        filmRequestVo.getCatId());
                break;
            default:
                filmVo = filmServiceAPI.getHotFilms(false,
                        filmRequestVo.getPageSize(),
                        filmRequestVo.getNowPage(),
                        filmRequestVo.getSortId(),
                        filmRequestVo.getSourceId(),
                        filmRequestVo.getYearId(),
                        filmRequestVo.getCatId());
                break;
        }
        //根据sortId排序
        //添加各种条件查询
        //判断当前是第几页

        return ResponseVo.success(filmVo.getNowPage(), filmVo.getTotalPage(),img_pre , filmVo.getFilmInfo());

    }
    //4.影片详情查询接口

    @RequestMapping(value = "films/{searchParam}" ,method = RequestMethod.GET)
    public ResponseVo films(@PathVariable("searchParam")String searchParam, int searchType ) throws ExecutionException, InterruptedException {  //@PathVariable 学习 ：https://www.cnblogs.com/fangpengchengbupter/p/7823493.html
        //根据searchType判断查询类型
        FilmDetailVo filmDetail = filmServiceAPI.getFilmDetail(searchType, searchParam);
        if(filmDetail==null){
            return ResponseVo.serviceFail("没有可查询的影片");
        }else if(filmDetail.getFilmId()==null || filmDetail.getFilmId().trim().length()==0){
            return ResponseVo.serviceFail("没有可查询的影片");
        }

        //不同的查询类型，传入的条件会有不同【】
        // 查询影片的详细信息 ->Dubbo的异步获取
        String filmIdId = filmDetail.getFilmId();

        // 获取影片描述信息
//        FilmDescVo filmDesc =filmAsyncServiceAPI.getFilmDesc(filmIdId);
        filmAsyncServiceAPI.getFilmDesc(filmIdId);
        Future<FilmDescVo> filmDescVoFuture = RpcContext.getContext().getFuture();
        // 获取图片信息
//        ImgVo imgs = filmAsyncServiceAPI.getImgs(filmIdId);
        filmAsyncServiceAPI.getImgs(filmIdId);
        Future<ImgVo> imgVoFuture = RpcContext.getContext().getFuture();
        // 获取导演信息
        filmAsyncServiceAPI.getDecInfo(filmIdId);
        Future<ActorVo> directorVoFuture = RpcContext.getContext().getFuture();
        // 获取演员信息
        filmAsyncServiceAPI.getActors(filmIdId);
        Future<List<ActorVo>> actorsVOFuture = RpcContext.getContext().getFuture();
        // 组织info对象
        InfoRequestVo infoRequestVo = new InfoRequestVo();
        ActorRequestVo actorRequestVo = new ActorRequestVo();
        // 组织Actor属性
        actorRequestVo.setActors(actorsVOFuture.get());
        actorRequestVo.setDirector(directorVoFuture.get());
        // 组织info对象
        infoRequestVo.setActors(actorRequestVo);
        infoRequestVo.setBiography(filmDescVoFuture.get().getBiography());
        infoRequestVo.setFilmId(filmIdId);
        infoRequestVo.setImgVo(imgVoFuture.get());
        // 组织成返回值
        filmDetail.setInfo04(infoRequestVo);
        return ResponseVo.success("http://img.meetingshop.cn/",filmDetail);
    }
}
