package com.yyhdbl.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yyhdbl.common.R;
import com.yyhdbl.entity.User;
import com.yyhdbl.service.UserService;
import com.yyhdbl.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 发送短信验证码
     *
     * @param user
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {

//        获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {

//        生成四位随机验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();

//        调用阿里云短信服务api发送短信
            log.info(code);
//        将要生成的验证码保存到session
            session.setAttribute(phone, code);
            return R.success("短信发送成功"+code);
        }

        return R.error("短信发送失败");


    }


    /**
     * 发送短信验证码
     *
     * @param map
     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {

//        获取手机号
        String phone = map.get("phone").toString();
//        获取验证码
        String code = map.get("code").toString();
//        从session中获取保存的验证码
        Object codeInSession = session.getAttribute(phone);
//        进行验证码比对
        if (codeInSession != null && codeInSession.equals(code)) {
            //判断当前手机号是否位新用户 如果是新用户则自动注册
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(lambdaQueryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }


}
