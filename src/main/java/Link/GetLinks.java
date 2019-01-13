package Link;

import java.awt.GridLayout;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.jmeter.visualizers.Dijkstra;
import org.apache.jmeter.visualizers.LinkClass;
import org.apache.jmeter.visualizers.LinkOfSwitch;

import Util.*;

public class GetLinks {
	
	public static void print() {}
	
	public static String getLast(StringBuilder sb) {
		String s = new String(sb);
		s = s.substring(s.length()-1, s.length());
		return s;
	}
	
	//��������ȡ����������������Ǹ����ӿڣ����硰s1-eth2��   ת����   ���������+�˿ڱ��  ����������������
	public static List<String> getIFs(String start,String end){
		//1.�����ڽӾ���
		
		

		
				//String[] sArr={};
				//FileUtil fu = new FileUtil();
				List<String> dpid =  null;
				try {
					dpid = FileUtil.fileReaderToListLine("E:\\apache-jmeter-4.0\\bin\\db\\dpid.txt");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] sArr = FileUtil.changeListToArray(dpid);
				List<String> al = new ArrayList<String>();
				for(String str:sArr) {
					if(str != null) {
						al.add(str);
					}	
				}
				sArr = new String[al.size()];
				for(int i=0;i<sArr.length;i++) {
					sArr[i] = al.get(i);
				}
				
				int index = -1;
				
				for(int i=0;i<sArr.length;i++) {
					if(sArr[i].equals(start)) {
						index =i;
						break;
					}
				}
				
				String[][] NearSwitch = new String[al.size()][al.size()];
			

				for(String ss:al) {
					System.out.println(ss);
				}
				//(1) ������ ��ý�������Ϣ
				LinkOfSwitch los = new LinkOfSwitch();

				
				//���ɶ�άLinkClass���� 
				LinkClass[][] lcArr = new LinkClass[al.size()][];
				for(int i=0;i<al.size();i++) {
					los.writeJmx(al.get(i));              //0000000000000001
					los.writeBatAndRun(al.get(i));
					
					
					//��ȡ���Ϊal.get(i)�Ľ���������Ӧ��Ϣ
					String response = los.readJtl(al.get(i));
					List<String> alStr = new ArrayList<String>();
					
					//��ȡ����������Ӧ��Ϣ�йؼ���Ϣ���洢��������
					alStr = los.getLinkList(response);
					//��ӡresp�����е���Ϣ
					for (String string : alStr) {
						System.out.println(string);
					}
					lcArr[i]  = new LinkClass[alStr.size()];
					//LinkClass[] lc = new LinkClass[alStr.size()];			
					for(int j=0;j<alStr.size();j++) {
						//lc[j] = new LinkClass(alStr.get(j));
						lcArr[i][j] = new LinkClass(alStr.get(j),true);
						//System.out.println(alStr.get(j));
						NearSwitch[i][j] =
								lcArr[i][j].getDstSwitch();
						//System.out.println(lc[j]);
					}
				}

				for(int i=0;i<lcArr.length;i++) {
					for(int j=0;j<lcArr[i].length;j++) {
		/*				System.out.println(lcArr[i][j].getDstPort());
						System.out.println(lcArr[i][j].getDstSwitch());
						System.out.println(lcArr[i][j].getSrcPort());*/
					//	System.out.println("dstName"+lcArr[i][j].getDstPName());
					//	System.out.println("srcName"+lcArr[i][j].getSrcPName());
					}
				}
				
				//2.�����ڽӽڵ����
				int[][] NearDist= new int[NearSwitch.length][NearSwitch.length];

				for(int i=0;i<NearSwitch.length;i++) {
					for(int j=0;j<NearSwitch.length;j++) {		
						if(i==j) {
							NearDist[i][j] = 0;
						}else {
							NearDist[i][j] = 1000;
						}
						
					}
				}
				
				for(int i=0;i<NearSwitch.length;i++) {
					for(int j=0;j<NearSwitch.length;j++) {		
						for(int k=0;k<NearSwitch.length;k++) {
							if(NearSwitch[i][j] ==null) {
								continue;
							}
							else if (NearSwitch[i][j].equals( sArr[k])) {
								NearDist[i][k] = 1;
							}	
						}
					}
				}
				
				for(int i=0;i<NearDist.length;i++) {
					for(int j=0;j<NearDist[i].length;j++) {
							//System.out.print( "   "+   NearDist[i][j]);
					}
					System.out.println();
				}
				
				int vstart = index;
				String[] links = Dijkstra.dijkstra(vstart,NearDist);
				//3.������·����
				String[][] prenode = new String[NearDist.length][2];
		 		for(int i=0;i<NearDist.length;i++) {
					String[] sp = links[i].split("->");
					prenode[i][0] = sp[0];
					prenode[i][1] = sp[1];
				}
		 		List<String> LinkDes =  new ArrayList<>();
				LinkDes = Dijkstra.find("v"+vstart,links);
				
				for(int i=0;i<LinkDes.size();i++) {
					for(int j=0;j<al.size();j++) {
						if(LinkDes.get(i).contains(i+"")) {
							LinkDes.get(i).replaceAll(i+"", al.get(i));
						}
					}
				}
				
				
				//��������ڵ㼯�ϣ���������Ϊ
				List<String> vpot = new ArrayList<>();
				
				for(String stem: LinkDes) {
					Matcher m = Pattern.compile("v\\d+").matcher(stem);
					vpot.remove(stem);
					while(m.find()) {
						if(!(vpot.contains(m.group()))){
							vpot.add(m.group());
						}
					}
				}
				Collections.sort(vpot);
				
				for(String s:LinkDes) {
					System.out.println(s);
				}
				
				for(int i=LinkDes.size();i>0;i--) {
					String s = LinkDes.get(i-1);
					LinkDes.remove(s);
					for(int j=0;j<vpot.size();j++) {
						//System.out.println(s);
						s = s.replaceAll(vpot.get(j), sArr[j]);
						
					}
					LinkDes.add(s);
				}
				
				for(String s:LinkDes) {//��ӡ��·����
					System.out.println(s);
				}
				
				//#������ʵ�ڵ�Ѱ�ҽ�����
	
			//	String end = "0000000000000007";

				
				String sta = "v"+index;

				String route = "";

				boolean flag =false;
				for(String s: LinkDes) {
					if(s.endsWith(end)) {
						route = s;
						
						flag = true;
						break;	
					}
				}
				System.out.println(flag?"true":"false");
				//#������·��Ϣ��þ���Ķ˿ڣ����ݱ�����������һ����������Ϣ����ȷ����������Ķ˿�
				/*���Ȼ�õ�ǰ��������Ȼ���sArr�ַ����ȶԣ���ö�Ӧ����š���������ҵ�lcArr����һ����������Ӧ��������
				 *  
				 * */
				String[] routes = route.split("->");
				List<String> routeIf = new ArrayList<>();
				
				System.out.println("��ʼ��ӡ");
				
				for(int i=0;i<routes.length-1;i++) {
					for(int j=0;j<sArr.length;j++) {
						if(sArr[j].equals(routes[i])) {
							 for(int k=0;k<lcArr[j].length;k++) {
								 if((lcArr[j][k].getDstSwitch()).equals(routes[i+1])) {
									 routeIf.add(lcArr[j][k].getSrcPName());
									 routeIf.add(lcArr[j][k].getDstPName());
									 break;
								 }
								 else { 
									 continue; 
								 }
							 }
						}
						else {
							continue;
						}
					}
				}
/*				for(String s:routeIf) {
					System.out.println(s);
				}*/
				return routeIf;
	}
	
