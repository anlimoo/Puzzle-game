package dao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class InformationWR {
	static String src="userinfo.txt";
	
	public static boolean Check_User_Psw(String user,String psw) {
		String str;
		try {
			FileInputStream fis=new FileInputStream(src);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			while((str=br.readLine())!=null) {
				String[] info=str.split(String.valueOf((char)30));
				if(info[1].equals(user)&&info[3].equals(psw)) {
					Closes(0,fis,isr,br,null,null,null);
					return true;
				}
			}
			Closes(0,fis,isr,br,null,null,null);
			return false;
		}catch(FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			System.exit(0);
			return false;
		}
	}
	
	public static boolean Write_info(String user,String nick,String psw) {
		try {
			File path=new File(src);
			FileOutputStream fos=new FileOutputStream(path);
			OutputStreamWriter osr=new OutputStreamWriter(fos);
			BufferedWriter bw=new BufferedWriter(osr);
			
			FileInputStream fis=new FileInputStream(src);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			String str;
			int count=0;
			while((str=br.readLine())!=null) {
				String[] info=str.split(String.valueOf((char)30));
				if(info[1]==user) {
					Closes(1,null,null,null,fos,osr,bw);
					Closes(0,fis,isr,br,null,null,null);
					return false;
				}
				count++;
			}
			
			bw.write((count+1)+String.valueOf((char)30)+user+String.valueOf((char)30)+nick+String.valueOf((char)30)+psw+"\r\n");
			Closes(1,null,null,null,fos,osr,bw);
			Closes(0,fis,isr,br,null,null,null);
			return true;
		}catch(IOException e) {
			return false;
		}
	}
	
	public static String[] getUsers() {
		String[] users;
		try {
			FileInputStream fis=new FileInputStream(src);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			String str;
			String[] s=new String[1000];
			int count=0;
			while((str=br.readLine())!=null) {
				String[] info=str.split(String.valueOf((char)30));
				s[count++]=info[1];
			}
			users=new String[count];
			for(int i=0;i<count;i++) {
				users[i]=s[i];
			}
			Closes(0,fis,isr,br,null,null,null);
			return users;
		}catch(IOException e) {
			return null;
		}
	}
	
	public static String[] getUserInfo(String user) {
		String str;
		try {
			FileInputStream fis=new FileInputStream(src);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			while((str=br.readLine())!=null) {
				String[] info=str.split(String.valueOf((char)30));
				if(info[1].equals(user)) {
					Closes(0,fis,isr,br,null,null,null);
					return info;
				}
			}
			Closes(0,fis,isr,br,null,null,null);
			return null;
		}catch(FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			System.exit(0);
			return null;
		}

	}
	
	public static void Closes(int c,FileInputStream fis,InputStreamReader isr,BufferedReader br,FileOutputStream fos,OutputStreamWriter osr,BufferedWriter bw) {
		if(c==0) {
			try {
				br.close();
				isr.close();
				fis.close();
			}catch(IOException e) {
				System.exit(0);
			}
		}else if(c==1) {
			try {
				bw.close();
				osr.close();
				fos.close();
			}catch(IOException e) {
				System.exit(0);
			}
		}
	}

}
