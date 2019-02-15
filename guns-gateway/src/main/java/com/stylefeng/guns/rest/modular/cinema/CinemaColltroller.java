package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.api.cinema.CinemaServiceAPI;
import com.stylefeng.guns.api.cinema.vo.*;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldResponseVo;
import com.stylefeng.guns.rest.modular.cinema.vo.CinemaFieldsResponseVo;
import com.stylefeng.guns.rest.modular.cinema.vo.ConditionResponseVo;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/cinema/")
public class CinemaColltroller {

    @Reference(interfaceClass = CinemaServiceAPI.class , check = false)
    private CinemaServiceAPI cinemaServiceAPI;

    private static final String IMG_PRE = "http://img.meetingshop.cn/";
    //1、查询影院列表-根据条件查询所有影院
    @RequestMapping(value = "getCinemas", method = RequestMethod.GET)
    public ResponseVo getCinemas(CinemaQueryVo cinemaQueryVo){

        try {
            //根据条件进行筛选
            Page<CinemaVo> cinemas = cinemaServiceAPI.getCinemas(cinemaQueryVo);
            if (cinemas.getRecords() == null || cinemas.getRecords().size() == 0) {
                return ResponseVo.success("没有影院可查");
            }else {
                return ResponseVo.success(cinemas.getCurrent(), (int)cinemas.getPages(), "", cinemas.getRecords());
            }
        }catch (Exception e){
            //如果出现异常 处理
            log.error("获取影院信息异常", e);
            return ResponseVo.serviceFail("查询影院列表失败");
        }

    }


    //2、获取影院列表查询条件
    @RequestMapping(value = "getCondition", method = RequestMethod.GET)
    public ResponseVo getCondition(CinemaQueryVo cinemaQueryVo){
        //status(ResponseVo success(M m)中包含) + 获取三个集合 封装成一个对象 返回
        //brandList areaList halltypeList
        try {
            List<BrandVo> brands = cinemaServiceAPI.getBrands(cinemaQueryVo.getBrandId());
            List<AreaVo> areas = cinemaServiceAPI.getAreas(cinemaQueryVo.getBrandId());
            List<HallTypeVo> hallTypes = cinemaServiceAPI.getHallType(cinemaQueryVo.getBrandId());
            ConditionResponseVo conditionResponseVo = new ConditionResponseVo();
            conditionResponseVo.setBrandList(brands);
            conditionResponseVo.setAreaList(areas);
            conditionResponseVo.setHalltypeList(hallTypes);
            return ResponseVo.success(conditionResponseVo);
        } catch (Exception e) {
            log.error("获取影院条件列表失败", e);
            return ResponseVo.serviceFail("获取影院条件列表失败");
        }


    }

    //3、获取播放场次接口
    @RequestMapping(value = "getFields" , method = RequestMethod.GET)
    public ResponseVo getFields(Integer cinemaId){
        try {
            CinemaInfoVo cinemaInfoById = cinemaServiceAPI.getCinemaInfoById(cinemaId);

            List<FilmInfoVo> filmInfoByCinemaId = cinemaServiceAPI.getFilmInfoByCinemaId(cinemaId);

            CinemaFieldsResponseVo cinemaFieldsResponseVo = new CinemaFieldsResponseVo();
            cinemaFieldsResponseVo.setCinemaInfo(cinemaInfoById);
            cinemaFieldsResponseVo.setFilmList(filmInfoByCinemaId);

            return ResponseVo.success(IMG_PRE, cinemaFieldsResponseVo);

        } catch (Exception e) {
            log.error("获取播放场次失败", e);
            return ResponseVo.serviceFail("获取播放场次失败");
        }
    }

    //4、获取场次详细信息接口
    @RequestMapping(value = "getFieldInfo" , method = RequestMethod.POST)
    public ResponseVo getFieldInfo(Integer cinemaId ,Integer fieldId){
        try {
            //filmInfo + cinemaInfo +hallInf
            CinemaInfoVo cinemaInfoById = cinemaServiceAPI.getCinemaInfoById(cinemaId);

            FilmInfoVo filmInfoByFieldId = cinemaServiceAPI.getFilmInfoByFieldId(fieldId);

            HallInfoVo filmFields = cinemaServiceAPI.getFilmFields(fieldId);
            //测试 销售的假数据 后面对接 订单接口
            filmFields.setSoldSeats("1,2,3");


            CinemaFieldResponseVo cinemaFieldResponseVo = new CinemaFieldResponseVo();
            cinemaFieldResponseVo.setFilmInfo(filmInfoByFieldId);
            cinemaFieldResponseVo.setCinemaInfo(cinemaInfoById);
            cinemaFieldResponseVo.setHallInfo(filmFields);

            return ResponseVo.success(IMG_PRE, cinemaFieldResponseVo);
        } catch (Exception e) {
            log.error("获取场次详细信息", e);
            return ResponseVo.serviceFail("获取场次详细信息");
        }
    }
}
