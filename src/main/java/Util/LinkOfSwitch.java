package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.Test;


/**
 * 	本类的功能是查看交换机的topo链路信息
 * 
 * */
public class LinkOfSwitch {
	
	private SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public int getNo(String s) {
		String[] split = s.split("src");
		int len = split.length-1;
		return len;
	}
	
	//替换模板中的相关的数据，生成新的jmx文件
	public void writeJmx(String switchName) {
		//在这里加进制转换
	/*	long sName = Long.parseLong(switchName, 16);
		switchName = Long.toString(sName);*/
		System.out.println("read writeJmx = "+switchName);
		File temJmx = new File("E:\\apache-jmeter-4.0\\bin\\TopoLinksTem.jmx");
		try {
			String temStr = srb.readFile(temJmx);

			temStr = temStr.replaceAll("/topology/links/0000000000000002</stringProp>",
					"/topology/links/"+switchName+"</stringProp>");
			File newJmx = new File("E:\\apache-jmeter-4.0\\bin\\TopoLinks"+switchName+".jmx");
			if(newJmx.exists()) {
				newJmx.delete();
			}
			srb.writeToFile(temStr, newJmx);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	@Test
	public void fun1(){
		//readJtl("0000009027f0358a");
		readJtl("1");
	}*/
	
	//替换模板中的相关的数据，生成新的bat脚本文件
	public void writeBatAndRun(String switchName) {
		//在这里加进制转换
	/*	long sName = Long.parseLong(switchName, 16);
		switchName = Long.toString(sName);*/
		System.out.println("read writeBatAndRun = "+switchName);	
		writeJmx(switchName);
		File newJtl = new File("E:\\apache-jmeter-4.0\\bin\\TopoLinks"+switchName+".jtl");
		newJtl.delete();
		File temScript = new File ("E:\\apache-jmeter-4.0\\bin\\bat\\TopoLinksTem.bat");
		File newScript = new File("E:\\apache-jmeter-4.0\\bin\\bat\\TopoLinks"+switchName+".bat");
		if(newScript.exists()) {
			newScript.delete();
		}
		try {
			String str = srb.readFile(temScript);
			//str = str.replaceAll("del /F /S /Q TopoLinks0000000000000002.jtl", 
					//"del /F /S /Q TopoLinks"+switchName+".jtl");
			str =str.replaceAll("jmeter -n -t TopoLinksTem.jmx -l TopoLinksTem.jtl", 
					"jmeter -n -t TopoLinks"+switchName+".jmx -l TopoLinks"+switchName+".jtl");
			srb.writeToFile(str, newScript);
			String s = "cmd /c start /b "+newScript.getPath();
			new BatUtil(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//读取jmx文件的运行结果，该结果被保存在相应的jtl文件中
	public String readJtl(String switchName ) {
/*		//在这里加进制转换
		long sName = Long.parseLong(switchName, 16);
		switchName = Long.toString(sName);*/
		System.out.println("read jtl = "+switchName);		
		writeBatAndRun(switchName);
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\TopoLinks"+switchName+".jtl");
		int index = 0 ;
		while(!jtlFile.exists()){
			if(index>20){
				System.out.println("there is no such file!");
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			index++;
		}
		String jtlStr ="" ;
		try {
			jtlStr = srb.readFile(jtlFile);		
			System.out.println(jtlStr);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//删除jtl
		
		return jtlStr;
	}

	@Test
	public void fun(){
		//String switchName ="0000009027f0358a";
		String switchName = "0000000000000001";
	   	//long z;
//	   	z = Long.parseLong(switchName, 16);
//    	//z = Integer.parseInt(switchName,16);
//    	switchName = Long.toString(z);
    	System.out.println(switchName);
		System.out.println(readJtl(switchName));
	}
	
//貌似没用到
/*	public List<String> getLinkList(String s ){
		String[] split = s.split("src");	
		for(int i=0;i<split.length;i++) {
			if(split[i].length()<4) {
				continue;
			}
			else {
				split[i] = split[i].substring(4);
				//split[i] = split[i].substring(0, 211);
			}
		}
		List<String> al = new ArrayList<String>();
		for(String str :split) {
			if(str.equals("[{\"")) {
				continue;
			}
			else {
				al.add(str);
			}
		}
		for(int i=0;i<al.size();i++) {
			System.out.println(al.get(i));
		}
		for(String tem :al) {
			System.out.println(tem.indexOf("\"na"));
		}
		al = this.rangeList(al);
		return al;
	}*/
	
	//应该是获取一个String型的里链路信息
	public List<String> getStringList(String res){
		List<String> ls = new ArrayList<String>();
		// ls = los.getLinkList(s);
		System.out.println(res);
		res = res.replace("\"\"", "\"");
		String[] strArr = res.split("src");
		for(String tem: strArr) {
			//System.out.println(tem);
			
			if(tem.contains("hw_addr")) {
				try {
					tem = tem.substring(tem.indexOf(":"));
					//tem = tem.substring(tem.indexOf(","));
				}catch(StringIndexOutOfBoundsException e){
					e.printStackTrace();
					System.out.println("Խ��");
				}
				
				ls.add(tem);
			}
		}
		return ls;
	}
	
	//将一个String类型的list转换为LinkClass类型的list
	public List<LinkClass> getLinkClassList(List<String> ls){
		List<LinkClass> lcList = new ArrayList<LinkClass>();
		for (String s : ls) {
			lcList.add(new LinkClass(s));
		}	
		return lcList;
	}
	//确认为垃圾代码
/*	public void printList(List<LinkClass> lcList) {
		 for (LinkClass linkClass : lcList) {
			System.out.print("src_name->"+linkClass.getSrcPort()+"   dst_name->"+linkClass.getDstPort()+"    dst_dpid->"+linkClass.getDstSwitch()  );
			System.out.println();
		 }
	}*/
	
	//貌似是垃圾代码
/*	public List<String> rangeList(List<String> l) {
		List<String> newList = new ArrayList<String>();

		String tem1 = "";
		String tem2 = "";
		for(String tem: l) {
			StringBuilder sb = new StringBuilder();
			tem1 = tem.substring(0, 31);
			tem2 = tem.substring(31);
			sb.append(tem2);
			sb.append(tem1);
			newList.add(new String (sb));
		}
		Collections.sort(newList);
		return newList;
	}*/
	
	//垃圾代码
	public static void main(String[] args) {
		//String s="[{\"src\": {\"hw_addr\": \"a6:d5:e4:6c:25:bc\", \"name\": \"s5-eth1\", \"port_no\": \"00000001\", \"dpid\": \"0000000000000005\"}, \"dst\": {\"hw_addr\": \"52:64:8c:04:81:52\", \"name\": \"s6-eth3\", \"port_no\": \"00000003\", \"dpid\": \"0000000000000006\"}}, {\"src\": {\"hw_addr\": \"1e:17:86:ef:43:5f\", \"name\": \"s5-eth2\", \"port_no\": \"00000002\", \"dpid\": \"0000000000000005\"}, \"dst\": {\"hw_addr\": \"42:7e:90:24:3f:7c\", \"name\": \"s7-eth3\", \"port_no\": \"00000003\", \"dpid\": \"0000000000000007\"}}, {\"src\": {\"hw_addr\": \"d2:cd:92:49:8e:81\", \"name\": \"s5-eth3\", \"port_no\": \"00000003\", \"dpid\": \"0000000000000005\"}, \"dst\": {\"hw_addr\": \"62:ee:1f:87:3a:88\", \"name\": \"s1-eth2\", \"port_no\": \"00000002\", \"dpid\": \"0000000000000001\"}}]";

/*		String[] split = s.split("src");

		for(int i=0;i<split.length;i++) {
			if(split[i].length()<4) {
				continue;
			}
			else {
				split[i] = split[i].substring(4);
				split[i] = split[i].substring(0, 211);
			}
		}
		
		List<String> al = new ArrayList<String>();
		for(String str :split) {
			if(str.equals("[{\"")) {
				continue;
			}
			else {
				al.add(str);
			}
		}
		*/
		
		LinkOfSwitch los = new LinkOfSwitch();
		los.writeJmx("0000000000000002");
		los.writeBatAndRun("0000000000000002");
		
		String s =los.readJtl("0000000000000002");
/*		 String s="timeStamp,elapsed,label,responseCode,responseMessage,threadName,dataType,success,failureMessage,bytes,sentBytes,grpThreads,allThreads,Latency,IdleTime,Connect\r\n" + 
				"1528270570378,55,TopoLinksTem,200,\"[{\"\"src\"\": {\"\"hw_addr\"\": \"\"02:fd:60:2f:28:b0\"\", \"\"name\"\": \"\"s2-eth3\"\", \"\"port_no\"\": \"\"00000003\"\", \"\"dpid\"\": \"\"0000000000000002\"\"}, \"\"dst\"\": {\"\"hw_addr\"\": \"\"9e:58:0d:1e:a4:c7\"\", \"\"name\"\": \"\"s1-eth1\"\", \"\"port_no\"\": \"\"00000001\"\", \"\"dpid\"\": \"\"0000000000000001\"\"}}, {\"\"src\"\": {\"\"hw_addr\"\": \"\"96:99:5d:d5:2c:6b\"\", \"\"name\"\": \"\"s2-eth1\"\", \"\"port_no\"\": \"\"00000001\"\", \"\"dpid\"\": \"\"0000000000000002\"\"}, \"\"dst\"\": {\"\"hw_addr\"\": \"\"62:03:73:68:83:f1\"\", \"\"name\"\": \"\"s3-eth3\"\", \"\"port_no\"\": \"\"00000003\"\", \"\"dpid\"\": \"\"0000000000000003\"\"}}, {\"\"src\"\": {\"\"hw_addr\"\": \"\"76:c5:dc:de:d9:f5\"\", \"\"name\"\": \"\"s2-eth2\"\", \"\"port_no\"\": \"\"00000002\"\", \"\"dpid\"\": \"\"0000000000000002\"\"}, \"\"dst\"\": {\"\"hw_addr\"\": \"\"c2:b0:c0:d1:8b:47\"\", \"\"name\"\": \"\"s4-eth3\"\", \"\"port_no\"\": \"\"00000003\"\", \"\"dpid\"\": \"\"0000000000000004\"\"}}]\",Thread Group 1-1,text,true,,820,157,1,1,54,0,41\r\n" + 
				"";*/
		 List<String > strList = new ArrayList<>();
		 List<LinkClass > linkClassList = new ArrayList<>();
		 strList = los.getStringList(s);
		 for (String string : strList) {
			System.out.println(string);
		}
		 linkClassList = los.getLinkClassList(strList);
		 for (LinkClass linkClass : linkClassList) {
			System.out.print("src_name->"+linkClass.getSrcPort()+"   dst_name->"+linkClass.getDstPort()+"    dst_dpid->"+linkClass.getDstSwitch()  );
			System.out.println();
		 }


	}
}
