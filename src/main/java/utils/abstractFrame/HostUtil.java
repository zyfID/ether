package utils.abstractFrame;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;


import utils.classes.Port;

public class HostUtil {
	
	//从结果文件中找出IP地址信息	
	public static List<String> getIpAddressList(String res){
		String regex = "\\d+\\.\\d+\\.\\d+\\.\\d+";
		List<String> list = new ArrayList<>();
		Matcher matcher  = Pattern.compile(regex).matcher(res);
		while(matcher.find()){
			String s = matcher.group();
			if(!list.contains(s)){
				if(s.equals("0.0.0.0")){
					continue;
				}
				list.add(s);
			}	
		}
		Collections.sort(list);
		return list;
	}
	
	//从结果文件中找出网桥名称信息	
	public static String getDpName(String res){
		String regex = "eth\\d";
		Matcher matcher  = Pattern.compile(regex).matcher(res);
		String s ="";
		while(matcher.find()){
			 s = matcher.group();
		}
		return s;
	}
	
	//从结果文件中找出网桥端口号信息
	public static String getDpPort(String res){
		String regex = "\"\\d{8}\"";  //加上引号以防止匹配到交换机id号
		Matcher matcher  = Pattern.compile(regex).matcher(res);
		String s ="";
		while(matcher.find()){
			 s = matcher.group();
		}
		return s;
	}
	
	//从结果文件中找出网桥id
	public static String getDpId(String res){
		String regex = "\\w{16}";
		Matcher matcher  = Pattern.compile(regex).matcher(res);
		String s ="";
		while(matcher.find()){
			 s = matcher.group();
		}
		return s;
	}
	
	
	//从jtl文件中提取相关的主机信息
	/*
{""port"": {""hw_addr"": ""00:90:27:ef:4e:59"", ""name"": ""eth3"", ""port_no"": ""00000004"", ""dpid"": ""0000000000000001""}, 
""mac"": ""54:a0:50:51:82:2f"", ""ipv4"": [""0.0.0.0"", ""10.20.3.167""], ""ipv6"": [""::"", ""fe80::4c57:9c6f:a8a6:f78c""]}, 

{""port"": {""hw_addr"": ""00:90:27:ef:4e:59"", ""name"": ""eth3"", ""port_no"": ""00000004"", ""dpid"": ""0000000000000001""},
 ""mac"": ""04:92:26:d5:fe:5e"", ""ipv4"": [""169.254.117.201"", ""169.254.189.252"", ""169.254.179.99"", ""169.254.65.69"", ""0.0.0.0"", ""10.20.218.87""],
  ""ipv6"": [""fe80::bc2d:c39e:4e57:6bb9"", ""fe80::98ac:87a6:baa4:75c9"", ""fe80::61e1:b5eb:de34:bdfc"", ""fe80::f40f:9a1f:6486:b363"", ""::"", ""fe80::b5c2:e094:8e95:4145""]},
  
   {""port"": {""hw_addr"": ""00:90:27:ef:4e:59"", ""name"": ""eth3"", ""port_no"": ""00000004"", ""dpid"": ""0000000000000001""}, 
   ""mac"": ""e2:eb:33:33:00:00"", ""ipv4"": [], ""ipv6"": []}, 
   
 
	 * 
	 * */
	
	//将jtl中的结果转换为元素为String的List
	public static List<String> getListFromJtl(String resJtl){
		String[] split = resJtl.split("hw");
		List<String> list = new ArrayList<>();
		for (String string : split) {
			if(string.indexOf("ipv4")!=-1){ // 如果一个字符串不包括“ipv4”，则该字符串为无效字符
				list.add(string);
			}
		}
		return list;
	}
		
	
	/**
	 * {""port"": {""hw_addr"": ""00:90:27:ef:4e:59"", ""name"": ""eth3"", ""port_no"": ""00000004"", ""dpid"": ""0000000000000001""}, 
		""mac"": ""54:a0:50:51:82:2f"", ""ipv4"": [""0.0.0.0"", ""10.20.3.167""], ""ipv6"": [""::"", ""fe80::4c57:9c6f:a8a6:f78c""]}, 
	 * */
	public static List<Host> getHostInfoFromList(List<String> resList){
		//resList中，元素如上所示
		//将每个String元素转换成Host对象
		List<Host> hosts = new ArrayList<>();
		//String ipv4;
		String name;
		String portNo;
		String dpid;
		List<String> ipv4s;   //一个物理机可能会有多个虚拟机
/*		String regex;
		Matcher matcher;*/
		for(int i=0;i<resList.size();i++){
			//匹配ip地址
			String res = resList.get(i);
			ipv4s = HostUtil.getIpAddressList(res);
			name = HostUtil.getDpName(res);
			portNo = HostUtil.getDpPort(res);
			dpid = HostUtil.getDpId(res);
			if(ipv4s==null || name==null || portNo == null || dpid ==null){
				continue;
			}
			else{
				for (String ipv4 : ipv4s) {
					/*System.out.println("ipv4="+ipv4);
					System.out.println("name="+name);
					System.out.println("portNo="+portNo);
					System.out.println("dpid="+dpid);*/
					Host host = new Host(ipv4, name, portNo, dpid);
					System.out.println(host.toString());
					hosts.add(host);
				}
			}
		}
		return hosts;
	}
	
	
	@Test
	public void fun(){
		String string = "{\"\"port\"\": {\"\"hw_addr\"\": \"\"00:90:27:ef:4e:59\"\", \"\"name\"\": \"\"eth3\"\", \"\"port_no\"\": \"\"00000004\"\", \"\"dpid\"\": \"\"0000000000000001\"\"},"+
	"\"\"mac\"\": \"\"54:a0:50:51:82:2f\"\", \"\"ipv4\"\": [\"\"0.0.0.0\"\", \"\"10.20.3.167\"\"], \"\"ipv6\"\": [\"\"::\"\", \"\"fe80::4c57:9c6f:a8a6:f78c\"\"]},";
		
		List<String> list = new ArrayList<>();
		list.add(string);
		List<Host> hosts = HostUtil.getHostInfoFromList(list);
		for (Host host : hosts) {
			System.out.println(host.getDpid()+"  "+ host.getIpv4()+"  "+ host.getName()+"  "+host.getPortNo());
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
