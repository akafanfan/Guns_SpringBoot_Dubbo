package com.stylefeng.guns.rest.modular.film.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.film.FilmAsyncServiceAPI;
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
@Service(interfaceClass = FilmAsyncServiceAPI.class)
public class DefaultFilmAsyncServiceImpl implements FilmAsyncServiceAPI {

    @Autowired
    private FFilmInfoTMapper fFilmInfoTMapper;
    @Autowired
    private FActorTMapper fActorTMapper;


    //
    private FFilmInfoT getFilmInfo(String filmId){
        FFilmInfoT fFilmInfoT = new FFilmInfoT();
        fFilmInfoT.setFilmId(filmId);
        fFilmInfoT = fFilmInfoTMapper.selectOne(fFilmInfoT);
        return fFilmInfoT;
    }

    @Override
    public FilmDescVo getFilmDesc(String filmId) {
        FFilmInfoT fFilmInfoT = getFilmInfo(filmId);

        FilmDescVo filmDescVo = new FilmDescVo();
        filmDescVo.setBiography(fFilmInfoT.getBiography());
        filmDescVo.setFilmId(filmId);
        return filmDescVo;
    }

    @Override
    public ImgVo getImgs(String filmId) {
        FFilmInfoT fFilmInfoT = getFilmInfo(filmId);
        // 图片地址是五个以逗号为分隔的链接URL
        String filmImgsStr = fFilmInfoT.getFilmImgs();
        String[] strings = filmImgsStr.split(",");

        ImgVo imgVo = new ImgVo();
        imgVo.setMainImg(strings[0]);
        imgVo.setImg01(strings[1]);
        imgVo.setImg02(strings[2]);
        imgVo.setImg03(strings[3]);
        imgVo.setImg04(strings[4]);

        return imgVo;
    }

    @Override
    public ActorVo getDecInfo(String filmId) {
        FFilmInfoT fFilmInfoT = getFilmInfo(filmId);
        // 获取导演编号
        Integer directorId = fFilmInfoT.getDirectorId();

        FActorT fActorT = fActorTMapper.selectById(directorId);
        ActorVo actorVo = new ActorVo();
        actorVo.setImgAddress(fActorT.getActorImg());
        actorVo.setDirectorName(fActorT.getActorName());

        return actorVo;
    }

    @Override
    public List<ActorVo> getActors(String filmId) {
        List<ActorVo> actors = fActorTMapper.getActors(filmId);
        return actors;

    }


}
