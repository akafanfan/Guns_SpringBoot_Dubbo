package com.stylefeng.guns.rest.modular.cinema.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.YAreaDictT;
import com.stylefeng.guns.rest.common.persistence.model.YBrandDictT;
import com.stylefeng.guns.rest.common.persistence.model.YCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.YHallDictT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = CinemaServiceAPI.class)
public class DefaultCinemaServiceImpl implements CinemaServiceAPI {

    @Autowired
    private YFieldTMapper yFieldTMapper;
    @Autowired
    private YCinemaTMapper yCinemaTMapper;
    @Autowired
    private YAreaDictTMapper yAreaDictTMapper;
    @Autowired
    private YBrandDictTMapper yBrandDictTMapper;
    @Autowired
    private YHallDictTMapper yHallDictTMapper;
    @Autowired
    private YHallFilmInfoTMapper yHallFilmInfoTMapper;


    @Override
    public Page<CinemaVo> getCinemas(CinemaQueryVo cinemaQueryVo) {
        //业务实体集合
        List<CinemaVo> cinemas = new ArrayList<>();

        //判断是否传入查询条件->brandId ,hallType ,districtId是否为 99
        EntityWrapper<YCinemaT> wrapper = new EntityWrapper<>();
        if (cinemaQueryVo.getBrandId() != 99) {
            wrapper.eq("brand_id", cinemaQueryVo.getBrandId());
        }
        if (cinemaQueryVo.getHallType() != 99) {// %#3#%
            wrapper.eq("hall_ids", "%#+" + cinemaQueryVo.getHallType() + "+#%");
        }
        if (cinemaQueryVo.getDistrictId() != 99) {
            wrapper.eq("are_id", cinemaQueryVo.getDistrictId());
        }

        Page<YCinemaT> page = new Page<>(cinemaQueryVo.getNowPage(), cinemaQueryVo.getPageSize());
        //将数据实体转为实体业务
        List<YCinemaT> yCinemaTS = yCinemaTMapper.selectPage(page, wrapper);
        for (YCinemaT yCinemaT : yCinemaTS) {
            CinemaVo cinemaVo = new CinemaVo();
            cinemaVo.setUuid(yCinemaT.getUuid() + "");
            cinemaVo.setCinemaName(yCinemaT.getCinemaName());
            cinemaVo.setAddress(yCinemaT.getCinemaAddress());
            cinemaVo.setMinimumPrice(yCinemaT.getMinimumPrice() + "");

            cinemas.add(cinemaVo);

        }

        // 根据条件，判断影院列表总数
        long count = yCinemaTMapper.selectCount(wrapper);

        // 组织返回值对象
        Page<CinemaVo> res = new Page<>();
        res.setRecords(cinemas);
        res.setSize(cinemaQueryVo.getPageSize());
        res.setTotal(count);


        return res;
    }

    //2.根据条件获取品牌列表[除了就99以外，其他的数字为isActive]
    @Override
    public List<BrandVo> getBrand(int brandId) {
        //标志位
        boolean flag = false;
        List<BrandVo> brands = new ArrayList<>();
        //判断brandId是否存在
        YBrandDictT yBrandDictT = yBrandDictTMapper.selectById(brandId);
        //判断brandId =? 99
        if (brandId == 99 || yBrandDictT == null || yBrandDictT.getUuid() == null) {
            flag = false;
        }
        // 查询所有列表
        List<YBrandDictT> yBrandDictTS = yBrandDictTMapper.selectList(null);
        // 判断flag如果为true，则将99置为isActive ,如为false，则匹配上的内容为active
        for (YBrandDictT brand : yBrandDictTS) {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandName(brand.getShowName());
            brandVo.setBrandId(brand.getUuid() + "");
            if (flag) {
                if (brand.getUuid() == 99) {
                    brandVo.setIsActive(true);
                }
            } else {
                if (brand.getUuid() == brandId) {
                    brandVo.setIsActive(true);
                }
            }
            brands.add(brandVo);
        }


        return brands;
    }

