package com.jnf.process.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.jnf.auth.service.SysUserService;
import com.jnf.model.process.Process;
import com.jnf.model.process.ProcessTemplate;
import com.jnf.model.system.SysUser;
import com.jnf.process.service.MessageService;
import com.jnf.process.service.OaProcessService;
import com.jnf.process.service.OaProcessTemplateService;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * @author jnfstart
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private OaProcessService processService ;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private OaProcessTemplateService processTemplateService;

    @Autowired
    private WxMpService wxMpService ;

    /**
     * 推送待审批人员
     *
     * @param processId
     * @param userId
     * @param taskId
     */
    @Override
    public void pushPendingMessage(Long processId, Long userId, String taskId) {

        //1.查询流程信息
        Process process = processService.getById(processId);

        //2.查询要推送人的信息
        SysUser sysUser = sysUserService.getById(userId);

        //3.查询审批模板信息
        ProcessTemplate processTemplate = processTemplateService.getById(process.getProcessTemplateId());

        //4.提交审批人信息
        SysUser submitSysUser = sysUserService.getById(process.getUserId());

        //获取要给的消息人的openId
        String openId = sysUser.getOpenId();

        if (StringUtils.isEmpty(openId)) openId = "omwf25izKON9dktgoy0dogqvnGhk";  //TODO 为了测试,添加默认值 当前自己的默认值

        //5.设置消息发送的信息
        WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()

                //给谁发送消息，openId值
                .toUser(openId)

                //创建模板信息的Id值
                .templateId("yswSkZsshPmqbWFZYscQIi4MqZsieJLRM0CFJLAEz14")

                //点击消息，跳转的地址
                .url("http://ggkt1.vipgz1.91tunnel.com/#/show/" + processId + "/" + taskId)

                .build();

        JSONObject jsonObject = JSON.parseObject(process.getFormValues());

        JSONObject formShowData = jsonObject.getJSONObject("formShowData");

        StringBuffer content = new StringBuffer();

        for (Map.Entry entry : formShowData.entrySet()) {

            content.append(entry.getKey()).append("：").append(entry.getValue()).append("\n ");
        }

        //6.设置模板里面参数值
        templateMessage.addData(new WxMpTemplateData("first",

                submitSysUser.getName()+"提交"+processTemplate.getName()+"请注意查看","#272727"));

        templateMessage.addData(new WxMpTemplateData("keyword1", process.getProcessCode(), "#272727"));

        templateMessage.addData(new WxMpTemplateData("keyword2",

                new DateTime(process.getCreateTime()).toString("yyyy-MM-dd HH:mm:ss"), "#272727"));

        templateMessage.addData(new WxMpTemplateData("content", content.toString(), "#272727"));

        //调用方法发送
        try {
            String msg = wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage);

            System.out.println(msg);

        } catch (WxErrorException e) {
            e.printStackTrace();
        }

    }
}