	public static List<String> getIFsWithDpidAndIF(String start,String end){
		//1.�����ڽӾ���
		
		

		
				//String[] sArr={};
				//FileUtil fu = new FileUtil();
				List<String> dpid =  null;
				try {
					dpid = FileUtil.fileReaderToListLine("E:\\apache-jmeter-4.0\\bin\\db\\dpid.txt");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] sArr = FileUtil.changeListToArray(dpid);
				List<String> al = new ArrayList<String>();
				for(String str:sArr) {
					if(str != null) {
						al.add(str);
					}	
				}
				sArr = new String[al.size()];
				for(int i=0;i<sArr.length;i++) {
					sArr[i] = al.get(i);
				}
				
				int index = -1;
				
				for(int i=0;i<sArr.length;i++) {
					if(sArr[i].equals(start)) {
						index =i;
						break;
					}
				}
				
				String[][] NearSwitch = new String[al.size()][al.size()];
			

				for(String ss:al) {
					System.out.println(ss);
				}
				//(1) ������ ��ý�������Ϣ
				LinkOfSwitch los = new LinkOfSwitch();

				
				//���ɶ�άLinkClass���� 
				LinkClass[][] lcArr = new LinkClass[al.size()][];
				for(int i=0;i<al.size();i++) {
					los.writeJmx(al.get(i));              //0000000000000001
					los.writeBatAndRun(al.get(i));
					
					
					//��ȡ���Ϊal.get(i)�Ľ���������Ӧ��Ϣ
					String response = los.readJtl(al.get(i));
					List<String> alStr = new ArrayList<String>();
					
					//��ȡ����������Ӧ��Ϣ�йؼ���Ϣ���洢��������
					alStr = los.getLinkList(response);
					lcArr[i]  = new LinkClass[alStr.size()];
					//LinkClass[] lc = new LinkClass[alStr.size()];			
					for(int j=0;j<alStr.size();j++) {
						//lc[j] = new LinkClass(alStr.get(j));
						lcArr[i][j] = new LinkClass(alStr.get(j),true);
						//System.out.println(alStr.get(j));
						NearSwitch[i][j] =
								lcArr[i][j].getDstSwitch();
						//System.out.println(lc[j]);
					}
				}

				for(int i=0;i<lcArr.length;i++) {
					for(int j=0;j<lcArr[i].length;j++) {
		/*				System.out.println(lcArr[i][j].getDstPort());
						System.out.println(lcArr[i][j].getDstSwitch());
						System.out.println(lcArr[i][j].getSrcPort());*/
					//	System.out.println("dstName"+lcArr[i][j].getDstPName());
					//	System.out.println("srcName"+lcArr[i][j].getSrcPName());
					}
				}
				
				//2.�����ڽӽڵ����
				int[][] NearDist= new int[NearSwitch.length][NearSwitch.length];

				for(int i=0;i<NearSwitch.length;i++) {
					for(int j=0;j<NearSwitch.length;j++) {		
						if(i==j) {
							NearDist[i][j] = 0;
						}else {
							NearDist[i][j] = 1000;
						}
						
					}
				}
				
				for(int i=0;i<NearSwitch.length;i++) {
					for(int j=0;j<NearSwitch.length;j++) {		
						for(int k=0;k<NearSwitch.length;k++) {
							if(NearSwitch[i][j] ==null) {
								continue;
							}
							else if (NearSwitch[i][j].equals( sArr[k])) {
								NearDist[i][k] = 1;
							}	
						}
					}
				}
				
				for(int i=0;i<NearDist.length;i++) {
					for(int j=0;j<NearDist[i].length;j++) {
							//System.out.print( "   "+   NearDist[i][j]);
					}
					System.out.println();
				}
				
				int vstart = index;
				String[] links = Dijkstra.dijkstra(vstart,NearDist);
				//3.������·����
				String[][] prenode = new String[NearDist.length][2];
		 		for(int i=0;i<NearDist.length;i++) {
					String[] sp = links[i].split("->");
					prenode[i][0] = sp[0];
					prenode[i][1] = sp[1];
				}
		 		List<String> LinkDes =  new ArrayList<>();
				LinkDes = Dijkstra.find("v"+vstart,links);
				
				for(int i=0;i<LinkDes.size();i++) {
					for(int j=0;j<al.size();j++) {
						if(LinkDes.get(i).contains(i+"")) {
							LinkDes.get(i).replaceAll(i+"", al.get(i));
						}
					}
				}
				
				
				//��������ڵ㼯�ϣ���������Ϊ
				List<String> vpot = new ArrayList<>();
				
				for(String stem: LinkDes) {
					Matcher m = Pattern.compile("v\\d+").matcher(stem);
					vpot.remove(stem);
					while(m.find()) {
						if(!(vpot.contains(m.group()))){
							vpot.add(m.group());
						}
					}
				}
				Collections.sort(vpot);
				
				for(String s:LinkDes) {
					System.out.println(s);
				}
				
				for(int i=LinkDes.size();i>0;i--) {
					String s = LinkDes.get(i-1);
					LinkDes.remove(s);
					for(int j=0;j<vpot.size();j++) {
						//System.out.println(s);
						s = s.replaceAll(vpot.get(j), sArr[j]);
						
					}
					LinkDes.add(s);
				}
				
				for(String s:LinkDes) {//��ӡ��·����
					System.out.println(s);
				}
				
				//#������ʵ�ڵ�Ѱ�ҽ�����
	
			//	String end = "0000000000000007";

				
				String sta = "v"+index;

				String route = "";

				boolean flag =false;
				for(String s: LinkDes) {
					if(s.endsWith(end)) {
						route = s;
						
						flag = true;
						break;	
					}
				}
				System.out.println(flag?"true":"false");
				//#������·��Ϣ��þ���Ķ˿ڣ����ݱ�����������һ����������Ϣ����ȷ����������Ķ˿�
				/*���Ȼ�õ�ǰ��������Ȼ���sArr�ַ����ȶԣ���ö�Ӧ����š���������ҵ�lcArr����һ����������Ӧ��������
				 *  
				 * */
				String[] routes = route.split("->");
				List<String> routeIf = new ArrayList<>();
				
				System.out.println("��ʼ��ӡ");
				
				for(int i=0;i<routes.length-1;i++) {
					for(int j=0;j<sArr.length;j++) {
						if(sArr[j].equals(routes[i])) {
							 for(int k=0;k<lcArr[j].length;k++) {
								 if((lcArr[j][k].getDstSwitch()).equals(routes[i+1])) {
									/* routeIf.add(lcArr[j][k].getSrcPName());
									 routeIf.add(lcArr[j][k].getDstPName());*/
									// routeIf.add(lcArr[j][k].getSrcSwitch()+","+lcArr[j][k].getSrcPort());
									 routeIf.add(lcArr[j][k].getDstSwitch()+","+lcArr[j][k].getDstPort());
									 
									 break;
								 }
								 else { 
									 continue; 
								 }
							 }
						}
						else {
							continue;
						}
					}
				}
/*				for(String s:routeIf) {
					System.out.println(s);
				}*/
				return routeIf;
	}
	
	
	public static void main(String[] args) throws IOException {
/*		//���Է��� getIFs
		List<String> al = 		GetLinks.getIFs("0000000000000004", "0000000000000001");
		for(String s:al) {
			System.out.println(s);
		}*/
		List<String> al = GetLinks.getIFsWithDpidAndIF("0000000000000004", "0000000000000001");
		for(String s:al) {
			System.out.println(s);
		}
		
		
		
	/*	//1.�����ڽӾ���
		//String[] sArr={};
		FileUtil fu = new FileUtil();
		List<String> dpid = fu.fileReaderToListLine("C:\\apache-jmeter-4.0\\bin\\dpid.txt");
		String[] sArr = fu.changeListToArray(dpid);
		//String[] sArr = {"0000000000000001","0000000000000002","0000000000000003","0000000000000004","0000000000000005","0000000000000006","0000000000000007"}; 
		
		List<String> al = new ArrayList<String>();
		for(String str:sArr) {
			if(str != null) {
				al.add(str);
			}	
		}
		sArr = new String[al.size()];
		for(int i=0;i<sArr.length;i++) {
			sArr[i] = al.get(i);
		}
		String[][] NearSwitch = new String[al.size()][al.size()];
	

		for(String ss:al) {
			System.out.println(ss);
		}
		//(1) ������ ��ý�������Ϣ
		//File oldJtl = new File("C:\\apache-jmeter-4.0\\bin\\getTopo.jtl");
		LinkOfSwitch los = new LinkOfSwitch();
		
											//����һ��ʼ�ɹ��ģ�Ϊ�˳��Խ��ж�ά��������ɣ�����������һ����
											for(int i=0;i<al.size();i++) {
												los.writeJmx(al.get(i));              //0000000000000001
												los.writeBatAndRun(al.get(i));
												String response = los.readJtl(al.get(i));
												List<String> alStr = new ArrayList<String>();	
												alStr = los.getLinkList(response);
												LinkClass[] lc = new LinkClass[alStr.size()];			
												for(int j=0;j<alStr.size();j++) {
													lc[j] = new LinkClass(alStr.get(j));
													System.out.println(alStr.get(j));
													NearSwitch[i][j] =
															lc[j].getDstSwitch();
													//System.out.println(lc[j]);
												}
											}
		
		//���ɶ�άLinkClass���� 
		LinkClass[][] lcArr = new LinkClass[al.size()][];
		for(int i=0;i<al.size();i++) {
			los.writeJmx(al.get(i));              //0000000000000001
			los.writeBatAndRun(al.get(i));
			String response = los.readJtl(al.get(i));
			List<String> alStr = new ArrayList<String>();	
			alStr = los.getLinkList(response);
			lcArr[i]  = new LinkClass[alStr.size()];
			//LinkClass[] lc = new LinkClass[alStr.size()];			
			for(int j=0;j<alStr.size();j++) {
				//lc[j] = new LinkClass(alStr.get(j));
				lcArr[i][j] = new LinkClass(alStr.get(j),true);
				//System.out.println(alStr.get(j));
				NearSwitch[i][j] =
						lcArr[i][j].getDstSwitch();
				//System.out.println(lc[j]);
			}
		}

		for(int i=0;i<lcArr.length;i++) {
			for(int j=0;j<lcArr[i].length;j++) {
												System.out.println(lcArr[i][j].getDstPort());
												System.out.println(lcArr[i][j].getDstSwitch());
												System.out.println(lcArr[i][j].getSrcPort());
				System.out.println("dstName"+lcArr[i][j].getDstPName());
				System.out.println("srcName"+lcArr[i][j].getSrcPName());
			}
		}
		
										for(int i=0;i<NearSwitch.length;i++) {
											for(int j=0;j<NearSwitch[i].length;j++) {
												//if(NearSwitch[i][j]!=null) {
													System.out.print(NearSwitch[i][j]+"");
												//}
												//else {
												//	continue;
												//}
											}
											System.out.println();
										}
		//2.�����ڽӽڵ����
		int[][] NearDist= new int[NearSwitch.length][NearSwitch.length];
										for(int i=0;i<NearSwitch.length;i++) {
											for(int j=0;j<NearSwitch.length;j++) {
												if(i==j) {
													NearDist[i][j] = 0;
												}
												else if(NearSwitch[i][j] != null) {
													NearDist[i][j] = 1;
												}
												else {
													NearDist[i][j] = 1000;
												}
											}
										}
		for(int i=0;i<NearSwitch.length;i++) {
			for(int j=0;j<NearSwitch.length;j++) {		
				if(i==j) {
					NearDist[i][j] = 0;
				}else {
					NearDist[i][j] = 1000;
				}
				
			}
		}
		for(int i=0;i<NearSwitch.length;i++) {
			for(int j=0;j<NearSwitch.length;j++) {		
				for(int k=0;k<NearSwitch.length;k++) {
					if(NearSwitch[i][j] ==null) {
						continue;
					}
					else if (NearSwitch[i][j].equals( sArr[k])) {
						NearDist[i][k] = 1;
					}	
				}
			}
		}
		for(int i=0;i<NearDist.length;i++) {
			for(int j=0;j<NearDist[i].length;j++) {
					System.out.print( "   "+   NearDist[i][j]);
			}
			System.out.println();
		}
		
		int vstart = 1;
		String[] links = Dijkstra.dijkstra(vstart,NearDist);
		//3.������·����
		String[][] prenode = new String[NearDist.length][2];
 		for(int i=0;i<NearDist.length;i++) {
			String[] sp = links[i].split("->");
			prenode[i][0] = sp[0];
			prenode[i][1] = sp[1];
		}
 		List<String> LinkDes =  new ArrayList<>();
		LinkDes = Dijkstra.find("v"+vstart,links);
		
		for(int i=0;i<LinkDes.size();i++) {
			for(int j=0;j<al.size();j++) {
				if(LinkDes.get(i).contains(i+"")) {
					LinkDes.get(i).replaceAll(i+"", al.get(i));
				}
			}
		}
		
		
		//��������ڵ㼯�ϣ���������Ϊ
		List<String> vpot = new ArrayList<>();
		
		for(String stem: LinkDes) {
			Matcher m = Pattern.compile("v\\d+").matcher(stem);
			vpot.remove(stem);
			while(m.find()) {
				if(!(vpot.contains(m.group()))){
					vpot.add(m.group());
				}
			}
		}
		Collections.sort(vpot);
		
		for(String s:LinkDes) {
			System.out.println(s);
		}
		
		for(int i=LinkDes.size();i>0;i--) {
			String s = LinkDes.get(i-1);
			LinkDes.remove(s);
			for(int j=0;j<vpot.size();j++) {
				//System.out.println(s);
				s = s.replaceAll(vpot.get(j), sArr[j]);
				
			}
			LinkDes.add(s);
		}
		
		

		
									for(String s: vpot) {//��ӡ�ڵ����
										System.out.print  (s  +"  ");
									}
									System.out.println("  ");
									
									for(String s:sArr) {//��ӡ����������
										System.out.print(s+ "    ");
									}
									
		for(String s:LinkDes) {//��ӡ��·����
			System.out.println(s);
		}
		
									//#������ʵ�ڵ�Ѱ�ҽ�����
									String sta = "0000000000000001";
		String end = "0000000000000007";
		String sta = "v1";

		String route = "";

		boolean flag =false;
		for(String s: LinkDes) {
			if(s.endsWith(end)) {
				route = s;
				
				flag = true;
				break;	
			}
		}
		System.out.println(flag?"true":"false");
		//#������·��Ϣ��þ���Ķ˿ڣ����ݱ�����������һ����������Ϣ����ȷ����������Ķ˿�
		���Ȼ�õ�ǰ��������Ȼ���sArr�ַ����ȶԣ���ö�Ӧ����š���������ҵ�lcArr����һ����������Ӧ��������
		 *  
		 * 
		String[] routes = route.split("->");
		List<String> routeIf = new ArrayList<>();
		
		System.out.println("��ʼ��ӡ");
		
		for(int i=0;i<routes.length-1;i++) {
			for(int j=0;j<sArr.length;j++) {
				if(sArr[j].equals(routes[i])) {
					 for(int k=0;k<lcArr[j].length;k++) {
						 if((lcArr[j][k].getDstSwitch()).equals(routes[i+1])) {
							 routeIf.add(lcArr[j][k].getDstPort());
							 routeIf.add(lcArr[j][k].getSrcPort());
							 break;
						 }
						 else {
							 continue; 
						 }
					 }
				}
				else {
					continue;
				}
			}
		}
		for(String s:routeIf) {
			System.out.println(s);
		}*/
	}


}
