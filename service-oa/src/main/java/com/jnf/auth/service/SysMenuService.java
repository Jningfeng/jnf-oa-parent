package com.jnf.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jnf.model.system.SysMenu;
import com.jnf.vo.system.AssginMenuVo;
import com.jnf.vo.system.RouterVo;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author jnf
 * @since 2023-03-25
 */
public interface SysMenuService extends IService<SysMenu> {

    //菜单列表接口
    List<SysMenu> findNodes();

    //删除菜单
    void removeMenuById(Long id);

    //根据角色获取菜单
    List<SysMenu> findSysMenuByRoleId(Long roleId);

    //给角色分配菜单
    void doAssign(AssginMenuVo assignMenuVo);

    //4.根据用户id获取用户可以操作的菜单列表
    List<RouterVo> findUserMenuListByUserId(Long userId);

    //5.根据用户id获取用户可以操作的按钮列表
    List<String> findUserPermsByUserId(Long userId);
}
