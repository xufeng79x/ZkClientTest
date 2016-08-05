package com.xufeng.master;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;


public class Master implements Runnable {
	// master节点路径
	private String masterPath = null;
	
	// 当前master的信息
	private MasterInfo myinfo = null;
	
	// 当前主master的信息
	private MasterInfo activeMasterInfo = null;
	
	// 控制当前master的运行时间
	private long runningTime = 0;
	
	// 监控handler
	private IZkDataListener masterPathListener = null;
	
	// 当前session
	private ZkClient zc = new ZkClient("xufeng-1:2181,xufeng-2:2181,xufeng-3:2181", 10000, 10000, new SerializableSerializer());
	
	// 任务调度器
	private ScheduledExecutorService delayExector = Executors.newScheduledThreadPool(1);
	
	public Master(MasterInfo info, long runningTime, String masterPath)
	{
		this.myinfo = info;
		this.runningTime = runningTime;
		this.masterPath = masterPath;
		this.masterPathListener = new IZkDataListener() {
			// 当节点被删除的时候触发此方法
			public void handleDataDeleted(String dataPath) throws Exception {
				if (null != activeMasterInfo && activeMasterInfo.equals(myinfo))
				{
					// 如果当前master就是主master的时候，直接去抢注
					attackMaster();
				}
				else
				{
					// 如果不是则延迟5秒去抢注，给原先的主master一个机会
					delayExector.schedule(new Runnable() {
						
						public void run() {
							attackMaster();
							
						}
					}, 5, TimeUnit.SECONDS);
				}
				
			}
			
			public void handleDataChange(String dataPath, Object data) throws Exception {
				// do nothing
				
			}
		};
		
		// 订阅节点
		zc.subscribeDataChanges(masterPath, masterPathListener);
	}

	public void run() {
		attackMaster();
		try {
			Thread.sleep(runningTime);
		} catch (InterruptedException e) {
			// do nothing
		}
	}
	
	// 去争抢这个节点创建（注册）
	private void attackMaster()
	{
		try {
			// 注册节点
			zc.create(masterPath, myinfo, CreateMode.EPHEMERAL);
			// 如果当前注册成功了，那么他就是主master
			activeMasterInfo = myinfo;
			System.out.println("the active master is : " + activeMasterInfo);
		}
		catch (ZkNodeExistsException e)
		{
			// 当节点已经被其他master注册了
			activeMasterInfo = zc.readData(masterPath);
			// 当无法读取到节点信息则认为其他master可能宕机了，再去抢注
			if (null == activeMasterInfo)
			{
				attackMaster();
			}
			else{
				System.out.println(activeMasterInfo + " has become active! " + myinfo +  " wait for next time to be active!");
			}
		}
		catch (Exception e)
		{
			// 当发生其他错误的时候，不去例会
		}
		
	}
}
