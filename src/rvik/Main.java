package rvik;

public class Main {
	
	public static int[] reg = new int[32];
	public static int pc;
	
	public static int[] pm = new int[1000]; // Size TBD
	public static int[] mem = new int[1000]; // Size TBD
	
	public static int[] fillSegment(int[] instr, int first, int last) {
		
		// Gets an instruction and a bit range and returns an array filtering out
		// the rest of the instruction. Used in fillInstr() for simplicity purposes.
		
		int length = last - first + 1;
		int[] result = new int[length];
		
		int o = 0;
		for (int i = first; i < last + 1; i++) {
			result[o] = instr[i];
			o++;
		}
		
		return result;
	}
	
	public static int[] bitExpansion(int[] segment, int resultBits) {
		int[] result = new int[resultBits];
		
		for (int i = 0; i < segment.length; i++) {
			result[i] = segment[i];
		}
		return result;
	}
	
	public static String segmentToString(int[] segment) {
		
		// Returns a string with the segment value in binary.
		// Used for convenience in runInstruction()
		
		String result = "";
		for (int i = 0; i < segment.length; i++) {
			result = result + Integer.toString(segment[i]);
		}
		return result;
	}
	
	public static int btiu(int[] segment) {
		
		// Binary to int unsigned.
		
		int result = 0;

		for (int i = 0; i < segment.length; i++) {
			if (segment[i] == 1) {
				result += 1 * Math.pow(2,i);
			}
		}
		return result;
	}
	
	public static int btis(int[] segment) {
		
		// Binary to int signed.
		
		int result = 0;
		
		if (segment[segment.length - 1] == 0) {
		
			for (int i = 0; i < segment.length - 1; i++) {
				if (segment[i] == 1) {
					result += 1 + Math.pow(2, i);
				}
			}
		}
			
		if (segment[segment.length - 1] == 1) {
			
			for (int i = 0; i < segment.length - 1; i++) {
				if (segment[i] == 0) {
					result += 1 + Math.pow(2, i);
				}
			}
			result++;
			result = 0 - result;
		}
		return result;
		
	}
	
	public static void runInstruction(int[] instr) {
		
		// Runs the given instruction.
		
		Instruction instruction = getInstrType(instr);
		
		if (instruction instanceof TypeLui) {
			TypeLui typeLui = (TypeLui) instruction;
			
			reg[btiu(((TypeLui) instruction).getRd())] = btis(bitExpansion(typeLui.getImm20(), 32)) << 12; // rd <- imm_u
			pc = pc + 4; // pc <- pc + 4
		}
		
		if (instruction instanceof TypeAuipc) {
			TypeAuipc typeAuipc = (TypeAuipc) instruction;
			
			reg[btiu(((TypeLui) instruction).getRd())] = pc + btis(bitExpansion(typeAuipc.getImm20(), 32)) << 12;; // rd <- pc + imm_u
			pc = pc + 4; // pc <- pc + 4
		}
		
		if (instruction instanceof TypeJ) {
			TypeJ typeJ = (TypeJ) instruction;
			
			reg[btiu(typeJ.getRd())] = pc + 4; // rd <- pc + 4
			pc = pc + btis(typeJ.getImm20()); // pc <- pc + imm_j
		}
		
		if (instruction instanceof TypeJalr) {
			TypeJalr typeJalr = (TypeJalr) instruction;
			
			reg[btiu(typeJalr.getRd())] = pc + 4; // rd <- pc + 4
			pc = reg[btiu(typeJalr.getRs1())] + btis(typeJalr.getImm12()); // pc < rs1 + imm_i
		}
		
		if (instruction instanceof TypeB) {
			TypeB typeB = (TypeB) instruction;
			
			if (segmentToString(typeB.getFunct3()).equals("000")){ // This condition is better checked with btiu probably
				if (reg[btiu(typeB.getRs1())] == reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm12());
				} else {
					pc = pc + 4;
				}
			}
			
			if (segmentToString(typeB.getFunct3()).equals("001")) { // This condition is better checked with btiu probably
				if (reg[btiu(typeB.getRs1())] != reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm12());
				} else {
					pc = pc + 4;
				}
			}
			
