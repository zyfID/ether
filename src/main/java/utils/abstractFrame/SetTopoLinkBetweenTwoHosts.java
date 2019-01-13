package utils.abstractFrame;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Util.BatUtil;
import Util.FileUtil;
import Util.SplitResponesByDpid;
import getLinks.GetLinks;

/**
 * @author jusang
 *   为两个主机建立一条链路。先获取需要先知道连接这两台主机的网络端口，然后为端口建立链路
 *   ！！！当s1连接s2,s2连接h1的时候，获取s1的主机信息时，不会出现h1的信息！！！
 */
public class SetTopoLinkBetweenTwoHosts {
	
	/**
	 * 1.获取两个IP的所在交换机及端口
	 * 
	 * */
	public static List<Host> getHostInfo(String ipAddr1,String ipAddr2){
		//获取交换机列表
		List <Host> hosts = new ArrayList<>();
		List <Host> temHosts;
		List<String> dpidList = FileUtil.fileReadLineDpid("E:\\apache-jmeter-4.0\\bin\\db\\dpid.txt");
		for (String string : dpidList) {
			//找到了ip1所在的交换机
			if(HasSuchHost(string, ipAddr1)){
				//将该交换机下的主机信息转换成一个hosts列表，并对根据IP地址匹配，匹配上则返回该host
				temHosts = LimitPortSpeed.readJtl(string);
				for (Host host : temHosts) {
					if(host.getIpv4().equals(ipAddr1)){
						hosts.add(host);
					}
				}
			}
			//找到了ip2所在的交换机
			if(HasSuchHost(string, ipAddr2)){
				//将该交换机下的主机信息转换成一个hosts列表，并对根据IP地址匹配，匹配上则返回该host
				temHosts = LimitPortSpeed.readJtl(string);
				for (Host host : temHosts) {
					if(host.getIpv4().equals(ipAddr2)){
						hosts.add(host);
					}
				}
			}
		}
		return hosts;
	}
	
	@Test
	public void fun(){
		getHostInfo(null, null);
	}
	
	
	
	//util 获取指定交换机的hosts信息，判断指定的主机是否位于该节点下
	public static boolean HasSuchHost(String switchName,String ipAddr){
		String jtl = LimitPortSpeed.readJtlToString(switchName);
		if((jtl.indexOf(ipAddr))==-1){
			return false;
		}
		else{
			return true;
		}   
	}
	
	
	//
	
	//
	/**
	 * @param ipAddr1 第一个主机的ip地址
	 * @param ipAddr2 第二个主机的IP地址
	 * @param qosLevel qos等级
	 * 
	 */
	public static void setTopoLink(String ipAddr1,String ipAddr2,String qosLevel){
		//1.现获取这两个主机位于位于哪两个交换机上
		//1.1  两台主机位于同一个交换机，则不需要设置
		//1.2 位于不同交换机上，则需要配置链路
		List<Host> hosts = getHostInfo(ipAddr1, ipAddr2);
		Host h1 = hosts.get(0);
		Host h2 = hosts.get(1);
		//判断是否位于同一个交换机
		//相等
		if(h1.getDpid().equals(h2.getDpid())){
			return ;
		}
		//否则不相等		
		//2.获取这两台交换机间的最短路径，利用迪杰斯特拉算法
		List<String> al = new GetLinks().getIFsWithDpidAndIF(h1.getDpid(), h2.getDpid());
		/*
		 * 结果示例：
		 * 0000000000000006,00000003      交换机dpid，交换机端口号 
			0000000000000005,00000001
			两组表示一条链路，即交换机6的3端口连接交换机5的1端口
			
			0000000000000005,00000003
			0000000000000001,00000002
			
			0000000000000001,00000001
			0000000000000002,00000003
			
			0000000000000002,00000002
			0000000000000004,00000003
		 * */
		
		//3.根据交换机的链路信息配置命令
		//3.1 正向 1->2
		writeJmxAndRun(al, 0, ipAddr1, ipAddr2, qosLevel);
		
		//3.2 逆向 2->1  
		writeJmxAndRunReverse(al, 1, ipAddr1, ipAddr2, qosLevel);
	/*	{"ipv4_src": "192.168.0.1", "eth_type": 2048}
		{"ipv4_dst": "192.168.10.10/255.255.255.0", "eth_type": 2048}*/
		/*
		 * {"type": "SET_QUEUE", "queue_id": 7}
		 * "OUTPUT", "port": 3
		 * */
		
		
	}
	
	//配置网络策略
	
