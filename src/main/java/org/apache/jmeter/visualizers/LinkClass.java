package org.apache.jmeter.visualizers;

import java.util.ArrayList;
import java.util.List;

public class LinkClass {
	private String srcPort;
	private String dstPort;
	private String dstSwitch;
	private String srcPName;
	private String dstPName;
	

	public LinkClass(String srcPort,String dstPort,String dstSwitch) {
		//super();
		this.srcPort = srcPort;
		this.dstPort = dstPort;
		this.dstSwitch = dstSwitch;
	}
	
	public LinkClass() {
		
	}
	
	public LinkClass(String s) {
		
		String[] sp = s.split("\"");
		this.srcPort = sp[7];
		this.dstPort = sp[25];
		this.dstSwitch = sp[29];
	}
	
	public LinkClass(String s,boolean addPortName) {
		
		String[] sp = s.split("\"");
		this.srcPort = sp[7];
		this.dstPort = sp[25];
		this.dstSwitch = sp[29];
		this.srcPName = sp[3];
		this.dstPName = sp[21];
	}
	
	public String getSrcPort() {
		return srcPort;
	}

	public void setSrcPort(String srcPort)
	{
		this.srcPort = srcPort;
	}
	
	
	public String getSrcPName() {
		return srcPName;
	}

	public void setSrcPName(String srcPName) {
		this.srcPName = srcPName;
	}

	public String getDstPName() {
		return dstPName;
	}

	public void setDstPName(String dstPName) {
		this.dstPName = dstPName;
	}

	public String getDstPort() {
		return dstPort;
	}

	public void setDstPort(String dstPort) {
		this.dstPort = dstPort;
	}
	
	public String getDstSwitch() {
		return dstSwitch;
	}
	
	public void setDstSwitch(String dstSwitch) {
		this.dstSwitch = dstSwitch;
	}


	public static void main(String[] args) {
		
/*		String s="[{\"src\": {\"hw_addr\": \"a6:d5:e4:6c:25:bc\", \"name\": \"s5-eth1\", \"port_no\": \"00000001\", \"dpid\": \"0000000000000005\"}, \"dst\": {\"hw_addr\": \"52:64:8c:04:81:52\", \"name\": \"s6-eth3\", \"port_no\": \"00000003\", \"dpid\": \"0000000000000006\"}}, {\"src\": {\"hw_addr\": \"1e:17:86:ef:43:5f\", \"name\": \"s5-eth2\", \"port_no\": \"00000002\", \"dpid\": \"0000000000000005\"}, \"dst\": {\"hw_addr\": \"42:7e:90:24:3f:7c\", \"name\": \"s7-eth3\", \"port_no\": \"00000003\", \"dpid\": \"0000000000000007\"}}, {\"src\": {\"hw_addr\": \"d2:cd:92:49:8e:81\", \"name\": \"s5-eth3\", \"port_no\": \"00000003\", \"dpid\": \"0000000000000005\"}, \"dst\": {\"hw_addr\": \"62:ee:1f:87:3a:88\", \"name\": \"s1-eth2\", \"port_no\": \"00000002\", \"dpid\": \"0000000000000001\"}}]";
		LinkOfSwitch los = new LinkOfSwitch();
		
		List<String> al = new ArrayList<String>();
		
		al = los.getLinkList(s);
		
		LinkClass[] lc = new LinkClass[al.size()];
		
	
		for(int i=0;i<al.size();i++) {
			lc[i] = new LinkClass(al.get(i));
			System.out.println(lc[i].getDstPort());
			System.out.println(lc[i].getDstSwitch());
			System.out.println(lc[i].getSrcPort());
		}*/
	
	}
		
	
	
}
