package com.jnf.wechat.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jnf.model.wechat.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 菜单 Mapper 接口
 * </p>
 *
 * @author jnf
 * @since 2023-04-08
 */
@Repository
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

}
