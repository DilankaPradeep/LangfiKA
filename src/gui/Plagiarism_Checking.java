package gui;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Plagiarism_Checking {
    public static final String USER_AGENT =LangfiKA_GUI.USER_AGENT;
    final static int THREADHOLD_VALUE=LangfiKA_GUI.THREADHOLD_VALUE;
    
	 public static String[] checkPlagiarism(String[] textInput, String[] referenceText){
	        /** array comparison for plagiarism */
	        String[][][] result=new String[textInput.length][referenceText.length][3];
	        for(int i = 0;i<textInput.length;i++){
	            for(int j = 0; j<referenceText.length;j++){
	                double x=plagiarismAlgo(textInput[i],referenceText[j]);
	                double y=normalize(textInput[i],referenceText[j]);
	                double plagiarismPercentage=(1-(x/y))*100;
	                result[i][j][0]=""+plagiarismPercentage;
	                result[i][j][1]=""+textInput[j];
	                result[i][j][2]=""+referenceText[j];
	            }
	        }
	        /** to get max value array */
	        String[] maxIndex=new String[textInput.length+1];
	        double[] percentageArray=new double[textInput.length];
	        for(int k=0;k<textInput.length;k++) {
	            double max = 0;
	            for (int l = 0; l < referenceText.length; l++) {
	                if (Double.parseDouble(result[k][l][0]) > max) {
	                    max = Double.parseDouble(result[k][l][0]);

	                    /** Threshold Filtering-Plagiarism */
	                    if (max > THREADHOLD_VALUE) {
	                        maxIndex[k] = "<b>Sentence number : "+(k+1)+"</b><br><b>Similarity percentage : "+ Math.round(Double.parseDouble(result[k][l][0])) + "%"+"</b><br><b>  Input Text : </b>" + textInput[k] + "<br><b> Reference Test :  </b>" + referenceText[l];
	                        percentageArray[k] = Double.parseDouble(result[k][l][0]);
	                    }
	                }
	            }
	        }
	        /** get Percentage of plagiarism */
	        double total=0;
	        int arrayLength=0;
	        for(int i = 0;i<percentageArray.length;i++){
	            if(percentageArray[i]!=0){
	                arrayLength+=1;
	                total+=percentageArray[i];
	            }
	        }
	        double average=total/arrayLength;
	        maxIndex[textInput.length]=""+Math.round(average);
	        return maxIndex;
	    }
	    static String CheckOnlinePlagiarism(String[] inputText){
	    	int a=1;
	    	String outPut="";
	        for (int i = 0; i < inputText.length; i++) {
				try {
		            //Fetch the page
		            Document doc;
					doc = Jsoup.connect("https://google.com/search?q="+inputText[i]+"").userAgent(USER_AGENT).get();
		
		            //Traverse the results
		            Elements result =doc.select("h3.r a");
		
		            final String title = result.text();
		            final String url = result.attr("href");
		
		            //Now do something with the results (maybe something more useful than just printing to console)
		
		//            System.out.println(a+" "+title + " -> " + url);
		
		            outPut=outPut+"<b>"+inputText[i]+"</b><br><br> "+" "+title + "<br><b> URL </b>-><a href="+url+">"+url+"</a><br><br>";
		            a++;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
	        }
	        return outPut;
	    }

	        public static int normalize(String s,String t){
	            int n = s.length(); // length of s
	            int m = t.length(); // length of t
	            if(n>m){
	                return n;
	            }else{
	                return m;
	            }
	        }
	        public static int plagiarismAlgo(String s,String t){
	      //------------- Plagiarism Detection - Calculating Levenshtein Distance of the given text in LangfiKA----------
	            // S and T are the two strings we want to compare
	            int n = s.length(); // length of s
	            int m = t.length(); // length of t
	            if (n == 0) {
	                return m;
	            }
	            else if (m == 0){
	                return n;
	            }
	            int p[] = new int[n+1];
	            int d[] = new int[n+1];
	            int _d[];
	            int i;
	            int j;
	            char t_j;
	            int cost;
	            for (i = 0; i<=n; i++) {
	                p[i] = i;
	            }
	            for (j = 1; j<=m; j++) {
	                t_j = t.charAt(j-1);
	                d[0] = j;
	                for (i=1; i<=n; i++) {
	                    cost = s.charAt(i-1)==t_j ? 0 : 1;
	                    d[i] = Math.min(Math.min(d[i-1]+1, p[i]+1), p[i-1]+cost);
	                }
	                _d = p;
	                p = d;
	                d = _d;
	            }
	            return p[n]; // Levenshtein Distance between S and T

	        }
}
