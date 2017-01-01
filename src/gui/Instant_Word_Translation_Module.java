package gui;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JProgressBar;

public class Instant_Word_Translation_Module extends Thread{

	static String[][] rule=new String[50000][2];	// remove the Hrd Code
	
	static JProgressBar prog=new JProgressBar();
	
	public static void Dictionary() throws IOException{
		
		int count=0;
		float percent=0;
		
		File f = new File("src\\Pattern/WORD.txt");
		if(f.exists() && !f.isDirectory()) { 
		   
		}else{
			
			System.out.println(" Dictionary File NOT Exists");
		}
		
		for (String line : Files.readAllLines(Paths.get("src\\Pattern/WORD.txt"))) {	// read the Rules file in the Src folder and Extract a Line
			rule[count]=line.split(",");	// split the String line in the Rules text file
			count++;
			percent=(count/490);
			
			prog.setValue((int)percent);
		
		}


		try {
			sleep(4000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Loading_Page.Clse(); // Close Loading Window
	}
	
	public static String Check(String var){
		String Result=null;
		for(int i=0;i<rule.length;i++){
			for(int j=0;j<2;j++){
				if(var.equals(rule[i][j])){
					//System.out.println(rule[i][j+1]);
					Result=rule[i][j+1];
				}
			}
		}
		return Result;
	}
	
}
