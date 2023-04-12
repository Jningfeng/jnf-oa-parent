package com.jnf.wechat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jnf.model.wechat.Menu;
import com.jnf.vo.wechat.MenuVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 菜单 服务类
 * </p>
 *
 * @author jnf
 * @since 2023-04-08
 */
public interface MenuService extends IService<Menu> {

    //获取全部菜单
    List<MenuVo> findMenuInfo();

    //同步菜单
    void syncMenu();

    //删除菜单
    void removeMenu();
}
