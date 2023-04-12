package com.jnf.process.service.impl;

import com.jnf.auth.service.SysUserService;
import com.jnf.model.process.ProcessRecord;
import com.jnf.model.system.SysUser;
import com.jnf.process.mapper.OaProcessRecordMapper;
import com.jnf.process.service.OaProcessRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jnf.security.custom.LoginUserInfoHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 审批记录 服务实现类
 * </p>
 *
 * @author jnf
 * @since 2023-04-07
 */
@Service
public class OaProcessRecordServiceImpl extends ServiceImpl<OaProcessRecordMapper, ProcessRecord> implements OaProcessRecordService {

    @Autowired
    private SysUserService sysUserService ;

    @Override
    public void record(Long processId, Integer status, String description) {

        Long userId = LoginUserInfoHelper.getUserId();
        SysUser user = sysUserService.getById(userId);

        ProcessRecord processRecord = new ProcessRecord();

        processRecord.setProcessId(processId);
        processRecord.setStatus(status);
        processRecord.setDescription(description);
        processRecord.setOperateUser(user.getName());
        processRecord.setOperateUserId(userId);

        baseMapper.insert(processRecord);
    }
}
