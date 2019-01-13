package Frame;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.CardLayout;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Frame.second.AbstractDialog;

/**
 * @author jusang
 *	主界面Frame类，包括标题JPanel类tp和内容JPanel类cp
 */

public class MainFrame extends JFrame {
	JPanel tp ;
	JPanel cp ;
	final CardLayout c;
	public MainFrame() {
		tp = new JPanel();
		cp = new JPanel();
		c = new CardLayout();
		cp.setLayout(c);
		init();
		System.out.println(this);
	}

	/**
	 * 初始化界面
	 * */
	private void init() {
		
		this.setVisible(true);
		this.setTitle("配置");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setBounds(100, 100, 550, 600);
		this.add(tp,BorderLayout.NORTH);
		this.add(cp,BorderLayout.CENTER);
		
		String[] name = {"1stP","2ndP","3rdP","4thP"};
		
		cp.add(name[0], new BasicContPanel());
		cp.add(name[1], new BatchContPanel());
		cp.add(name[2], new NewAbstractContPanel());
		cp.add(name[3], new SmartContPanel());
		
		
		JButton[] choice = new JButton[4];
		tp.setLayout(new GridLayout(1,4));
		for(int i=0;i<choice.length;i++) {
			choice[i] = new JButton();
			tp.add(choice[i]);
		}
		
		choice[0].setText("基本配置");
		choice[0].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//点击“基本配置”按键时，显示相应的界面
				c.show(cp, "1stP");
			}
		});
		
		choice[1].setText("批量配置");	
		choice[1].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				c.show(cp, "2ndP");
			}
		});
		
		choice[2].setText("抽象配置");
		choice[2].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				c.show(cp, "3rdP");
			}
		});
		
		choice[3].setText("智能配置");	

		choice[3].addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				c.show(cp,"4thP");
			}
		});
		
		
	}
	
	

	
	/**
	 * @param args
	 * 程序主入口
	 */
	public static void main(String[] args) {
		new MainFrame();
	}
}


