package gui;

import java.awt.EventQueue;
//import java.awt.Frame;
//import java.awt.Shape;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JProgressBar;

import java.awt.Color;
import java.awt.Panel;
//import java.awt.geom.Ellipse2D;
//import java.io.File;
//import java.net.URI;

//import javax.swing.JTextField;
import javax.swing.JLabel;


//import com.kitfox.svg.SVGCache;
//import com.kitfox.svg.SVGDiagram;
//import com.kitfox.svg.SVGElement;
//import com.kitfox.svg.ShapeElement;
//import com.sun.awt.AWTUtilities;

public class Loading_Page {

	private static JFrame frame;
	static JProgressBar progressBar = new JProgressBar();
	/**
	 * Launch the application.
	 */
	public static void Load() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Loading_Page window = new Loading_Page();
					LangfiKA_GUI.frame.setVisible(false);
					window.frame.setVisible(true);
					
					Dictionary_Load_Thread a=new Dictionary_Load_Thread();
					a.start();
					Instant_Word_Translation_Module.prog=progressBar;
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Loading_Page() {
		initialize();
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setUndecorated(true);
		frame.getContentPane().setLayout(null);
		frame.setLocationRelativeTo(null);
		Panel panel = new Panel();
		panel.setBounds(0, 0, 450, 300);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblPreparingTheInterface = new JLabel("Loading...");
		lblPreparingTheInterface.setBounds(10, 264, 153, 14);
		panel.add(lblPreparingTheInterface);
		progressBar.setForeground(new Color(255, 140, 0));
		
		
		progressBar.setBounds(10, 283, 430, 14);
		panel.add(progressBar);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 11, 430, 289);
		lblNewLabel.setIcon(new ImageIcon("src\\LangfiKA_LogoK.png"));
		panel.add(lblNewLabel);

		
	}
	public static void Clse(){
		frame.setVisible(false);
		LangfiKA_GUI.frame.setVisible(true);
	}
}
