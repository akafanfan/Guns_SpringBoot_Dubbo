package com.stylefeng.guns.api.user;

import com.stylefeng.guns.api.user.vo.UserInfoModel;
import com.stylefeng.guns.api.user.vo.UserModel;

public interface UserAPI {
    int login(String username, String password);//登录

    boolean registry(UserModel userModel);//注册

    boolean checkUsername(String username);

    UserInfoModel getUserInfo(int uuid);//基本信息

    UserInfoModel updateUserInfo(UserInfoModel userInfoModel);
}
