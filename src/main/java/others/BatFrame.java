package others;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//貌似是垃圾代码
public class BatFrame  extends JFrame{
	private BatPanel bp;
	
	public BatFrame() {
		this.setTitle("��������");
		this.setVisible(true);
		this.setBounds(200, 200, 500, 500);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		bp = new BatPanel(this);
		this.add(bp);
		bp.init();
	}
	
	public BatFrame(LinkedList<String> s) {
		this();
		bp.init();
	
	}
	
	public BatFrame(String[] s) {
		this.setTitle("��������");
		this.setVisible(true);
		this.setBounds(600, 200, 500, 500);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		bp = new BatPanel(this);
		this.add(bp);
		bp.str = s;
		bp.init();
	}
}


