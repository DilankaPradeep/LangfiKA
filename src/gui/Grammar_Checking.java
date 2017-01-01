package gui;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;

public class Grammar_Checking {
	static JTextArea textArea=LangfiKA_GUI.textArea;
	
	//----------Grammar Checker--------------------------------------------
	public static String CheckGrammar(String sentence) throws IOException{
		String outPut="";
		JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
		// comment in to use statistical ngram data:
	        List<RuleMatch> matches = langTool.check(sentence);
	        for (RuleMatch match : matches) {
	        	outPut=outPut+"<h3>Potential error at characters " +
	                    match.getFromPos() + "-" + match.getToPos() + ": </h3>" +
	                    " &nbsp; &nbsp; &nbsp; &nbsp;"+match.getMessage();
	            outPut=outPut+"<br> &nbsp; &nbsp; &nbsp; &nbsp;Suggested correction(s): " +
	                    match.getSuggestedReplacements()+"<br><br>";
		        // HEIGHLIGHTER
		        Highlighter highlighter = textArea.getHighlighter();
		        HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
		        try {
					highlighter.addHighlight(match.getFromPos(),  match.getToPos(), painter );
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	        return outPut;
		
	}
}

// langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));