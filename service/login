package service;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.InformationWR;
import ui.MyMainFrame;

@SuppressWarnings("serial")
public class Login extends JFrame implements ActionListener {  
	JTextField UserName;
	JPasswordField PassWord;
	JButton LoginButton;
	JButton RefisterButton;
	JButton skip;
	Container contenPane;
	
	public Login(String s) {
		super(s);
		setLayout(null); 
		contenPane=this.getContentPane();
		
		UserName=new JTextField(20);
		PassWord=new JPasswordField(30);
		LoginButton=new JButton("登录");
		RefisterButton=new JButton("注册");
		JLabel label1=new JLabel("用户名");
		JLabel label2=new JLabel("密    码");
		skip=new JButton("试玩>>");
		skip.setContentAreaFilled(false);
		skip.setFocusPainted(false);
		skip.setBorder(null);
		
		label1.setBounds(90,60,40,20);
		label2.setBounds(90,90,40,20);
		skip.setBounds(285,220,45,20);
		UserName.setBounds(135,60,120,20);
		PassWord.setBounds(135,90,120,20);
		LoginButton.setBounds(105,150,60,30);
		RefisterButton.setBounds(175,150,60,30);
		
		contenPane.add(label1);
		contenPane.add(label2);
		contenPane.add(skip);
		contenPane.add(UserName);
		contenPane.add(PassWord);
		contenPane.add(LoginButton);
		contenPane.add(RefisterButton);
		
		setIconImage((new ImageIcon("images/login.jpg")).getImage());
		setLayout(null);
		setResizable(false);
		setVisible(true);
		setLocation((getToolkit().getScreenSize().width-350)/2,(getToolkit().getScreenSize().height-280)/2);
		setSize(350,280);
		contenPane.setBackground(Color.ORANGE);
		
		PassWord.addActionListener(this);
		UserName.addActionListener(this);
		LoginButton.addActionListener(this);
		RefisterButton.addActionListener(this);
		skip.addActionListener(this);
		addWindowListener(new WindowAdapter(){ 
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}//end of login
	
	public void init_After_Refister(String u,String p) {
		UserName.setText(u);
		PassWord.setText(p);
	}
	
	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==LoginButton) {   
			String user= UserName.getText();
			String psw=PassWord.getText();
			if(!user.equals("")&&!psw.equals("")) {
				if(InformationWR.Check_User_Psw(user,psw)){
					JOptionPane.showMessageDialog(this,"登陆成功，欢迎使用!","welcome",JOptionPane.INFORMATION_MESSAGE);
					new MyMainFrame(user);
					dispose();
				} else {
					JOptionPane.showMessageDialog(this,"用户名或密码不正确!","提示",JOptionPane.WARNING_MESSAGE);
				}
			}
		} else if(e.getSource()==RefisterButton) {
			new Refister(this);
		} else if(e.getSource()==skip) {
			new MyMainFrame(null);
			dispose();
		}
	}
	
	public static void main(String args[]){ 
		new Login("登录");
	}
}//end of class login
