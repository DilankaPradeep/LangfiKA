package gui;

import java.io.IOException;

//import javax.swing.JProgressBar;

public class Dictionary_Load_Thread extends Thread{
	
	public void run(){
		
		try {
			Instant_Word_Translation_Module.Dictionary();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}
