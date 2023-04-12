package com.jnf.wechat.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jnf.auth.jwt.JwtHelper;
import com.jnf.auth.result.Result;
import com.jnf.auth.service.SysUserService;
import com.jnf.model.system.SysUser;
import com.jnf.vo.wechat.BindPhoneVo;
import io.swagger.annotations.ApiOperation;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author jnfstart
 */
@Controller
@RequestMapping("/admin/wechat")
@CrossOrigin //跨域
public class WechatController {


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private WxMpService wxMpService ;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {

        //1.授权路径，在哪个路径获取微信信息  2.固定值，授权类型    3.授权成功后，路径跳转 'guiguoa' 转换  '#'

        String redirectUrl = null;
        try {

            redirectUrl = wxMpService.getOAuth2Service().buildAuthorizationUrl(userInfoUrl,
                    WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                    URLEncoder.encode(returnUrl.replace("guiguoa", "#"),"utf-8"));

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "redirect:" + redirectUrl ;

    }

    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code, @RequestParam("state") String returnUrl) throws Exception {

        //获取accessToken
        WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);

        //使用accessToken获取openId
        String openId = accessToken.getOpenId();

        System.out.println("openId"+openId);

        //获取微信用户信息
        WxOAuth2UserInfo wxMpUser = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);

        System.out.println("微信用户信息"+JSON.toJSONString(wxMpUser));

        //根据openId查询用户表
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SysUser::getOpenId,openId);

        SysUser sysUser = sysUserService.getOne(wrapper);

        String token = "";

        //判断openId是否存在
        if (sysUser != null){
            token = JwtHelper.createToken(sysUser.getId(),sysUser.getUsername());
        }

        if(returnUrl.indexOf("?") == -1) {
            return "redirect:" + returnUrl + "?token=" + token + "&openId=" + openId;
        } else {
            return "redirect:" + returnUrl + "&token=" + token + "&openId=" + openId;
        }
    }

    //微信账号绑定手机
    @PostMapping("bindPhone")
    @ResponseBody
    public Result bindPhone(@RequestBody BindPhoneVo bindPhoneVo) {

        //1.根据手机号查询数据库
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(SysUser::getPhone,bindPhoneVo.getPhone());

        SysUser sysUser = sysUserService.getOne(wrapper);

        //2.如果存在,更新记录 openid
        if (sysUser != null){

            sysUser.setOpenId(bindPhoneVo.getOpenId());

            sysUserService.updateById(sysUser);

            String  token = JwtHelper.createToken(sysUser.getId(),sysUser.getUsername());

            return Result.ok(token);
        }else {

            return Result.fail("手机号不存在，请联系管理员修改");
        }
    }

}
