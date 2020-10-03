package tool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Split {
	public static BufferedImage[][] cuttingImage(String path,int type,int w,int h){
		try {
			BufferedImage image=ImageIO.read(new File(path));
			BufferedImage[][] subimages=new BufferedImage[type][type];
			if(type*w>image.getWidth()||type*h>image.getHeight()) {
				return null;
			}
			for(int i=0;i<type;i++) {
				for(int j=0;j<type;j++) {
					subimages[i][j]=image.getSubimage(j*w, i*h, w, h);
				}
			}
			return subimages;
		} catch(IOException e) {
			return null;
		}
	}
}
