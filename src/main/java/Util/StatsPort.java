package Util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jusang
 * 端口工具类
 */
public class StatsPort {
	private String switchName;
	private SplitResponesByDpid srb;
	public StatsPort() {
		// TODO Auto-generated constructor stub
		srb = new SplitResponesByDpid();
	}

	public StatsPort(String switchName) {
		super();
		this.switchName = switchName;
	}
	
	public int getNo(String switchName) {
		int leng = 0;
		return leng;
	}
	
	//替换模板中的相关的数据，生成新的jmx文件
	public void writeJmx(String switchName) {
		File templateFile = new File("E:\\apache-jmeter-4.0\\bin\\StatsPort.jmx");
		try {
			String templateStr = srb.readFile(templateFile);
			templateStr = templateStr.replaceAll("<stringProp name=\"HTTPSampler.path\">/stats/port/0000000000000002</stringProp>", 
					"<stringProp name=\"HTTPSampler.path\">/stats/port/"+switchName+"</stringProp>");
			File jmxFile =  new File("E:\\apache-jmeter-4.0\\bin\\StatsPort"+switchName+".jmx");
			if(jmxFile.exists()) {
				jmxFile.delete();
			}
			srb.writeToFile(templateStr, jmxFile);
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//替换模板中的相关的数据，生成新的bat脚本文件
	public void writeBatAndRun(String switchName) {
		writeJmx(switchName);
		File scriptFile = new File ("E:\\apache-jmeter-4.0\\bin\\bat\\StatsPort.bat");
		File newscriptFile = new File("E:\\apache-jmeter-4.0\\bin\\bat\\StatsPort"+switchName+".bat");
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\StatsPort"+switchName+".jtl");
		jtlFile.delete();
		try {
			String scriptStr  =  srb.readFile(scriptFile);
			scriptStr = scriptStr.replaceAll("jmeter -n -t StatsPort0000000000000002.jmx -l StatsPort0000000000000002.jtl", 
					"jmeter -n -t StatsPort"+switchName+".jmx -l StatsPort"+switchName+ ".jtl"	);
			scriptStr = scriptStr.replaceAll("del /F /S /Q StatsPort0000000000000002.jtl",
					"del /F /S /Q StatsPort"+switchName+".jtl");
			
			if(newscriptFile.exists()) {
				newscriptFile.delete();
			}
			
			srb.writeToFile(scriptStr, newscriptFile);
			String s = "cmd /c start /b "+newscriptFile.getPath();
			new BatUtil(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//读取jmx文件的运行结果，该结果被保存在相应的jtl文件中
	public String readJtl(String switchName) {
		writeBatAndRun(switchName);
		File jtlFile = new File("E:\\apache-jmeter-4.0\\bin\\StatsPort"+switchName+".jtl");
/*		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String jtlStr ="";
		try {
			 jtlStr =  srb.readFile(jtlFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jtlStr;*/
		while(!jtlFile.exists()){
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String jtlStr ="";
		try {
			 jtlStr =  srb.readFile(jtlFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jtlStr;
	}
	
	
	public String modifyString(String res) {
		System.out.println(res);
		res =res.replaceAll("\"\"", "\"");
		
		System.out.println("{ s index="+res.indexOf("{"));
		res = res.substring(res.indexOf("{"));
		res = res.substring(0, res.indexOf(",T"));
		res = res.substring(res.indexOf("[{"));
		res = res.substring(0, res.indexOf("}\""));
	    res = this.removeCommaAndColon(res);
		return res;
	}
	
	public String removeCommaAndColon(String res) {
		res = res.replace(",", " ");
		res = res.replace(":", " ");
		return res;
	}
	
	public List<String> getList(String res){
		List<String > al = new ArrayList<String>();
		
		String[] split = res.split("}");
		String tem1;
		String tem2;
		for(String tem: split) {
			StringBuilder sb =new StringBuilder();
			if((tem.indexOf("\"tx"))!=-1) {
				tem = tem.substring(tem.indexOf("\"tx"));
				
				//������������ �Ե�������
				tem1 = tem.substring(tem.indexOf("\"por"));
				tem2 = tem.substring(0, tem.indexOf("\"por"));
				sb.append(tem1);
				sb.append(tem2);
								
				al.add(new String(sb));
				
			}		
		}
		for(String tem: al) {
			System.out.println(tem);
		}
		return al;
	}

	//垃圾代码 
/*	public static void main(String[] args) {
		StatsPort  sp = new StatsPort();
		sp.writeJmx("0000000000000002");
		sp.writeBatAndRun("0000000000000002");
		//System.out.println(	sp.modifyString(sp.readJtl("0000000000000001")));
		sp.getList(sp.modifyString(sp.readJtl("0000000000000002")));
	}*/
}

