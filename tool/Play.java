package tool;

import java.io.File;
import java.io.IOException;

//多引进东西，减少代码的数量
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

import ui.MyMainFrame;

public class Play extends Thread{
	public String file_path="sounds/1.wav";
	public AudioInputStream ais; 
	public SourceDataLine line; 
	public AudioFormat baseFormat;
	public static final int BUFFER_SIZE=4000*4;
    
	public SourceDataLine getLine(AudioFormat audioFormat) {
		SourceDataLine res = null;
		DataLine.Info info = new DataLine.Info(SourceDataLine.class,audioFormat);
		try {
			res = (SourceDataLine) AudioSystem.getLine(info);
			res.open(audioFormat);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
    
	public void run() {
		while(MyMainFrame.msign) {
			try {
				ais= AudioSystem.getAudioInputStream(new File(file_path));
			} catch (UnsupportedAudioFileException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			baseFormat = ais.getFormat();
	        
			line = getLine(baseFormat);
			line.start();
			int inBytes = 0;
			byte[] audioData=new byte[BUFFER_SIZE];  
			while (inBytes != -1&&MyMainFrame.msign) {
				try {
					inBytes = ais.read(audioData, 0, BUFFER_SIZE);
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
				if (inBytes >= 0) { 
					@SuppressWarnings("unused")
					int outBytes = line.write(audioData, 0, inBytes);
				}
			}
		}
	}
}
