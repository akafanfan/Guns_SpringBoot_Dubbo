package com.stylefeng.guns.rest.modular.film.vo;

import lombok.Data;

@Data
public class FilmRequestVo {

    private Integer showType = 1;
    private Integer sortId = 1;
    private Integer catId = 99;
    private Integer sourceId = 99;
    private Integer yearId = 99;
    private Integer nowPage = 1;
    private Integer pageSize = 18;

    //Integer 允许为null值，int默认0，
    // 数据库里面如果有个字段没有值可能默认值为null，用Integer比较合适。
}
