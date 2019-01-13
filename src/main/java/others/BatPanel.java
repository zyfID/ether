package others;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import Util.FileUtil;
import Util.SplitResponesByDpid;

//貌似是垃圾代码
public class BatPanel extends JPanel{
	private JButton getBtn;
	private JButton deployBtn;
	private  JPanel jp;
	public String[] str;
	private JCheckBox[] jcb;
	private BatFrame bf;
	private SplitResponesByDpid srb = new SplitResponesByDpid();
	
	public BatPanel() {
		init();
		this.setBounds(200, 200, 300, 300);
	}
	
	public BatPanel(BatFrame bf) {	
		this.bf = bf;
		this.str = str;
	}
	
	public BatPanel(BatFrame bf,String[] str) {
		this(bf);
			
	}
	
	public void init() {
		str = FileUtil.changeListToArray(FileUtil.fileReaderDpid("E:\\apache-jmeter-4.0\\bin\\dpid.txt"));
		getBtn = new JButton("��ȡ��ǰ�Ľ������б�");
		deployBtn = new JButton("Ϊѡ�еĽ�������������");
		jp =  new JPanel();	
		System.out.println("strlength:"+str.length);
		jcb = new JCheckBox[str.length];     //�ǵö��峤��
		this.setLayout(new BorderLayout());	
		//this.add(getBtn, BorderLayout.NORTH);
		this.add(deployBtn, BorderLayout.SOUTH);		
				// TODO Auto-generated method stub
		for(int i=0;i<str.length-1;i++) {
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
				for(int i=0;i<str.length-1;i++) {  //��������Ϊ������������߸�,��������ĳ���Ϊ8.�ʼ�һ,��ͬ
					//if(!(jcb[i].isSelected())) {
					//	str[i] = null;
					//}
					if(jcb[i].isSelected()) {
						al.add(str[i]);
					}
				}
				//������
				for(String tem:al) {
					System.out.println(tem);
				}
/*				for(int i=0;i<str.length;i++) {
					if(str[i] != null) {
						System.out.println(str[i]);
					}
				}*/
				
			}
		});
	}
}
