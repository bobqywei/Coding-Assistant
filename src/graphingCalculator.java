/*********************************HEADER*************************************************
// File: graphingCalculator.java
// Name: Bob Wei
// Date: 6/03/16
// Desc: Program to graph desired functions
// Usage: The program takes input from the user and outputs a graph on a labeled axis
//***************************************************************************************
//****************************************************************************************/





/** Imports libraries necessary for certain features in program
 * 
 *  
 */
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Rectangle;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;




/**
	Declaration of public class
 */
public class graphingCalculator {

	
	
	
	
	/*********************************************************
	//* Declaration of Global variables used throughout program
	//*********************************************************/
	//main JFrame 
	private JFrame frmdMultigrapher;
	//JPanel on which graphics will be drawn
	private JPanel graph;
	//combo box for selecting type of function
	private JComboBox<?> comboBox = new JComboBox<Object>();
	//button to graph linear function
	private JButton btnLinear;
	//following text fields for input of certain constant values depending on the type of function being inputed
	private JTextField mValueLinear;
	private JTextField bValueLinear;
	private JTextField aValueQuad;
	private JTextField bValueQuad;
	private JTextField cValueQuad;
	private JTextField aValueCubic;
	private JTextField bValueCubic;
	private JTextField cValueCubic;
	private JTextField dValueCubic;
	private JTextField aValueRoot;
	private JTextField kValueRoot;
	private JTextField dValueRoot;
	private JTextField cValueRoot;
	private JTextField aValueExp;
	private JTextField bValueExp;
	private JTextField cValueExp;
	private JTextField aValueLog;
	private JTextField bValueLog;
	private JTextField kValueLog;
	private JTextField dValueLog;
	private JTextField cValueLog;
	private JTextField aValueTrig;
	private JTextField kValueTrig;
	private JTextField dValueTrig;
	private JTextField cValueTrig;
	//check boxes for color selection
	private JCheckBox black;
	private JCheckBox red;
	private JCheckBox blue;
	private JCheckBox purple;
	private JCheckBox orange;
	private JCheckBox yellow;
	private JCheckBox cyan;
	private JCheckBox green;
	//variable for storing the currently selected color
	private Color mainColor = null;
	//combo box for selection of type of trigonometric function
	private JComboBox<?> trigComboBox = new JComboBox<Object>();
	private JTextField pValueRoot;
	private JTextField pValuePoly;
	private JTextField aValuePoly;
	private JTextField kValuePoly;
	private JTextField dValuePoly;
	private JTextField cValuePoly;
	
	
	
	
	
	/***************************************************
	//////////// DEFAULT CODE ////////////////////////
	*/
	
	 /* Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					graphingCalculator window = new graphingCalculator();
					window.frmdMultigrapher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	/*
	 * Create the application.
	 */
	public graphingCalculator() {
		initialize();
		
	}
	/****************************************************/
	
	
	
	
	
	
	
	
	
	/*************************************************************************************************************
	///////////////////////////////////////////////////////
	/* Initialize the contents of the frame.
	////////////////////////////////////////////////////*/
	
