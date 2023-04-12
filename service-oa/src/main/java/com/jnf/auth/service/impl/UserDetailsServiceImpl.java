package com.jnf.auth.service.impl;

import com.jnf.auth.service.SysMenuService;
import com.jnf.auth.service.SysUserService;
import com.jnf.model.system.SysUser;
import com.jnf.security.custom.CustomUser;
import com.jnf.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author jnfstart
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private SysUserService sysUserService ;

    @Autowired
    private SysMenuService sysMenuService ;
    /**
     * 根据用户名获取用户对象（获取不到直接抛异常）
     *
     * @param username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SysUser sysUser = sysUserService.getUserByUserName(username);

        if (null == sysUser) throw new UsernameNotFoundException("用户名不存在");

        if (sysUser.getStatus() == 0) throw new RuntimeException("账号已停用");

        //根据用户id查询用户操作权限数据
        List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());

        //创建List集合，封装最终权限数据
        List<SimpleGrantedAuthority> authList = new ArrayList<>();

        //查询list集合遍历
        for (String perms : userPermsList) {
            authList.add(new SimpleGrantedAuthority(perms.trim()));
        }
        return new CustomUser(sysUser, authList);
    }
}
