package com.xufeng.testzkclient;

import java.io.Serializable;

/**
 * 用于测试zonde的数据
 * @author apple
 *
 */

// 必须实现序列化接口
public class Server  implements Serializable{
	private String name = null;
	private String id = null;
	
	public Server(String name, String id) {
		super();
		this.name = name;
		this.id = id;
	}
	// 序列化与反序列化中必须要有这个构造方法(默认是有的，但是你一旦创建了其他构造方法，那么就默认没有了)
	public Server() {
		super();
		this.name = null;
		this.id = null;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Server other = (Server) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Server [name=" + name + ", id=" + id + "]";
	}
}
