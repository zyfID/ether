package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.sun.media.jfxmedia.events.NewFrameEvent;


//流表项类
public class FlowTableUtil {
	private SplitResponesByDpid srb ;
	private String switchName;
	
	public FlowTableUtil() {
		// TODO Auto-generated constructor stub
		srb = new SplitResponesByDpid();
	}
	
	public FlowTableUtil(String switchName) {
		this.switchName = switchName;
	}
	
	//替换模板中的相关的数据，生成新的jmx文件
	public void writeFile(String switchName) {
		
		//在这里加进制转换
		long sName = Long.parseLong(switchName, 16);
		switchName = Long.toString(sName);
		System.out.println("read switchName="+switchName);
		
		File templateFile = new File("E:\\apache-jmeter-4.0\\bin\\StatsFlow.jmx");
		File newJmxFile =  new File("E:\\apache-jmeter-4.0\\bin\\StatsFlow"+switchName+".jmx");
		if(newJmxFile.exists()) {
			newJmxFile.delete();
		}
		try {
			String temStr = srb.readFile(templateFile);
			temStr = temStr.replaceAll("<stringProp name=\"HTTPSampler.path\">/stats/flow/0000000000000002</stringProp>",
					"<stringProp name=\"HTTPSampler.path\">/stats/flow/"+switchName+"</stringProp>");
			//字符串替换此字符串匹配给定的正则表达式的每个子字符串。
			srb.writeToFile(temStr, newJmxFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//替换模板中的相关的数据，生成新的bat脚本文件
	public void writeBatAndRun (String switchName) {
		writeFile(switchName);//替换模板中的相关的数据，生成新的jmx文件
		File templateFile = new File("E:\\apache-jmeter-4.0\\bin\\bat\\StatFlow.bat");
		File newBatFile = new File("E:\\apache-jmeter-4.0\\bin\\bat\\StatFlow"+switchName+".bat");
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\StatsFlow"+switchName+".jtl");
		jtlFile.delete();
		if(newBatFile.exists()) {
			newBatFile.delete();
		}
		try {
			String temStr = srb.readFile(templateFile);
			temStr = temStr.replaceAll("del /F /S /Q StatFlow.jtl", 
					"del /F /S /Q StatsFlow"+switchName+".jtl");
			temStr = temStr.replaceAll("jmeter -n -t StatsFlow0000000000000002.jmx -l StatsFlow0000000000000002.jtl", 
					"jmeter -n -t StatsFlow"+switchName+".jmx -l StatsFlow"+switchName+".jtl");
			srb.writeToFile(temStr, newBatFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String s = "cmd /c start /b "+newBatFile.getPath();
		new BatUtil(s);

	}
	
//	@Test
//	public void fun(){
//		this.readJtl("0000009027f0358a");
//	}
	
	//读取jmx文件的运行结果，该结果被保存在相应的jtl文件中
	public String readJtl(String switchName) {
		writeBatAndRun(switchName);
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\StatsFlow"+switchName+".jtl");
/*		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String res ="";
		try {
			res = srb.readFile(jtlFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		res = this.modifyRes(res);
		return res;*/
		
		//modified in 18/9/20
		
		while(!jtlFile.exists()){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String res ="";
		try {
			res = srb.readFile(jtlFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		res = this.modifyRes(res);
		return res;
	}
	
/*	public String SumUp(String SwitchName) {
		
	}*/
	
	
	public String modifyRes(String res) {
		
		res =res.replace("\"\"", "\"");
		res = res.substring(res.indexOf("{\""));//截取剩余
		res = res.substring(0, res.indexOf(",T"));
		/*{""1"": [{""actions"": [""OUTPUT:CONTROLLER""], 
		 * ""idle_timeout"": 0, ""cookie"": 0, ""packet_count"": 550925, 
		 * ""hard_timeout"": 0, ""byte_count"": 33055500, ""duration_sec"": 278159, 
		 * ""duration_nsec"": 429000000, ""priority"": 65535, ""length"": 96, ""flags"": 0, 
		 * ""table_id"": 0, ""match"": {""dl_type"": 35020, ""dl_dst"": ""01:80:c2:00:00:0e""}},
		 * {""actions"": [""OUTPUT:2""], ""idle_timeout"": 0, ""cookie"": 1, ""packet_count"":
		 * 1040, ""hard_timeout"": 0, ""byte_count"": 110696, ""duration_sec"": 3, ""duration
		 * _nsec"": 21000000, ""priority"": 11121, ""length"": 88, ""flags"": 0, ""table_id"":
		 *  0, ""match"": {""in_port"": 2}}, {""actions"": [""OUTPUT:CONTROLLER""],
		 * ""idle_timeout"": 0, ""cookie"": 0, ""packet_count"": 598, ""hard_timeout"": 0, 
		 * ""byte_count"": 66768, ""duration_sec"": 278159, ""duration_nsec"":
		 * 429000000, ""priority"": 0, ""length"": 80, ""flags"": 0, ""table_id"": 
		 * 0, ""match"": {}}]}"*/
		
		return res;
	}
}
