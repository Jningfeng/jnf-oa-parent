package com.jnf.process.service;

import com.jnf.model.process.ProcessRecord;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 审批记录 服务类
 * </p>
 *
 * @author jnf
 * @since 2023-04-07
 */
public interface OaProcessRecordService extends IService<ProcessRecord> {

    //
    void record(Long processId,Integer status,String description);

}
