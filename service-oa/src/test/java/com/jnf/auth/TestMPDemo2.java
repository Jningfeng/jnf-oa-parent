package com.jnf.auth;

import com.jnf.auth.mapper.SysRoleMapper;
import com.jnf.auth.service.SysRoleService;
import com.jnf.model.system.SysRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author jnfstart
 */

@SpringBootTest
public class TestMPDemo2 {

    @Autowired
    private SysRoleService sysRoleService;

    @Test
    public void getAll(){

        List<SysRole> sysRoles = sysRoleService.list(null);


        System.out.println(sysRoles);
    }
}
