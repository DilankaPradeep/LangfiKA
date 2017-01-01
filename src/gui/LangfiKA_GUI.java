package gui;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.io.*;

import com.inet.jortho.FileUserDictionary;
import com.inet.jortho.SpellChecker;

import java.net.URI;

import com.sun.awt.AWTUtilities;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.ShapeElement;

public class LangfiKA_GUI extends JFrame {
	//--------------- Plagiarism Def------------
    //We need a real browser user agent or Google will block our request with a 403 - Forbidden
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36";
    final static int THREADHOLD_VALUE=60;// 60% percent
    final static int ELIGIBLE_MINIMUM_CHARACTER_COUNT=10; // Minimum charactor count to identify as a sentence
    
	private int xMouse;
	private int yMouse;
	static JPanel contentPane;
	protected String lastWord;
	
	//------------------Dictionary---------------------------------------
	static JPanel Translator_UI = new JPanel();
	private final JTextPane Translated_Text = new JTextPane();
	private final JLabel Translation_Header = new JLabel("");
	
	//------------------TEXT Area------------------
	static String[] ary=new String[10];
	static JTextArea textArea = new JTextArea();
	JLabel Main_Window = new JLabel("");
	static JTextPane suggessionArea = new JTextPane();
	
	//------------------------------Status Bar--------------------------
	private JLabel Status = new JLabel();
	
	//------------------------------GUI Def--------------------------
	static LangfiKA_GUI frame = new LangfiKA_GUI();
	

    
    	// plagiarism GUI
    TextArea inputTextPlane = new TextArea();
    TextArea referenceTextPlane = new TextArea();
    
    //------------- main Planes
	JPanel SpelChk_plane = new JPanel();
	JPanel Menu_Bar_SpellChecker = new JPanel();
	JPanel Help_plane = new JPanel();
	JPanel Plagiarism_plane = new JPanel();
	JPanel Menu_Bar_Plagiarism = new JPanel();
	JPanel Option_plane = new JPanel();
	JPanel Menu_Bar_Options = new JPanel();
	JPanel Menu_Bar_Help = new JPanel();
	private final JButton btnCheckOnline = new JButton("Check Online");
	private final JTextPane plagiarismResults = new JTextPane();
	private final JButton btnOpen = new JButton("Open");
	private final JButton btnOpen_1 = new JButton("Open File");

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Loading_Page.Load();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	//-------------------------- Detect Mouse Motion to Move the Window ---------------------------------------
	public void mouseMotion(java.awt.event.MouseEvent arg0) {
		xMouse=arg0.getX();
		yMouse=arg0.getY();
	}
//---------------------------------- GUI Main Plane Codes---------------------------------
	public LangfiKA_GUI() {
		setSize(1366,768);
	    setUndecorated(true);
	    setVisible(true);
		contentPane = new JPanel();
		setContentPane(contentPane);
		
		
		langfiKAInitSpelCheck();
		
		
		//--------------------------------------- Content Pane DRAG Movements ---------------------------------------
		contentPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				mouseMotion(e);
			}
		});
		//------------------Get Difference of Mouse location and Dragged Position---------------------------------------
		contentPane.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent avt) {
				int x=avt.getXOnScreen();
				int y=avt.getYOnScreen();
				
				frame.setLocation(x-xMouse,y-yMouse);
			}
		});
		contentPane.setLayout(null);
				
				
				// ----------- Plagiarism Menu---------------
				Menu_Bar_Plagiarism.setBackground(Color.WHITE);
				Menu_Bar_Plagiarism.setBounds(169, 27, 962, 76);
				Menu_Bar_Plagiarism.setLayout(null);
				Menu_Bar_Plagiarism.setVisible(false);
				
//------------- Main Menu Bars -------------------------------
						Menu_Bar_SpellChecker.setBackground(Color.WHITE);
						Menu_Bar_SpellChecker.setBounds(169, 27, 962, 76);
						Menu_Bar_SpellChecker.setVisible(true);
						
							
