package com.xufeng.master;

public class MasterFailoverController {

	public static void main(String[] args) throws InterruptedException {
		String masterPath = "/master";
		// 先去创建一个master
		Master masterA = new Master(new MasterInfo("A", "masterA"), Long.MAX_VALUE, masterPath);
		Master masterB = new Master(new MasterInfo("B", "masterB"), Long.MAX_VALUE, masterPath);
		Master masterC = new Master(new MasterInfo("C", "masterC"), Long.MAX_VALUE, masterPath);
		Master masterD = new Master(new MasterInfo("D", "masterD"), Long.MAX_VALUE, masterPath);
		Master masterE = new Master(new MasterInfo("E", "masterE"), Long.MAX_VALUE, masterPath);
		Master masterF = new Master(new MasterInfo("F", "masterF"), Long.MAX_VALUE, masterPath);
		Master masterG = new Master(new MasterInfo("G", "masterG"), Long.MAX_VALUE, masterPath);
		Master masterH = new Master(new MasterInfo("H", "masterH"), Long.MAX_VALUE, masterPath);
		
		// 以下线程启动后将会去争抢成为主master，到底哪个会成功呢:>
		new Thread(masterA).start();
		new Thread(masterB).start();
		new Thread(masterC).start();
		new Thread(masterD).start();
		new Thread(masterE).start();
		new Thread(masterF).start();
		new Thread(masterG).start();
		new Thread(masterH).start();
	}

}
