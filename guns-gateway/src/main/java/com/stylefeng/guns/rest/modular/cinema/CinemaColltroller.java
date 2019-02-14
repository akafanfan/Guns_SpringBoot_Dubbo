package com.stylefeng.guns.rest.modular.cinema;

import com.stylefeng.guns.api.cinema.vo.CinemaQueryVo;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cinema/")
public class CinemaColltroller {

    private C

    //1、查询影院列表-根据条件查询所有影院
    @RequestMapping(value = "getCinemas" ,method = RequestMethod.GET)
    public ResponseVo getCinemas(CinemaQueryVo cinemaQueryVo){


        return null;
    }


    //2、获取影院列表查询条件
    @RequestMapping(value = "getCondition", method = RequestMethod.GET)
    public ResponseVo getCondition(CinemaQueryVo cinemaQueryVo){


        return null;
    }

    //3、获取播放场次接口
    @RequestMapping(value = "getFields" , method = RequestMethod.GET)
    public ResponseVo getFields(Integer cinemaId){


        return null;
    }

    //4、获取场次详细信息接口
    @RequestMapping(value = "getFieldInfo" , method = RequestMethod.POST)
    public ResponseVo getFieldInfo(Integer cinemaId ,Integer fieldId){


        return null;
    }
}
