package Util;


/**
 * @author jusang
 *  端口信息类
 */
public class PortClass {
	private String addr;
	private String name;
	private String portNo;
	
	public PortClass() {
		// TODO Auto-generated constructor stub		
	}

	public PortClass(String addr,String name,String portNo) {
		this.addr =addr;
		this.name = name;
		this.portNo = portNo;
	}
	
	public PortClass(String s) {
		// TODO Auto-generated constructor stub	
		String[] argues = s.split("\"");
		this.name = argues[3];
		this.portNo = argues[7];
		this.addr = argues[15];
	}
	
	public static void main(String[] args) {
/*		PortClass pc = new PortClass("\r\n" + 
				"                \"hw_addr\": \"12:87:95:97:5c:c8\", \r\n" + 
				"                \"name\": \"s1-eth1\", \r\n" + 
				"                \"port_no\": \"00000001\", \r\n" + 
				"                \"dpid\": \"0000000000000001\"");*/
		
		PortClass pc = new PortClass("\"hw_addr\": \"ee:5e:f1:36:75:93\", \"name\": \"s2-eth1\", \"port_no\": \"00000001\", \"dpid\": \"0000000000000002\"");
		//System.out.println();
		System.out.println(pc.addr);
		System.out.println(pc.name);
		System.out.println(pc.portNo);
	}
	
	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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
	
	public void print() {
		System.out.println(this.getPortNo()+"  "+this.getName()+"   "+this.getAddr());
	}
}


class SuBPort {
	public SuBPort() {
		// TODO Auto-generated constructor stub
	}
	
	public SuBPort(String portRes) {
		super ();
	
		
	}
	

}