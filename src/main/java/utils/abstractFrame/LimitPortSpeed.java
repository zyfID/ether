package utils.abstractFrame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import Util.BatUtil;
import Util.SplitResponesByDpid;

/**
 * @author jusang
 * 本类的功能是实现抽象配置中的端口速率功能
 *   端口速率控制的主要思路是  限制该端口下的所有主机的访问速率，所以要获取位于该端口下的所有主机IP
 *   		
 *   
 */
public class LimitPortSpeed {
	
	/**
	 * 1.获取交换机下的所有主机IP
	 * 		编写jmx文件，bat文件，读取jtl
	 * */
	
	//1.1   编写jmx文件
	public static void writeJmx(String switchName){
		//LimitSpeedOfCertainPortOfCertainDpTem.jmx
		File templateJmx = new File("E:/apache-jmeter-4.0/bin/LimitSpeedOfCertainPortOfCertainDpTem.jmx");
		File newJmx = new File("E:/apache-jmeter-4.0/bin/LimitSpeedOfCertainPortOfCertainDp.jmx");
		SplitResponesByDpid srb = new SplitResponesByDpid();
		String temStr;
		try {
			temStr = srb.readFile(templateJmx);
			temStr = temStr.replaceAll("0000000000000001", switchName);
			if(newJmx.exists()){
				newJmx.delete();
			}
			srb.writeToFile(temStr, newJmx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//1.2   编写bat文件并运行
	public static void writeBat(String switchName){
		writeJmx(switchName);
		File jtlFile = new File("E:/apache-jmeter-4.0/bin/LimitSpeedOfCertainPortOfCertainDp.jtl");
		if(jtlFile.exists()){
			jtlFile.delete();
		}
		String s = "cmd /c start /b E:/apache-jmeter-4.0/bin/bat/portAbstractNew.bat";
		new BatUtil(s);
	}
	
	//1.3  读取jtl，将字符串转换成一个个Host对象
	public static List<Host> readJtl(String switchName){
		writeBat(switchName);
		File jtlFile = new File("E:/apache-jmeter-4.0/bin/LimitSpeedOfCertainPortOfCertainDp.jtl");
		int index = 0;
		SplitResponesByDpid srb = new SplitResponesByDpid();
		List<Host> hosts =null;
		while(!jtlFile.exists()){
			if(index>20){
				System.out.println("读取端口限速结果出错了");
				return null;
			}
			index++;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String jtlStr;
			try {
				jtlStr =srb.readFile(jtlFile);
				hosts = HostUtil.getHostInfoFromList(HostUtil.getListFromJtl(jtlStr));
				/*for (Host host : hosts) {
					System.out.println(host.toString());
				}*/
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return hosts;
	}
	
	//1.3 读取jtl文件
	public static String readJtlToString(String switchName){
		writeBat(switchName);
		File jtlFile = new File("E:/apache-jmeter-4.0/bin/LimitSpeedOfCertainPortOfCertainDp.jtl");
		int index = 0;
		SplitResponesByDpid srb = new SplitResponesByDpid();
		List<Host> hosts =null;
		while(!jtlFile.exists()){
			if(index>20){
				System.out.println("读取端口限速结果出错了");
				return null;
			}
			index++;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		String jtlStr=null;
		try {
			jtlStr =srb.readFile(jtlFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jtlStr;
	}
	
	@Test
	public void fun1(){
		String s = readJtlToString("0000000000000001");
		System.out.println(s);
	}
	
	//1.3  读取jtl，将字符串转换成一个个Host对象,不做序列化，直接进行匹配，输出匹配成功的结果
	public static List<String> readJtl(String switchName,String PortNo){
		writeBat(switchName);
		File jtlFile = new File("E:/apache-jmeter-4.0/bin/LimitSpeedOfCertainPortOfCertainDp.jtl");
		int index = 0;
		SplitResponesByDpid srb = new SplitResponesByDpid();
		List<String> IpList = new ArrayList<>();
		while(!jtlFile.exists()){
			if(index>20){
				System.out.println("读取端口限速结果出错了");
				return null;
			}
			index++;
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String jtlStr;
			try {
				jtlStr =srb.readFile(jtlFile);
				List<Host> hosts = HostUtil.getHostInfoFromList(HostUtil.getListFromJtl(jtlStr));
				for (Host host : hosts) {
					//System.out.println(host.toString());
					if(host.getPortNo().equals(PortNo)){
						IpList.add(host.getIpv4());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//return new String();
		return IpList;
	}
	
	@Test
	public void fun(){
		
		List<String> IpList = readJtl("0000000000000001","\"00000001\"");
		for (String string : IpList) {
			System.out.println(string);
		}
	}
	
	//1.4  将结果反序列化，并根据交换机端口匹配主机IP地址 
	public static List<String>  getIpAddr(){
		return new ArrayList<>();
	}
	
	//
	/**
	 * 2.对上述结果得到的IP进行限速，可能需要使用添加流表项的代码
	 * **/
	public void deployPolicies(List<String> IpList){
		
	}
	
	
	
	
	
	
}
