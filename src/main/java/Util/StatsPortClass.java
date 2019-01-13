package Util;


/**
 * @author jusang
 *  端口类，封装端口的统计信息
 */
public class StatsPortClass {
	private String txDropped;
	private String rxDropped;
	private String rxPackets;
	private String txBytes;
	private String rxBytes;
	private String txErrors;
	private String rxErrors;
	private String port;
	private String txPackets;

	public StatsPortClass() {
		// TODO Auto-generated constructor stub
	}
	

	public String getTxDropped() {
		return txDropped;
	}


	public void setTxDropped(String txDropped) {
		this.txDropped = txDropped;
	}


	public String getRxDropped() {
		return rxDropped;
	}


	public void setRxDropped(String rxDropped) {
		this.rxDropped = rxDropped;
	}


	public String getRxPackets() {
		return rxPackets;
	}


	public void setRxPackets(String rxPackets) {
		this.rxPackets = rxPackets;
	}


	public String getTxBytes() {
		return txBytes;
	}


	public void setTxBytes(String txBytes) {
		this.txBytes = txBytes;
	}


	public String getRxBytes() {
		return rxBytes;
	}


	public void setRxBytes(String rxBytes) {
		this.rxBytes = rxBytes;
	}


	public String getTxErrors() {
		return txErrors;
	}


	public void setTxErrors(String txErrors) {
		this.txErrors = txErrors;
	}


	public String getRxErrors() {
		return rxErrors;
	}


	public void setRxErrors(String rxErrors) {
		this.rxErrors = rxErrors;
	}


	public String getPort() {
		return port;
	}


	public void setPort(String port) {
		this.port = port;
	}


	public String getTxPackets() {
		return txPackets;
	}


	public void setTxPackets(String txPackets) {
		this.txPackets = txPackets;
	}

	//一般端口
	public StatsPortClass(String res) {
		this();
		String[] str = res.split("\"");
		this.port = str[2];
		this.rxBytes = str[8];
		this.txBytes = str[24];
		this.rxPackets = str[20];
		this.txPackets = str[16];
		this.rxDropped = str[26];
		this.txDropped = str[18];
		this.rxErrors = str[14];
		this.txErrors = str[10];
	}

	//	LOCAL端口，但好像没用
	public StatsPortClass(String res,boolean isLOCAL) {
		//this();
		String[] str = res.split("\"");
		this.port = str[3];
		this.rxBytes = str[10];
		this.txBytes = str[26];
		this.rxPackets = str[22];
		this.txPackets = str[18];
		this.rxDropped = str[28];
		this.txDropped = str[20];
		this.rxErrors = str[16];
		this.txErrors = str[12];
	}
	
	//打印 垃圾代码
	public void print() {
		System.out.println("端口"+this.port);
		System.out.println("接收字节数"+this.rxBytes);
		System.out.println("发送字节数"+this.txBytes);
		System.out.println("接收包数"+this.rxPackets);
		System.out.println("发送包数"+this.txPackets);
		System.out.println("接收时丢包数"+this.rxDropped);
		System.out.println("发送时丢包数"+this.txDropped);
		System.out.println("接收报错数"+this.rxErrors);
		System.out.println("发送报错数"+this.txErrors);
	}
	
	public static void main(String[] args) {
		//StatsPortClass spc =new StatsPortClass("\"tx_dropped\": 0, \"rx_packets\": 0, \"rx_crc_err\": 0, \"tx_bytes\": 0, \"rx_dropped\": 297, \"port_no\": \"LOCAL\", \"rx_over_err\": 0, \"rx_frame_err\": 0, \"rx_bytes\": 0, \"tx_errors\": 0, \"collisions\": 0, \"rx_errors\": 0, \"tx_packets\": 0");
		StatsPortClass spc =new StatsPortClass("\"port_no\": 3, \"rx_over_err\": 0, \"rx_frame_err\": 0, \"rx_bytes\": 5769042, \"tx_errors\": 0, \"collisions\": 0, \"rx_errors\": 0, \"tx_packets\": 95952\"tx_dropped\": 0, \"rx_packets\": 96032, \"rx_crc_err\": 0, \"tx_bytes\": 5761129, \"rx_dropped\": 0," + 
			"");
		//boolean flag =true;
		 spc =new StatsPortClass("\"port_no\": \"LOCAL\", \"rx_over_err\": 0, \"rx_frame_err\": 0, \"rx_bytes\": 0, \"tx_errors\": 0, \"collisions\": 0, \"rx_errors\": 0, \"tx_packets\": 0\"tx_dropped\": 0, \"rx_packets\": 0, \"rx_crc_err\": 0, \"tx_bytes\": 0, \"rx_dropped\": 306, \r\n" + 
		 		"",true);
		spc.print();
/*		System.out.println("�˿�"+spc.port);
		System.out.println("�����ֽ���"+spc.rxBytes);
		System.out.println("�����ֽ���"+spc.txBytes);
		System.out.println("���հ���"+spc.rxPackets);
		System.out.println("���Ͱ���"+spc.txPackets);
		System.out.println("����ʱ������"+spc.rxDropped);
		System.out.println("����ʱ������"+spc.txDropped);
		System.out.println("���ձ�����"+spc.rxErrors);
		System.out.println("���ͱ�����"+spc.txErrors);*/
		//System.out.println("\"tx_dropped\": 0, \"rx_packets\": 0, \"rx_crc_err\": 0, \"tx_bytes\": 0, \"rx_dropped\": 297, \"port_no\": \"LOCAL\", \"rx_over_err\": 0, \"rx_frame_err\": 0, \"rx_bytes\": \r\n" + 
			//	"0, \"tx_errors\": 0, \"collisions\": 0, \"rx_errors\": 0, \"tx_packets\": 0");
	}
}
