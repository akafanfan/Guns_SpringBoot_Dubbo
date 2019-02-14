package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.common.persistence.dao.UserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.UserT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Service(interfaceClass = UserAPI.class,loadbalance="roundrobin")
public class UserServiceImpl implements UserAPI {
//看到service 说明是微服务对外暴露的实现
    @Autowired
    private UserTMapper userTMapper;

    //登录业务
    @Override
    public int login(String username, String password) {
        //根据登录账号获取数据库信息
        UserT userT = new UserT();
        userT.setUserName(username);
        UserT result = userTMapper.selectOne(userT);
        //获取到的结果然后再与加密密码做匹配
        if (result!=null&&result.getUuid()>0){
            String md5Password = MD5Util.encrypt(password);
            if (result.getUserPwd().equals(md5Password)) {
                return result.getUuid();
            }
        }
        return 0;
    }


    //注册业务
    @Override
    public boolean registry(UserModel userModel) {
        //获取注册信息

        //将注册信息转换为数据实体
        UserT userT = new UserT();
        userT.setUserName(userModel.getUsername());
//        userT.setUserPwd(userModel.getPassword());//密码加密格式
        userT.setEmail(userModel.getEmail());
        userT.setAddress(userModel.getAddress());
        userT.setUserPhone(userModel.getPhone());
            //创建时间和修改时间 ——>currentt_timestamp

            //数据加密 【MD5加密+盐值 ->shiro加密】
            String md5Password = MD5Util.encrypt(userModel.getPassword());
        userT.setUserPwd(md5Password);

        //将数据实体存如数据库
        Integer insert = userTMapper.insert(userT);
        if (insert > 0) {
            return true;
        }else {
            return false;
        }
    }

    //检查用户名是否存在
    @Override
    public boolean checkUsername(String username) {
        EntityWrapper<UserT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("user_name", username);
        Integer result = userTMapper.selectCount(entityWrapper);
        if (result!=null&&result>0){
            return false;
        }else {
            return true;
        }

    }

    //公共方法
    private UserInfoModel do4UserInfo(UserT userT){
        UserInfoModel userInfoModel = new UserInfoModel();

        userInfoModel.setUsername(userT.getUserName());
        userInfoModel.setUpdateTime(userT.getUpdateTime().getTime());
        userInfoModel.setSex(userT.getUserSex());
        userInfoModel.setPhone(userT.getUserPhone());
        userInfoModel.setNickname(userT.getNickName());
        userInfoModel.setLifeState(""+userT.getLifeState());
        userInfoModel.setHeadAddress(userT.getHeadUrl());
        userInfoModel.setEmail(userT.getEmail());
        userInfoModel.setBeginTime(userT.getBeginTime().getTime());
        userInfoModel.setBirthday(userT.getBirthday());
        userInfoModel.setBiography(userT.getBiography());
        userInfoModel.setAddress(userT.getAddress());

        return userInfoModel;
    }
    private Date time2Date(long time){
        Date date = new Date(time);
        return date;
    }
    //根据主键查询用户信息
    @Override
    public UserInfoModel getUserInfo(int uuid) {
        //根据主键查询信息
        UserT userT = userTMapper.selectById(uuid);
        //将UserT转换为UserInfoModel
        UserInfoModel userInfoModel = do4UserInfo(userT);

        return userInfoModel;
    }

    @Override
    public UserInfoModel updateUserInfo(UserInfoModel userInfoModel) {
        //将传入的数据转换为 UserT;
        UserT userT = new UserT();
        userT.setUserName(userInfoModel.getUsername());
        userT.setUuid(userInfoModel.getUuid());
        userT.setUserSex(userInfoModel.getSex());
        userT.setNickName(userInfoModel.getNickname());
        userT.setHeadUrl(userInfoModel.getHeadAddress());
        userT.setBirthday(userInfoModel.getBirthday());
        userT.setBiography(userInfoModel.getBiography());
        userT.setUserSex(userInfoModel.getSex());
        userT.setAddress(userInfoModel.getAddress());
        userT.setLifeState(Integer.parseInt(userInfoModel.getLifeState()));
        userT.setBeginTime(null);
        userT.setUpdateTime(null);
        //将数据存入数据库
        Integer result = userTMapper.updateById(userT);
        if(result>0){
            //按照id将用户信息查出
            UserInfoModel userInfo = getUserInfo(userT.getUuid());
            //返回给前端
            return userInfo;
        }else {
            return userInfoModel;
        }


    }


}
