package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import dao.InformationWR;
import service.Login;

@SuppressWarnings("serial")
public class MyMainFrame extends JFrame implements ActionListener {
	final int widthFrame=720;
	final int heightFrame=540;
	final int widthCanvas=480;
	final int heightCanvas=480;
	
	Dialog dialog;
	MyCanvas myCanvas;
	JPanel panelMenu,panelPreview,bc; 
	static JButton start=new JButton();
	JButton preview=new JButton();
	JButton set=new JButton();
	JButton restore=new JButton();
	JButton about=new JButton();
	JButton log_of=new JButton();
	JLabel timeLabel=new JLabel("时间：00:00",SwingConstants.CENTER);
	public static JLabel stepLabel=new JLabel("步数：0",SwingConstants.CENTER);
	Container contentPane; 
	int type=3;
	public static int steps=0;
	String[] userInfo=new String[4];
	
	countTime ct; //计时线程
	public static boolean tf=true; //标记是否停止或开始计时
	public static boolean res=false; //标记是否正在复原拼图
	public static boolean msign;  //标记是否播放或暂停音乐
	
	public MyMainFrame(String user) {
		if(user==null) {
			userInfo[0]=userInfo[1]=userInfo[3]=null;
			userInfo[2]="路人";
		} else {
			userInfo=InformationWR.getUserInfo(user);
		}
		contentPane=this.getContentPane();
		int widthMenu=widthFrame-widthCanvas-40;
		
		panelMenu=new JPanel();
		panelMenu.setLayout(null);
		panelMenu.setBounds(widthCanvas+20,10,widthMenu,heightCanvas);
		
		JPanel panelInfo=new JPanel();
		panelInfo.setLayout(null);
		panelInfo.setBounds(20,0,widthMenu-40,widthMenu);
		panelInfo.setBorder(new TitledBorder(null,"用户信息"));
		Icon icon1=new ImageIcon ("images/tuxiang.jpg");
		JLabel tuxiang=new JLabel(icon1);     
		tuxiang.setBounds(15,20,widthMenu-70,widthMenu-70);
		panelInfo.add(tuxiang);
		JLabel userName=new JLabel("用户名："+(user==null?"路人":userInfo[1]));
		JLabel nickName=new JLabel("昵称："+userInfo[2]);
		userName.setHorizontalAlignment(SwingConstants.CENTER);
		nickName.setHorizontalAlignment(SwingConstants.CENTER);
		userName.setBounds(0,widthMenu-45,widthMenu-40,20);
		nickName.setBounds(0,widthMenu-25,widthMenu-40,20);
		panelInfo.add(userName);
		panelInfo.add(nickName);
		panelMenu.add(panelInfo);
		
		String[] btnTexts= {"开        始","预览原图","设        置","自动复原","帮        助","退出登录"};
		JButton[] btns= {start,preview,set,restore,about,log_of};
		int btnX=widthMenu/4,_btnY=widthMenu+70,btnW=widthMenu/2,btnH=(heightCanvas-widthMenu-60)/btns.length*2/3,jiange=(heightCanvas-widthMenu-60)/btns.length;
		for(int i=0;i<btns.length;i++) {
			btns[i].setText(btnTexts[i]);;
			btns[i].setBounds(btnX, _btnY+i*jiange, btnW, btnH);
			btns[i].addActionListener(this);
			panelMenu.add(btns[i]);
		}
		preview.setEnabled(false);
		restore.setEnabled(false);
		
		timeLabel.setFont(new Font("宋体", Font.BOLD, 12)); 
		timeLabel.setForeground(Color.RED); 
		timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
		timeLabel.setBounds(0,widthMenu+10,widthMenu,20);
		stepLabel.setHorizontalAlignment(SwingConstants.CENTER);
		stepLabel.setBounds(0,widthMenu+40,widthMenu,20);
		panelMenu.add(timeLabel);
		panelMenu.add(stepLabel);
		if(tf) {
			start.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) {
					if(start.getText().equals("开        始")) {
						ct=new countTime();
						ct.start();
					}
				} 
			});
		}
		
		try {
			MyCanvas.tag=ImageIO.read(new File( MyCanvas.picturePath));
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		panelPreview=new JPanel();
		panelPreview.setLayout(null);
		panelPreview.setBounds(10, 10, widthCanvas, heightCanvas);
		panelPreview.setVisible(false);
		Icon icon2=new ImageIcon (MyCanvas.picturePath);
		//Icon icon2=new ImageIcon (MyCanvas.tag);
		JLabel label=new JLabel(icon2);     
		label.setBounds(0,0,widthCanvas,heightCanvas);      
		panelPreview.add(label);
		
        bc=new JPanel();
        bc.setBounds(10, 10, widthCanvas, heightCanvas);
        bc.setBackground(Color.LIGHT_GRAY);
		
		//myCanvas=new MyCanvas(type,widthCanvas,heightCanvas);  
		//contentPane.add(myCanvas);
		//myCanvas.setVisible(false);
        contentPane.add(bc);
		contentPane.add(panelPreview);
		contentPane.add(panelMenu);
		setTitle("拼图小游戏");
		setIconImage((new ImageIcon("images/pintu.jpg")).getImage());
		setLocation((getToolkit().getScreenSize().width-widthFrame)/2,(getToolkit().getScreenSize().height-heightFrame)/2);
		setSize(widthFrame,heightFrame);
		setLayout(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}  //end of MyMainFrame()
	
	

	@SuppressWarnings("deprecation")
	public void actionPerformed(ActionEvent e) {
		if(res)  return;  //如果正在复原图像则点击按钮时点击无效
		JButton button=(JButton)e.getSource();
		if(button==start) {
			if(start.getLabel()=="开        始") {
				if(MyCanvas.finished) {
					contentPane.remove(myCanvas);
				}
				tf=true;
				steps=0;
				stepLabel.setText("步数：0");
				contentPane.remove(panelPreview);
				contentPane.remove(bc);
				myCanvas=new MyCanvas(type,widthCanvas,heightCanvas);
				contentPane.add(myCanvas);
				contentPane.repaint();
				myCanvas.Start();
				start.setLabel("结        束");
				myCanvas.setVisible(true);
				preview.setEnabled(true);
				restore.setEnabled(true);
			} else {
				if(JOptionPane.showConfirmDialog(this, new JLabel("您真的要结束游戏吗?"), "结束游戏", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.YES_OPTION) {
					tf=false;
					steps=0;
					stepLabel.setText("步数：0");
					start.setLabel("开        始");
					contentPane.remove(myCanvas);
					contentPane.add(bc);
					contentPane.repaint();
					preview.setEnabled(false);
					restore.setEnabled(false);
				}
			}
		}
		if(button==preview){
			if(button.getLabel()=="预览原图") {
				contentPane.remove(myCanvas);
				contentPane.add(panelPreview);
				panelPreview.setVisible(true);
				panelPreview.updateUI();
				contentPane.repaint();
				button.setLabel("返        回");
			} else {
				contentPane.remove(panelPreview);
				contentPane.add(myCanvas);
				contentPane.repaint();
				button.setLabel("预览原图");
			}
		}
		if(button==set) { 
			SetDialog sd=new SetDialog(this,MyCanvas.pictureID,type,msign,start.getText().equals("结        束"));
			msign=sd.m;
			/*if(msign) {
				Play p1=new Play();
				p1.start();
			}*/
			if(start.getText().equals("开        始")&&sd.se) {
				MyCanvas.pictureID=sd.picID;
				if(MyCanvas.pictureID==0) {
					MyCanvas.picturePath=sd.picPath;
					MyCanvas.tag=sd.tag;
				} else {
					MyCanvas.picturePath="images/pic_"+MyCanvas.pictureID+".jpg";
					try {
						MyCanvas.tag=ImageIO.read(new File("images/pic_"+MyCanvas.pictureID+".jpg"));
					} catch (IOException e1) {
						// TODO 自动生成的 catch 块
						e1.printStackTrace();
					}
				}
				type=sd.type;
				if(myCanvas!=null)
					myCanvas.reLoadPictrue();
				Icon icon=new ImageIcon(MyCanvas.picturePath);
				//Icon icon=new ImageIcon(MyCanvas.tag);
				JLabel label=new JLabel(icon);
				label.setBounds(0,0,widthCanvas,heightCanvas);
				panelPreview.removeAll();
				panelPreview.add(label);
				panelPreview.repaint();
				panelPreview.setVisible(true);
				contentPane.remove(bc);
				contentPane.add(panelPreview);
				contentPane.repaint();
			}
		}
		if(button==restore) {
			//if(userInfo[1]==null) {
				//JOptionPane.showMessageDialog(this, new JLabel("请注册登录后使用此功能"), "警告", JOptionPane.WARNING_MESSAGE);
			//}else {
				res=true;
				tf=false;
				steps=0;
				myCanvas.AutoRestore();
			//}
		}
		if(button==about) {
			String help0 = "游戏说明:欢迎来到拼图游戏！\n";
			String help1 = "游戏帮助:点击开始即可游戏；点击预览原图可以查看原图；\n                  点击设置可以选择其他图片、设置难度等；注册\n                  登录后可以使用自动解答功能。";
			JOptionPane.showMessageDialog(this, help0 + help1,"帮助",JOptionPane.INFORMATION_MESSAGE);
		}
		if(button==log_of) {
			if(JOptionPane.showConfirmDialog(this, "退出登录?", "退出", JOptionPane.OK_CANCEL_OPTION)==JOptionPane.YES_OPTION) {
				dispose();
				new Login("登录");
			}
		}
	}
	
	class countTime extends Thread { //运行秒针线程 
    	private int minutes; 
    	private int seconds; 

    	public countTime() { 
    		this.minutes = 0;
    		this.seconds = 0;
    	}

    	public void run() { 
    		while(tf) {
	    		seconds++;
		    	if (seconds == 60) { 
		    		seconds = 0; 
		    		minutes++; 
		    	} 
		    	timeLabel.setText("用时："+String.format("%02d:%02d",minutes,seconds)); 
		    	try {
		    		sleep(1000);
		    	}catch (InterruptedException e) {
		    		e.printStackTrace();
				}
    		}
	    }
	}
	
	public static void main(String args[]) throws UnsupportedAudioFileException, LineUnavailableException, IOException { 
		new MyMainFrame(null);
		//Play p1=new Play();
		//p1.start();
	}

} //end of MyMainFrame class