    //3、获取行政区域列表
    @Override
    public List<AreaVo> getArea(int areaId) {
        //标志位
        boolean flag = false;
        List<AreaVo> areas = new ArrayList<>();
        //判断brandId是否存在
        YAreaDictT yAreaDictT = yAreaDictTMapper.selectById(areaId);
        //判断brandId =? 99
        if (areaId == 99 || yAreaDictT == null || yAreaDictT.getUuid() == null) {
            flag = false;
        }
        // 查询所有列表
        List<YAreaDictT> yAreaDictTS = yAreaDictTMapper.selectList(null);
        // 判断flag如果为true，则将99置为isActive ,如为false，则匹配上的内容为active
        for (YAreaDictT area : yAreaDictTS) {
            AreaVo areaVo = new AreaVo();
            areaVo.setAreaName(area.getShowName());
            areaVo.setAreaId(area.getUuid()+"");
            if (flag) {
                if (area.getUuid() == 99) {
                    areaVo.setIsActive(true);
                }
            } else {
                if (area.getUuid() == areaId) {
                    areaVo.setIsActive(true);
                }
            }
            areas.add(areaVo);
        }
        return areas;
    }
    //4、获取影厅类型列表
    @Override
    public List<HallTypeVo> HallType(int halltypeId) {
        //标志位
        boolean flag = false;
        List<HallTypeVo> hallTypes = new ArrayList<>();
        //判断brandId是否存在
        YHallDictT yHallDictT = yHallDictTMapper.selectById(halltypeId);
        //判断brandId =? 99
        if (halltypeId == 99 || yHallDictT == null || yHallDictT.getUuid() == null) {
            flag = false;
        }
        // 查询所有列表
        List<YHallDictT> yHallDictTS = yHallDictTMapper.selectList(null);
        // 判断flag如果为true，则将99置为isActive ,如为false，则匹配上的内容为active
        for (YHallDictT hallType : yHallDictTS) {
            HallTypeVo hallTypeVo = new HallTypeVo();
            hallTypeVo.setHalltypeName(hallType.getShowName());
            hallTypeVo.setHalltypeId(hallType.getUuid()+"");
            if (flag) {
                if (hallType.getUuid() == 99) {
                    hallTypeVo.setIsActive(true);
                }
            } else {
                if (hallType.getUuid() == halltypeId) {
                    hallTypeVo.setIsActive(true);
                }
            }
            hallTypes.add(hallTypeVo);
        }
        return hallTypes;
    }
    //5、根据影院编号，获取影院信息
    @Override
    public CinemaInfoVo getCinemaInfoById(int cinemaId) {
        //数据实体
        YCinemaT yCinemaT = yCinemaTMapper.selectById(cinemaId);
        if (yCinemaT == null) {
            return new CinemaInfoVo();
        }
        //将数据实体转换为业务实体
        CinemaInfoVo cinemaInfoVo = new CinemaInfoVo();
        cinemaInfoVo.setCinemaAdress(yCinemaT.getCinemaAddress());
        cinemaInfoVo.setImgUrl(yCinemaT.getImgAddress());
        cinemaInfoVo.setCinemaPhone(yCinemaT.getCinemaPhone());
        cinemaInfoVo.setCinemaName(yCinemaT.getCinemaName());
        cinemaInfoVo.setCinemaId(yCinemaT.getUuid()+"");
        return cinemaInfoVo;
    }

    //6、获取所有电影的信息和对应的放映场次信息，根据影院编号
    @Override
    public FilmInfoVo getFilmInfoByCinemaId(int cinemaId) {

        return null;
    }

    //7、根据放映场次ID获取放映信息
    @Override
    public FilmFields getFilmFields(int fieldId) {

        return null;
    }

    //8、根据放映场次查询播放的电影编号，然后根据电影编号获取对应的电影信息
    @Override
    public FilmInfoVo getFilmInfoByFieldId(int fieldId) {

        return null;
    }
}
