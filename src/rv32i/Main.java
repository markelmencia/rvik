package rv32i;

import javax.swing.JFileChooser;
import java.io.File;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Dimension;

public class Main {
	
	static String FILE_PATH = "";
	static String output = "";
	
	public static void printMemory(int address, int range) {
		output += "\nMEMORY VALUES FROM " + address + " TO " + (range + 1) + "\n--------------------------";
		
		int value;
		for (int i = address; i < address + range; i++) {
			value = 0;
			
			if (Compiler.mem.get(i)) {
				value = 1;
			}
			output += "\nmem[" + i + "]: " + value;
			}
		output += "\n--------------------------\n";
	}
	
	public static void printRegisters() {
		
		output += "\nREGISTER VALUES\n--------------------------";
		
		for (int i = 0; i < 32; i++) {
			output += "\n    x" + Integer.toString(i) + ": " + Compiler.reg[i];
		}
			output += "\n    PC: " + Compiler.pc;
		
		output += "\n--------------------------";
	}
	
	public static void getFile() {
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		int result = chooser.showOpenDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
		    File selectedFile = chooser.getSelectedFile();
		    FILE_PATH = selectedFile.getAbsolutePath();
		} else {
			FILE_PATH = "";
		}
	}
	
	public static void main(String[] args) {
		
		getFile();
		
		if (FILE_PATH == "") {
			JOptionPane.showMessageDialog(null, "File search cancelled");
		} else {
			int memoryOption = JOptionPane.showConfirmDialog(null, "Print memory?");
			String addressRangeString = "0, 0";
			if (memoryOption == 0) {
				addressRangeString = JOptionPane.showInputDialog("Enter address and range with the following format:\naddress, range");
			}
			
			Assembler.assembleFile(FILE_PATH);
			Compiler.run();
			output += "Program ran successfully\n";
			printRegisters();
			if (memoryOption == 0) {
				printMemory(Integer.parseInt(addressRangeString.split(", ")[0]), Integer.parseInt(addressRangeString.split(", ")[1]));
			}
			JTextArea textArea = new JTextArea(output);
			JScrollPane scrollPane = new JScrollPane(textArea);  
			textArea.setLineWrap(true);  
			textArea.setWrapStyleWord(true); 
			scrollPane.setPreferredSize( new Dimension( 500, 500 ) );
			JOptionPane.showMessageDialog(null, scrollPane, "rvik - RV32i Assembler and simulator (v1.0)", JOptionPane.DEFAULT_OPTION);
		}
	}	
}
