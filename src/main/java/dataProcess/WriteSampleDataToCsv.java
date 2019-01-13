package dataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class WriteSampleDataToCsv {
	
	private Map IPandTimeMap;
	private List<String> Ips;
	private File file;
	private String[][] saveArray;
	private String[][] onlineTimes;
	private List<String> date_hour_ipaddress;
	private String temIPaddress;
	private File csvFile ;
	
	public WriteSampleDataToCsv() {
		super();
		IPandTimeMap = new HashMap<String, int[][]>();
		///  home/zero/Experiment/
		file = new File("D:\\FTCache\\a.txt");
		
		csvFile = new File("D:\\FTCache\\data.csv");
		date_hour_ipaddress = new ArrayList<String>();
		Ips = new ArrayList<String>();
	}

	
	//获取每个时间点（整时）的在线时间信息，以“27.11.10.20.216.223”的格式存储到
	public void saveAsTrippleArray(){
		//saveArray = new String[IPandTimeMap.size()][3];
		int i=0;
		BufferedReader br = null;
		String str;
		System.out.println("reading...");
		//String temp;
		try {
			 br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			while(br.read() != -1){
				temIPaddress = null;
			       str = br.readLine();  
			      // str = str.replaceAll("", "");
			       Matcher m = Pattern.compile("\\D*\\d*-\\d*-\\d*").matcher(str);
			       while(m.find()){
			    	   String s = m.group();
			    	   s= s.substring(s.length()-2);
			    	   temIPaddress = s;
			    	 //  System.out.println(temIPaddress);
			    	   //saveArray[i][0] = s;
			       }
			      Matcher m1 = Pattern.compile("((08)|(09)|(1\\d)|(20)|(21)|(22)|(23)):\\d*:\\d*").matcher(str);
			      // Matcher m1 = Pattern.compile("((10):\\d*:\\d*").matcher(str);
			       //Matcher m1 = Pattern.compile("1\\d:\\d*:\\d*").matcher(str);
			       while(m1.find()){
			    	   String s = m1.group();
			    	  
			    	 //  String[] subStr = s.split(":");
			    	  s = s.substring(0, 2);
			    	 // s=subStr[0];
			    	   temIPaddress=temIPaddress+"-"+s;
			    	//   System.out.println(temIPaddress);
			    	   //saveArray[i][1] = s;
			    	   break;
			       }
			       Matcher m2 = Pattern.compile("src_ip:/10.20.\\d*.\\d*").matcher(str);
			       while(m2.find()){
			    	   String s = m2.group();
			    	   s = s.substring(s.indexOf("1"), s.length());
			    	   s=s+"o"; 
			    	   //String s = m.group();
			    	   if(!Ips.contains(s)){
			    		   /*****************************************************************/
			    		    //添加字符串结尾符号	
			    		   Ips.add(s);
			    		   System.out.println(s);
			    		  // writeString(s, new File("G:\\cc.txt"));
			    		   
			    	   }
			    	   temIPaddress=temIPaddress+"."+s;
			    	   if(!date_hour_ipaddress.contains(temIPaddress)){
			    		   //
			    		   date_hour_ipaddress.add(temIPaddress);
			    		   System.out.println(temIPaddress);
			    		//   writeString(temIPaddress, new File("G:\\dd.txt"));
			    	   }
			    	  // saveArray[i][2] = s;
			    	   break;
			       }
			       i++;
			       if(i==this.IPandTimeMap.size()){
			    	   break;
			       }

	
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getTimes(List<String> list, String str){  
		int times = 0;
		for(int i=0;i<list.size();i++){
			Matcher m = Pattern.compile(str).matcher(list.get(i));
		       while(m.find()){
		    	   times++;
		       }	
				
		}
		return times;
	}
	
	/**
	 * 根据时间点+IP地址去IPandTimeMap链表例查找该字符串出现多少次。这个次数即是该IP一周内在该时间段在线的次数
	 * 输入:IP列表字符串，
	 * 输出:二维数组；
	 * 			每行的内容是IP及其各时间点的在线次数
	 * 
	 * */
	public void getTimesOfIpOnline(){
		//saveAsTrippleArray();  ***********************这个很重要的***********************
		String temp;
		System.out.println("一共有"+Ips.size()+"个IP");
		onlineTimes = new String[Ips.size()][17];
		//Collections.sort(date_hour_ipaddress);

		
		for(int i=0;i<Ips.size();i++){
			//for (String s : Ips) {
			String str = "";
				onlineTimes[i][0] = 
						Ips.get(i);
				onlineTimes[i][1] = getTimes(date_hour_ipaddress,"08."+Ips.get(i))+"";
				onlineTimes[i][2] = getTimes(date_hour_ipaddress,"09."+Ips.get(i))+"";
				str = onlineTimes[i][0]+","+onlineTimes[i][1]+","+onlineTimes[i][2];
				//onlineTimes[i][j] = getTimes(date_hour_ipaddress,Ips.get(i))+"";
				for(int j=3;j<17;j++){
				
					onlineTimes[i][j] = getTimes(date_hour_ipaddress,j+7+"."+Ips.get(i))+"";
					str = str +","+onlineTimes[i][j];
				}
			//}
			writeString(str, csvFile);
			System.out.println(str);
		}
		file.delete();
	}

	public void writeString(String s,File f){
		//date_hour_ipaddress
		//saveArray = new String[IPandTimeMap.size()][3];

		//BufferedWriter bw = null;
		FileWriter bw = null;
			try {
				bw = new FileWriter(f, true);
				bw.append(s);
				bw.append("\r\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally{
				try {
					bw.flush();
					bw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
	}

	public static void main(String[] args) {
		WriteSampleDataToCsv gal = new WriteSampleDataToCsv();	
		gal.saveAsTrippleArray();
		gal.getTimesOfIpOnline();
		//gal.
	}
}
