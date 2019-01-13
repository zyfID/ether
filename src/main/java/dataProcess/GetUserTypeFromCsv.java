package dataProcess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;



public class GetUserTypeFromCsv {
	
	private File file;
	private File simpliedFile;
	private int[][] timeIP;
	private int index;
	private String[][] result;
	private String[][] result2;
	private List<String> IpAddress;
	
	private static final double[][] CENTER_POINT =
		new double[][] 
		{{0.181,0.419,0.5365,0.2857,0.346,0.4889,0.3968, 0.419,0.381,0.3143,0.3397,0.1937,0.1778,0.1651,0.1238,0.1048 },
		{1.2424,1.3737,2.202,1.7273,1.8586,2.2828,2.2525,1.8182,2.2222,2.2828,2.1919,1.8889,1.404,1.303,0.9899,0.8586},
		{4.4407,4.2203,4.7119,3.5085,4.339,4.5932,3.7797,3.8644,4.6949,5.2373,4.1017,3.6271,3.0508,3.5085,4.1525,4.2373}};
	private static final int LENGTH = CENTER_POINT.length;
	
	public GetUserTypeFromCsv(){
		// /home/zero/Experiment/a.txt
		 file = new File("D:\\FTCache\\data.csv");
		 IpAddress = new ArrayList<String>();
		// simpliedFile = new File("G:\\H.csv");

	}
	

	public void saveAs2DArray(){  //将文件中的字符串转化为二维数组
		//saveArray = new String[IPandTimeMap.size()][3];
		saveIPNameAsArrayList();
		 timeIP = new int[IpAddress.size()][];
		 result = new String [IpAddress.size()][2];
		 result2 = new String[IpAddress.size()][2];
		BufferedReader br = null;
		//BufferedWriter bw = null;
		String str;
		System.out.println("reading...");
		//String temp;
		try {
			 br = new BufferedReader(new FileReader(file));
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
	//	String s="IP,t1,t2,t3,t4,t5,t6,t7,t8,t9,710,t11,t12,t13,t14,t15,t16" ;
		this.index = 0;
		String s;
		try {
			//String[] subStr = new String[17];
			while(( s =br.readLine())!=null){
				timeIP[index] = 
						changeStrToIntArray(s);
				if(this.index == timeIP.length){
					break;
				}
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for(int i=0;i<timeIP.length;i++){
			for(int j=0;j<17;j++){
				System.out.print(timeIP[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	/*以链表的形式保存文件中的所有IP地址*/
	public void saveIPNameAsArrayList(){  //将文件中的字符串转化为链表
		String[] strArr = null;
		
		BufferedReader br = null;
		//BufferedWriter bw = null;
		String str;
		System.out.println("reading...");
		//String temp;
		try {
			 br = new BufferedReader(new FileReader(file));
			 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		this.index = 0;
		String s;
		try {
			//String[] subStr = new String[17];
			while(( s =br.readLine())!=null){
				strArr = s.split(",");
				IpAddress.add(strArr[0]);
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*
	 * 将字符串转化为一个整形数组
	 * **/
	public int[] changeStrToIntArray(String str){
		int intArray[] = new int[17];
		String subStr[] ;
		subStr = str.split(",");
		intArray[0] = this.index;
		for(int i=1;i<subStr.length;i++){
			intArray[i] =Integer.valueOf(subStr[i]);
		}
		return intArray;
	}
	
	/**
	 * 返回离采样点最近的聚类中心点*/
	public int returnNearestPointNumber(int[] p){
		double l1,l2,l0;
		l0 = minus(p,CENTER_POINT[0]);
		l1 = minus(p,CENTER_POINT[1]);
		l2 = minus(p,CENTER_POINT[2]);
		return min(l0,l1,l2);
	}
	
	/*返回该点与目标点的值
	 * point 为目标点 长度为 n
	 * point2为某个中心点 长度为n-1
	 **/
	public double minus(int[] point, double[] point2){
		double d =0;
		if(point.length-1 != point2.length){
			System.out.println("wrong value");
			return -1;
		}
		for(int i=0;i<point2.length;i++ ){
			d = d+ (point2[i]-point[i+1])*(point2[i]-point[i+1]);
		}
		return d;
	}
	
	public int min(double l0,double l1,double l2){
		if(l0<=l1&&l0<=l1){
			return 0;
		}
		if(l1<=l0&&l1<=l2){
			return 1;
		}
		if(l2<=l0&&l2<=l1){
			return 2;
		}
		System.out.println("wrong in jduging max value");
		return -1;
	}
	
	/*获得聚类结果*/

	public void getResult(){
		saveAs2DArray();
		for(int i=0;i<timeIP.length;i++){
			result[i][0] = IpAddress.get(i);
			switch(returnNearestPointNumber(timeIP[i]))
			{
				case 0:{
					result[i][1]= "Undergraduate";
					break;
				}
				case 1:{
					result[i][1]= "Teacher";
					break;
				}
				case 2:{
					result[i][1]= "Postgraduate";
					break;
				}
			}
			//result[i][1] = returnNearestPointNumber(timeIP[i]);
			System.out.print(result[i][0]+" "+result[i][1]);
			System.out.println();
		}
	}
	
	public void writeResultToFile(){
		// /home/zero/Experiment/
		getResult();
		//File f = new File("G:\\type.txt");
		File f = new File("D:\\FTCache\\type.txt");
		BufferedWriter bw = null;
		try {
			 bw = new BufferedWriter(new FileWriter(f)) ;
			 for(int i=0;i<IpAddress.size();i++){
				 String s = result[i][0].substring(0, result[i][0].length()-1);
				 bw.write(s+","+result[i][1]);
				 bw.write("\r\n");
			 }
	    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				bw.flush();
				bw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		file.delete();
	}
	
	public static void main(String[] args) {
		GetUserTypeFromCsv c = new GetUserTypeFromCsv();
		c.writeResultToFile();
		//c.file.delete();
	}
}
