package ui;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JButton;
        
@SuppressWarnings("serial")
public class Cell extends JButton {
	Cell(Icon icon,int w,int h) {  //实际为ICON
		super(icon);
		this.setBackground(Color.PINK);
		this.setSize(w,h);
	}

	public void move(String direction,int w,int h) {//方格的移动
		if(direction=="UP") {
			this.setLocation(this.getBounds().x,this.getBounds().y-h);
		} else if(direction=="DOWN") {
			this.setLocation(this.getBounds().x,this.getBounds().y+h);
		} else if(direction=="LEFT") {
			this.setLocation(this.getBounds().x-w,this.getBounds().y);
		} else {
			this.setLocation(this.getBounds().x+w,this.getBounds().y);
		}
	}
}
