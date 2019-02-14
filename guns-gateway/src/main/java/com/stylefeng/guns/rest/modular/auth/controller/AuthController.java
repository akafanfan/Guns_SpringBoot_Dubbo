package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.api.user.UserAPI;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthRequest;
import com.stylefeng.guns.rest.modular.auth.controller.dto.AuthResponse;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.modular.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Reference(interfaceClass = UserAPI.class,check = false)
    private UserAPI userAPI;
                //    @Resource(name = "simpleValidator")
                //    private IReqValidator reqValidator;

    @RequestMapping(value = "${jwt.auth-path}")
    public ResponseVo<?> createAuthenticationToken(AuthRequest authRequest) {

        boolean validate = true;
        //不使用guns自带的用户名密码验证机制，使用自己的
        int userid = userAPI.login(authRequest.getUserName(), authRequest.getPassword());
//测试  固定返回4
//        int userid = 4;
        if (userid==0){//等于0验证不通过
            validate=false;
        }

        if (validate) {
            //randomKey和token生成完毕
            final String randomKey = jwtTokenUtil.getRandomKey();
//            final String token = jwtTokenUtil.generateToken(authRequest.getUserName(), randomKey);
            final String token = jwtTokenUtil.generateToken(""+userid, randomKey);
            //返回值
            return ResponseVo.success(new AuthResponse(token, randomKey));
//            return ResponseEntity.ok(new AuthResponse(token, randomKey));
        } else {
            return ResponseVo.serviceFail("用户名或密码错误！");
        }
    }
}
