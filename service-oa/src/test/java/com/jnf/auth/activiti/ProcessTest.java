package com.jnf.auth.activiti;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author jnfstart
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcessTest {

    @Autowired
    private RepositoryService repositoryService ;

    @Autowired
    private RuntimeService runtimeService ;

    @Autowired
    private TaskService taskService ;

    @Autowired
    private HistoryService historyService ;

    //单个流程实例挂起
    @Test
    public void SingleSuspendProcessInstance() {
        String pId = "62fbc817-d3c1-11ed-9046-e670b8ee1b19";
        ProcessInstance instance = runtimeService.createProcessInstanceQuery().processInstanceId(pId).singleResult();

        boolean suspended = instance.isSuspended();

        if (suspended){runtimeService.activateProcessInstanceById(pId);System.out.println(pId+"激活");}

        if (!suspended){runtimeService.suspendProcessInstanceById(pId);System.out.println(pId+"挂起");}
    }

    //全部流程实例挂起
    @Test
    public void suspendProcessInstance(){
        //1.获取流程定义的对象
        ProcessDefinition qingjia = repositoryService.createProcessDefinitionQuery().processDefinitionKey("qingjia").singleResult();

        //2调用流程定义对象的方法判断当前状态：挂起  激活
        boolean suspended = qingjia.isSuspended();

        //3.判断如果挂起，实现激活     //第一个参数流程定义Id  2.是否激活 true  3.时间点
        if (suspended){
            repositoryService.activateProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println(qingjia.getId()+"激活了");
        }
        //4.如果激活实现挂起
        if (!suspended){
            repositoryService.suspendProcessDefinitionById(qingjia.getId(),true,null);
            System.out.println(qingjia.getId()+"挂起");
        }



    }

    //创建流程实例，指定Businesskey
    @Test
    public void startUpProcessAddBusinesskey(){
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("qingjia", "1001");
        System.out.println(instance.getBusinessKey());
        System.out.println(instance.getId());
    }

    //查询已经处理过的任务
    @Test
    public void findcompleteTaskList(){
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().taskAssignee("lisi").finished().list();

        for (HistoricTaskInstance historicTaskInstance : list) {
            System.out.println("流程实例id：" + historicTaskInstance.getProcessInstanceId());
            System.out.println("任务id：" + historicTaskInstance.getId());
            System.out.println("任务负责人：" + historicTaskInstance.getAssignee());
            System.out.println("任务名称：" + historicTaskInstance.getName());
        }
    }

    //处理当前任务
    @Test
    public void completTask(){
        //查询负责人需要处理任务，返回一条
        Task task = taskService.createTaskQuery().taskAssignee("zhangsan").singleResult();
        //完成任务,参数: 任务Id
        taskService.complete(task.getId());
    }

    //查询个人待办任务---zhangsan
    @Test
    public void findTaskList(){
        String assign = "zhangsan";
        List<Task> list = taskService.createTaskQuery().taskAssignee(assign).list();
        for (Task task : list) {
            System.out.println("流程实例id：" + task.getProcessInstanceId());
            System.out.println("任务id：" + task.getId());
            System.out.println("任务负责人：" + task.getAssignee());
            System.out.println("任务名称：" + task.getName());
        }
    }


    //启动流程实例
    @Test
    public void startProcess(){
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("qingjia");
        System.out.println("流程定义Id"+processInstance.getProcessDefinitionId());
        System.out.println("流程实例Id"+processInstance.getId());
        System.out.println("流程活动Id"+processInstance.getActivityId());
    }


    //单个文件部署
    @Test
    public void deployProcess(){
        //流程部署
        Deployment deploy = repositoryService.createDeployment()
                .addClasspathResource("process/qingjia.bpmn20.xml")
                .addClasspathResource("process/qingjia.png")
                .name("请假申请流程")
                .deploy();
        System.out.println(deploy.getId());
        System.out.println(deploy.getName());
    }

}
