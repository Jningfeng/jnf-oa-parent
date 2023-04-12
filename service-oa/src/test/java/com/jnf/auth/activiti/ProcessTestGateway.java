package com.jnf.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jnfstart
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTestGateway {
    @Autowired
    private RepositoryService repositoryService ;

    @Autowired
    private RuntimeService runtimeService ;

    @Autowired
    private TaskService taskService ;

    @Autowired
    private HistoryService historyService ;


    //部署流程定义
    @Test
    public void deployProcess(){
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia001.bpmn20.xml")
                .name("请假申请流程001").deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

    //启动流程测试
    @Test
    public void startProcessInstance(){
        Map<String,Object> map = new HashMap<>();
        //设置请假天数
        map.put("day",2);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia001", map);
        System.out.println(processInstance.getProcessDefinitionId());
        System.out.println(processInstance.getId());
    }

    //查询个人待办任务---zhao6
    @Test
    public void findTaskList(){
        String assign = "wang5";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }


    //处理当前任务
    @Test
    public void completTask(){
        //查询负责人需要处理任务，返回一条
        Task task = taskService.createTaskQuery().taskAssignee("wang5").singleResult();
        //完成任务,参数: 任务Id
        taskService.complete(task.getId());
    }
}
