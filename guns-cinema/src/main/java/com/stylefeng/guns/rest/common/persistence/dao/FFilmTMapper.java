package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.FilmDetailVo;
import com.stylefeng.guns.rest.common.persistence.model.FFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author FanFanStudio
 * @since 2019-01-20
 */
public interface FFilmTMapper extends BaseMapper<FFilmT> {

    FilmDetailVo getFilmDetailByName(@Param("filmName") String filmName);
    FilmDetailVo getFilmDetailById(@Param("uuid") String uuid);
}