// ------------------------------- Planes Definition --------------------
						//---------------------------- Spell Check Plane ----------------------
						SpelChk_plane.setBackground(Color.DARK_GRAY);
						SpelChk_plane.setBounds(116, 103, 1021, 477);
						SpelChk_plane.setVisible(true);					
						//-------------------------------------- Main Text Area Ends ----------------------------
						
						//----------------- Plagiarism Plane ----------------------------------------
						Plagiarism_plane.setBackground(Color.DARK_GRAY);
						Plagiarism_plane.setBounds(116, 103, 1021, 477);
						Plagiarism_plane.setVisible(false);
						contentPane.add(Plagiarism_plane);
						Plagiarism_plane.setLayout(null);
						inputTextPlane.setBounds(10, 10, 531, 452);
						Plagiarism_plane.add(inputTextPlane);
						
						JButton btnCheck = new JButton("Check Plagiarism");
						btnCheck.setBounds(566, 423, 147, 39);
						btnCheck.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
							}
						});
						btnCheck.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								// UI Changes to select 2nd tab - Results tab
								fileReferenceToggle("results");
								// Extraxt sentences
								String[] inputText=LangfiKA_Utill.langfiKASentenceExtractor(inputTextPlane.getText());
								String[] referenceText=LangfiKA_Utill.langfiKASentenceExtractor(referenceTextPlane.getText());
								// Check for offline plagiarism
								String[] maxIndex=Plagiarism_Checking.checkPlagiarism(inputText,referenceText);
						
						        /** plagiarism percentage */
						        double overallPlagiarismPercentage=Double.parseDouble(maxIndex[maxIndex.length-1]);
						
						        String resultText="";
						        
						        // Display Purposes
						        for(int a=0;a<inputText.length;a++) {
						            //System.out.println(a+"-"+inputText[a]);// split from ? . !
						        	//resultText=appendString(resultText,a+"-"+inputText[a]+"\n");
						        }
						        // dont print NULLs
						        for(int k=0;k<inputText.length;k++){
						            if(maxIndex[k]!=null){
						              //  System.out.println(maxIndex[k]);
						            	resultText=LangfiKA_Utill.appendString(resultText,maxIndex[k]+"<br><br>");
						            }
						        }
						        //System.out.println("Overall Plagiarism Percentage is : "+overallPlagiarismPercentage+"%");
						        resultText=LangfiKA_Utill.appendString(resultText,"<b>Overall Plagiarism Percentage is : "+overallPlagiarismPercentage+"%</b><br><br>");

						        plagiarismResults.setContentType("text/html");
						        plagiarismResults.setText(resultText);
							}
						});
						Plagiarism_plane.add(btnCheck);
						btnCheckOnline.setBounds(723, 422, 147, 39);
						btnCheckOnline.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								// UI Changes
								fileReferenceToggle("results");
								// extract sentence
								String[] inputText=LangfiKA_Utill.langfiKASentenceExtractor(inputTextPlane.getText());
								// call Online Request
								plagiarismResults.setContentType("text/html");
								plagiarismResults.setText(Plagiarism_Checking.CheckOnlinePlagiarism(inputText));
							}
						});
						btnCheckOnline.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
							}
						});
						
						Plagiarism_plane.add(btnCheckOnline);
						btnOpen_1.setBounds(879, 422, 123, 39);
						btnOpen_1.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								fileReferenceToggle("file");
							}
						});
						btnOpen_1.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								referenceTextPlane.setText(LangfiKA_Utill.OpenFile());
							}
						});
						
						Plagiarism_plane.add(btnOpen_1);
						referenceTextPlane.setBounds(547, 27, 455, 390);
						Plagiarism_plane.add(referenceTextPlane);
						plagiarismResults.setBounds(547, 27, 455, 390);
						Plagiarism_plane.add(plagiarismResults);
						
						JLabel lblFile = new JLabel("File");
						lblFile.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								fileReferenceToggle("file");
							}
						});
						lblFile.setBounds(547, 11, 73, 14);
						Plagiarism_plane.add(lblFile);
						
						JLabel lblResult = new JLabel("Result");
						lblResult.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								fileReferenceToggle("results");
							}
						});
						lblResult.setBounds(630, 10, 73, 14);
						Plagiarism_plane.add(lblResult);
						
						