	private void initialize() {
		//initialization of the main frame
		frmdMultigrapher = new JFrame();
		frmdMultigrapher.setTitle("2D Multi-Grapher");
		frmdMultigrapher.setResizable(false);
		frmdMultigrapher.setBounds(100, 100, 700, 900);
		frmdMultigrapher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmdMultigrapher.getContentPane().setLayout(null);
		//Initialize the graphics panel
		graph = new JPanel();
		graph.setBackground(Color.WHITE);
		graph.setBounds(new Rectangle(100, 0, 600, 600));
		graph.setBounds(26, 88, 640, 640);
		frmdMultigrapher.getContentPane().add(graph);
		
		//Initialization of JLabel for displaying the title
		JLabel title = new JLabel("2D Multi-Grapher");
		title.setBackground(Color.GRAY);
		title.setForeground(Color.GRAY);
		title.setHorizontalAlignment(SwingConstants.LEFT);
		title.setFont(new Font("Eras Medium ITC", Font.BOLD, 25));
		title.setBounds(26, 20, 640, 47);
		frmdMultigrapher.getContentPane().add(title);
		
		
		
		//Button used to clear or initialize graph
		JButton btnInitializeclearAxes = new JButton("Initialize/Clear Axes");
		btnInitializeclearAxes.setBounds(196, 736, 239, 26);
		frmdMultigrapher.getContentPane().add(btnInitializeclearAxes);
		//if button is clicked a separate method is called
		btnInitializeclearAxes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGraph();
			}
		});
		//Main panel area that will display the input fields of different functions
		//Combo box allows for multiple frames to be switched and used in a single area on the screen
		JPanel equationPanel = new JPanel();
		equationPanel.setBounds(26, 773, 640, 70);
		frmdMultigrapher.getContentPane().add(equationPanel);
		equationPanel.setLayout(new CardLayout(0, 0));
		//set background to transparent
		equationPanel.setOpaque(false);
		
		//////////////////////THE FOLLOWING ARE PANELS FOR EACH TYPE OF FUNCTION/////////////////////////////////////////
		////////////////////ONLY ONE PANEL IS SHOWN AT A TIME AND THIS IS REGULATED BY A COMBO BOX//////////////////////
		/////////EACH PANEL IS VERY SIMILAR WITH THE OTHERS AND SO DOCUMENTATION IS REDUNDANT IN SOME CASES////////////////
		
		
		////////////////////////////PANEL FOR LINEAR FUNCTION INPUT INFORMATION////////////////////////////////////////
		
		//declaration of the new panel
		JPanel linearPanel = new JPanel();
		//Panel is assigned under the main panel declared above
		equationPanel.add(linearPanel, "Linear");
		//absolute layout
		linearPanel.setLayout(null);
		linearPanel.setOpaque(false);
		
		//ITEMS DECLARED UNDER LINEARPANEL
		
		//label with default formatting
		JLabel lblNewLabel = new JLabel("f(x) =");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(0, 0, 92, 60);
		linearPanel.add(lblNewLabel);
		
		//input field for the 'm' value and formatting info
		mValueLinear = new JTextField();
		mValueLinear.setHorizontalAlignment(SwingConstants.CENTER);
		mValueLinear.setFont(new Font("Tahoma", Font.PLAIN, 20));
		mValueLinear.setBounds(62, 13, 47, 35);
		linearPanel.add(mValueLinear);
		mValueLinear.setColumns(10);
		mValueLinear.setOpaque(false);
		
		//label with formatting
		JLabel lblX = new JLabel("x + ");
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX.setBounds(119, 13, 37, 35);
		linearPanel.add(lblX);
		
		//input field for the 'b' value and formatting info
		bValueLinear = new JTextField();
		bValueLinear.setHorizontalAlignment(SwingConstants.CENTER);
		bValueLinear.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bValueLinear.setColumns(10);
		bValueLinear.setBounds(166, 13, 47, 35);
		bValueLinear.setOpaque(false);
		linearPanel.add(bValueLinear);
		
		//button declaration with action listener
		btnLinear = new JButton("Graph Linear Function");
		btnLinear.setEnabled(false);
		btnLinear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//once clicked, a separate method is called (declared underneath)
				graphLinear();
			}
		});
		btnLinear.setBounds(425, 14, 215, 29);
		linearPanel.add(btnLinear);
		
		////////////////////////////////////////PANEL FOR QUADRATIC FUNCTION INPUT INFORMATION//////////////////////////////////////////////
		//new panel declaration and assignment under the main panel 'equationPanel'
		JPanel quadraticPanel = new JPanel();
		equationPanel.add(quadraticPanel, "Quadratic");
		quadraticPanel.setLayout(null);
		quadraticPanel.setOpaque(false);
		//new button with listener
		JButton btnQuadratic = new JButton("Graph Quadratic Function");
		btnQuadratic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//calls separate method
				graphQuadratic();
			}
		});
		btnQuadratic.setBounds(424, 14, 216, 29);
		quadraticPanel.add(btnQuadratic);
		
		/*
		 * label and input field declarations and default formatting code 
		 */
		
		JLabel label = new JLabel("f(x) =");
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label.setBounds(0, 0, 92, 60);
		quadraticPanel.add(label);
		
		aValueQuad = new JTextField();
		aValueQuad.setHorizontalAlignment(SwingConstants.CENTER);
		aValueQuad.setFont(new Font("Tahoma", Font.PLAIN, 20));
		aValueQuad.setColumns(10);
		aValueQuad.setBounds(61, 11, 47, 35);
		aValueQuad.setOpaque(false);
		quadraticPanel.add(aValueQuad);
		
		JLabel lblX_1 = new JLabel("x^2 + ");
		lblX_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX_1.setBounds(118, 0, 71, 60);
		quadraticPanel.add(lblX_1);
		
		bValueQuad = new JTextField();
		bValueQuad.setHorizontalAlignment(SwingConstants.CENTER);
		bValueQuad.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bValueQuad.setColumns(10);
		bValueQuad.setBounds(186, 11, 47, 35);
		bValueQuad.setOpaque(false);
		quadraticPanel.add(bValueQuad);
		
		JLabel lblX_2 = new JLabel("x + ");
		lblX_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX_2.setBounds(243, 0, 37, 60);
		quadraticPanel.add(lblX_2);
		
		cValueQuad = new JTextField();
		cValueQuad.setHorizontalAlignment(SwingConstants.CENTER);
		cValueQuad.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cValueQuad.setColumns(10);
		cValueQuad.setBounds(282, 11, 47, 35);
		cValueQuad.setOpaque(false);
		quadraticPanel.add(cValueQuad);
		
		////////////////////////////////////////PANEL FOR CUBIC FUNCTION INPUT INFORMATION//////////////////////////////////////////////
		//new panel declaration and assignment under the main panel 'equationPanel'
		JPanel cubicPanel = new JPanel();
		equationPanel.add(cubicPanel, "Cubic");
		cubicPanel.setLayout(null);
		cubicPanel.setOpaque(false);
		//button with action listener
		JButton btnGraphCubicFunction = new JButton("Graph Cubic Function");
		btnGraphCubicFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//calls separate method once clicked
				graphCubic ();
			}
		});
		btnGraphCubicFunction.setBounds(443, 14, 197, 29);
		cubicPanel.add(btnGraphCubicFunction);
		
		/*
		 * Code for declaration and formatting labels and input fields
		 */
		JLabel label_1 = new JLabel("f(x) =");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_1.setBounds(0, 0, 92, 60);
		cubicPanel.add(label_1);
		
		aValueCubic = new JTextField();
		aValueCubic.setHorizontalAlignment(SwingConstants.CENTER);
		aValueCubic.setFont(new Font("Tahoma", Font.PLAIN, 20));
		aValueCubic.setColumns(10);
		aValueCubic.setBounds(61, 11, 47, 35);
		aValueCubic.setOpaque(false);
		cubicPanel.add(aValueCubic);
		
		JLabel lblX_3 = new JLabel("x^3 + ");
		lblX_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX_3.setBounds(116, 0, 71, 60);
		cubicPanel.add(lblX_3);
		
		bValueCubic = new JTextField();
		bValueCubic.setHorizontalAlignment(SwingConstants.CENTER);
		bValueCubic.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bValueCubic.setColumns(10);
		bValueCubic.setBounds(176, 11, 47, 35);
		bValueCubic.setOpaque(false);
		cubicPanel.add(bValueCubic);
		
		JLabel lblX_4 = new JLabel("x^2 + ");
		lblX_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX_4.setBounds(225, 0, 64, 60);
		cubicPanel.add(lblX_4);
		
		cValueCubic = new JTextField();
		cValueCubic.setHorizontalAlignment(SwingConstants.CENTER);
		cValueCubic.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cValueCubic.setColumns(10);
		cValueCubic.setBounds(286, 11, 47, 35);
		cValueCubic.setOpaque(false);
		cubicPanel.add(cValueCubic);
		
		JLabel lblX_5 = new JLabel("x + ");
		lblX_5.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX_5.setBounds(340, 0, 64, 60);
		cubicPanel.add(lblX_5);
		
		dValueCubic = new JTextField();
		dValueCubic.setHorizontalAlignment(SwingConstants.CENTER);
		dValueCubic.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dValueCubic.setColumns(10);
		dValueCubic.setBounds(376, 11, 47, 35);
		dValueCubic.setOpaque(false);
		cubicPanel.add(dValueCubic);
		
		////////////////////////////////////////PANEL FOR GENERAL POLYNOMIAL FUNCTION INPUT INFORMATION//////////////////////////////////////////////
		//new panel declaration and assignment under the main panel 'equationPanel'
		JPanel polyPanel = new JPanel();
		equationPanel.add(polyPanel, "General Polynomial");
		polyPanel.setLayout(null);
		polyPanel.setOpaque(false);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setOpaque(false);
		panel_1.setBounds(0, 0, 640, 70);
		polyPanel.add(panel_1);
		
		//button with action listener
		JButton btnGraphPolynomialFunction = new JButton("Graph Polynomial Function");
		btnGraphPolynomialFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//calls separate method
				graphPoly();
			}
		});
		
		/*
		 * Default code for labels and input fields
		 */
		btnGraphPolynomialFunction.setBounds(424, 14, 216, 29);
		panel_1.add(btnGraphPolynomialFunction);
		
		pValuePoly = new JTextField();
		pValuePoly.setText("2");
		pValuePoly.setHorizontalAlignment(SwingConstants.CENTER);
		pValuePoly.setFont(new Font("Tahoma", Font.PLAIN, 10));
		pValuePoly.setColumns(10);
		pValuePoly.setBounds(302, 0, 28, 19);
		pValuePoly.setOpaque(false);
		panel_1.add(pValuePoly);
		
		JLabel label_16 = new JLabel("f(x) =");
		label_16.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_16.setBounds(0, 0, 92, 60);
		panel_1.add(label_16);
		
		aValuePoly = new JTextField();
		aValuePoly.setHorizontalAlignment(SwingConstants.CENTER);
		aValuePoly.setFont(new Font("Tahoma", Font.PLAIN, 20));
		aValuePoly.setColumns(10);
		aValuePoly.setBounds(61, 11, 47, 35);
		aValuePoly.setOpaque(false);
		panel_1.add(aValuePoly);
		
		JLabel label_17 = new JLabel("\u00B7 [");
		label_17.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_17.setBounds(118, 0, 71, 60);
		panel_1.add(label_17);
		
		kValuePoly = new JTextField();
		kValuePoly.setHorizontalAlignment(SwingConstants.CENTER);
		kValuePoly.setFont(new Font("Tahoma", Font.PLAIN, 20));
		kValuePoly.setColumns(10);
		kValuePoly.setBounds(141, 11, 47, 35);
		kValuePoly.setOpaque(false);
		panel_1.add(kValuePoly);
		
		JLabel label_18 = new JLabel("(x + ");
		label_18.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_18.setBounds(193, 0, 48, 60);
		panel_1.add(label_18);
		
		dValuePoly = new JTextField();
		dValuePoly.setHorizontalAlignment(SwingConstants.CENTER);
		dValuePoly.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dValuePoly.setColumns(10);
		dValuePoly.setBounds(239, 11, 47, 35);
		dValuePoly.setOpaque(false);
		panel_1.add(dValuePoly);
		
		JLabel label_19 = new JLabel(")]");
		label_19.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_19.setBounds(290, 0, 22, 60);
		panel_1.add(label_19);
		
		cValuePoly = new JTextField();
		cValuePoly.setHorizontalAlignment(SwingConstants.CENTER);
		cValuePoly.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cValuePoly.setColumns(10);
		cValuePoly.setBounds(362, 11, 47, 35);
		cValuePoly.setOpaque(false);
		panel_1.add(cValuePoly);
		
		JLabel label_20 = new JLabel("+");
		label_20.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_20.setBounds(340, 0, 22, 60);
		panel_1.add(label_20);
		
		////////////////////////////////////////PANEL FOR SQUARE ROOT FUNCTION INPUT INFORMATION//////////////////////////////////////////////
		////new panel declaration and assignment under the main panel 'equationPanel'
		JPanel rootPanel = new JPanel();
		equationPanel.add(rootPanel, "General Root");
		rootPanel.setLayout(null);
		rootPanel.setOpaque(false);
		//button with action listener
		JButton btnGraphSquareRoot = new JButton("Graph Root Function");
		btnGraphSquareRoot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//separate method is called once clicked
				graphRoot();
			}
		});
		/*
		 * Default code for labels and input fields
		 */
		btnGraphSquareRoot.setBounds(424, 14, 216, 29);
		rootPanel.add(btnGraphSquareRoot);
		
		pValueRoot = new JTextField();
		pValueRoot.setText("2");
		pValueRoot.setHorizontalAlignment(SwingConstants.CENTER);
		pValueRoot.setFont(new Font("Tahoma", Font.PLAIN, 10));
		pValueRoot.setColumns(10);
		pValueRoot.setBounds(128, 2, 22, 19);
		pValueRoot.setOpaque(false);
		rootPanel.add(pValueRoot);
		
		JLabel label_2 = new JLabel("f(x) =");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_2.setBounds(0, 0, 92, 60);
		rootPanel.add(label_2);
		
		aValueRoot = new JTextField();
		aValueRoot.setHorizontalAlignment(SwingConstants.CENTER);
		aValueRoot.setFont(new Font("Tahoma", Font.PLAIN, 20));
		aValueRoot.setColumns(10);
		aValueRoot.setBounds(61, 11, 47, 35);
		aValueRoot.setOpaque(false);
		rootPanel.add(aValueRoot);
		
		JLabel label_3 = new JLabel("\u00B7   \u221A[");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_3.setBounds(118, 0, 71, 60);
		rootPanel.add(label_3);
		
		kValueRoot = new JTextField();
		kValueRoot.setHorizontalAlignment(SwingConstants.CENTER);
		kValueRoot.setFont(new Font("Tahoma", Font.PLAIN, 20));
		kValueRoot.setColumns(10);
		kValueRoot.setBounds(170, 11, 47, 35);
		kValueRoot.setOpaque(false);
		rootPanel.add(kValueRoot);
		
		JLabel lblX_6 = new JLabel("(x + ");
		lblX_6.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX_6.setBounds(219, 0, 48, 60);
		rootPanel.add(lblX_6);
		
		dValueRoot = new JTextField();
		dValueRoot.setHorizontalAlignment(SwingConstants.CENTER);
		dValueRoot.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dValueRoot.setColumns(10);
		dValueRoot.setBounds(265, 11, 47, 35);
		dValueRoot.setOpaque(false);
		rootPanel.add(dValueRoot);
		
		JLabel label_5 = new JLabel(")] + ");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_5.setBounds(316, 0, 48, 60);
		rootPanel.add(label_5);
		
		cValueRoot = new JTextField();
		cValueRoot.setHorizontalAlignment(SwingConstants.CENTER);
		cValueRoot.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cValueRoot.setColumns(10);
		cValueRoot.setBounds(363, 11, 47, 35);
		cValueRoot.setOpaque(false);
		rootPanel.add(cValueRoot);
		
		////////////////////////////////////////PANEL FOR EXPONENTIAL FUNCTION INPUT INFORMATION//////////////////////////////////////////////
		//new panel declaration and assignment under the main panel 'equationPanel'
		JPanel expPanel = new JPanel();
		equationPanel.add(expPanel, "Exponential");
		expPanel.setLayout(null);
		expPanel.setOpaque(false);
		//button with action listener
		JButton btnGraphExponentialFunction = new JButton("Graph Exponential Function");
		btnGraphExponentialFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//separate method is called once clicked
				graphExponential();
			}
		});
		btnGraphExponentialFunction.setBounds(424, 14, 216, 29);
		expPanel.add(btnGraphExponentialFunction);
		/*
		 * default code for labels and input fields
		 */
		JLabel label_4 = new JLabel("f(x) =");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_4.setBounds(0, 0, 92, 60);
		expPanel.add(label_4);
		
		aValueExp = new JTextField();
		aValueExp.setHorizontalAlignment(SwingConstants.CENTER);
		aValueExp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		aValueExp.setColumns(10);
		aValueExp.setBounds(61, 11, 47, 35);
		aValueExp.setOpaque(false);
		expPanel.add(aValueExp);
		
		JLabel label_6 = new JLabel("\u00B7 (");
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_6.setBounds(118, 0, 31, 60);
		expPanel.add(label_6);
		
		bValueExp = new JTextField();
		bValueExp.setHorizontalAlignment(SwingConstants.CENTER);
		bValueExp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		bValueExp.setColumns(10);
		bValueExp.setBounds(141, 11, 47, 35);
		bValueExp.setOpaque(false);
		expPanel.add(bValueExp);
		
		JLabel label_7 = new JLabel(")");
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_7.setBounds(192, 0, 14, 60);
		expPanel.add(label_7);
		
		cValueExp = new JTextField();
		cValueExp.setHorizontalAlignment(SwingConstants.CENTER);
		cValueExp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cValueExp.setColumns(10);
		cValueExp.setBounds(249, 11, 47, 35);
		cValueExp.setOpaque(false);
		expPanel.add(cValueExp);
		
		JLabel lblX_7 = new JLabel("x");
		lblX_7.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX_7.setBounds(202, 0, 14, 23);
		expPanel.add(lblX_7);
		
		JLabel label_9 = new JLabel("+ ");
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_9.setBounds(216, 0, 21, 60);
		expPanel.add(label_9);
		
		/////////////////////////////////////PANEL FOR LOGARITHMIC FUNCTION INPUT INFORMATION ////////////////////////////////////////////////
		//new panel declaration and assignment under the main panel 'equationPanel'
		JPanel logPanel = new JPanel();
		equationPanel.add(logPanel, "Logarithmic");
		logPanel.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBounds(0, 0, 640, 70);
		logPanel.add(panel);
		logPanel.setOpaque(false);
		panel.setOpaque(false);
		
		//button with action listener
		JButton btnGraphLogarithmicFunction = new JButton("Graph Logarithmic Function");
		btnGraphLogarithmicFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//separate method
				graphLogarithmic();
			}
		});
		btnGraphLogarithmicFunction.setBounds(424, 14, 216, 29);
		panel.add(btnGraphLogarithmicFunction);
		
		/*
		 * Default code for labels an input fields
		 */
		JLabel label_8 = new JLabel("f(x) =");
		label_8.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_8.setBounds(0, 0, 92, 60);
		panel.add(label_8);
		
		aValueLog = new JTextField();
		aValueLog.setHorizontalAlignment(SwingConstants.CENTER);
		aValueLog.setFont(new Font("Tahoma", Font.PLAIN, 20));
		aValueLog.setColumns(10);
		aValueLog.setBounds(61, 11, 47, 35);
		aValueLog.setOpaque(false);
		panel.add(aValueLog);
		
		JLabel lblLog = new JLabel("log");
		lblLog.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLog.setBounds(112, 0, 32, 60);
		panel.add(lblLog);
		
		bValueLog = new JTextField();
		bValueLog.setHorizontalAlignment(SwingConstants.CENTER);
		bValueLog.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bValueLog.setColumns(10);
		bValueLog.setBounds(141, 36, 22, 24);
		bValueLog.setOpaque(false);
		panel.add(bValueLog);
		
		JLabel label_11 = new JLabel("[");
		label_11.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_11.setBounds(165, 0, 14, 60);
		panel.add(label_11);
		
		kValueLog = new JTextField();
		kValueLog.setHorizontalAlignment(SwingConstants.CENTER);
		kValueLog.setFont(new Font("Tahoma", Font.PLAIN, 20));
		kValueLog.setColumns(10);
		kValueLog.setBounds(176, 11, 47, 35);
		kValueLog.setOpaque(false);
		panel.add(kValueLog);
		
		JLabel lblX_8 = new JLabel("(x + ");
		lblX_8.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblX_8.setBounds(226, 0, 46, 60);
		panel.add(lblX_8);
		
		JLabel label_13 = new JLabel(")] +");
		label_13.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_13.setBounds(321, 0, 38, 60);
		panel.add(label_13);
		
		dValueLog = new JTextField();
		dValueLog.setHorizontalAlignment(SwingConstants.CENTER);
		dValueLog.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dValueLog.setColumns(10);
		dValueLog.setBounds(272, 11, 47, 35);
		dValueLog.setOpaque(false);
		panel.add(dValueLog);
		
		cValueLog = new JTextField();
		cValueLog.setHorizontalAlignment(SwingConstants.CENTER);
		cValueLog.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cValueLog.setColumns(10);
		cValueLog.setBounds(361, 11, 47, 35);
		cValueLog.setOpaque(false);
		panel.add(cValueLog);
		
		/////////////////////////////////////PANEL FOR TRIGONOMETRIC (SINE, COSINE, TANGENT) FUNCTION INPUT INFORMATION////////////////////////////////////////////////
		//new panel declaration and assignment under the main panel 'equationPanel'
		JPanel trigPanel = new JPanel();
		equationPanel.add(trigPanel, "Trigonometric");
		trigPanel.setLayout(null);
		trigPanel.setOpaque(false);
		
		JLabel label_10 = new JLabel("f(x) =");
		label_10.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_10.setBounds(0, 0, 92, 60);
		trigPanel.add(label_10);
		
		JButton btnGraphSineFunction = new JButton("Graph Trigonometric Function");
		btnGraphSineFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//separate method is called once button is clicked
				graphTrig();
			}
		});
		btnGraphSineFunction.setBounds(423, 14, 217, 29);
		trigPanel.add(btnGraphSineFunction);
		
		/*
		 * Default code for labels and input fields
		 */
		aValueTrig = new JTextField();
		aValueTrig.setHorizontalAlignment(SwingConstants.CENTER);
		aValueTrig.setFont(new Font("Tahoma", Font.PLAIN, 20));
		aValueTrig.setColumns(10);
		aValueTrig.setBounds(53, 11, 47, 35);
		aValueTrig.setOpaque(false);
		trigPanel.add(aValueTrig);
		
		JLabel lblSin = new JLabel("\u00B7 ");
		lblSin.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSin.setBounds(102, 0, 13, 60);
		trigPanel.add(lblSin);
		
		kValueTrig = new JTextField();
		kValueTrig.setHorizontalAlignment(SwingConstants.CENTER);
		kValueTrig.setFont(new Font("Tahoma", Font.PLAIN, 20));
		kValueTrig.setColumns(10);
		kValueTrig.setBounds(177, 11, 47, 35);
		kValueTrig.setOpaque(false);
		trigPanel.add(kValueTrig);
		
		JLabel label_14 = new JLabel("(x + ");
		label_14.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_14.setBounds(224, 0, 48, 60);
		trigPanel.add(label_14);
		
		dValueTrig = new JTextField();
		dValueTrig.setHorizontalAlignment(SwingConstants.CENTER);
		dValueTrig.setFont(new Font("Tahoma", Font.PLAIN, 20));
		dValueTrig.setColumns(10);
		dValueTrig.setBounds(267, 11, 47, 35);
		dValueTrig.setOpaque(false);
		trigPanel.add(dValueTrig);
		
		JLabel label_15 = new JLabel(")] + ");
		label_15.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_15.setBounds(318, 0, 48, 60);
		trigPanel.add(label_15);
		
		cValueTrig = new JTextField();
		cValueTrig.setHorizontalAlignment(SwingConstants.CENTER);
		cValueTrig.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cValueTrig.setColumns(10);
		cValueTrig.setBounds(358, 11, 47, 35);
		cValueTrig.setOpaque(false);
		trigPanel.add(cValueTrig);
		
		JLabel label_12 = new JLabel("[");
		label_12.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label_12.setBounds(167, 0, 13, 60);
		trigPanel.add(label_12);
		
		//string array to store different options available in combo box
		String [] trig = {"sin","cos", "tan", "csc", "sec"};
		//initialize already declared combo box with the array
		trigComboBox = new JComboBox<Object>(trig);
		trigComboBox.setFont(new Font("Tahoma", Font.PLAIN, 15));
		trigComboBox.setBounds(112, 11, 53, 33);
		trigComboBox.setMaximumRowCount(3);;
		//assigned under the panel "sinePanel"
		trigPanel.add(trigComboBox);
		
		//////////////////////////////////////CARD LAYOUT CODE (SWITCHES PANELS FROM USER INPUT)//////////////////////////////////////////////////
		//new cardlayout assigned with the layout of the main panel "equation panel" (which includes all other child panels declared above)
		CardLayout cl = (CardLayout)(equationPanel.getLayout());
		//array of strings for different types of functions
		String[] items = {"Linear", "Quadratic", "Cubic", "Exponential", "Logarithmic", "Trigonometric", "General Polynomial", "General Root"};
		comboBox = new JComboBox<Object>(items);
		//combo box is disabled at start
		comboBox.setEnabled(false);
		comboBox.setMaximumRowCount(5);
		//action listener
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
					/*
					 * shows the corresponding panel to the option selected by the user in the combo box (eg. if user selects "Linear" in combo box,
					 * linearPanel is displayed under the "equationPanel" layout area)
					 */
					cl.show(equationPanel, comboBox.getSelectedItem().toString());
			}
		});
		comboBox.setBounds(26, 736, 154, 26);
		frmdMultigrapher.getContentPane().add(comboBox);
		
		///////////////////////////////////////DEFAULT CODE FOR 8 CHECK BOXES (EACH REPRESENTING A COLOR)//////////////////////////////////////////////
		//toolbar that will contain all 8 check boxes
		JToolBar colorToolBar = new JToolBar();
		colorToolBar.setBackground(Color.LIGHT_GRAY);
		colorToolBar.setBounds(451, 736, 215, 26);
		frmdMultigrapher.getContentPane().add(colorToolBar);
		
		/*
		 * Declaration and formatting for all check boxes
		 */
		black = new JCheckBox();
		colorToolBar.add(black);
		black.setSelected(true);
		black.setHorizontalAlignment(SwingConstants.CENTER);
		black.setBackground(Color.BLACK);
		black.setForeground(Color.BLACK);
		
		red = new JCheckBox();
		colorToolBar.add(red);
		red.setHorizontalAlignment(SwingConstants.CENTER);
		red.setForeground(Color.RED);
		red.setBackground(new Color(255, 0, 0));
		
		orange = new JCheckBox();
		colorToolBar.add(orange);
		orange.setHorizontalAlignment(SwingConstants.CENTER);
		orange.setForeground(Color.ORANGE);
		orange.setBackground(Color.ORANGE);
		
		yellow = new JCheckBox();
		colorToolBar.add(yellow);
		yellow.setHorizontalAlignment(SwingConstants.CENTER);
		yellow.setForeground(Color.YELLOW);
		yellow.setBackground(Color.YELLOW);
		
		blue = new JCheckBox();
		colorToolBar.add(blue);
		blue.setHorizontalAlignment(SwingConstants.CENTER);
		blue.setForeground(Color.BLUE);
		blue.setBackground(Color.BLUE);
		
		purple = new JCheckBox();
		colorToolBar.add(purple);
		purple.setHorizontalAlignment(SwingConstants.CENTER);
		purple.setForeground(new Color(148, 0, 211));
		purple.setBackground(new Color(148, 0, 211));
		
		green = new JCheckBox();
		colorToolBar.add(green);
		green.setHorizontalAlignment(SwingConstants.CENTER);
		green.setForeground(new Color(154, 205, 50));
		green.setBackground(new Color(154, 205, 50));
		
		cyan = new JCheckBox();
		colorToolBar.add(cyan);
		cyan.setHorizontalAlignment(SwingConstants.CENTER);
		cyan.setForeground(Color.CYAN);
		cyan.setBackground(Color.CYAN);
		
		//Initializes image variable with image extracted from source folder
		Image img = new ImageIcon(this.getClass().getResource("/background.jpg")).getImage();
		//creates JLabel for displaying background image
		JLabel background = new JLabel("");
		background.setBounds(0, 0, 694, 872);
		//sets the background to the image declared above
		background.setIcon(new ImageIcon(img));
		frmdMultigrapher.getContentPane().add(background);
		
		cyan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colorChange(cyan);
			}
		});
		green.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colorChange(green);
			}
		});
		purple.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colorChange(purple);
			}
		});
		blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colorChange(blue);
			}
		});
		yellow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colorChange(yellow);
			}
		});
		orange.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colorChange(orange);
			}
		});
		red.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				colorChange(red);
			}
		});
		black.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//once check box is clicked, separate method is called with the specific check box id being passed to the method
				colorChange(black);
			}
		});
		
		
	}
	/**************************************************************************************************************
	//////////////////////////////////END OF DESIGN ITEMS AND CONTAINERS///////////////////////////////////////////
	**************************************************************************************************************/

	
	
	
	
	
	/*******************************************************
	// SEPARATE METHODS BELOW USED TO SET UP THE SOFTWARE
	//*******************************************************/
	
	
	
	
	
	
	
	/*************************************************************************
	//method for clearing graph space or for the initialization of the axes
	/////////////////////////////////////////////////////////////////////////*/
	public void newGraph () {
		//uses the graphics library with 'graph' as the canvas
		Graphics g = graph.getGraphics();
		//sets color to white and fills in the rectangular canvas
		g.setColor(Color.white);
		g.fillRect(0, 0, 640, 640);
		//sets font 
		g.setFont(new Font("Calibri", Font.BOLD, 12)); 
		//changes color to black for axes
		g.setColor(Color.black);
		//draws x-axis
		g.drawLine(0, 320, 640, 320);
		//draws y-axis
		g.drawLine(320, 0, 320, 640);
		//draws '0' at the origin
		g.drawString("0", 312, 332);
		//for loop will run through the x-pixels of the canvas by 32's
		for (int i = 0; i < 641; i+=32){
			
			//forces the color as gray
			g.setColor(Color.LIGHT_GRAY);
			//for loop draws grid lines (if statement avoids drawing over the x and y axis with gray lines) 
			if (i != 320){
				g.drawLine(i, 0, i, 640);
				g.drawLine(0, i, 640, i);
			}
			//draws the two grid lines at the rightmost side and bottom
			g.drawLine(639, 0, 639, 640);
			g.drawLine(0, 639, 640, 639);
			
			//color forced to black for drawing labels
			g.setColor(Color.BLACK);
			//if statement skips '0' since it is already drawn
			if (i != 320){
				//pixels from 0 to 640 is converted to a -10 to 10 scale and is outputed as labels every 32 pixels along the x-axis
				g.drawString(Integer.toString((i-320)/32), i-5, 336);
			}
			
			//same process for drawing labels on y-axis, however different distances are used to optimize aesthetic effect
			if (i < 320){
				g.drawString(Integer.toString((i-320)/(-32)), 308, i+5);
			} else if (i > 320) {
				g.drawString(Integer.toString((i-320)/(-32)), 304, i+5);
			}
		}
		//enables other buttons and combo boxes if they are disabled
		if (!comboBox.isEnabled()){
			comboBox.setEnabled(true);
			btnLinear.setEnabled(true);
		}
	}
	
	
	/**************************************************************************
	//method for changing the main color with which functions are drawn
	///////////////////////////////////////////////////////////////////*/
	//parameter contains a JCheckBox variable which will be the check box that was clicked by the user and was thus passed to this method in the above code
	public void colorChange (JCheckBox j) {
		//deselects all buttons
		black.setSelected(false);
		blue.setSelected(false);
		red.setSelected(false);
		purple.setSelected(false);
		orange.setSelected(false);
		yellow.setSelected(false);
		green.setSelected(false);
		cyan.setSelected(false);
		//reselects whichever button was clicked by the user
		j.setSelected(true);
		//the main color is stored as the color of the button itself (1 of 8)
		mainColor = j.getBackground();
	}
	
	
	
	
	
	
	
	/**
	//////////////////////////////////   FOLLOWING ARE METHODS USED TO GRAPH EACH TYPE OF FUNCTION    ////////////////////////////////////////////////////////
	/////////////////////    EACH METHOD IS VERY SIMILAR IN DESIGN AND IN THE CODE ITSELF SINCE THEY USE THE SAME PROCESS BUT USE DIFFERENT VARIABLE TYPES    
	///////////////////////////////////////////    THUS DOCUMENTATION IS HIGHLY REDUNDANT IN SOME CASES    ///////////////////////////////////////////////////*/
	
	
	
	
	
	
	
	/*********************************
	//method used to graph linear lines
	//////////////////////////////////*/
	public void graphLinear () {
		//double variables that will hold the values of the user input (where m is the slope and b is the y-intercept)
		double m = 0, b = 0;
		//array for storing x-coordinates
		int[] xPoints = new int[641];
		//array for storing y-coordinates
		double[] yPoints = new double[641];
		
		//only accepts input if the input field actually contains a value
		if (!mValueLinear.getText().isEmpty()) {
			m = Double.parseDouble(mValueLinear.getText());
		}
		if (!bValueLinear.getText().isEmpty()){
			b = Double.parseDouble(bValueLinear.getText());
		}
		
		//for loop that stores coordinates(in the pixel system) to the x and y arrays
		for (int i = 0; i < 641; i++){
			//pixels from 0 to 640 are stored in the x-coordinate array
			xPoints[i] = i;
			
			/*
			 * algorithm subtracts half of the frame width and divides by 32 to attain a x-value between -10 and 10
			 * this value is then used to find the corresponding y-value between -10 and 10 by applying the necessary calculations
			 * then this y-value is converted back to pixel coordinate form and is stored in the y-points array
			 */
			yPoints[i] = (((double)(i-320)/32)*m + b)*(-32) + 320;
		}
		//declares instance of graphics class for the graphing plot
		Graphics g = graph.getGraphics();
		//sets the drawing color to mainColor variable (which is modified by the color change method)
		g.setColor(mainColor);
		//for loop runs through each value in the above arrays
		for (int i = 0; i < 640; i++){
				// draws a line between consecutive coordinates, subsequently making a smooth curve
				g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
		}
	}
	
	/*********************************
	//method used to graph quadratic lines
	//////////////////////////////////*/
	public void graphQuadratic () {
		//double variables that will hold the values of the user input
		double a = 0, b = 0, c = 0;
		//array for storing x-coords
		int[] xPoints = new int[641];
		//array for storing y-coords
		double[] yPoints = new double[641];
		
		//only accepts input if the input field actually contains a value
		if (!aValueQuad.getText().isEmpty()) {
			a = Double.parseDouble(aValueQuad.getText());
		}
		if (!bValueQuad.getText().isEmpty()){
			b = Double.parseDouble(bValueQuad.getText());
		}
		if (!cValueQuad.getText().isEmpty()){
			c = Double.parseDouble(cValueQuad.getText());
		}
		
		//for loop that stores coordinates(in the pixel system) to the x and y arrays
		for (int i = 0; i < 641; i++){
			//pixels from 0 to 640 are stored in the x-coordinate array
			xPoints[i] = i;
			/*
			 * algorithm subtracts half of the frame width and divides by 32 to attain a x-value between -10 and 10
			 * this value is then used to find the corresponding y-value between -10 and 10 by applying the necessary calculations
			 * then this y-value is converted back to pixel coordinate form and is stored in the y-points array
			 */
			yPoints[i] = (a * Math.pow(((double)(i-320)/32), 2) + b * ((double)(i-320)/32) + c) * (-32) + 320;
		}
		
		//declares instance of graphics class for the graphing plot
		Graphics g = graph.getGraphics();
		//sets the drawing color to mainColor variable (which is modified by the color change method)
		g.setColor(mainColor);
		//for loop runs through each value in the above arrays
		for (int i = 0; i < 640; i++){
			// draws a line between consecutive coordinates, subsequently making a smooth curve
				g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
		}
	}
	
	/*********************************
	//method used to graph cubic lines
	//////////////////////////////////*/
	public void graphCubic () {
		//double variables that will hold the values of the user input
		double a = 0, b = 0, c = 0, d = 0;
		//array for storing x-coords
		int[] xPoints = new int[641];
		//array for storing y-coords
		double[] yPoints = new double[641];
		
		//only accepts input if the input field actually contains a value
		if (!aValueCubic.getText().isEmpty()) {
			a = Double.parseDouble(aValueCubic.getText());
		}
		if (!bValueCubic.getText().isEmpty()){
			b = Double.parseDouble(bValueCubic.getText());
		}
		if (!cValueCubic.getText().isEmpty()){
			c = Double.parseDouble(cValueCubic.getText());
		}
		if (!dValueCubic.getText().isEmpty()){
			d = Double.parseDouble(dValueCubic.getText());
		}
		
		//for loop that stores coordinates(in the pixel system) to the x and y arrays
		for (int i = 0; i < 641; i++){
			//pixels from 0 to 640 are stored in the x-coordinate array
			xPoints[i] = i;
			
			/*
			 * algorithm subtracts half of the frame width and divides by 32 to attain a x-value between -10 and 10
			 * this value is then used to find the corresponding y-value between -10 and 10 by applying the necessary calculations for a cubic function
			 * then this y-value is converted back to pixel coordinate form and is stored in the y-points array
			 */
			yPoints[i] = (a * Math.pow(((double)(i-320)/32), 3) + b * Math.pow(((double)(i-320)/32), 2) + c * ((double)(i-320)/32) + d) * (-32) + 320;
		}
		
		//declares instance of graphics class for the graphing plot
		Graphics g = graph.getGraphics();
		//sets the drawing color to mainColor variable (which is modified by the color change method)
		g.setColor(mainColor);
		
		//for loop runs through each value in the above arrays
		for (int i = 0; i < 640; i++){
			// draws a line between consecutive coordinates, subsequently making a smooth curve
			g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
		}
	}
	
	/*********************************
	//method used to graph polynomial lines
	//////////////////////////////////*/
	public void graphPoly () {
		//double variables that will hold the values of the user input
		double a = 0, k = 0, d = 0, c = 0, p = 2;
		//array for storing x-coords
		int[] xPoints = new int[641];
		//array for storing y-coords
		double[] yPoints = new double[641];
		
		//only accepts input if the input field actually contains a value
		if (!aValuePoly.getText().isEmpty()) {
			a = Double.parseDouble(aValuePoly.getText());
		}
		if (!kValuePoly.getText().isEmpty()){
			k = Double.parseDouble(kValuePoly.getText());
		}
		if (!dValuePoly.getText().isEmpty()){
			d = Double.parseDouble(dValuePoly.getText());
		}
		if (!cValuePoly.getText().isEmpty()){
			c = Double.parseDouble(cValuePoly.getText());
		}
		if (!pValuePoly.getText().isEmpty()){
			p = Double.parseDouble(pValuePoly.getText());
		}
		
		//for loop that stores coordinates(in the pixel system) to the x and y arrays
		for (int i = 0; i < 641; i++){
			//pixels from 0 to 640 are stored in the x-coordinate array
			xPoints[i] = i;
			
			/*
			 * algorithm subtracts half of the frame width and divides by 32 to attain a x-value between -10 and 10
			 * this value is then used to find the corresponding y-value between -10 and 10 by applying the necessary calculations
			 * then this y-value is converted back to pixel coordinate form and is stored in the y-points array
			 */
			yPoints[i] = (a * Math.pow(k*(((double)(i-320)/32) + d), p) + c) * (-32) + 320;
		}
		
		//declares instance of graphics class for the graphing plot
		Graphics g = graph.getGraphics();
		//sets the drawing color to mainColor variable (which is modified by the color change method)
		g.setColor(mainColor);
			
		//for loop runs through each value in the above arrays
		for (int i = 0; i < 640; i++){
			// draws a line between consecutive coordinates, subsequently making a smooth curve
			g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
		}
	}
	
	/*********************************
	//method used to graph root lines
	//////////////////////////////////*/
	public void graphRoot () {
		//double variables that will hold the values of the user input
		double a = 0, k = 0, d = 0, c = 0, p = 2;
		//array for storing x-coords
		int[] xPoints = new int[641];
		//array for storing y-coords
		double[] yPoints = new double[641];
		
		//only accepts input if the input field actually contains a value
		if (!aValueRoot.getText().isEmpty()) {
			a = Double.parseDouble(aValueRoot.getText());
		}
		if (!kValueRoot.getText().isEmpty()){
			k = Double.parseDouble(kValueRoot.getText());
		}
		if (!dValueRoot.getText().isEmpty()){
			d = Double.parseDouble(dValueRoot.getText());
		}
		if (!cValueRoot.getText().isEmpty()){
			c = Double.parseDouble(cValueRoot.getText());
		}
		if (!pValueRoot.getText().isEmpty()){
			p = Double.parseDouble(pValueRoot.getText());
		}
		
		//for loop that stores coordinates(in the pixel system) to the x and y arrays
		for (int i = 0; i < 641; i++){
			//pixels from 0 to 640 are stored in the x-coordinate array
			xPoints[i] = i;
			
			/*
			 * algorithm subtracts half of the frame width and divides by 32 to attain a x-value between -10 and 10
			 * this value is then used to find the corresponding y-value between -10 and 10 by applying the necessary calculations
			 * then this y-value is converted back to pixel coordinate form and is stored in the y-points array
			 */
			yPoints[i] = (a * Math.pow(k*(((double)(i-320)/32) + d), (double)(1/p)) + c) * (-32) + 320;
		}
		
		//declares instance of graphics class for the graphing plot
		Graphics g = graph.getGraphics();
		//sets the drawing color to mainColor variable (which is modified by the color change method)
		g.setColor(mainColor);
				
		//for loop runs through each value in the above arrays
		for (int i = 0; i < 640; i++){
			//does not draw lines in the domain in which the root function does not exist
			if ((k*(((double)(i-319)/32) + d)) >= 0 && yPoints[i] >= 0 && (k*(((double)(i-320)/32) + d)) > 0) {
				// draws a line between consecutive coordinates, subsequently making a smooth curve
				g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
			}
		}
	}
	
	/*********************************
	//method used to graph exponential lines
	//////////////////////////////////*/
	public void graphExponential () {
		//double variables that will hold the values of the user input
		double a = 0, b = 0, c = 0;
		//array for storing x-coords
		int[] xPoints = new int[641];
		//array for storing y-coords
		double[] yPoints = new double[641];
		
		//only accepts input if the input field actually contains a value
		if (!aValueExp.getText().isEmpty()) {
			a = Double.parseDouble(aValueExp.getText());
		}
		if (!bValueExp.getText().isEmpty()){
			b = Double.parseDouble(bValueExp.getText());
		}
		if (!cValueExp.getText().isEmpty()){
			c = Double.parseDouble(cValueExp.getText());
		}
		
		//for loop that stores coordinates(in the pixel system) to the x and y arrays
		for (int i = 0; i < 641; i++){
			//pixels from 0 to 640 are stored in the x-coordinate array
			xPoints[i] = i;
			
			/*
			 * algorithm subtracts half of the frame width and divides by 32 to attain a x-value between -10 and 10
			 * this value is then used to find the corresponding y-value between -10 and 10 by applying the necessary calculations for an exponential function
			 * then this y-value is converted back to pixel coordinate form and is stored in the y-points array
			 */
			yPoints[i] = (a * Math.pow(b, ((double)(i-320)/32)) + c) * (-32) + 320;
		}
		
		//declares instance of graphics class for the graphing plot
		Graphics g = graph.getGraphics();
		//sets the drawing color to mainColor variable (which is modified by the color change method)
		g.setColor(mainColor);
				
		//for loop runs through each value in the above arrays
		for (int i = 0; i < 640; i++){
			//only graphs y-points that are in the output area
			if (yPoints[i] >= 0) {
				// draws a line between consecutive coordinates, subsequently making a smooth curve
				g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
			}
		}
	}
	
	/*********************************
	//method used to graph logarithmic lines
	//////////////////////////////////*/
	public void graphLogarithmic () {
		//double variables that will hold the values of the user input
		double a = 0, b = 0, c = 0, k = 0, d = 0;
		//array for storing x-coords
		int[] xPoints = new int[641];
		//array for storing y-coords
		double[] yPoints = new double[641];
		
		//only accepts input if the input field actually contains a value
		if (!aValueLog.getText().isEmpty()) {
			a = Double.parseDouble(aValueLog.getText());
		}
		if (!bValueLog.getText().isEmpty()){
			b = Double.parseDouble(bValueLog.getText());
		}
		if (!kValueLog.getText().isEmpty()){
			k = Double.parseDouble(kValueLog.getText());
		}
		if (!dValueLog.getText().isEmpty()){
			d = Double.parseDouble(dValueLog.getText());
		}
		if (!cValueLog.getText().isEmpty()){
			c = Double.parseDouble(cValueLog.getText());
		}
		
		//for loop that stores coordinates(in the pixel system) to the x and y arrays
		for (int i = 0; i < 641; i++){
			//pixels from 0 to 640 are stored in the x-coordinate array
			xPoints[i] = i;
			
			/*
			 * algorithm subtracts half of the frame width and divides by 32 to attain a x-value between -10 and 10
			 * this value is then used to find the corresponding y-value between -10 and 10 by applying the necessary calculations for a logarithmic function
			 * then this y-value is converted back to pixel coordinate form and is stored in the y-points array
			 */
			yPoints[i] = (a * Math.log( k*(((double)(i-320)/32) + d) ) / Math.log(b) + c) * (-32) + 320;
		}
		
		//declares instance of graphics class for the graphing plot
		Graphics g = graph.getGraphics();
		//sets the drawing color to mainColor variable (which is modified by the color change method)
		g.setColor(mainColor);
				
		//for loop runs through each value in the above arrays
		for (int i = 0; i < 640; i++){
			
			/*
			 * 
			 * if statements run through each possibility for the location of the asymptote
			 * then a line is drawn to represent the function approaching the asymptote
			 */
			
			//if the argument inside the "log" operator is positive for consecutive x-values, then a line is drawn between the two coordinates
			if ((k*(((double)(i-319)/32) + d)) > 0 && (k*(((double)(i-320)/32) + d)) > 0) {
				
				g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
			
			//if the argument equals zero and the log function is not reflected in y-axis then...
			} else if (k > 0 && (k*(((double)(i-320)/32) + d)) == 0){
				if (a>0){
					if (b > 1){
						g.drawLine(xPoints[i+1], (int)Math.round(yPoints[i+1]), xPoints[i+1], 640);
					} else if (b < 1){
						g.drawLine(xPoints[i+1], (int)Math.round(yPoints[i+1]), xPoints[i+1], 0);
					}
				} else if (a<0){
					if (b > 1){
						g.drawLine(xPoints[i+1], (int)Math.round(yPoints[i+1]), xPoints[i+1], 0);
					} else if (b < 1){
						g.drawLine(xPoints[i+1], (int)Math.round(yPoints[i+1]), xPoints[i+1], 640);
					}
				}
			//if the argument equals zero and the log function is reflected in y-axis then...
			}else if (k < 0 && (k*(((double)(i-320)/32) + d)) == 0){
				if (a>0){
					if (b > 1){
						g.drawLine(xPoints[i-1], (int)Math.round(yPoints[i-1]), xPoints[i-1], 640);
					} else if (b < 1){
						g.drawLine(xPoints[i-1], (int)Math.round(yPoints[i-1]), xPoints[i-1], 0);
					}
				} else if (a<0){
					if (b > 1){
						g.drawLine(xPoints[i-1], (int)Math.round(yPoints[i-1]), xPoints[i-1], 0);
					} else if (b < 1){
						g.drawLine(xPoints[i-1], (int)Math.round(yPoints[i-1]), xPoints[i-1], 640);
					}
				}
			}
		}
	}
	
	/*********************************
	//method used to graph trigonometric lines
	//////////////////////////////////*/
	public void graphTrig () {
		//double variables that will hold the values of the user input
		double a = 0, k = 0, d = 0, c = 0;
		//array for storing x-coords
		int[] xPoints = new int[641];
		//array for storing y-coords
		double[] yPoints = new double[641];
		
		//only accepts input if the input field actually contains a value
		if (!aValueTrig.getText().isEmpty()) {
			a = Double.parseDouble(aValueTrig.getText());
		}
		if (!kValueTrig.getText().isEmpty()){
			k = Double.parseDouble(kValueTrig.getText());
		}
		if (!dValueTrig.getText().isEmpty()){
			d = Double.parseDouble(dValueTrig.getText());
		}
		if (!cValueTrig.getText().isEmpty()){
			c = Double.parseDouble(cValueTrig.getText());
		}
		
		//for loop that stores coordinates(in the pixel system) to the x and y arrays
		for (int i = 0; i < 641; i++){
			//pixels from 0 to 640 are stored in the x-coordinate array
			xPoints[i] = i;
			
			/*
			 * y-value is calculated using the same algorithm above but is dependent on the trig operator selected
			 * options are "sin", "cos", "tan", "sec", and "csc". 
			 */
			if (trigComboBox.getSelectedItem().toString() == "sin"){
				yPoints[i] = (a * Math.sin(k*(((double)(i-320)/32) + d)) + c) * (-32) + 320;
			} else if (trigComboBox.getSelectedItem().toString() == "cos"){
				yPoints[i] = (a * Math.cos(k*(((double)(i-320)/32) + d)) + c) * (-32) + 320;
			} else if (trigComboBox.getSelectedItem().toString() == "tan"){
					yPoints[i] = (a * Math.tan(k*(((double)(i-320)/32) + d)) + c) * (-32) + 320;
			} else if (trigComboBox.getSelectedItem().toString() == "csc"){
					yPoints[i] = (a * (1 / Math.sin(k*(((double)(i-320)/32) + d))) + c) * (-32) + 320;
			} else if (trigComboBox.getSelectedItem().toString() == "sec"){
					yPoints[i] = (a * (1 / Math.cos(k*(((double)(i-320)/32) + d))) + c) * (-32) + 320;
			}
		}
		
		//declares instance of graphics class for the graphing plot
		Graphics g = graph.getGraphics();
		//sets the drawing color to mainColor variable (which is modified by the color change method)
		g.setColor(mainColor);
				
		//for loop runs through each value in the above arrays
		for (int i = 0; i < 640; i++){
			//if the function is either "sin" or "cos" then the same algorithm for drawing lines is used
			if (trigComboBox.getSelectedItem().toString() == "sin" || trigComboBox.getSelectedItem().toString() == "cos"){
				g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
			//if the function is "tan", "sec", or "csc", then only consecutive values that have the same sign are graphed (this prevents lines from being drawn between values split by an asymptote
			} else {
				if ((double)((yPoints[i]-320)/32) * (double)((yPoints[i+1]-320)/32) > 0) {
					g.drawLine(xPoints[i], (int)Math.round(yPoints[i]), xPoints[i+1], (int)Math.round(yPoints[i+1]));
				}
			}
		}
	}
}
