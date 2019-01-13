package getLinks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import Util.*;

public class LinkOfSwitch {
	
	private SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public int getNo(String s) {
		String[] split = s.split("src");
		int len = split.length-1;
		return len;
	}
	
	public void writeJmx(String switchName) {
		/*File templateFile = new File("C:\\apache-jmeter-4.0\\bin\\TopoLinksSwitch.jmx");
		
		try {
			String templateStr =  srb.readFile(templateFile);
			templateStr = templateStr.replaceAll("   <stringProp name=\"HTTPSampler.path\">/v1.0/topology/links/0000000000000002</stringProp>",
					"   <stringProp name=\"HTTPSampler.path\">/v1.0/topology/links/"+switchName+"</stringProp>");
			File jmxFile = new File("C:\\apache-jmeter-4.0\\bin\\TopoLinksSwitch"+switchName+".jmx");
			if(jmxFile.exists()) {
				jmxFile.delete();
				
			}
			srb.writeToFile(templateStr, jmxFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		File temJmx = new File("E:\\apache-jmeter-4.0\\bin\\TopoLinksTem.jmx");
		try {
			String temStr = srb.readFile(temJmx);
			temStr = temStr.replaceAll("<stringProp name=\"HTTPSampler.path\">/v1.0/topology/links/0000000000000002</stringProp>",
					"<stringProp name=\"HTTPSampler.path\">/v1.0/topology/links/"+switchName+"</stringProp>");
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
	
	public void writeBatAndRun(String switchName) {
		/*File scriptFile = new File("C:\\apache-jmeter-4.0\\bin\\bat\\TopoLinksSwitch.bat");
		File newScriptFile = new File("C:\\apache-jmeter-4.0\\bin\\bat\\"+"TopoLinksSwitch"+switchName+".bat");
		File newJtl = new File("C:\\apache-jmeter-4.0\\bin\\TopoLinksSwitch"+switchName+".jtl");
		if(newJtl.exists()) {
			newJtl.delete();
		}
		try {
			String scriptStr =  srb.readFile(scriptFile);
			scriptStr = scriptStr.replaceAll("jmeter -n -t TopoLinksSwitch0000000000000002.jmx -l TopoLinksSwitch0000000000000002.jtl", 
					"jmeter -n -t TopoLinksSwitch"+switchName+".jmx -l TopoLinksSwitch"+switchName+".jtl");
			//scriptStr = scriptStr.replaceAll("del TopoLinksSwitch0000000000000002.jtl", "del TopoLinksSwitch"+switchName+".jtl");
			if(newScriptFile.exists()) {
				newScriptFile.delete();
				//newScriptFile = new File("C:\\apache-jmeter-4.0\\bin\\bat\\"+"TopoSwitch"+switchName+".bat");			
			}
			
				srb.writeToFile(scriptStr, newScriptFile);
				String s = "cmd /c start /b "+newScriptFile.getPath();
			//	System.out.println(s);
				new BatUtil(s);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		File newJtl = new File("E:\\apache-jmeter-4.0\\bin\\TopoLinks"+switchName+".jtl");
		File temScript = new File ("E:\\apache-jmeter-4.0\\bin\\bat\\TopoLinksTem.bat");
		File newScript = new File("E:\\apache-jmeter-4.0\\bin\\bat\\ToloLinks"+switchName+".bat");
		if(newJtl.exists()) {
			newJtl.delete();
		}
		if(newScript.exists()) {
			newScript.delete();
		}
		try {
			String str = srb.readFile(temScript);
			str =str.replaceAll("jmeter -n -t TopoLinksTem.jmx -l TopoLinksTem.jtl", 
					"jmeter -n -t TopoLinks"+switchName+".jmx -l TopoLinks"+switchName+".jtl");
			srb.writeToFile(str, newScript);
			String s = "cmd /c start /b "+newScript.getPath();
			new BatUtil(s);
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public String readJtl(String switchName ) {

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\TopoLinks"+switchName+".jtl");
		String jtlStr ="" ;
		try {
			jtlStr = srb.readFile(jtlFile);//读出字符串
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(jtlStr);
		int index = jtlStr.indexOf("[{");
		
		if(index  <  0 ) {
			index = 194;
		}
		System.out.println(index+"");
		jtlStr = jtlStr.substring(index);
		jtlStr = jtlStr.substring(0, jtlStr.indexOf(",T"));
		jtlStr = jtlStr.replaceAll("\"\"", "\"");
		System.out.println(jtlStr);
		return jtlStr;
	}
	

	
	
	/*public String getString(String switchName) {
		//1.дjmx�ļ�
		File templateFile = new File("C:\\apache-jmeter-4.0\\bin\\TopoLinksSwitch.jmx");
		
		try {
			String templateStr =  srb.readFile(templateFile);
			templateStr = templateStr.replaceAll("   <stringProp name=\"HTTPSampler.path\">/v1.0/topology/links/0000000000000002</stringProp>",
					"   <stringProp name=\"HTTPSampler.path\">/v1.0/topology/links/"+switchName+"</stringProp>");
			File jmxFile = new File("C:\\apache-jmeter-4.0\\bin\\TopoLinksSwitch"+switchName+".jmx");
			if(jmxFile.exists()) {
				jmxFile.delete();
				
			}
			srb.writeToFile(templateStr, jmxFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	//2.��д �� ִ�нű��ļ�
		//is null
		File scriptFile = new File("C:\\apache-jmeter-4.0\\bin\\bat\\TopoLinksSwitch.bat");
		File newScriptFile = new File("C:\\apache-jmeter-4.0\\bin\\bat\\"+"TopoLinksSwitch"+switchName+".bat");
		try {
			String scriptStr =  srb.readFile(scriptFile);
			scriptStr = scriptStr.replaceAll("jmeter -n -t TopoLinksSwitch0000000000000002.jmx -l TopoLinksSwitch0000000000000002.jtl", 
					"jmeter -n -t TopoLinksSwitch"+switchName+".jmx -l TopoLinksSwitch"+switchName+".jtl");
			scriptStr = scriptStr.replaceAll("del TopoLinksSwitch0000000000000002.jtl", "del TopoLinksSwitch"+switchName+".jtl");
			if(newScriptFile.exists()) {
				newScriptFile.delete();
				//newScriptFile = new File("C:\\apache-jmeter-4.0\\bin\\bat\\"+"TopoSwitch"+switchName+".bat");
			
			}
			
				srb.writeToFile(scriptStr, newScriptFile);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//3.���ű��ļ�������һ���ַ���
		File jtlFile = new File("C:\\apache-jmeter-4.0\\bin\\TopoLinksSwitch"+switchName+".jtl");
		String jtlStr ="" ;
		try {
			jtlStr = srb.readFile(jtlFile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jtlStr = jtlStr.substring(jtlStr.indexOf("[{"));
		jtlStr = jtlStr.substring(0, jtlStr.indexOf(",T"));
		jtlStr = jtlStr.replaceAll("\"\"", "\"");
		
		return jtlStr;
		
	}
	*/
	
	
	/*
	 * �������ֵ��ʲô��˼��Ӧ��������res��Ϣ���ַ�����Ϣ��
	 * 
	 * */
	public List<String> getLinkList(String s ){
		String[] split = s.split("src");	
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
		
		for(int i=0;i<al.size();i++) {
			System.out.println(al.get(i));
		}
		for(String tem :al) {
			System.out.println(tem.indexOf("\"na"));
		}
		al = this.rangeList(al);
		return al;
	}
	
	public List<String> rangeList(List<String> l) {
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
	}
	
	
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
		los.writeJmx("0000000000000001");
		los.writeBatAndRun("0000000000000001");
		
		String s =los.readJtl("0000000000000001");
		
		List<String> ls = new ArrayList<String>();
		 ls = los.getLinkList(s);
		
		for(int i=0;i<ls.size();i++) {
			System.out.println(ls.get(i));
		}	
	


	}
}
