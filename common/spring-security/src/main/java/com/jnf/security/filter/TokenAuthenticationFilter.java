package com.jnf.security.filter;


import com.alibaba.fastjson.JSON;
import com.jnf.security.custom.LoginUserInfoHelper;
import com.jnf.security.result.JwtHelper;
import com.jnf.security.result.ResponseUtil;
import com.jnf.security.result.Result;
import com.jnf.security.result.ResultCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author jnfstart
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {


    private RedisTemplate redisTemplate ;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //如果是登录接口，直接放行
        if("/admin/system/index/login".equals(request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if(null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.LOGIN_ERROR));
        }
    }


    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        //请求头是否有token
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            if (!StringUtils.isEmpty(username)) {
                //当前用户信息放到ThreadLocal里面
                LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));
                LoginUserInfoHelper.setUsername(username);

                //通过username从redis获取权限数据
                String authString = (String) redisTemplate.opsForValue().get(username);

                //把redis获取字符串权限数据转换要求集合类型List<SimpleGrantedAuthority>
                if (!StringUtils.isEmpty(authString)){
                    List<Map> mapList = JSON.parseArray(authString, Map.class);

                    System.out.println(mapList);

                    List<SimpleGrantedAuthority> authList = new ArrayList<>();

                    for (Map map : mapList) authList.add(new SimpleGrantedAuthority((String) map.get("authority")));

                    return new UsernamePasswordAuthenticationToken(username,null, authList);

                }else return new UsernamePasswordAuthenticationToken(username,null, new ArrayList<>());


            }

        }
        return null ;
    }
}