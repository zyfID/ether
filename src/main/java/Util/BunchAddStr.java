package Util;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

//垃圾代码？

public class BunchAddStr {
	private String[] str;
	private String test;
	private List<String> l;
	private SplitResponesByDpid srb ;
	private FlowtableAddUtil fau;
	
	public BunchAddStr(String[] str,List<String> l) {
		this.str = str;
		this.l = l;
		fau = new FlowtableAddUtil();
		srb = new SplitResponesByDpid();
		//System.out.println(str);
	}
	
	public BunchAddStr(String[] str,String test) {
		
	}

/*	public void bunchString() {
		//File[] temFile =  new File[l.size()];
		
		for(int i=0;i<l.size();i++) {
			File newFile = new File("C:\\apache-jmeter-4.0\\bin\\FlowtableAdd"+l.get(i)+".jmx");
			if(newFile.exists()) {
				newFile.delete();
			}
			File temFile = new File("C:\\apache-jmeter-4.0\\bin\\FlowtableAddTem.jmx");
			try {
				String str = srb.readFile(temFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}*/
	
	public String bunchString(String[] str ) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"dpid\":");
		sb.append("could_be_changed");
		//sb.append(switchName);
		sb.append(",\"cookie\":");
		sb.append(str[0]);
		sb.append(",\"cookie_mask\":");
		sb.append(str[1]);
		sb.append(",\"table_id\":");
		sb.append(str[2]);
		sb.append(",\"idle_timeout\":");
		sb.append(str[3]);
		sb.append(",\"hard_timeout\":");
		sb.append(str[4]);
		sb.append(",\"priority\":");
		sb.append(str[5]);
		sb.append(",\"buffer_id\":");
		sb.append(str[6]);
		sb.append(",\"flag\":");
		sb.append(str[7]);
		//sb.append(",\"match\":{");
		sb.append(",\"match\":");
		sb.append(str[8]);
		//sb.append("},\"action\":[{");
		sb.append(",\"action\":");
		sb.append(str[9]);
		//sb.append("]}");
		sb.append("}");
		String bunch = new String (sb);
		return bunch;
				
	}
	
	public String bunchString(String[] str,String switchName) {
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("\"dpid\":");
		//sb.append("could_be_changed");
		sb.append(switchName);
		sb.append(",\"cookie\":");
		sb.append(str[0]);
		sb.append(",\"cookie_mask\":");
		sb.append(str[1]);
		sb.append(",\"table_id\":");
		sb.append(str[2]);
		sb.append(",\"idle_timeout\":");
		sb.append(str[3]);
		sb.append(",\"hard_timeout\":");
		sb.append(str[4]);
		sb.append(",\"priority\":");
		sb.append(str[5]);
		sb.append(",\"buffer_id\":");
		sb.append(str[6]);
		sb.append(",\"flag\":");
		sb.append(str[7]);
		//sb.append(",\"match\":{");
		sb.append(",\"match\":");
		sb.append(str[8]);
		//sb.append("},\"action\":[{");
		sb.append(",\"action\":");
		sb.append(str[9]);
		//sb.append("]}");
		sb.append("}");
		String bunch = new String (sb);
		return bunch;
				
	}
	
	
	
	public String replaceQuotaion(String s) {
		s = s.replace("\"", "&quot;");
        Pattern p = Pattern.compile("\\s*|\t|\r|\n");
        Matcher m = p.matcher(s);
        s = m.replaceAll("");
		//s = s.replace("","");
		return s;
	}
	

}