			if (segmentToString(typeB.getFunct3()).equals("100")) { // This condition is better checked with btiu probably
				if (reg[btiu(typeB.getRs1())] < reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm12());
				} else {
					pc = pc + 4;
				}
			}
			
			if (segmentToString(typeB.getFunct3()).equals("101")) { // This condition is better checked with btiu probably
				if (reg[btiu(typeB.getRs1())] >= reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm12());
				} else {
					pc = pc + 4;
				}
			}
			
			if (segmentToString(typeB.getFunct3()).equals("110")) { // This condition is better checked with btiu probably
				if (Math.abs(reg[btiu(((TypeB) instruction).getRs1())]) < Math.abs(reg[btiu(((TypeB) instruction).getRs2())])) {
					pc = pc + btis(typeB.getImm12());
				} else {
					pc = pc + 4;
				}
			}
			
			if (segmentToString(typeB.getFunct3()).equals("111")) { // This condition is better checked with btiu probably
				if (Math.abs(reg[btiu(((TypeB) instruction).getRs1())]) >= Math.abs(reg[btiu(((TypeB) instruction).getRs2())])) {
					pc = pc + btis(typeB.getImm12());
				} else {
					pc = pc + 4;
				}
			}
		}
		
		if (instruction instanceof TypeLoad) {
			
		}
		
		if (instruction instanceof TypeS) {
			
		}
		
		if (instruction instanceof TypeImm) {
			TypeImm typeImm = (TypeImm) instruction;
			
			if (segmentToString(typeImm.getFunct3()).equals("000")) {
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] + btis(typeImm.getImm12()); // rd <- rs1 + imm12
				pc = pc + 4;
			}
			
			if (segmentToString(typeImm.getFunct3()).equals("010")) {
				if (reg[btiu(typeImm.getRs1())] < btis(typeImm.getImm12())) {
					reg[btiu(typeImm.getRd())] = 1;
				} else {
					reg[btiu(typeImm.getRd())] = 0;
				}
				pc = pc + 4;
			}
			
			if (segmentToString(typeImm.getFunct3()).equals("011")) {
				if (Math.abs(reg[btiu(typeImm.getRs1())]) < Math.abs(btis(typeImm.getImm12()))) {
					reg[btiu(typeImm.getRd())] = 1;
				} else {
					reg[btiu(typeImm.getRd())] = 0;
				}
				pc = pc + 4;
			}
			
			if (segmentToString(typeImm.getFunct3()).equals("100")) {
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] ^ btis(typeImm.getImm12());
				pc = pc + 4;
			}
			
			if (segmentToString(typeImm.getFunct3()).equals("110")) {
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] | btis(typeImm.getImm12());
				pc = pc + 4;
			}
			
			if (segmentToString(typeImm.getFunct3()).equals("111")) {
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] & btis(typeImm.getImm12());
				pc = pc + 4;
			}
			
			if (segmentToString(typeImm.getFunct3()).equals("001")) {
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] << btis(typeImm.getImm12());
				pc = pc + 4;
			}
			
			if (segmentToString(typeImm.getFunct3()).equals("101")) {
				if (instr[30] == 1) {
					reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] >>> btis(typeImm.getImm12());
				} else {
					reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] >> btis(typeImm.getImm12());
				}
				pc = pc + 4;
			}
			
		}
		
		if (instruction instanceof TypeR) {
			TypeR typeR = (TypeR) instruction;
			
			if (segmentToString(typeR.getFunct3()).equals("000")) {
				if (instr[30] == 0) {
					reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] + reg[btiu(typeR.getRs2())];
				}
				
				if (instr[30] == 1) {
					reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] - reg[btiu(typeR.getRs2())];
				}
				pc = pc + 4;
			}
			
			if (segmentToString(typeR.getFunct3()).equals("001")) {
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] << reg[btiu(typeR.getRs2())];
				pc = pc + 4;
			}
			
			if (segmentToString(typeR.getFunct3()).equals("010")) {
				if (reg[btiu(typeR.getRs1())] < reg[btiu(typeR.getRs2())]) {
					reg[btiu(typeR.getRd())] = 1;
				} else {
					reg[btiu(typeR.getRd())] = 0;
				}
				pc = pc + 4;	
			}
			
			if (segmentToString(typeR.getFunct3()).equals("011")) {
				if (Math.abs(reg[btiu(typeR.getRs1())]) < Math.abs(reg[btiu(typeR.getRs2())])) {
					reg[btiu(typeR.getRd())] = 1;
				} else {
					reg[btiu(typeR.getRd())] = 0;
				}
				pc = pc + 4;		
			}
			
			if (segmentToString(typeR.getFunct3()).equals("100")) {
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] ^ reg[btiu(typeR.getRs2())];
				pc = pc + 4;
			}
			
			if (segmentToString(typeR.getFunct3()).equals("101")) {
				if (instr[30] == 0) {
					reg[btiu(typeR.getFunct3())] = reg[btiu(typeR.getRs1())] >>> reg[btiu(typeR.getRs2())];
				}
				
				if (instr[30] == 1) {
					reg[btiu(typeR.getFunct3())] = reg[btiu(typeR.getRs1())] >> reg[btiu(typeR.getRs2())];
				}
				pc = pc + 4;
			}
			
			if (segmentToString(typeR.getFunct3()).equals("110")) {
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] | reg[btiu(typeR.getRs2())];
				pc = pc + 4;
			}
			
			if (segmentToString(typeR.getFunct3()).equals("111")) {
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] & reg[btiu(typeR.getRs2())];
				pc = pc + 4;
			}	
		}
		
		if (instruction instanceof TypeCallAtomic) {
			TypeCallAtomic typeCallAtomic = (TypeCallAtomic) instruction;
		}
		
	}
	
	public static Instruction getInstrType(int[] instr) {
		
		// Returns the specific instruction object (its type)
		// via the given instruction's code operation.
		
		String codeop = ""; // Little endian
		Instruction result = null;
		for (int i = 0; i < 7; i++) {
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
			result = (TypeImm) new TypeImm();
		}
		
		if (codeop.equals("0110011")) { // R
			result = (TypeR) new TypeR();
		}
		
		if (codeop.equals("1110011")) { // calls / atomic
			result = (TypeCallAtomic) new TypeCallAtomic();
		}
	
		return result;
	}
	
	public static void fillInstr(int[] instr, Instruction instruction) {
		
		// Fills in the attributes of an instruction according to its 
		// instruction type.
		
		if (instruction instanceof TypeLui) {
			TypeLui typeLui = (TypeLui) instruction;
			
			typeLui.setImm20(fillSegment(instr, 12, 31));
			typeLui.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeAuipc) {
			TypeAuipc typeAuipc = (TypeAuipc) instruction;
			
			typeAuipc.setImm20(fillSegment(instr, 12, 31));
			typeAuipc.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeJ) {
			int[] jImm20 = {instr[21], instr[22], instr[23], instr[24], instr[25], instr[26], instr[27], instr[28], instr[29], instr[30], instr[20], instr[12], instr[13], instr[14], instr[15], instr[16], instr[17], instr[18], instr[19], instr[31] };
			TypeJ typeJ = (TypeJ) instruction;
			
			typeJ.setImm20(jImm20);
			typeJ.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeJalr) {
			TypeJalr typeJalr = (TypeJalr) instruction;
			
			typeJalr.setImm12(fillSegment(instr, 20, 31));
			typeJalr.setRs1(fillSegment(instr, 15, 19));
			typeJalr.setFunct3(fillSegment(instr, 12, 14));
			typeJalr.setRd(fillSegment(instr, 11, 7));
		}
		
		if (instruction instanceof TypeB) {
			int [] bImm12 = {instr[8], instr[9], instr[10], instr[11], instr[25], instr[26], instr[27], instr[28], instr[29], instr[30], instr[7], instr[31]};
			TypeB typeB = (TypeB) instruction;
			
			typeB.setImm12(bImm12);
			((TypeB) instruction).setRs2(fillSegment(instr, 20, 24));
			((TypeB) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeB) instruction).setFunct3(fillSegment(instr, 12, 14));
		}
		
		if (instruction instanceof TypeLoad) {
			((TypeLoad) instruction).setImm12(fillSegment(instr, 20, 31));
			((TypeLoad) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeLoad) instruction).setFunct3(fillSegment(instr, 12, 14));
			((TypeLoad) instruction).setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeS) {
			int[] sImm12 = {instr [7], instr[8], instr[9], instr[10], instr[11], instr[25], instr[26], instr[27], instr[28], instr[29], instr[30], instr[31]};
			((TypeS) instruction).setImm12(sImm12);
			((TypeS) instruction).setRs2(fillSegment(instr, 20, 24));
			((TypeS) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeS) instruction).setFunct3(fillSegment(instr, 12, 14));
			((TypeS) instruction).setRd(fillSegment(instr,7, 11));
		}
		
		if (instruction instanceof TypeImm) {
			((TypeImm) instruction).setImm12(fillSegment(instr, 20, 31));
			((TypeImm) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeImm) instruction).setFunct3(fillSegment(instr, 12, 14));
			((TypeImm) instruction).setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeR) {
			((TypeR) instruction).setFunct7(fillSegment(instr, 25, 31));
			((TypeR) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeR) instruction).setRs2(fillSegment(instr, 20, 24));
			((TypeR) instruction).setFunct3(fillSegment(instr, 12, 14));
			((TypeR) instruction).setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeCallAtomic) {
			((TypeCallAtomic) instruction).setCsr12(fillSegment(instr, 20, 31));
			((TypeCallAtomic) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeCallAtomic) instruction).setFunct3(fillSegment(instr, 12, 14));
			((TypeCallAtomic) instruction).setRd(fillSegment(instr, 7, 11));
		}
	}
	
	public static void main(String[] args) {
		// Check that btiu and btis work without length argument
	}
}
