package utils.abstractFrame;

import java.io.Serializable;

public class Host implements Serializable{
	
	

	private Port port;
	private String macAddr;
	private String ipv4;
	private String ipv6;
	private String name;
	private String portNo;
	private String dpid;
	
	@Override
	public String toString() {
		return "Host [ipv4=" + ipv4 + ", name=" + name + ", portNo=" + portNo + ", dpid=" + dpid + "]";
	}
	
	public Host(String ipv4, String name, String portNo, String dpid) {
		super();
		this.ipv4 = ipv4;
		this.name = name;
		this.portNo = portNo;
		this.dpid = dpid;
	}

	public Host(Port port, String ipv4) {
		super();
		this.port = port;
		this.ipv4 = ipv4;
	}
	
/*	public Host(String ipv4, String portNo, String dpid) {
		super();
		this.ipv4 = ipv4;
		this.portNo = portNo;
		this.dpid = dpid;
	}*/
	
	public Host(String ipv4, String name, String dpid) {
		super();
		this.ipv4 = ipv4;
		this.name = name;
		this.dpid = dpid;
	}
	
	public Port getPort() {
		return port;
	}
	public void setPort(Port port) {
		this.port = port;
	}
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	public String getIpv4() {
		return ipv4;
	}
	public void setIpv4(String ipv4) {
		this.ipv4 = ipv4;
	}
	public String getIpv6() {
		return ipv6;
	}
	public void setIpv6(String ipv6) {
		this.ipv6 = ipv6;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPortNo() {
		return portNo;
	}
	public void setPortNo(String portNo) {
		this.portNo = portNo;
	}
	public String getDpid() {
		return dpid;
	}
	public void setDpid(String dpid) {
		this.dpid = dpid;
	}

	
	
	
	
}
