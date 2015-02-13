/**
 * LiveTestUtil.java
 * @author Trushank
 * Date Dec 27, 2012
 * Version 1.0
 * 
 */
package dand.trushank;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Trushank
 * 
 */

public class LiveTestUtil extends JFrame {

    JLabel sdk_path;
    JTextField sdk_path_txt;
    JButton file_picker;
    JComboBox<String> device_number_txt;
    JLabel device_number;
    JButton Install;
    JButton push;
    JLabel tcpdum;
    JTextField install_txt;
    JTextField push_txt;
    JButton start;
    JButton tcpPull;
    JButton refresh;
    JButton shell;

    public LiveTestUtil() {
	getContentPane().setLayout(null);

	setupGUI();
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    void setupGUI() {
	sdk_path = new JLabel();
	sdk_path.setLocation(16, 11);
	sdk_path.setSize(218, 25);
	sdk_path.setText("Select SDK Path");
	getContentPane().add(sdk_path);

	sdk_path_txt = new JTextField();
	sdk_path_txt.setLocation(16, 45);
	sdk_path_txt.setSize(227, 28);
	sdk_path_txt.setText("");
	sdk_path_txt.setColumns(10);
	getContentPane().add(sdk_path_txt);

	file_picker = new JButton();
	file_picker.setLocation(255, 46);
	file_picker.setSize(111, 29);
	file_picker.setText("Browse");
	getContentPane().add(file_picker);

	device_number_txt = new JComboBox<String>();

	device_number_txt.setLocation(17, 130);
	device_number_txt.setSize(113, 25);

	getContentPane().add(device_number_txt);

	device_number = new JLabel();
	device_number.setLocation(16, 92);
	device_number.setSize(112, 27);
	device_number.setText("Device Number:");
	getContentPane().add(device_number);

	refresh = new JButton();
	refresh.setLocation(141, 131);
	refresh.setSize(84, 25);
	refresh.setText("Refresh");
	getContentPane().add(refresh);

	Install = new JButton();
	Install.setLocation(231, 173);
	Install.setSize(94, 25);
	Install.setText("Install");
	getContentPane().add(Install);

	shell = new JButton();
	shell.setLocation(280, 318);
	shell.setSize(100, 50);
	shell.setText("Shell");
	getContentPane().add(shell);
	push = new JButton();
	push.setLocation(233, 220);
	push.setSize(93, 25);
	push.setText("Push");
	getContentPane().add(push);

	tcpdum = new JLabel();
	tcpdum.setLocation(19, 268);
	tcpdum.setSize(99, 25);
	tcpdum.setText("TCP Dump");
	getContentPane().add(tcpdum);

	install_txt = new JTextField();
	install_txt.setLocation(18, 173);
	install_txt.setSize(180, 25);
	install_txt.setText("");
	install_txt.setColumns(10);
	getContentPane().add(install_txt);

	push_txt = new JTextField();
	push_txt.setLocation(18, 222);
	push_txt.setSize(181, 25);
	push_txt.setText("");
	push_txt.setColumns(10);
	getContentPane().add(push_txt);

	start = new JButton();
	start.setLocation(19, 319);
	start.setSize(100, 50);
	start.setText("Start");
	getContentPane().add(start);

	tcpPull = new JButton();
	tcpPull.setLocation(157, 319);
	tcpPull.setSize(100, 50);
	tcpPull.setText("Pull Dump");
	getContentPane().add(tcpPull);

	setTitle("RIT Live Test Utility");
	setSize(400, 473);
	setVisible(true);
	setResizable(true);

	try {
	    sdk_path_txt.setText((new BufferedReader(new FileReader(
		    "sdkpath.txt")).readLine()));
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	}
	refresh.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		checkDevice();

	    }
	});

	tcpPull.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		String command = ("cmd /C start /wait "
			+ sdk_path_txt.getText() + "\\platform-tools\\adb -s "
			+ device_number_txt.getSelectedItem().toString()
			+ " pull /data/local/out.pcap "
			+ device_number_txt.getSelectedItem().toString() + ".pcap");
		try {
		    Process p = Runtime.getRuntime().exec(command);
		  
			    JOptionPane.showMessageDialog(new JFrame(),
					"Please check working directory for "+device_number_txt.getSelectedItem().toString()+".pcap");
			
		} catch (IOException e1) {
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

	    }
	});
	file_picker.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnVal = fc.showOpenDialog(LiveTestUtil.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    if (new File(file.getAbsolutePath()
			    + "\\platform-tools\\adb.exe").exists()) {
			sdk_path_txt.setText(file.getAbsolutePath());
			try {
			    PrintWriter p = new PrintWriter(new File(
				    "sdkpath.txt"));
			    p.println(file.getAbsolutePath());
			    p.flush();
			    p.close();
			} catch (FileNotFoundException e) {

			    e.printStackTrace();
			}
		    } else
			JOptionPane.showMessageDialog(new JFrame(),
				"SDK not found");
		}

	    }
	});
	start.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    String command = ("cmd /C start /wait "
			    + sdk_path_txt.getText()
			    + "\\platform-tools\\adb -s "
			    + device_number_txt.getSelectedItem().toString() + " push tcpdump /data/local/");

		    // Process p = Runtime.getRuntime().exec(command);

		    // command=("cmd /C start /wait "+sdk_path_txt.getText()+"\\platform-tools\\adb -s "+device_number_txt.getSelectedItem().toString()+" shell (cd /data/local/ ; chmod 777 tcpdump ; tcpdump -s 0 -v -w out.pcap) ");
		    command = ("cmd /C start /wait " + sdk_path_txt.getText()
			    + "\\platform-tools\\adb -s "
			    + device_number_txt.getSelectedItem().toString() + " shell (cd /data/local/ ; tcpdump -s 0 -v -w out.pcap) ");
		    Runtime.getRuntime().exec(command);

		} catch (Exception e1) {
		    JOptionPane.showMessageDialog(new JFrame(),
			    "TCPDump not found");
		    e1.printStackTrace();
		}

	    }
	});
	Install.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(LiveTestUtil.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    if (file.getName().contains(".apk")
			    || file.getName().contains(".APK")) {
			install_txt.setText(file.getAbsolutePath());
			Process p;
			try {
			    String command = ("cmd /C start /wait "
				    + sdk_path_txt.getText()
				    + "\\platform-tools\\adb -s "
				    + device_number_txt.getSelectedItem()
					    .toString() + " install \""
				    + install_txt.getText() + "\"");
			    p = Runtime.getRuntime().exec(command);
			    System.out.println(command);

			} catch (Exception e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			}
		    } else {
			JOptionPane.showMessageDialog(new JFrame(),
				"Please Select an APK");
		    }

		}
	    }
	});
	shell.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		//
		try {
		    Runtime.getRuntime().exec(
			    "cmd /C start /wait "
				    + sdk_path_txt.getText()
				    + "\\platform-tools\\adb -s "
				    + device_number_txt.getSelectedItem()
					    .toString() + " shell");
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}

	    }
	});
	push.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {

		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int returnVal = fc.showOpenDialog(LiveTestUtil.this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
		    File file = fc.getSelectedFile();
		    push_txt.setText(file.getAbsolutePath());
		    Process p;
		    try {
			String command = ("cmd /C start /wait "
				+ sdk_path_txt.getText()
				+ "\\platform-tools\\adb -s "
				+ device_number_txt.getSelectedItem()
					.toString() + " push \""
				+ push_txt.getText() + "\" /mnt/sdcard/");
			p = Runtime.getRuntime().exec(command);
			System.out.println(command);

		    } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}

	    }

	});

    }

    public static void main(String args[]) {
	LiveTestUtil l = new LiveTestUtil();
	l.checkDevice();
    }

    public int checkDevice() {
	try {
	    Process p = Runtime.getRuntime().exec(
		    "cmd /C " + sdk_path_txt.getText()
			    + "\\platform-tools\\adb devices");
	    BufferedReader in = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));
	    String input = new String();
	    in.readLine();
	    Vector<String> devices = new Vector<String>();
	    while ((input = in.readLine()).length() > 0) {

		devices.add(input.substring(0, input.length() - 7));
	    }
	    if (!devices.isEmpty()) {
		Object o[] = devices.toArray();
		String s[] = new String[o.length];
		for (int i = 0; i < o.length; i++) {
		    s[i] = o[i].toString();

		}
		
		device_number_txt.setModel(new JComboBox<String>(s).getModel());
	    }else{
		    JOptionPane.showMessageDialog(new JFrame(),
				"Please start a emulator before continuing");
		}

	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return 1;

    }
}
