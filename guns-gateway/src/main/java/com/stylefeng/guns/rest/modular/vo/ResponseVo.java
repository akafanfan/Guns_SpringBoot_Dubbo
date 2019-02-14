package com.stylefeng.guns.rest.modular.vo;

import lombok.Data;

@Data
public class ResponseVo<M> {
    //返回状态 0-成功 1-失败 999-业务异常
    private int status;
    //返回信息
    private String msg;
    //返回数据实体
    private M data;
    //图片前缀
    private String imgPre;
    //分页
    private int nowPage;
    private int totalPage;


    private ResponseVo(){

    }
    public static<M> ResponseVo success(int nowPage,int totalPage,String imgPre,M m){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(0);
        responseVo.setData(m);
        responseVo.setImgPre(imgPre);
        responseVo.setTotalPage(totalPage);
        responseVo.setNowPage(nowPage);

        return responseVo;

    }

    public static<M> ResponseVo success(String imgPre,M m){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(0);
        responseVo.setData(m);
        responseVo.setImgPre(imgPre);

        return responseVo;
    }

    public static<M> ResponseVo success(M m){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(0);
        responseVo.setData(m);

        return responseVo;
    }
    public static<M> ResponseVo success(String msg){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(0);
        responseVo.setData(msg);

        return responseVo;
    }
    public static<M> ResponseVo serviceFail(String msg){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(1);
        responseVo.setMsg(msg);

        return responseVo;
    }

    public static<M> ResponseVo appFail(String msg){
        ResponseVo responseVo = new ResponseVo();
        responseVo.setStatus(999);
        responseVo.setMsg(msg);

        return responseVo;
    }

}
