package com.xufeng.testzkclient;

import java.util.List;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

public class ZkClientCDESU {
	
	public static void main(String[] args) {
		String znodePath = "/xufeng";
		String znodeChildPath1 = "/xufeng/xufeng";
		String znodeChildPath2 = "/xufeng/xufeng";
		
		// 创建连接（既创建session）
		//param1:zookeeper集群信息
		//param2:session超时时间
		//param3:连接超时时间
		//param4:序列化方法，既我们设定在znode上的数据类的序列与反序列方法
		ZkClient zc = new ZkClient("xufeng-1:2181,xufeng-2:2181,xufeng-3:2181", 1000, 10000, new SerializableSerializer());
		System.out.println("connected ok!");
		
		//由于在创建此节点的时候已经指定了一般pojo类的序列类，所以我们可以直接将一个实例类在创建的时候设定到znode上去
		Server server = new Server("myserver", "123456");
		
		zc.create(znodePath, server, CreateMode.PERSISTENT);
		
		// 检查znode是否创建成功
		boolean isExist = zc.exists(znodePath);
		if (isExist)
		{
			System.out.println(znodePath + " has been created!");
		}
		
		// 勾取znode的状态信息
		Stat stat = new Stat();
		server = zc.readData(znodePath, stat);
		System.out.println("server info : " + server + " state : " + stat);
		
		// 创建子节点
		zc.create(znodeChildPath1, null, CreateMode.EPHEMERAL_SEQUENTIAL);
		zc.create(znodeChildPath2, null, CreateMode.EPHEMERAL_SEQUENTIAL);
		if (zc.exists(znodeChildPath1) && zc.exists(znodeChildPath2))
		{
			System.out.println("znodeChildPaths +  has been created!");
		}
		// 取得子节点列表
		List<String> childs = zc.getChildren(znodePath);
		System.out.println(childs);
		
		// 删除没有子节点的节点
		boolean e1 = zc.delete(znodeChildPath1);
		// 删除有子节点的节点
		boolean e2 = zc.deleteRecursive(znodePath);
		if (e1 && e2)
		{
			System.out.println("All znode have been deleted!");
		}
	}

}
