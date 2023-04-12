package com.jnf.process.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jnf.model.process.Process;
import com.jnf.model.process.ProcessType;
import com.jnf.vo.process.ProcessQueryVo;
import com.jnf.vo.process.ProcessVo;

import java.util.List;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author jnf
 * @since 2023-04-06
 */
public interface OaProcessTypeService extends IService<ProcessType> {

    //查询所有审批分类和每个分类所有审批模板
    List<ProcessType> findProcessType();
}
