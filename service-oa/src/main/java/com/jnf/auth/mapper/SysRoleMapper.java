package com.jnf.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jnf.model.system.SysRole;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author jnfstart
 */

@Repository
public interface SysRoleMapper extends BaseMapper<SysRole> {
}
