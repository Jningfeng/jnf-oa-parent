package com.jnf.process.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jnf.model.process.Process;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jnf.vo.process.ApprovalVo;
import com.jnf.vo.process.ProcessFormVo;
import com.jnf.vo.process.ProcessQueryVo;
import com.jnf.vo.process.ProcessVo;

import java.util.Map;

/**
 * <p>
 * 审批类型 服务类
 * </p>
 *
 * @author jnf
 * @since 2023-04-06
 */
public interface OaProcessService extends IService<Process> {


    //审批管理列表
    IPage<ProcessVo> selectPage(Page<ProcessVo> pageParam, ProcessQueryVo processQueryVo);

    //部署流程定义
    void deployByZip(String deployPath);

    //启动流程实例
    void startUp(ProcessFormVo processFormVo);

    //查询待处理任务列表
    Page<ProcessVo> findPending(Page<Process> pageParam);

    //查看审批详情信息
    Map<String, Object> show(Long id);

    //审批
    void approve(ApprovalVo approvalVo);

    //已处理任务
    Object findProcessed(Page<Process> pageParam);

    //已发起
    Object findStarted(Page<ProcessVo> pageParam);
}
