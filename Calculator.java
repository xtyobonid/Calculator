

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

class Calculator extends JFrame implements ActionListener
{
	public static final int FRAME_HEIGHT = 400;
	public static final int FRAME_WIDTH = 500;
	
	private JTextField field;
	private JPanel panel;
	private ArrayList<JButton> buttons;
	private String opo;
	private String opt;
	private String opr;
	private boolean isResult;
	
	public Calculator()
	{
		setLayout(new BorderLayout(5,5));
		setBackground(Color.GRAY);
		
		field = new JTextField();
		field.setEditable(false);
		add(field, BorderLayout.NORTH);
		
		buttons = new ArrayList<JButton>();
		String buttonLabels = "789/S456*C123-c0.%+=";
		for (int i = 0; i < buttonLabels.length(); i++) {
			String preLab = buttonLabels.substring(i, i + 1);
			switch (preLab.charAt(0)) {
				case 'S':
					buttons.add(new JButton("Sqrt"));
					break;
				case 'C':
					buttons.add(new JButton("CE"));
					break;
				case 'c':
					buttons.add(new JButton("Clr"));
					break;
				default:
					buttons.add(new JButton(preLab));
					break;
			}
		}
		
		
		panel = new JPanel(new GridLayout(4,5,5,5));
		for (int i = 0; i < buttons.size(); i++) {
			panel.add(buttons.get(i));
		}
		for (JButton b: buttons) {
			b.setFont(new Font("Arial", Font.BOLD, 20));
			b.addActionListener(this);	
		}
		
		add(panel, BorderLayout.CENTER);
		
		setSize(FRAME_WIDTH,FRAME_HEIGHT); // modify as needed
		setTitle("CALCULATOR");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		opo = opt = opr = "";
		isResult = true;
	}
	
	public boolean isOperator(char c) {
		return (c == '+' || c == '-' || c == '*' || c == '/');
	}
	
	public void actionPerformed(ActionEvent e)
	{	
		String tgn = ((JButton) e.getSource()).getText();
		if (tgn.charAt(0) >= '0' && tgn.charAt(0) <= '9' || tgn.charAt(0) == '.' || tgn.charAt(0) == '%' || tgn.equals("Sqrt")) {
			if (isResult) {
				if (tgn.charAt(0) != '%' && !tgn.equals("Sqrt")) {
					field.setText("" + tgn.charAt(0));
					opo = "";
					isResult = false;
				}
			} else if ((tgn.charAt(0) != '.' || field.getText().indexOf(".") == -1) && (field.getText().indexOf("%") == -1) && (field.getText().indexOf("√") == -1)) {
				if (tgn.equals("Sqrt"))
					field.setText("√" + field.getText());
				else
					field.setText(field.getText() + tgn.charAt(0));
			}
		} else if (tgn.equals("CE")) {
			field.setText("");
			isResult = false;
		} else if (tgn.equals("Clr")) {
			field.setText("");
			opo = opt = "";
			isResult = false;
		} else if (isOperator(tgn.charAt(0))) {
			if (isResult)
				field.setText("");
			if (field.getText().equals("") && tgn.charAt(0) == '-') {
				field.setText("-");
			} else if (opo.equals("") && opt.equals("")) {
				if (field.getText().equals(""))
					opo = "0";
				else
					opo = field.getText();
				opr = tgn;
				field.setText("");
			} else if (!opo.equals("") && opt.equals("")) {
				if (field.getText().equals("")) {
					opr = tgn;
				} else {
					opt = field.getText();
					calculate();
					opr = tgn;
				}
			}
		} else if (tgn.equals("=")) {
			if (opo == "") {
				if (field.getText().contains("%")) {
					opo = "" + (Double.parseDouble(field.getText().substring(0, field.getText().indexOf("%")))/100);
				} else if (field.getText().contains("√")) {
					opo = "" + Math.sqrt(Double.parseDouble(field.getText().substring(1)));
				} else if (field.getText().equals("")) {
					opo = "0";
				} else {
					opo = field.getText();
				}
				field.setText(opo);
			} else if (opr == "") {
				field.setText(opo);
			} else if (opt == "") {
				if (field.getText().equals(""))
					opt = opo;
				else
					opt = field.getText();
				calculate();
			} else {
				calculate();
			}
			isResult = true;
		}
	}
	
	public void calculate() {// zeroes, triple minus
		//Should clear opr and opt
		//should store result in opo
		if (opo.contains("√"))
			opo = "" + Math.sqrt(Double.parseDouble(opo.substring(1)));
		if (opt.contains("√"))
			opt = "" + Math.sqrt(Double.parseDouble(opt.substring(1)));
		if (opo.contains("%"))
			opo = "" + (Double.parseDouble(opo.substring(0, opo.indexOf("%")))/100);
		if (opt.contains("%")) {
			opt = "" + (Double.parseDouble(opo) * (Double.parseDouble(opt.substring(0, opt.indexOf("%")))/100));
		}
		System.out.println(opo + " " + opr + " " + opt);
		switch (opr.charAt(0)) {
			case '+':
				opo = "" + (Double.parseDouble(opo) + Double.parseDouble(opt));
				break;
			case '-':
				opo = "" + (Double.parseDouble(opo) - Double.parseDouble(opt));
				break;
			case '*':
				opo = "" + (Double.parseDouble(opo) * Double.parseDouble(opt));
				break;
			case '/':
				opo = "" + (Double.parseDouble(opo) / Double.parseDouble(opt));
				break;
		}
		if (Double.parseDouble(opo) == 0)
			opo = "0";
		opr = "";
		opt = "";
		field.setText(opo);
	}
}