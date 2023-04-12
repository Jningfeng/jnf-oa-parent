package com.jnf.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jnf.model.system.SysUser;

import java.util.Map;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author jnf
 * @since 2023-03-25
 */
public interface SysUserService extends IService<SysUser> {

    //更新状态
    void updateStatus(Long id, Integer status);

    //根据用户名获取用户对象
    SysUser getUserByUserName(String username);

    //获取当前用户基本信息
    Map<String, Object> getCurrentUser();
}
