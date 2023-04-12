package com.jnf.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jnf.auth.exception.JnfException;
import com.jnf.auth.jwt.JwtHelper;
import com.jnf.auth.result.Result;
import com.jnf.auth.service.SysMenuService;
import com.jnf.auth.service.SysUserService;
import com.jnf.auth.utils.MD5;
import com.jnf.model.system.SysUser;
import com.jnf.vo.system.LoginVo;
import com.jnf.vo.system.RouterVo;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 后台登录登出
 * </p>
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {


    @Autowired
    private SysUserService sysUserService ;


    @Autowired
    private SysMenuService sysMenuService ;

    /**
     * 登录
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody LoginVo loginVo) {
        /*Map<String, Object> map = new HashMap<>();
        map.put("token","admin-token");
        return Result.ok(map);*/

        //1.获取输入的用户名和密码
        //2.根据用户名查询数据库
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(queryWrapper);

        //3.用户信息是否存在
        if (sysUser == null){
            throw new JnfException(201,"用户名不存在");
        }

        //4.判断密码
        //数据库存的密码
        String password_db = sysUser.getPassword();
        //获取输入的密码
        String password_input = MD5.encrypt(loginVo.getPassword());

        if (password_db.equals(password_input)){
            throw new JnfException(201,"密码错误");
        }

        //5.判断用户是否被禁用   1 可用   0 禁用
        if (sysUser.getStatus().intValue() == 0){
            throw new JnfException(201,"用户已经被禁用");
        }

        //6.使用jwt根据用户id和用户名称生成token字符串
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());

        //7.返回
        Map<String,Object> map = new HashMap<>();
        map.put("token",token);

        return Result.ok(map);

    }
    /**
     * 获取用户信息
     * @return
     */
    @GetMapping("/info")
    public Result info(HttpServletRequest request) {
        //1.从请求头获取用户信息（获取请求头token字符串）
        String token = request.getHeader("token");

        //2.从token获取用户id  或 用户名
        Long userId = JwtHelper.getUserId(token);

        //3.根据id查询数据库，把用户信息获取出来
        SysUser sysUser = sysUserService.getById(userId);

        //4.根据用户id获取用户可以操作的菜单列表
        //查询数据库动态构建路由结构，进行显示
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

        //5.根据用户id获取用户可以操作的按钮列表
        List<String> permsList =  sysMenuService.findUserPermsByUserId(userId);

        Map<String, Object> map = new HashMap<>();

        map.put("roles","[admin]");

        map.put("name",sysUser.getName());

        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");

        //用户可以操作的菜单
        map.put("routers",routerList);

        //用户可以操作的按钮
        map.put("buttons",permsList);

        return Result.ok(map);
    }
    /**
     * 退出
     * @return
     */
    @PostMapping("/logout")
    public Result logout(){
        return Result.ok();
    }
}
