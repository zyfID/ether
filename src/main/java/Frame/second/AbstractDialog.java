package Frame.second;

import javax.swing.JDialog;
import javax.swing.JFrame;


//貌似是垃圾代码
public class AbstractDialog extends JDialog{
	
	JFrame jf;
	public AbstractDialog() {
		
	}
	
	public AbstractDialog(JFrame jf,String s, boolean isModal) {
		
		super(jf, s, isModal);
		this.jf = jf;
		//init();
		this.setVisible(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		this.setBounds(300, 300, 200, 200);
	}

	private void init() {
		this.setVisible(true);
		this.setDefaultCloseOperation(HIDE_ON_CLOSE);
		
		this.setBounds(300, 300, 200, 200);
		
	}
	
	
	
}
