package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.cinema.vo.FilmInfoVo;
import com.stylefeng.guns.api.cinema.vo.HallInfoVo;
import com.stylefeng.guns.rest.common.persistence.model.YFieldT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 放映场次表 Mapper 接口
 * </p>
 *
 * @author FanFanStudio
 * @since 2019-02-15
 */
public interface YFieldTMapper extends BaseMapper<YFieldT> {
    List<FilmInfoVo> getFilmInfos(@Param("cinemaId") int cinemaId);

    HallInfoVo getHallInfo(@Param("fieldId") int fieldId);

    FilmInfoVo getFilmInfoById(@Param("fieldId") int fieldId);
}
