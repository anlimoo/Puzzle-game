package ui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import tool.Play;

public class SetDialog implements ActionListener,ItemListener { 
	JDialog sd;
	JLabel choicePic,choiceGra,music;
	JComboBox<String> cbPic,cbGra;
	JRadioButton open,close;
	JButton confirm,cancel; 
	int type,picID;
	String picPath;
	BufferedImage tag;
	boolean m;
	boolean se=true;

	public SetDialog(JFrame f,int picID,int type,boolean m,boolean forb) { 
		this.picID=picID;
		this.type=type;
		this.m=m;
		
		sd=new JDialog(f,"设置",true);
		sd.setLayout(null);
		
		choicePic=new JLabel("选择图片：");
		choiceGra=new JLabel("选择难度：");
		music=new JLabel("音        乐：");
		String[] strPic = { "choose-pic","","","","","",""};
		String[] strGra = { "3行3列", "4行4列", "5行5列" };
		cbPic = new JComboBox<String>(strPic);
		cbGra = new JComboBox<String>(strGra);
		cbPic.setSelectedIndex(picID);
		cbGra.setSelectedIndex(type-3);
		open=new JRadioButton("开");
		close=new JRadioButton("关");
		close.setSelected(true);
		open.setSelected(m);
		confirm=new JButton("确定"); 
		cancel=new JButton("取消"); 
		if(forb) {
			cbPic.setEnabled(false);
			cbGra.setEnabled(false);
		}
		
		cbPic.addItemListener(this);
		open.addItemListener(this);
		close.addItemListener(this);
		confirm.addActionListener(this);
		cancel.addActionListener(this);
		
		choicePic.setBounds(80,30,65,30);
		choiceGra.setBounds(80,90,65,30);
		
		cbPic.setBounds(150,30,120,30);
		cbGra.setBounds(150,90,120,30);
		
		confirm.setBounds(100,200,60,30);
		cancel.setBounds(190,200,60,30);
		ButtonGroup musicGroup=new ButtonGroup();
		musicGroup.add(open);
		musicGroup.add(close);
		sd.add(choicePic);
		sd.add(choiceGra);
		sd.add(music);
		sd.add(cbPic);
		sd.add(cbGra);
		sd.add(open);
		sd.add(close);
		sd.add(confirm);
		sd.add(cancel);
		sd.setBounds(f.getLocation().x+(f.getWidth()-360)/2,f.getLocation().y+(f.getHeight()-280)/2,360,280);
		sd.setResizable(false);
		sd.setVisible(true);
		sd.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
	}
	
	//@Override
	public void actionPerformed(ActionEvent e) { 
		if(e.getSource()==confirm) {
			picID=cbPic.getSelectedIndex();
			type=cbGra.getSelectedIndex()+3;
			m=open.isSelected()?true:false;
			sd.dispose();
		} else if(e.getSource()==cancel) {
			se=false;
			sd.dispose();
		}
	}

	//@Override
	public void itemStateChanged(ItemEvent e) {
		if(e.getStateChange()==ItemEvent.SELECTED) {
			if(e.getSource()==cbPic) {
				if(cbPic.getSelectedIndex()==0) {
					JFileChooser fc=new JFileChooser("C:/");
					FileFilter filter=new FileNameExtensionFilter("图片文件", "bmp","jpg","png");
					fc.setFileFilter(filter);
					fc.setDialogTitle("请选择图片");
					int click=fc.showOpenDialog(new JFrame());
					if(click==JFileChooser.APPROVE_OPTION) {
						picPath=fc.getSelectedFile().getPath();
						//changepicsize();
					} else {
						cbPic.setSelectedIndex(picID);
					}
				}
			} else if(e.getSource()==open) {
				m=true;
				MyMainFrame.msign=m;
				Play p1=new Play();
				p1.start();
			} else if(e.getSource()==close) {
				m=false;
				MyMainFrame.msign=m;
			}
		}
	} 
	
	void changepicsize() {
		Image beforeimg=Toolkit.getDefaultToolkit().createImage(picPath);
		ImageIcon imgicon=new ImageIcon(picPath);
		int wid=imgicon.getIconWidth()/imgicon.getIconHeight()*480;
		Image afterimg=beforeimg.getScaledInstance(wid, 480, Image.SCALE_SMOOTH);
		tag=new BufferedImage(wid, 480, BufferedImage.TYPE_INT_RGB);
		Graphics g=tag.getGraphics();
		g.drawImage(afterimg, 0, 0, null);
		g.dispose();
	}
}
