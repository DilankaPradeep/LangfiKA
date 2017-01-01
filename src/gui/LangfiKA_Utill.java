package gui;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class LangfiKA_Utill {
	private static int ELIGIBLE_MINIMUM_CHARACTER_COUNT=LangfiKA_GUI.ELIGIBLE_MINIMUM_CHARACTER_COUNT;
	private static JPanel contentPane=LangfiKA_GUI.contentPane;
	private static JTextArea textArea=LangfiKA_GUI.textArea;

    /**
     * Open and read a file, and return the lines in the file as a list
     * of Strings.
     * (Demonstrates Java FileReader, BufferedReader, and Java5.)
     * 
     */
	private static String fileReader(String filename){
        try{
	        List<String> records = new ArrayList<String>();
	        BufferedReader reader = new BufferedReader(new FileReader(filename));
	        String line;
	        while ((line = reader.readLine()) != null)
	        {
	            records.add(line);
	        }
	        reader.close();
	        String readText="";
	        for(int i =0;i<records.size();i++){
	            readText+=records.get(i).toString();
	        }
	        return readText;
	        
        }catch (Exception e){
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
	}
	
    static String[] langfiKASentenceExtractor(String inputText){
            // split into sentences
            String[] textSegment=inputText.split("\n|\\.(?!\\d)|(?<!\\d)\\.|\\?|!");
            // sentence filter
            String[] tempText=new String[textSegment.length];
            int textSegmentCounter=0;
            for(int i = 0;i<textSegment.length;i++){
                if(textSegment[i].length()>ELIGIBLE_MINIMUM_CHARACTER_COUNT){
                    tempText[textSegmentCounter]=textSegment[i];
                    textSegmentCounter++;
                }
            }
            String[] sentenceText=new String[textSegmentCounter];
            int sentenceCounter=0;
            for(int i =0 ; i<tempText.length;i++){
                if(tempText[i]!=null){
                    sentenceText[sentenceCounter]=tempText[i];
                    sentenceCounter++;
                }
            }
            return sentenceText;
      
   }
    
    static String OpenFile(){
    	JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(contentPane);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
	            try{
	    	        List<String> records = new ArrayList<String>();
	    	        BufferedReader reader = new BufferedReader(new FileReader(file));
	    	        String line;
	    	        while ((line = reader.readLine()) != null){
	    	            records.add(line);
	    	        }
	    	        reader.close();
	    	        String readText="";
	    	        for(int i =0;i<records.size();i++){
	    	            readText+=records.get(i).toString()+"\n";
	    	        }
	    	        //set Textt
	    	        return readText;
	    	        
	            }catch (Exception e){
	                System.err.format("Exception occurred trying to read '%s'.", file);
	                e.printStackTrace();
	                return null;
	            }
            //This is where a real application would open the file.
            //System.out.println("Opening: " + file.getName() + ". \n");
            
	        } else {
	        	//System.out.println("Open command cancelled by user. \n");
	        	return null;
	        }
    }
    
    static void SaveFile(){
  		final JFileChooser fc = new JFileChooser();
  		int returnVal = fc.showSaveDialog(textArea); //parent component to JFileChooser
  		if (returnVal == JFileChooser.APPROVE_OPTION) { //OK button pressed by user
  		        File file = fc.getSelectedFile(); //get File selected by user
  		        try {
  					BufferedWriter buffer= new BufferedWriter(new FileWriter(file));
  					System.out.println(textArea.getText());
  					buffer.write(textArea.getText());
  					buffer.close();
  				} catch (IOException e1) {
  					// TODO Auto-generated catch block
  					e1.printStackTrace();
  				} //use its name
  		        //your writing code goes here
  		}
      }
    
    static String appendString(String text, String newText){
    	return text+newText;
    }
}