//------------INTERFACE and BUTTONS-----------------------------------------------------
						//---------------- Instant Translator UI -------------------------
						Translator_UI.setBackground(SystemColor.control);
						Translator_UI.setBounds(10, 11, 414, 81);
						Translator_UI.setLayout(null);
						Translator_UI.setBorder(UIManager.getBorder("FormattedTextField.border"));
						contentPane.add(Translator_UI);
						Translator_UI.setVisible(false);
						//--------------Translator_UI Contents----------
								Translation_Header.setBounds(10, 0, 200, 21);
								Translator_UI.add(Translation_Header);
								
								Translated_Text.setBackground(SystemColor.inactiveCaptionBorder);
								Translated_Text.setBounds(10, 21, 394, 49);
								Translated_Text.setFont(new Font("Iskoola Pota", Font.PLAIN, 11));
								Translator_UI.add(Translated_Text);
						SpelChk_plane.setLayout(null);
						contentPane.add(SpelChk_plane);
						
								//---------Suggession Area -------------------------
								//suggessionArea.setLineWrap(true);
								suggessionArea.setBackground(Color.WHITE);
								suggessionArea.setBounds(590, 11, 421, 450);
								suggessionArea.setText("");
								SpelChk_plane.add(suggessionArea);
								
								//------------ Main Text Area ---------------------------
								textArea.addKeyListener(new KeyAdapter() {
									@Override
									public void keyPressed(KeyEvent arg0) {
										try {
											StatusBar();
										} catch (BadLocationException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
									}
								});
								textArea.setBackground(Color.WHITE);
								textArea.setLineWrap(true);
								textArea.setBounds(10, 11, 570, 450);
								
										//---------------------------Dictionary Callings -------------------
										textArea.addMouseMotionListener(new MouseMotionAdapter() {
											@Override
											public void mouseMoved(MouseEvent avt) {
												int x=avt.getX();
												int y=avt.getY();
												
												Translator_UI.setLocation(x+130,y+120);
											}
										});
										textArea.addMouseListener(new MouseAdapter() {
											@Override
											public void mouseClicked(MouseEvent e) {
												UpdateCurrentStatus();
											}
										});
										
										SpelChk_plane.add(textArea);
						contentPane.add(Menu_Bar_SpellChecker);
						Menu_Bar_SpellChecker.setLayout(null);
						
						JButton btnCheckGrammar = new JButton("Check Grammar");
						btnCheckGrammar.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
							}
						});
						btnCheckGrammar.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
								suggessionArea.setContentType("text/html");
								try {
									suggessionArea.setText(Grammar_Checking.CheckGrammar(textArea.getText()));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						});
						btnCheckGrammar.setBounds(823, 30, 129, 35);
						Menu_Bar_SpellChecker.add(btnCheckGrammar);
						
						JButton button = new JButton("Save");
						button.setToolTipText("Save");
						//button.setIcon("");
						button.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								LangfiKA_Utill.SaveFile();
							}
						});
						button.setBounds(10, 30, 85, 35);
						Menu_Bar_SpellChecker.add(button);
						btnOpen.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
							}
						});
						btnOpen.addMouseListener(new MouseAdapter() {
							@Override
							public void mouseClicked(MouseEvent arg0) {
						        textArea.setText(LangfiKA_Utill.OpenFile());
							}
						});
						btnOpen.setBounds(101, 30, 95, 35);
						
						Menu_Bar_SpellChecker.add(btnOpen);
						
						JMenuBar menuBar = new JMenuBar();
						JMenu fileMenu = new JMenu("File");
							JMenuItem fileNew = new JMenuItem("New");
							fileMenu.add(fileNew);
							JMenuItem fileOpen = new JMenuItem("Open");
							fileMenu.add(fileOpen);
							JMenuItem fileSave = new JMenuItem("Save");
							fileMenu.add(fileSave);
							JMenuItem fileSaveAs = new JMenuItem("Save As");
							fileMenu.add(fileSaveAs);
							JMenuItem fileExit = new JMenuItem("Exit");
							fileMenu.add(fileExit);
							
						JMenu windowMenu = new JMenu("Window");
							JMenuItem windowGrammar = new JMenuItem("Grammar Checker");
							windowGrammar.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent arg0) {
									mainSwitch("spellCheck");
								}
							});
							windowMenu.add(windowGrammar);
							JMenuItem windowPlagiarism = new JMenuItem("Plagiarism Checker");
							windowPlagiarism.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent arg0) {
									mainSwitch("plagiarism");
								}
							});
							windowMenu.add(windowPlagiarism);
							JMenuItem windowOptions = new JMenuItem("Options");
							windowOptions.addMouseListener(new MouseAdapter() {
								@Override
								public void mouseClicked(MouseEvent arg0) {
									mainSwitch("options");
								}
							});
							windowMenu.add(windowOptions);
						
						JMenu editMenu = new JMenu("Edit");
							JMenuItem editUndo = new JMenuItem("Undo");
							editMenu.add(editUndo);
							JMenuItem editRedo = new JMenuItem("Redo");
							editMenu.add(editRedo);
							JMenuItem editCut = new JMenuItem("Cut");
							editMenu.add(editCut);
							JMenuItem editCopy = new JMenuItem("Copy");
							editMenu.add(editCopy);
							JMenuItem editPast = new JMenuItem("Paste");
							editMenu.add(editPast);
							
						JMenu helpMenu = new JMenu("Help");
							JMenuItem aboutLangfiKA = new JMenuItem("About LangfiKA");
							helpMenu.add(aboutLangfiKA);		
							JMenuItem help = new JMenuItem("Help");
							helpMenu.add(help);
						
						menuBar.setBounds(0, 0, 972, 21);
						Menu_Bar_SpellChecker.add(menuBar);
						menuBar.add(fileMenu);
						menuBar.add(editMenu);
						menuBar.add(windowMenu);
						menuBar.add(helpMenu);
						
						JButton btnB = new JButton("B");
						btnB.setFont(new Font("Tahoma", Font.BOLD, 12));
						btnB.setBounds(206, 30, 46, 35);
						Menu_Bar_SpellChecker.add(btnB);
						
						JButton btnI = new JButton("I");
						btnI.setFont(new Font("Tahoma", Font.ITALIC, 12));
						btnI.setBounds(262, 30, 46, 35);
						Menu_Bar_SpellChecker.add(btnI);
						
						JButton btnU = new JButton("U");
						btnU.setFont(new Font("Tahoma", Font.BOLD, 12));
						btnU.setBounds(318, 30, 46, 35);
						Menu_Bar_SpellChecker.add(btnU);
						contentPane.add(Menu_Bar_Plagiarism);
				
					JButton btnOneWithMultiple = new JButton("One with File");
					btnOneWithMultiple.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							plagiarismPlaneSwitch("twoFiles");
						}
					});
					btnOneWithMultiple.setBounds(157, 37, 147, 39);
					Menu_Bar_Plagiarism.add(btnOneWithMultiple);
					
					JButton btnNewButton = new JButton("Two Texts");
					btnNewButton.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent arg0) {
						}
					});
					btnNewButton.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent arg0) {
							plagiarismPlaneSwitch("twoInputs");
						}
					});
					btnNewButton.setBounds(0, 37, 147, 39);
					Menu_Bar_Plagiarism.add(btnNewButton);
					
					JButton btnOnline = new JButton("Online");
					btnOnline.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							plagiarismPlaneSwitch("online");
						}
					});
					btnOnline.setBounds(314, 37, 147, 39);
					Menu_Bar_Plagiarism.add(btnOnline);
		
		//----------------------- Options Plane------------------------------
		Option_plane.setBackground(new Color(0, 0, 255));
		Option_plane.setBounds(119, 103, 1012, 472);
		Option_plane.setVisible(false);
		contentPane.add(Option_plane);
		
		//----------------------- Help Plane------------------------------
		Help_plane.setBackground(Color.RED);
		Help_plane.setBounds(119, 103, 1012, 472);
		Help_plane.setVisible(false);
		contentPane.add(Help_plane);
					
				
				
				Menu_Bar_Options.setBackground(Color.WHITE);
				Menu_Bar_Options.setBounds(169, 27, 962, 76);
				Menu_Bar_Options.setLayout(null);
				Menu_Bar_Options.setVisible(false);
				contentPane.add(Menu_Bar_Options);
				
				Menu_Bar_Help.setBackground(Color.WHITE);
				Menu_Bar_Help.setBounds(169, 27, 962, 76);
				Menu_Bar_Help.setLayout(null);
				Menu_Bar_Help.setVisible(false);
				contentPane.add(Menu_Bar_Help);
		
