package utils.abstractFrame;

public class Port {
	private String macAddr;
	private String name;
	private String portNo;
	private String dpid;
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
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
	public Port(String macAddr, String name, String portNo, String dpid) {
		super();
		this.macAddr = macAddr;
		this.name = name;
		this.portNo = portNo;
		this.dpid = dpid;
	}
	public Port() {
		super();
	}
	public Port(String name, String portNo, String dpid) {
		super();
		this.name = name;
		this.portNo = portNo;
		this.dpid = dpid;
	}
	public Port(String name, String dpid) {
		super();
		this.name = name;
		this.dpid = dpid;
	}
	public Port(String portNo, String dpid, boolean usingPortNo) {
		super();
		this.portNo = portNo;
		this.dpid = dpid;
	}
	
	
}
