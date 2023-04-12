package com.jnf.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jnf.auth.mapper.SysUserMapper;
import com.jnf.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnf.model.system.SysUser;
import com.jnf.security.custom.LoginUserInfoHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author jnf
 * @since 2023-03-25
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {


    //更新状态
    @Override
    public void updateStatus(Long id, Integer status) {
        SysUser sysUser = baseMapper.selectById(id);
        sysUser.setStatus(status);
        baseMapper.updateById(sysUser);
    }

    //根据用户名获取用户对象
    @Override
    public SysUser getUserByUserName(String username) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = baseMapper.selectOne(queryWrapper);
        return sysUser;
    }

    //获取当前用户基本信息
    @Override
    public Map<String, Object> getCurrentUser() {

        SysUser sysUser = baseMapper.selectById(LoginUserInfoHelper.getUserId());

        Map<String, Object> map = new HashMap<>();

        map.put("name", sysUser.getName());
        map.put("phone", sysUser.getPhone());

        System.out.println(map.get("name"));

        return map;
    }
}