//-------------------------------Main Buttons Definition------------------------------
		//-------------------------------Spell Check Button-----------------------------
		JLabel lblSpellCheck = new JLabel("  Spell Check");
		lblSpellCheck.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mainSwitch("spellCheck");
			}
		});
		lblSpellCheck.setForeground(Color.LIGHT_GRAY);
		lblSpellCheck.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSpellCheck.setBackground(Color.WHITE);
		lblSpellCheck.setBounds(10, 129, 111, 68);
		contentPane.add(lblSpellCheck);
		
		//-------------------------------Plagiarism Button-----------------------------
		JLabel lblPlagiarism = new JLabel("  Plagiarism");
		lblPlagiarism.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//load UI Defaults
				plagiarismPlaneSwitch("loadDefaults");
				// switch to Plagiarism Plane
				mainSwitch("plagiarism");
			}
		});
		lblPlagiarism.setForeground(Color.LIGHT_GRAY);
		lblPlagiarism.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPlagiarism.setBackground(Color.WHITE);
		lblPlagiarism.setBounds(10, 208, 111, 68);
		contentPane.add(lblPlagiarism);
		
		//-------------------------------Options Button-----------------------------
		JLabel lblOptions = new JLabel("  Options");
		lblOptions.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mainSwitch("options");
			}
		});
		lblOptions.setForeground(Color.LIGHT_GRAY);
		lblOptions.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblOptions.setBackground(Color.WHITE);
		lblOptions.setBounds(10, 287, 111, 68);
		contentPane.add(lblOptions);
		
		//-------------------------------Help Button-----------------------------
		JLabel lblHelp = new JLabel("  Help");
		lblHelp.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				mainSwitch("help");
			}
		});
		lblHelp.setForeground(Color.LIGHT_GRAY);
		lblHelp.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblHelp.setBackground(Color.WHITE);
		lblHelp.setBounds(10, 366, 111, 68);
		contentPane.add(lblHelp);
	
		//---------------------------- Status Bar UI-------------------
		Status.setBounds(119, 586, 561, 14);
		contentPane.add(Status);
		

		
