package com.jnf.auth.utils;

import com.jnf.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jnfstart
 */
public class MenuHelper {

    //使用递归方法建菜单
    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        //创建List集合，用于存储最终数据
        List<SysMenu> trees = new ArrayList<>();

        //把所有菜单数据进行遍历
        for (SysMenu sysMenu : sysMenuList) {
            //递归入口  parentId = 0
            if (sysMenu.getParentId().longValue()==0){
                trees.add(getChildren(sysMenu,sysMenuList));
            }
        }
        return trees ;
    }

    private static SysMenu getChildren(SysMenu sysMenu, List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());

        //遍历所有菜单数据，判断id 和 parentId对应关系
        for (SysMenu it : sysMenuList) {
            if (sysMenu.getId().longValue() == it.getParentId().longValue()){
                if (sysMenu.getChildren() == null){
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(getChildren(it,sysMenuList));
            }

        }
        return sysMenu ;
    }
}