	/**
	 * @param al    链路信息 “交换机id，端口”
	 * @param start    起始为零，表正向；起始为一，表负向
	 * @param ipAddr1   
	 * @param ipAddr2
	 * @param level
	 */
	public static void writeJmxAndRun(List<String>al, int start,String ipAddr1,String ipAddr2, String level){
		File temJmx = new File("E:/apache-jmeter-4.0/bin/setTopoLinkTem.jmx");
		while(start<al.size()){
			File newJmx = new File("E:/apache-jmeter-4.0/bin/setTopoLinkNew.jmx");
			SplitResponesByDpid srb = new SplitResponesByDpid();
			String temStr;
			String[] strArr = new String[2];
			strArr = al.get(start).split(",");
			try {
				temStr = srb.readFile(temJmx);
				//替换tem文件中的相关数据
				//1. 源目的主机ip
				temStr = temStr.replace("192.168.0.1", ipAddr1);
				temStr = temStr.replace("192.168.10.10", ipAddr2);
				//2.交换机id
				temStr = temStr.replaceAll(" &quot;dpid&quot;: 0000000000000001,&#xd;", 
						" &quot;dpid&quot;: "+strArr[0]+",&#xd;");
				//3.qosLevel
				//quot;type&quot;: &quot;SET_QUEUE&quot;, &quot;queue_id&quot;: 7
				temStr = temStr.replaceAll("quot;type&quot;: &quot;SET_QUEUE&quot;, &quot;queue_id&quot;: 7",
						"quot;type&quot;: &quot;SET_QUEUE&quot;, &quot;queue_id&quot;: "+ level);
				//4.端口 &quot;port&quot;: 2
				temStr = temStr.replaceAll("&quot;port&quot;: 2", "&quot;port&quot;: "+strArr[1]);
				if(newJmx.exists()){
					newJmx.delete();
				}
				srb.writeToFile(temStr, newJmx);  //将新的字符串写入文件中
				//之后start+2
				start+=2;
				//然后运行这个文件
				/*
				 * File temBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowentryAdd.bat");
				String s ="cmd /c start /b " + temBat.getPath();
				new BatUtil(s);	
				
				SetTopoLink.bat
				 * */
				File temBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\SetTopoLink.bat");
				String s ="cmd /c start /b " + temBat.getPath();
				new BatUtil(s);	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}
	
	
	//调换源目的ip地址
	public static void writeJmxAndRunReverse(List<String>al, int start,String ipAddr1,String ipAddr2, String level){
		File temJmx = new File("E:/apache-jmeter-4.0/bin/setTopoLinkTem.jmx");
		while(start<al.size()){
			File newJmx = new File("E:/apache-jmeter-4.0/bin/setTopoLinkNew.jmx");
			SplitResponesByDpid srb = new SplitResponesByDpid();
			String temStr;
			String[] strArr = new String[2];
			strArr = al.get(start).split(",");
			try {
				temStr = srb.readFile(temJmx);
				//替换tem文件中的相关数据
				//1. 源目的主机ip
				temStr = temStr.replace("192.168.0.1", ipAddr2);     //<--这里
				temStr = temStr.replace("192.168.10.10", ipAddr1);
				//2.交换机id
				temStr = temStr.replaceAll(" &quot;dpid&quot;: 0000000000000001,&#xd;", 
						" &quot;dpid&quot;: "+strArr[0]+",&#xd;");
				//3.qosLevel
				//quot;type&quot;: &quot;SET_QUEUE&quot;, &quot;queue_id&quot;: 7
				temStr = temStr.replaceAll("quot;type&quot;: &quot;SET_QUEUE&quot;, &quot;queue_id&quot;: 7",
						"quot;type&quot;: &quot;SET_QUEUE&quot;, &quot;queue_id&quot;: "+ level);
				//4.端口 &quot;port&quot;: 2
				temStr = temStr.replaceAll("&quot;port&quot;: 2", "&quot;port&quot;: "+strArr[1]);
				if(newJmx.exists()){
					newJmx.delete();
				}
				srb.writeToFile(temStr, newJmx);  //将新的字符串写入文件中
				//之后start+2
				start+=2;
				//然后运行这个文件
				/*
				 * File temBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\FlowentryAdd.bat");
				String s ="cmd /c start /b " + temBat.getPath();
				new BatUtil(s);	
				
				SetTopoLink.bat
				 * */
				File temBat = new File("E:\\apache-jmeter-4.0\\bin\\bat\\SetTopoLink.bat");
				String s ="cmd /c start /b " + temBat.getPath();
				new BatUtil(s);	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		

	}
	

}
