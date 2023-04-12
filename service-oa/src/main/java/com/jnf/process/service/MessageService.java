package com.jnf.process.service;

/**
 * @author jnfstart
 */
public interface MessageService {


    /**
     * 推送待审批人员
     * @param processId
     * @param userId
     * @param taskId
     */
    void pushPendingMessage(Long processId, Long userId, String taskId);
}
