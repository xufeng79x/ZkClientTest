package com.xufeng.testzkclient;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;

public class saveEphemeralNode {
	public static void main(String[] args) throws InterruptedException {
		String znodePath = "/xufeng_EPHEMERAL";
		
		// 创建连接（既创建session）
		//param1:zookeeper集群信息
		//param2:session超时时间
		//param3:连接超时时间
		//param4:序列化方法，既我们设定在znode上的数据类的序列与反序列方法
		ZkClient zc = new ZkClient("xufeng-1:2181,xufeng-2:2181,xufeng-3:2181", 10000, 10000, new SerializableSerializer());
		System.out.println("connected ok!");
		
		zc.create(znodePath, null, CreateMode.EPHEMERAL);
		
		Thread.sleep(Long.MAX_VALUE);
	}

}
