package com.zz.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zz.reggie.Utils.SMSUtils;
import com.zz.reggie.Utils.ValidateCodeUtils;
import com.zz.reggie.common.R;
import com.zz.reggie.entity.User;
import com.zz.reggie.srevice.UserService;
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
@RequestMapping("/User")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendmsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
//        获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //        生成验证码 发送验证码 存储验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            SMSUtils.sendMessage("瑞吉外卖", "", phone, code);
            session.setAttribute(phone, code);
            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
//        获取手机号和验证码，比对验证码，如果成功，则登录成功；查询手机号是否在表中，如果不存在，为新用户，则需要保存
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeInSession = session.getAttribute(phone);
        if (codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(lambdaQueryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
//            这句代码和loginCheckFilter 中的过滤代码需要做对应， 该代码未写
//            session.getAttribute("user", user.getId());
            return R.success(user);
        }

        return R.error("登录失败");
    }


}
