package rvik;

public class Main {

	public static Object readCodeop(int[] instr) {
		// Little endian
		String codeop = "";
		Object result = null;
		for (int i = 25; i < 32; i++) {
			codeop += Integer.toString(instr[i]);
		}
		
		if (codeop.equals("1110110")) { // lui
			result = (TypeLui) new TypeLui();
		}
		
		if (codeop.equals("0010111")) { // auipc
			result = (TypeAuipc) new TypeAuipc();
		}
		
		if (codeop.equals("1101111")) { // J
			result = (TypeJ) new TypeJ();
		}
		
		if (codeop.equals("1100111")) { // jalr
			result = (TypeJalr) new TypeJalr();
		}
		
		if (codeop.equals("1100011")) { // B
			result = (TypeB) new TypeB();
		}
		
		if (codeop.equals("0000011")) { // load
			result = (TypeLoad) new TypeLoad();
		}
		
		if (codeop.equals("0100011")) { // S
			result = (TypeS) new TypeS();
		}
		
		if (codeop.equals("0010011")) { // Immediate
			result = (TypeImmediate) new TypeImmediate();
		}
		
		if (codeop.equals("0110011")) { // R
			result = (TypeR) new TypeR();
		}
		
		if (codeop.equals("1110011")) { // calls / atomic
			result = (TypeCallAtomic) new TypeCallAtomic();
		}
		
		System.out.println(codeop);
		return result;
	}
	
	public static void main(String[] args) {
	}
}
