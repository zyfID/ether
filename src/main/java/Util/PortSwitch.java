package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.junit.Test;

import com.sun.xml.internal.ws.wsdl.writer.document.Port;

public class PortSwitch {

	private static SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public static void writeJmxAndRun(String switchName) {
		System.out.println("read switchName="+switchName);
		
		File templateFile = new File("E:\\apache-jmeter-4.0\\bin\\TopoSwitchesTem.jmx");
		try {
			String templateStr =  srb.readFile(templateFile);
			templateStr = templateStr.replaceAll("/topology/switches/0000000000000002</stringProp>",
					"/topology/switches/"+switchName+"</stringProp>");
			File jmxFile = new File("E:\\apache-jmeter-4.0\\bin\\TopoSwitches"+switchName+".jmx");
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
		File scriptFile = new File("E:\\apache-jmeter-4.0\\bin\\bat\\TopoSwitchesTem.bat");
		File newScriptFile = new File("E:\\apache-jmeter-4.0\\bin\\bat\\"+"TopoSwitches"+switchName+".bat");
		File newJtl = new File("E:\\apache-jmeter-4.0\\bin\\TopoSwitches"+switchName+".jtl");
		newJtl.delete();
		if(newScriptFile.exists()) {
			newScriptFile.delete();
		}
		try {
			String scriptStr =  srb.readFile(scriptFile);
			//scriptStr = scriptStr.replaceAll("del /F /S /Q TopoSwitches0000000000000002.jtl", 
			//		"del /F /S /Q TopoSwitches"+switchName+".jtl");
			scriptStr = scriptStr.replaceAll("jmeter -n -t TopoSwitchesTem.jmx -l TopoSwitches0000000000000002.jtl", 
					"jmeter -n -t TopoSwitches"+switchName+".jmx -l TopoSwitches"+switchName+".jtl");
			srb.writeToFile(scriptStr, newScriptFile);
			String s = "cmd /c start /b "+newScriptFile.getPath();
			new BatUtil(s);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> rangeClassList(List<String> l){
		List<String> rangeList = new ArrayList<String>();
		String tem1;
		String tem2;
		for(int i=0;i<l.size();i++) {
			tem1 = l.get(i).substring(32);
			tem2 = l.get(i).substring(0, 30);
			StringBuilder sb = new StringBuilder();
			sb.append(tem1);
			sb.append(", ");
			sb.append(tem2);
			rangeList.add(new String(sb));
		}
		Collections.sort(rangeList);
		return rangeList;
	}
	
	public static int getNo(String res) {
		int no = -1;
		String[] subres = res.split("name");
		no = subres.length;
		return no -1;
	}
	
	public static List<String> portString(String res){
		StringTokenizer st = new StringTokenizer(res, "}") ;
		List<String> data = new ArrayList<String>();
		   while (st.hasMoreElements()) {
	            data.add(st.nextToken());
	        }
		   
		   while(true) {
			   if(data.size()>getNo(res)) {
				  // System.out.println(data.get(data.size()-1));
				   data.remove(data.size()-1);
			   }
			   else break;
		   }
		   
		   //�����ֶ�  
		   int index;

		   for(int i = (data.size()-1);i>=0;i--) {
			  if(i == 0) {
				  String s = data.get(0);
				  data.remove(0);
				  index = s.indexOf("\"h");
				  s = s.substring(index);
				  data.add(s);
			  }
			  else {
				  String s = data.get(i);
				  data.remove(i);
				  index = s.indexOf("\"h");
				  s = s.substring(index);
				  data.add(s);
			  }
		   }
		return data;
	}
	
	public static String readJtl (String switchName) {
		writeJmxAndRun(switchName);
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\TopoSwitches"+switchName+".jtl");
		String jtlStr ="" ;
		int index =0;
		while(!jtlFile.exists()){
			try {
				Thread.sleep(2000);
				index++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(index >=20){
			System.out.println("portSwitch的readJtl方法发生异常");
		}
		try {
			jtlStr = srb.readFile(jtlFile);
			System.out.println(jtlStr);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jtlStr;
	}
	
	public static String modifyString(String str) {
		str = str.substring(str.indexOf("\"[{"));
		str = str.substring(0, str.indexOf(",T"));
		str = str.substring(str.indexOf("[{\"\"h"));
		str = str.replaceAll("\"\"", "\"");
		
		return str;
	}
	
	public static List<PortClass> getPortSwitchList(String res){
		List<String> str = new ArrayList<String>();
		List<PortClass> pc = new ArrayList<PortClass>();
		res = modifyString(res);
		str = portString(res);
		str = rangeClassList(str);
		for(String tem:str) {
			System.out.println(tem);
		}
		for(int i=0;i<str.size();i++) {
			pc.add(new PortClass(str.get(i)));
		}
		for(int i=0;i<str.size();i++) {
			System.out.println(pc.get(i));
		}
		return pc;
	}
	
	@Test
	public void fun(){
/*		PortSwitch.writeJmxAndRun("0000009027f0358a");
		System.out.println(PortSwitch.readJtl("0000009027f0358a"));*/
		PortSwitch.readJtl("0000009027f0358a");
		
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PortSwitch.writeJmxAndRun("0000000000000005");
		String s = PortSwitch.readJtl("0000000000000005");
		System.out.println(PortSwitch.modifyString(s));
		PortSwitch.getPortSwitchList(PortSwitch.modifyString(s)) ;
		
	}

}
