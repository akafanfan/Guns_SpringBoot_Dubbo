package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.api.film.vo.ActorVo;
import com.stylefeng.guns.rest.common.persistence.model.FActorT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author FanFanStudio
 * @since 2019-01-20
 */
public interface FActorTMapper extends BaseMapper<FActorT> {
    List<ActorVo> getActors(@Param("filmId") String filmId);

}