// ------------- Window Buttons --- Close --- Minimize --- Maximize ------------------------
		JLabel Close = new JLabel("");	// Close Button As Transparent Label
		Close.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.exit(0);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Main_Window.setIcon(new ImageIcon("src\\guiNewDeselect.png"));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Main_Window.setIcon(new ImageIcon("src\\guiSample1.png"));
			}
		});
		Close.setBounds(1109, 0, 28, 30);
		contentPane.add(Close);
		
		JLabel Minimize = new JLabel("");	// Maximize Button Label
		Minimize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// setState(ICONIFIED);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Main_Window.setIcon(new ImageIcon("src\\guiNewDeselect.png"));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Main_Window.setIcon(new ImageIcon("src\\guiSample1.png"));
			}
		});
		
		
		Minimize.setOpaque(false);
		Minimize.setBounds(1080, 0, 28, 30);
		contentPane.add(Minimize);
		
		JLabel Maximize = new JLabel("");	// Minimize Button Label
		Maximize.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setState(ICONIFIED);
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				Main_Window.setIcon(new ImageIcon("src\\guiNewDeselect.png"));
			}
			@Override
			public void mouseExited(MouseEvent arg0) {
				Main_Window.setIcon(new ImageIcon("src\\guiSample1.png"));
			}
		});
		Maximize.setOpaque(false);
		Maximize.setBounds(1054, 0, 28, 30);
		contentPane.add(Maximize);
		
		

