package ui;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import tool.Solve;
import tool.Split;

@SuppressWarnings("serial")
public class MyCanvas extends JPanel implements MouseListener {
	final int widthCanvas;
	final int heightCanvas;
	int type=3;
	int cellw,cellh;
	boolean hasAddActionListener=false;
	BufferedImage[][] subimages;
	Cell cell[];
	JButton cellNull; 
	public static int pictureID=1; 
	public static String picturePath = "images/pic_"+pictureID+".jpg";
	public static BufferedImage tag;
	String[] directions= {"UP","LEFT","DOWN","RIGHT"};
	public static boolean finished=false;
	MyCanvas mc; //用于自动复原后移除图片按钮的ActionEvent事件
	
	public MyCanvas(int type,int widthCanvas,int heightCanvas) {
		this.type=type;
		this.widthCanvas=widthCanvas;
		this.heightCanvas=heightCanvas;
		this.mc=this;
		setLayout(null);
		setLocation(10, 10);
		setSize(widthCanvas,heightCanvas);
		cellw=widthCanvas/type;
		cellh=heightCanvas/type;
		cellNull=new JButton();
		cellNull.setBounds(cellw*(type-1),cellh*(type-1),cellw,cellh);
		cellNull.setBackground(Color.WHITE);
		this.add(cellNull);
		cell=new Cell[type*type];
		subimages=Split.cuttingImage(picturePath, type, cellw, cellh);
		//subimages=Split.cuttingImage(MyCanvas.tag, type, cellw, cellh);
		if(subimages==null) {
			return;
		}
		for (int i=0;i<type;i++) {
			for(int j=0;j<type;j++){
				cell[i*type+j]=new Cell(new ImageIcon(subimages[i][j]),cellw,cellh);
				cell[i*type+j].setLocation(j*cellw,i*cellh);
				this.add(cell[i*type+j]);
			}
		}
		this.remove(cell[type*type-1]);
	}

	public void reLoadPictrue(){ 
		for (int i=0;i<type;i++) {
			for(int j=0;j<type;j++) {
				cell[i*type+j].setIcon(new ImageIcon(subimages[i][j]));
			}
		}
	}

	public boolean isFinish(){ 
		for(int i=0;i<type*type-1;i++) {
			int x=cell[i].getBounds().x;
			int y=cell[i].getBounds().y;
			if(y/cellh*type+x/cellw!=i) {
				return false;
			}
		}
		return true;
	}
	        
	public void Start() { 
		while(cell[0].getBounds().x<=cellw && cell[0].getBounds().y<=cellh) { //当第一个方格距左上角较近时
			int x=cellNull.getLocation().x;
			int y=cellNull.getLocation().y;
			int dirid=(int)(Math.random()*4); 
			//String[] directions= {"RIGHT","LEFT","DOWN","UP"};
			//int[] X= {-cellw,cellw,0,0};
			//int[] Y= {0,0,-cellh,cellh};
			int[] X= {0,cellw,0,-cellw};
			int[] Y= {cellh,0,-cellh,0};
			
			x+=X[dirid];
			y+=Y[dirid];
			if(test(x,y)) {
				for(int j=0;j<type*type-1;j++) {
					if((cell[j].getBounds().x==x) && (cell[j].getBounds().y==y)) {
						cell[j].move(directions[dirid],cellw,cellh);
						cellNull.setLocation(x,y);
						break; 
					}
				}
			}
		}
	                
		if(!hasAddActionListener) {
			for(int i=0;i<type*type-1;i++) {
				cell[i].addMouseListener(this);
			}
		}
		hasAddActionListener=true;
	}
	
	private boolean test(int x,int y) {
		if((x>=0 && x<=widthCanvas-cellw)||(y>=0 && y<=heightCanvas-cellh)) {
			return true;
		} else {
			return false;
		}	
	}
	
	public void AutoRestore() {
		Re r=new Re();
		r.start();
	}

	public void mouseClicked(MouseEvent e)  { }
	public void mouseEntered(MouseEvent e)  { }
	public void mouseExited(MouseEvent e)   { }
	public void mouseReleased(MouseEvent e) { }
	
	public void mousePressed(MouseEvent e) {
		if(MyMainFrame.res) return; //如果正在复原图像则不能点击这些图片
        Cell button=(Cell)e.getSource();
        int x1=button.getBounds().x; 
        int y1=button.getBounds().y;
                
        int x2=cellNull.getBounds().x; 
        int y2=cellNull.getBounds().y;
                
        if(x1==x2 && y1-y2==cellh) {
        	button.move("UP",cellw,cellh);
        	MyMainFrame.steps++;
        	MyMainFrame.stepLabel.setText("步数："+MyMainFrame.steps);
        } else if(x1==x2 && y1-y2==-cellh) {
        	button.move("DOWN",cellw,cellh);
        	MyMainFrame.steps++;
        	MyMainFrame.stepLabel.setText("步数："+MyMainFrame.steps);
        } else if(x1-x2==cellw && y1==y2) {
        	button.move("LEFT",cellw,cellh);
        	MyMainFrame.steps++;
        	MyMainFrame.stepLabel.setText("步数："+MyMainFrame.steps);
        } else if(x1-x2==-cellw && y1==y2) {
        	button.move("RIGHT",cellw,cellh);
        	MyMainFrame.steps++;
        	MyMainFrame.stepLabel.setText("步数："+MyMainFrame.steps);
        }else {
        	return; 
        }
                
        cellNull.setLocation(x1,y1);
        this.repaint();
        if(isFinish()) { 
        	finished=true;
        	MyMainFrame.tf=false;
        	if(JOptionPane.showConfirmDialog(this,"Congratulations!您想要继续吗？","",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.YES_OPTION) {
        		
        	} else {
        		
        	}
        	MyMainFrame.start.setText("开        始");
        	for(int i=0;i<type*type-1;i++) {
        		cell[i].removeMouseListener(this);
        	}
        	hasAddActionListener=false;
        }
	}
	
	class Re extends Thread{
		int[][] State;
		int[] moves;
		public Re() {
			State=new int[type][type];
			for(int i=0;i<type*type-1;i++) {
				int x=cell[i].getBounds().x;
				int y=cell[i].getBounds().y;
				State[y/cellh][x/cellw]=i+1;
			}
			Solve s=new Solve(State);
			moves=s.startSlove();
		}
		
		public void run() {
			int[] X= {0,-cellw,0,cellw};
	        int[] Y= {-cellh,0,cellh,0};
			//System.out.println(moves.length);
			for(int i=0;i<moves.length;i++) {
				//System.out.print(moves[i]+" ");
				int x1=cellNull.getBounds().x; 
		        int y1=cellNull.getBounds().y;
		        int x2=x1+X[moves[i]];
		        int y2=y1+Y[moves[i]];
		        for(int j=0;j<type*type-1;j++) {
					if((cell[j].getBounds().x==x2) && (cell[j].getBounds().y==y2)) {
				        cell[j].move(directions[(moves[i]+2)%4],cellw,cellh);
				        cellNull.setLocation(x2,y2);
				        MyMainFrame.steps++;
			        	MyMainFrame.stepLabel.setText("步数："+MyMainFrame.steps);
				        repaint();
				        break;
					}
		        }
		        try {
		        	Thread.sleep(500);
		        }catch(InterruptedException e) {
		        	e.printStackTrace();
		        }
			}
			MyMainFrame.start.setText("开        始");
			finished=true;
			MyMainFrame.res=false;
			for(int i=0;i<type*type-1;i++) {
        		cell[i].removeMouseListener(mc);
        	}
        	hasAddActionListener=false;
		}
	}
	
}
