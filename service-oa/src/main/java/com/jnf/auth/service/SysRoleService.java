package com.jnf.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jnf.model.system.SysRole;
import com.jnf.vo.system.AssginRoleVo;

import java.util.Map;

/**
 * @author jnfstart
 */
public interface SysRoleService extends IService<SysRole> {

    //1.查询所有角色 和 当前用户所属角色
    Map<String, Object> findRoleDataByUserId(Long userId);

    //2.给用户分配角色
    void doAssign(AssginRoleVo assginRoleVo);
}
