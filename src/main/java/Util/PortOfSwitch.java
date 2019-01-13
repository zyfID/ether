package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;


/*!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 *显示交换机的端口信息
 * */

//貌似是垃圾代码
public class PortOfSwitch {
	private SplitResponesByDpid srb = new SplitResponesByDpid();
	public static int getNo(String res) {
		int no = -1;
		String[] subres = res.split("name");
		no = subres.length;
		return no -1;
	}
	
	public  String[] getMACAddr(String res) {
		String[] MACArr = new String[getNo(res)] ;
		MACArr = res.split("{");
		return MACArr;

	}
	
	public static void writeJmxAndBatRun(String switchName) {
		File templateFile = new File("E:\\apache-jmeter-4.0\\bin\\TopoSwitchesTem.jmx");
		SplitResponesByDpid srb =new SplitResponesByDpid();
		try {
			String templateStr =  srb.readFile(templateFile);
			
			templateStr = templateStr.replaceAll(" <stringProp name=\"HTTPSampler.path\">/v1.0/topology/switches/0000000000000002</stringProp>",
					"   <stringProp name=\"HTTPSampler.path\">/v1.0/topology/switches/"+switchName+"</stringProp>");
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
/*		if(newJtl.exists()) {
			newJtl.delete();
		}*/
		if(newScriptFile.exists()) {
			newScriptFile.delete();
			try {
				newScriptFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			String scriptStr =  srb.readFile(scriptFile);
			scriptStr = scriptStr.replaceAll("del /F /S /Q TopoSwitches0000000000000002.jtl", 
					"del /F /S /Q TopoSwitches"+switchName+".jtl");
			scriptStr = scriptStr.replaceAll("jmeter -n -t TopoSwitchesTem.jmx -l TopoSwitches0000000000000002.jtl", 
					"jmeter -n -t TopoSwitches"+switchName+".jmx -l TopoSwitches"+switchName+".jtl");
			//scriptStr = scriptStr.replaceAll("del TopologySwitches0000000000000002.jtl", "del TopologySwitches"+switchName+".jtl");

			//String n = newScriptFile.getPath();
			srb.writeToFile(scriptStr, newScriptFile);
			String s = "cmd /c start "+newScriptFile.getPath();
			//String s = "cmd /c start /b "+newScriptFile.getPath();
			new BatUtil(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getString(String switchName) {		
		//3.
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\TopoSwitches"+switchName+".jtl");
		String jtlStr ="" ;
		while(true) {
			if(jtlFile.exists()) {
				try {
					jtlStr = srb.readFile(jtlFile);
					System.out.println("jtlStr is "+ jtlStr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}else {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return jtlStr;
	}
	
	public static String readJtl(String switchName) {
		SplitResponesByDpid srb = new SplitResponesByDpid();
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\TopoSwitches"+switchName+".jtl");
		String jtlStr ="" ;
		while(true) {
			if(jtlFile.exists()) {
				try {
					jtlStr = srb.readFile(jtlFile);
					System.out.println("jtlStr is "+ jtlStr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}else {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return jtlStr;
	}
	
	//获取包含端口信息的字符串列表
	public  List<String> portString(String res){
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
	
	
	//调用它的代码是垃圾代码 估计这个代码也是垃圾代码
	public List<String> rangeClassList(List<String> l){
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
	
	//垃圾代码
	public  List<PortClass> getPortList(String switchName){
		
		List<PortClass> al = new ArrayList<>();

		//switchName = "0000000000000003";
		writeJmxAndBatRun(switchName);
		this.getString(switchName);
		String switchStr = this.getString(switchName);
		System.out.println(switchStr.indexOf("[{"));
		switchStr =switchStr.substring(switchStr.indexOf("[{"));

		switchStr = switchStr.substring(0, switchStr.indexOf("\",T"));
		System.out.println(switchStr);
		switchStr = switchStr.replaceAll("\"\"", "\"");
		System.out.println(switchStr);
		String res = switchStr;
		List<String> str = new ArrayList<String>();
		List<PortClass> pc = new ArrayList<PortClass>();

	
		str = this.portString(res);
		for(String tem:str) {
		//	System.out.println(tem.indexOf("\"name"));
		//	System.out.println(tem.indexOf(" \"port_no\""));
		}
		
		str = this.rangeClassList(str);
		
		for(String tem:str) {
			System.out.println(tem);
		}
		for(int i=0;i<str.size();i++) {
			pc.add(new PortClass(str.get(i)));
		}
		for(int i=0;i<str.size();i++) {
			System.out.println(pc.get(i));
		}
		
		return al;
	}
	
		
	
	public static void main(String[] args) {
		
		PortOfSwitch pos =  new PortOfSwitch();

		
		
		String switchName = "0000000000000004";
		writeJmxAndBatRun(switchName);
		pos.getString(switchName);
		String switchStr = pos.getString(switchName);
		System.out.println(switchStr);
		System.out.println(switchStr.indexOf("["));
		switchStr =switchStr.substring(switchStr.indexOf("{"));

		switchStr = switchStr.substring(0, switchStr.indexOf("\",T"));
		System.out.println(switchStr);
		switchStr = switchStr.replaceAll("\"\"", "\"");
		System.out.println(switchStr);
		String res = switchStr;
		List<String> str = new ArrayList<String>();
		List<PortClass> pc = new ArrayList<PortClass>();

	
		str = pos.portString(res);
		for(String tem:str) {
		//	System.out.println(tem.indexOf("\"name"));
		//	System.out.println(tem.indexOf(" \"port_no\""));
		}
		
		str = pos.rangeClassList(str);
		
		for(String tem:str) {
			System.out.println(tem);
		}
		for(int i=0;i<str.size();i++) {
			pc.add(new PortClass(str.get(i)));
		}
		for(int i=0;i<str.size();i++) {
			System.out.println(pc.get(i));
		}
	}
}