//--------------------------------------- Image of UI ---------------------------------------
		
		Main_Window.setBounds(0, 0, 1356, 768);	// Main Label
		Main_Window.setIcon(new ImageIcon("src\\guiSample1.png"));
		contentPane.add(Main_Window);
		
	
//--------------------------------------- Shape of the UI ---------------------------------------
		Shape shape=new Ellipse2D.Float(100, 100,1366,768);
	    shape = getShape("src/LangfiKAGUI1.svg");
	    AWTUtilities.setWindowShape(this, shape);
	     
	}

	private Shape getShape(String pgsvg) {
					//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
      try{
      URI uri = new File(pgsvg).toURI();
      SVGDiagram diagram=SVGCache.getSVGUniverse().getDiagram(uri);
      SVGElement element=diagram.getElement("path3008");
     @SuppressWarnings("rawtypes")
     java.util.List list=(element.getPath(null));
      return ((ShapeElement)(list.get(0))).getShape();
      }catch(Exception ex){
      }
      return null;
  }

	//--------------- Spell Checker Initialization (JOrtho)---------------------------------------
	public void langfiKAInitSpelCheck(){
		String userDictionaryPath="/dictionary/";
		SpellChecker.setUserDictionaryProvider(new FileUserDictionary(userDictionaryPath));
		SpellChecker.registerDictionaries(getClass().getResource(userDictionaryPath),"en");
		
		SpellChecker.register(textArea);
		
	}
	
	//-------------------- Instant Dictionary---------------------------------------
	private void UpdateCurrentStatus(){
		try {
			StatusBar(); // Update Status Bar Function
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//--------------------------------- Instatnt Word Translation --------------------------------
		String[] wordsArray = textArea.getText().split("\\s+"); // Split the TextPane Array
		String wholeText=textArea.getText();		// Get THe Length of the Whole Text
		String[][] splArray=new String[wordsArray.length][];	// make a 2d Arry to Represent Word and letters
		int CaretLetter=textArea.getCaretPosition();	// get Caret Position
		for(int a=0;a<wordsArray.length;a++){
			splArray[a]=wordsArray[a].split("");	// split into words
		}
		
		//int totalWords=wordsArray.length;
		int totalSpl=wholeText.length();	 // get Total Letters	
		int splPosition=totalSpl-CaretLetter;
		int barrier=0;
		int a=0;
		int b=0;
		for(a=wordsArray.length-1;a>-1;a--){// get the word from the tagged position charactor
			if(barrier==0){
				for(b=splArray[a].length-1;b>-1;b--){
					if(splPosition==0){
						barrier=1;
						break;
					}else{
						splPosition--;
					}
				}
			splPosition--;
			}else{
				break;
			}
		}
		if(Instant_Word_Translation_Module.Check(wordsArray[a+1].toLowerCase())!=null){
			Translator_UI.setVisible(true);
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
			  @Override
			  public void run() {
				  Translator_UI.setVisible(false);
			  }
			}, 5*1000);
		}
		Translation_Header.setText(wordsArray[a+1]+":");
		Translated_Text.setText(Instant_Word_Translation_Module.Check(wordsArray[a+1].toLowerCase()));
	}
	
	//---------- Status Bar ---------------------------------------
	public void StatusBar() throws BadLocationException{
		int CaretPos=textArea.getCaretPosition();
		int lineNum=textArea.getLineOfOffset(CaretPos);
		Status.setText("Col : "+(CaretPos-textArea.getLineStartOffset(lineNum))+" | Line : "+(lineNum+1) );
	}
	
	// main Switch
	private void mainSwitch(String input){
		switch (input) {
		case "spellCheck":
			SpelChk_plane.setVisible(true);
			Menu_Bar_SpellChecker.setVisible(true);
			
			Plagiarism_plane.setVisible(false);
			Menu_Bar_Plagiarism.setVisible(false);
			
			Option_plane.setVisible(false);
			Menu_Bar_Options.setVisible(false);
			
			Help_plane.setVisible(false);
			Menu_Bar_Help.setVisible(false);
			break;
		case "plagiarism":	
			SpelChk_plane.setVisible(false);
			Menu_Bar_SpellChecker.setVisible(false);
			
			Plagiarism_plane.setVisible(true);
			Menu_Bar_Plagiarism.setVisible(true);
			
			Option_plane.setVisible(false);
			Menu_Bar_Options.setVisible(false);
			
			Help_plane.setVisible(false);
			Menu_Bar_Help.setVisible(false);
			break;
		case "options":
			SpelChk_plane.setVisible(false);
			Menu_Bar_SpellChecker.setVisible(false);
			
			Plagiarism_plane.setVisible(false);
			Menu_Bar_Plagiarism.setVisible(false);
			
			Option_plane.setVisible(true);
			Menu_Bar_Options.setVisible(true);
			
			Help_plane.setVisible(false);
			Menu_Bar_Help.setVisible(false);
			break;
		case "help":
			SpelChk_plane.setVisible(false);
			Menu_Bar_SpellChecker.setVisible(false);
			
			Plagiarism_plane.setVisible(false);
			Menu_Bar_Plagiarism.setVisible(false);
			
			Option_plane.setVisible(false);
			Menu_Bar_Options.setVisible(false);
			
			Help_plane.setVisible(true);
			Menu_Bar_Help.setVisible(true);
			break;

		default:			
			SpelChk_plane.setVisible(true);
			Menu_Bar_SpellChecker.setVisible(true);
			
			Plagiarism_plane.setVisible(false);
			Menu_Bar_Plagiarism.setVisible(false);
			
			Option_plane.setVisible(false);
			Menu_Bar_Options.setVisible(false);
			
			Help_plane.setVisible(false);
			Menu_Bar_Help.setVisible(false);
			break;
		}
	}
	
	// plagiarsim Plane Switch
	private void plagiarismPlaneSwitch(String input){
		switch (input) {
		case "twoInputs":
			inputTextPlane.setVisible(true);
			referenceTextPlane.setVisible(true);
			plagiarismResults.setVisible(false);
			break;
		case "twoFiles":
			inputTextPlane.setVisible(true);
			referenceTextPlane.setVisible(false);
			plagiarismResults.setVisible(false);
			break;
		case "online":
			inputTextPlane.setVisible(true);
			referenceTextPlane.setVisible(false);
			plagiarismResults.setVisible(true);
			plagiarismResults.setContentType("text/html");
			//plagiarismResults.disable();
			//plagiarismResults.setText(appendString(plagiarismResults.getText(),"<a href='http://stackoverflow.com/questions/16729965/how-to-watch-multiple-variable-change-in-angular'>Click Here</a>"));
			plagiarismResults.setText("<a href='http://stackoverflow.com/questions/16729965/how-to-watch-multiple-variable-change-in-angular'>Click Here</a>");
			break;
			
		default:
			inputTextPlane.setVisible(true);
			referenceTextPlane.setVisible(true);
			plagiarismResults.setVisible(false);
			break;
		}
	}
	
	//-------- reference File switch
	public void fileReferenceToggle(String input){
		switch (input) {
		case "results":
			referenceTextPlane.setVisible(false);
			plagiarismResults.setVisible(true);
			break;

		default:
			referenceTextPlane.setVisible(true);
			plagiarismResults.setVisible(false);
			break;
		}
	}
}

class ChatLinkListener extends AbstractAction{
    private String textLink;
	private Object url;

    ChatLinkListener(String textLink){
        this.textLink = textLink;
    }

    protected void execute(){
        if("accept".equals(url)){
            //execute code
        }
        else if("decline".equals(url)){
            //execute code
        }
    }

    public void actionPerformed(ActionEvent e){
        execute();
    }
}
