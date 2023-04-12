package com.jnf.auth;

import com.jnf.auth.mapper.SysRoleMapper;
import com.jnf.model.system.SysRole;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;

/**
 * @author jnfstart
 */

@SpringBootTest
public class TestMPDemo1 {

    @Autowired
    private SysRoleMapper sysRoleMapper ;

    @Test
    public void getAll(){

        List<SysRole> sysRoles = sysRoleMapper.selectList(null);
        System.out.println(sysRoles);
    }
}
