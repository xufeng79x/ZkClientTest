package com.xufeng.testzkclient;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.BytesPushThroughSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher.Event.KeeperState;

public class ZkClientScribeTest {

	public static void main(String[] args) throws InterruptedException {
		String znodePath = "/xufeng";
		String znodeChildPath1 = "/xufeng/xufeng";
		String znodeChildPath2 = "/xufeng/xufeng";
		
		// 创建连接（既创建session）
		//param1:zookeeper集群信息
		//param2:session超时时间
		//param3:连接超时时间
		//param4:序列化方法，既我们设定在znode上的数据类的序列与反序列方法
		ZkClient zc = new ZkClient("xufeng-1:2181,xufeng-2:2181,xufeng-3:2181", 10000, 10000, new BytesPushThroughSerializer());
		System.out.println("connected ok!");
		
		// 创建节点与其子节点，为了易于操作，我们将以永久节点来进行测试
		zc.create(znodePath, "I am /xufeng".getBytes(), CreateMode.PERSISTENT);
		zc.create(znodeChildPath1, "I am /xufeng/xufeng*0".getBytes(), CreateMode.PERSISTENT_SEQUENTIAL);
		zc.create(znodeChildPath2, "I am /xufeng/xufeng*1".getBytes(), CreateMode.PERSISTENT_SEQUENTIAL);
		
		// 订阅节点数据发生变化
		zc.subscribeDataChanges(znodePath, new IZkDataListener() {
			// 当其本身或者子节点的删除或者增加的时候讲触发此操作
			public void handleDataDeleted(String dataPath) throws Exception {
				System.out.println(dataPath + " data has been deleted!");
			}
			
			public void handleDataChange(String dataPath, Object data) throws Exception {
				System.out.println(dataPath + " data has been change to " + String.valueOf(data));
			}
		});
		
		// 订阅节点及其子节点发生变化
		zc.subscribeChildChanges(znodePath, new IZkChildListener() {
			// 当其本身或者子节点的删除或者增加的时候讲触发此操作
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println(parentPath + " childs has been change : ");
				System.out.println(currentChilds);
				
			}
		});
		
		// 订阅节点状态发生变化
		zc.subscribeStateChanges(new IZkStateListener() {
			public void handleStateChanged(KeeperState state) throws Exception {
				System.out.println(state);
				
			}
			
			public void handleSessionEstablishmentError(Throwable error) throws Exception {
				System.out.println(error);
				
			}
			
			public void handleNewSession() throws Exception {
				System.out.println("new session!");
				
			}
		});
		
		
		//  使得进程保持，便于观察节点订阅效果
		Thread.sleep(Long.MAX_VALUE);

	}

}
