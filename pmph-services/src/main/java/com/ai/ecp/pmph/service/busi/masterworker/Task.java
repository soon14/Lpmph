package com.ai.ecp.pmph.service.busi.masterworker;

public class Task {
	
	//任务的id
	private Integer taskId;
	
	//任务的名称
	private String taskName;
	
	//任务的内容
	private Object obj;

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Object getObj() {
		return obj;
	}

	public void setObj(Object obj) {
		this.obj = obj;
	}
	
}
