package rv32i;

import javax.swing.plaf.synth.SynthOptionPaneUI;

public class Main {
	
	public static void main(String[] args) {
		
		Compiler.pc = 0;
		
		Compiler.pm.set(1, true);
		Compiler.pm.set(2, true);
		Compiler.pm.set(3, true);
		Compiler.pm.set(4, false);
		Compiler.pm.set(5, false);
		Compiler.pm.set(6, true);
		Compiler.pm.set(7, true);
		Compiler.pm.set(8, false);
		Compiler.pm.set(9, true);
		Compiler.pm.set(10, true);
		Compiler.pm.set(11, false);
		Compiler.pm.set(12, false);
		Compiler.pm.set(13, false);
		Compiler.pm.set(14, false);
		Compiler.pm.set(15, false);
		Compiler.pm.set(16, true);
		Compiler.pm.set(17, false);
		Compiler.pm.set(18, false);
		Compiler.pm.set(19, false);
		Compiler.pm.set(20, false);
		Compiler.pm.set(21, false);
		Compiler.pm.set(22, true);
		Compiler.pm.set(23, false);
		Compiler.pm.set(24, false);
		Compiler.pm.set(25, false);
		Compiler.pm.set(26, false);
		Compiler.pm.set(27, false);
		Compiler.pm.set(28, false);
		Compiler.pm.set(29, false);
		Compiler.pm.set(30, false);
		Compiler.pm.set(31, false);
		Compiler.pm.set(32, false);
		
		Compiler.pc = 1;
		Compiler.reg[1] = 1;
		Compiler.reg[2] = 4;
		
		Compiler.run();
		System.out.println(Compiler.pc);
		System.out.println(Compiler.reg[6]);

	}
	
}
