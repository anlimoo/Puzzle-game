package service;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import dao.InformationWR;

@SuppressWarnings("serial")
public class Refister extends JFrame implements ActionListener,KeyListener,FocusListener {  
	JTextField UserName;
	JTextField NickName;
	JPasswordField PassWord1;
	JPasswordField PassWord2;
	JButton RefisterButton;
	JButton Cancel;
	
	Login t;
	String[] users;
	
	JLabel[] l=new JLabel[4];
	String[] s= {"提示:用户名已存在","","提示:密码只能是6-20个数字或字母","提示:两次密码不一样"};
	boolean[] checks=new boolean[4];
	
	public Refister(Login t) {
		this.t=t;
		users=InformationWR.getUsers();
		
		setTitle("注册");
		setIconImage(null);
		setLayout(null);
		
		UserName=new JTextField(20);
		NickName=new JTextField(20);
		PassWord1=new JPasswordField(30);
		PassWord2=new JPasswordField(30);
		RefisterButton=new JButton("注册");
		Cancel=new JButton("返回");
		
		JLabel label1=new JLabel("用  户  名");
		JLabel label2=new JLabel("昵        称");
		JLabel label3=new JLabel("密        码");
		JLabel label4=new JLabel("确认密码");
		
		label1.setBounds(55, 10, 55, 20);
		label2.setBounds(55, 50, 55, 20);
		label3.setBounds(55, 90, 55, 20);
		label4.setBounds(55, 130, 55, 20);
		UserName.setBounds(115,10,120,20);
		NickName.setBounds(115,50,120,20);
		PassWord1.setBounds(115,90,120,20);
		PassWord2.setBounds(115,130,120,20);
		RefisterButton.setBounds(80,170,60,30);
		Cancel.setBounds(150,170,60,30);
		
		add(label1);
		add(label2);
		add(label3);
		add(label4);
		add(UserName);
		add(NickName);
		add(PassWord1);
		add(PassWord2);
		add(RefisterButton);
		add(Cancel);
		for(int i=0;i<4;i++) {
			l[i]=new JLabel(s[i]);
			l[i].setForeground(Color.RED);
			l[i].setFont(new Font("宋体", Font.BOLD, 10));
			l[i].setBounds(115,33+40*i,200,10);
			l[i].setVisible(false);
			add(l[i]);
		}
		
		setIconImage((new ImageIcon("images/refister.png")).getImage());
		setResizable(false);
		setVisible(true);
		setLocation((getToolkit().getScreenSize().width-300)/2,(getToolkit().getScreenSize().height-200)/2);
		setSize(300,250);
		
		UserName.addKeyListener(this);
		PassWord1.addKeyListener(this);
		PassWord1.addFocusListener(this);
		PassWord2.addFocusListener(this);
		RefisterButton.addActionListener(this);
		Cancel.addActionListener(this);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==RefisterButton) {
			String user=UserName.getText();
			String nick=NickName.getText();
			String psw1=PassWord1.getText();
			String psw2=PassWord2.getText();
			if(user.length()==0||nick.length()==0||psw1.length()==0||psw2.length()==0) {
				JOptionPane.showMessageDialog(this,"用户名、昵称或密码任何一个都不能为空!","提示",JOptionPane.WARNING_MESSAGE);
			} else if(psw1.length()<6&&psw1.length()>0) {
				
			} else if(!psw1.equals(psw2)) {
				l[3].setVisible(true);
				l[3].updateUI();
			} else if(!checks[0] && !checks[2] && !checks[3]) {
				if(InformationWR.Write_info(user, nick, psw1)) {
					JOptionPane.showMessageDialog(this,"注册成功!","提示",JOptionPane.INFORMATION_MESSAGE);
					this.setVisible(false);
					t.init_After_Refister(user, psw1);
				}else {
					JOptionPane.showMessageDialog(this,"注册失败!","提示",JOptionPane.WARNING_MESSAGE);
				}
			}	
		} else if(e.getSource()==Cancel) {
			setVisible(false);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void keyReleased(KeyEvent e) { 
		if(e.getSource()==UserName) {
			if(users!=null) {
				int i;
				for(i=0;i<users.length;i++) {
					if(UserName.getText().equals(users[i])) {
						l[0].setVisible(true);
						l[0].updateUI();
						checks[0]=true;
						break;
					}
				}
				if(i>=users.length) {
					l[0].setVisible(false);
					l[0].updateUI();
					checks[0]=false;
				}
			}
		}else if (e.getSource()==PassWord1) {
			if(!((e.getKeyChar()>='a'&&e.getKeyChar()<='z')||(e.getKeyChar()>='A'&&e.getKeyChar()<='Z')||(e.getKeyChar()>='0'&&e.getKeyChar()<='9'))) {
				l[2].setVisible(true);
				l[2].updateUI();
				checks[2]=true;
			}
			if(checks[2]) {
				char[] chs=(PassWord1.getText()).toCharArray();
				int i;
				for(i=0;i<chs.length;i++) {
					if(!((chs[i]>='a'&&chs[i]<='z')||(chs[i]>='A'&&chs[i]<='Z')||(chs[i]>='0'&&chs[i]<='9'))) {
						l[2].setVisible(true);
						l[2].updateUI();
						checks[2]=true;
						break;
					}
				}
				if(i>=chs.length) {
					l[2].setVisible(false);
					l[2].updateUI();
					checks[2]=false;
				}
			}
		}
	}
	
	@Override
	public void keyPressed(KeyEvent e) { }

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void focusGained(FocusEvent e) { 
		if(e.getSource()==PassWord1) {
			if(!checks[2]) {
				l[2].setVisible(false);
				l[2].updateUI();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void focusLost(FocusEvent e) {
		if(e.getSource()==PassWord1) {
			if(PassWord1.getText().length()<6&&PassWord1.getText().length()>0) {
				l[2].setVisible(true);
				l[2].updateUI();
			}else {
				if(!checks[2]) {
					l[2].setVisible(false);
					l[2].updateUI();
				}
			}
		} else if(e.getSource()==PassWord2) {
			String psw1=PassWord1.getText();
			String psw2=PassWord2.getText();
			if(psw2.length()!=0 && !psw1.equals(psw2)) {
				l[3].setVisible(true);
				l[3].updateUI();
				checks[3]=true;
			}else {
				l[3].setVisible(false);
				l[3].updateUI();
				checks[3]=false;
			}
		}
	}	
}
