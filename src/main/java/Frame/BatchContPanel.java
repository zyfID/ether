package Frame;


import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import Frame.second.BatchFrame;
import Util.FileUtil;
import Util.SplitResponesByDpid;

import org.apache.jmeter.visualizers.*;

/**
 * 批量配置类
 * 
 * */
public class BatchContPanel extends JPanel{
	private JButton getBtn;
	private JButton deployBtn;
	private  JPanel jp;
	public String[] str;
	private JCheckBox[] jcb;
	private SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public BatchContPanel() {
		init();
		this.setBounds(200, 200, 300, 300);
	}

	//初始化方法
	public void init() {
		//读取当前拓扑网络中的所有交换机ID
		str = FileUtil.changeListToArray(FileUtil.fileReadLineDpid("E:\\apache-jmeter-4.0\\bin\\db\\dpid.txt"));
		getBtn = new JButton("获取当前的交换机列表");
		deployBtn = new JButton("为选中的交换机配置网络");
		jp =  new JPanel();	
		System.out.println("strlength:"+str.length);
		jcb = new JCheckBox[str.length];     //�ǵö��峤��
		this.setLayout(new BorderLayout());	
		//this.add(getBtn, BorderLayout.NORTH);
		this.add(deployBtn, BorderLayout.SOUTH);		
				// TODO Auto-generated method stub
		for(int i=0;i<str.length;i++) {
			jp.setLayout(new GridLayout(str.length, 1));
			jcb[i]=new JCheckBox(str[i]);
			//jcb[i] = new JCheckBox();
			jcb[i].setVisible(true);
			jp.add(jcb[i]);
		}				
		this.add(jp, BorderLayout.CENTER);
		
		deployBtn.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				List<String> al = new ArrayList<String>();
				for(int i=0;i<str.length;i++) { 
					if(jcb[i].isSelected()) {
						al.add(str[i]);
					}
				}			
				new BatchFrame(al);
			}
		});
	}
}
