package com.jnf.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnf.auth.result.Result;
import com.jnf.auth.service.SysUserService;
import com.jnf.auth.utils.MD5;
import com.jnf.model.system.SysUser;
import com.jnf.vo.system.SysUserQueryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author jnf
 * @since 2023-03-25
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
@CrossOrigin
public class SysUserController {

     @Autowired
     private SysUserService sysUserService ;

    //更新状态
    @ApiOperation(value = "更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id, status);
        return Result.ok();
    }

     //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("/{page}/{limit}")
    public Result index(@PathVariable Long page , @PathVariable Long limit ,SysUserQueryVo sysUserQueryVo){

        Page<SysUser> pageParam = new Page<>(page,limit);

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper();
        String userName = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();

        if (!StringUtils.isEmpty(userName)){
            queryWrapper.like(SysUser::getUsername,userName);
        }

        //大于等于
        if (!StringUtils.isEmpty(createTimeBegin)){
            queryWrapper.ge(SysUser::getCreateTime,createTimeBegin);
        }

        //小于等于
        if (!StringUtils.isEmpty(createTimeEnd)){
            queryWrapper.le(SysUser::getCreateTime,createTimeEnd);
        }

        IPage<SysUser> pageModel = sysUserService.page(pageParam, queryWrapper);

        return Result.ok(pageModel);
    }


    @ApiOperation(value = "根据id获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    @ApiOperation(value = "添加用户")
    @PostMapping("/save")
    public Result save(@RequestBody SysUser user) {
        sysUserService.save(user);
        return Result.ok();
    }

    @ApiOperation(value = "修改用户")
    @PutMapping("/update")
    public Result updateById(@RequestBody SysUser user) {

        //密码进行加密，使用 MD5
        String passwordMD5 = MD5.encrypt(user.getPassword());
        user.setPassword(passwordMD5);

        sysUserService.updateById(user);
        return Result.ok();
    }

    @ApiOperation(value = "删除用户")
    @DeleteMapping("/remove/{id}")
    public Result remove(@PathVariable Long id) {
        sysUserService.removeById(id);
        return Result.ok();
    }

    //获取当前用户基本信息
    @GetMapping("/getCurrentUser")
    public Result getCurrentUser() {

        Map<String,Object> map =  sysUserService.getCurrentUser();

        return Result.ok(map);
    }

}

