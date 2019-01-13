package org.apache.jmeter.visualizers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class FileUtil {
	
	
	public static List<String> changeArrayToList(String[] str){
		List<String> al = new ArrayList<>();
		for(String s:str) {
			al.add(s);
		}
		return al;
	}
	
	public static String[] changeListToArray(List<String> al){
		String[] strArr = new String[al.size()];
		for(int i=0;i<al.size();i++) {
			strArr[i] = al.get(i);
		}
		return strArr;
	}
	
	
	
	
	public  String fileReader(String fileStr) throws IOException {
		//fileStr = "C:\\apache-jmeter-4.0\\bin\\getTopo.jtl";
		
		File f = new File(fileStr);
	//	String res = null;
		StringBuffer sb = new StringBuffer();
		if(!(f.exists())) {
			System.out.println(fileStr);
			System.out.println(f.getName()+" is null");
			return "";
		}
		else {
			FileInputStream in = null;
			
			 in = new FileInputStream(f);
			byte[] media = new byte[5];
			int length=-1;
			while(-1 !=(length= in.read(media))) {
				String ingo = new String(media, 0, length);
				sb.append(ingo);
				//System.out.print(ingo);
			}
			in.close();
			String res = new String(sb);
			return res;	
		}
	}
	
	public  static List<String> fileReaderDpid(String fileStr)  {
		
		File f = new File(fileStr);
	//	String res = null;
		List<String> dpid = new ArrayList<>();
		StringBuffer sb = new StringBuffer();
		if(!f.exists()) {
			System.out.println(f.getName()+" is null");
			return dpid;
		}
		else {
			FileInputStream in = null;
			
			 try {
				in = new FileInputStream(f);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			byte[] media = new byte[16];
			int length=-1;
			try {
				while(-1 !=(length= in.read(media))) {
					String ingo = new String(media, 0, length);
					//sb.append(ingo);
					dpid.add(ingo);
					//System.out.print(ingo);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//String res = new String(sb);
			return dpid;	
		}
	}
		
	public  static List<String> fileReaderToListLine(String fileStr) throws IOException  {
			
		List<String> dpid = new ArrayList<>();
	    File filename = new File(fileStr); // 要读取以上路径的input。txt文件  
	    InputStreamReader reader;
		try {
			reader = new InputStreamReader(  
			        new FileInputStream(filename));
		    BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言  
		    String line = "";  
		    //line = br.readLine();  
		    while (line != null) {  
		        line = br.readLine(); // 一次读入一行数据  
		        dpid.add(line);
		    }  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 建立一个输入流对象reader  

	    return dpid;
		
	}
		
	
	
	public File NewJmxFile(String newStr) {
		File newFile = null ;
		
		return newFile;
	}
	
	public boolean replaceFileContent(String newStr, String oldStr) {
		boolean flag = false;
		return flag;
	}
	
	public static void main(String[] args) {
		try {
		System.out.println(	new FileUtil().fileReader("C:\\apache-jmeter-4.0\\bin\\getTopo.jtl"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
