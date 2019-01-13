package Frame;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JPanel;

import Util.*;

//import Frame.ContPanel;
import Frame.second.SuperNewSwitchFrame;
/** 基本配置界面
 * @author jusang
 *
 */
public class BasicContPanel extends JPanel {
	private MyJPanel mp;
	
   
	
	public BasicContPanel() {
	
		this.removeAll();
		
		mp = new MyJPanel();//实现此类，执行构造方法				
		this.setLayout(new BorderLayout());//设置卡片按钮布局方式		
		this.add(mp, BorderLayout.CENTER);//将子容器加入到卡片容器中，设置位置为中心。
		

	}

}
/**
 * @author jusang
 *  绘制拓扑图
 */
class MyJPanel extends JPanel {
	int bianshu;//交换机个数。
	private int bianshuMax = 20;
	private int[] x = new int[bianshuMax];
	private int[] y = new int[bianshuMax];
	public String[] pointName = new String[bianshuMax];
	public ArrayList<String> pointNameInArr = new ArrayList<String>();
	public String[][] LinePairArray = new String[][] {};
	private JButton[] field = new JButton[bianshu];//按钮个数。
	SplitResponesByDpid srb = new SplitResponesByDpid();
	static FileUtil fu = new FileUtil();
	


	MyPoly mplg = new MyPoly(x, y, pointName);
	  /**
     * 绘制拓扑图。
     *1.读取拓扑文件getTopo.jtl，获取其中的交换机节点数量N以及id。
     *2.绘制一个正N边形，在每个正N边形的定点上坐标上插入一个交换机图片。
     *3.根据拓扑文件进行交换机的链接。
     *					by jusang
     * */
	public void paint(Graphics g) {
        super.paint(g);
		mplg.posOfPoint(bianshu);
		mplg.NameOfPoint(pointNameInArr);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image myImg = toolkit.getImage("E:\\apache-jmeter-4.0\\download.jpg");

		for (int i = 0; i < bianshu; i++) {

			g.setColor(Color.black);//设置颜色。
			g.drawString(pointName[i], x[i], y[i]);//写交换机名字
			g.drawImage(myImg, x[i], y[i], 50, 50, this);//画图片
		}
	
		for (int i = 0; i < LinePairArray.length; i++) {
			int[] index = getCoor(LinePairArray[i], pointName);
			g.drawLine(x[index[0]] + 10, y[index[0]] + 10, x[index[1]] + 10, y[index[1]] + 10);
		
		}

	}
	
	/*获取文档中的字符串*/
	/**
	 * 
	 * 读取拓扑文件，生成一个String对象，并对该对象内容进行拼接
	 * @return 
	 */
	public String getString() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String resFile = "E:\\apache-jmeter-4.0\\bin\\getTopo.jtl";
		
		String res = null;
		try {
			res = fu.fileReader(resFile);
			
			int index = res.indexOf("[");//查找位置
			res = res.substring(index);//截取字符串
			index = res.indexOf(",T");
			res = res.substring(0, index);
			
			res = res.replace("\"\"", "\"");// 这里记下所有的交换机的编号
			//将文档中的两个双引号变为一个双引号
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public MyJPanel() {
		//mplg.posOfPoint(bianshu);
		//mplg.NameOfPoint(pointNameInArr);
		String s = getString();//获取有交换机的字符串
		System.out.println("s=" + s);//输出控制台
		int i = srb.numberOfSwitches(s);//获得交换机的个数。
		this.bianshu = i;//将交换机的个数赋值给变量bianshu。
		this.pointNameInArr = srb.switchNameByArray(srb.getErWeiArray(s));//
		this.LinePairArray = srb.getErWeiArray(s);//
		this.pointName = srb.switchName(srb.getErWeiArray(s));//
		// write switch name into file to record it
		File dpidFile = new File("E:\\apache-jmeter-4.0\\bin\\db\\dpid.txt");
		new FileUtil().writeListToFile(dpidFile, this.pointNameInArr);

		this.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
		
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				for (int i = 0; i < bianshu; i++) {
					if (((x[i] - 7) < e.getX() && (e.getX() < x[i] + 50))
							&& ((y[i] - 7) < e.getY() && (e.getY() < y[i] + 55))) {
						
				
						/**
						 * 添加”点击“事件监听器响应函数，当点击交换机图片时，打开相应的界面。
						 * 该界面会显示交换机的信息，包括端口信息、链路信息、流表项信息以及收发数据包信息
						 *           by jusang
						 * */
						SuperNewSwitchFrame aa=new SuperNewSwitchFrame(pointName[i]);
						aa.setVisible(true)
						;
					}
				}
			}
		});
		
	}
/*
 字符串为*******00000000000000002
字符串为*******10000000000000001
字符串为*******20000000000000005
字符串为*******30000000000000006
字符串为*******40000000000000004
字符串为*******50000000000000003
字符串为*******60000000000000007
 */
	public int[] getCoor(String[] pair, String[] pName) {
		int[] coor = new int[2];
		for (int i = 0; i < pName.length; i++) {
			if (pName[i].equals(pair[0])) {
				coor[0] = i;
				break;
			}
		}
		for (int i = 0; i < pName.length; i++) {
			if (pName[i].equals(pair[1])) {
				coor[1] = i;
				break;
			}
		}
		return coor;
	}

}
/**
 * 获取正多边形的坐标
 *   
 * */
class MyPoly {// 求正多边形的顶点坐标
	private int[] x;
	private int[] y;
	private int startX;// 顶点的X坐标
	private int startY;// 顶点的Y坐标
	private int r;// 外接圆的半径
	private String[] PointName;

	public MyPoly(int[] x, int[] y, String[] PointName) {
		this.x = x;
		this.y = y;
		startX = 220;
		startY = 20;
		r = 200;
		this.PointName = PointName;
	}

	public void posOfPoint(int bianshu) {
		x[0] = startX;
		y[0] = startY;
		Point p = new Point();
		for (int i = 1; i < bianshu; i++) {
			p = nextPoint(((2 * Math.PI) / bianshu) * i);
			x[i] = p.x;
			y[i] = p.y;
		}
	}

	public void NameOfPoint(ArrayList<String> s) {
		for (int i = 0; i < s.size(); i++) {
			PointName[i] = s.get(i);
		}
	}

	public Point nextPoint(double arc) {// arc为弧度，在顶点处建立直角坐标系，用r和arc确定下一个点的坐标
		Point p = new Point();
		p.x = (int) (x[0] - r * Math.sin(arc));
		p.y = (int) (y[0] + r - r * Math.cos(arc));
		return p;
	}
}
