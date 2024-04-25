package rvik;

public class Main {

	public static int[] fillSegment(int[] instr, int first, int last) {
		
		int length = last - first + 1;
		int[] result = new int[length];
		
		int o = 0;
		for (int i = first; i < last + 1; i++) {
			result[o] = instr[i];
			o++;
		}
		
		return result;
	}
	
	public static Instruction readCodeop(int[] instr) {
		// Little endian
		String codeop = "";
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
		
		if (instruction instanceof TypeLui) {
			
			((TypeLui) instruction).setImm20(fillSegment(instr, 12, 31));
			((TypeLui) instruction).setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeAuipc) {
			((TypeAuipc) instruction).setImm20(fillSegment(instr, 12, 31));
			((TypeAuipc) instruction).setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeJ) {
			int[] jImm20 = {instr[21], instr[22], instr[23], instr[24], instr[25], instr[26], instr[27], instr[28], instr[29], instr[30], instr[20], instr[12], instr[13], instr[14], instr[15], instr[16], instr[17], instr[18], instr[19], instr[31] };
			
			((TypeJ) instruction).setImm20(jImm20);
			((TypeJ) instruction).setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeJalr) {
			((TypeJalr) instruction).setImm12(fillSegment(instr, 20, 31));
			((TypeJalr) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeJalr) instruction).setFunct3(fillSegment(instr, 12, 14));
			((TypeJalr) instruction).setRd(fillSegment(instr, 11, 7));
		}
		
		if (instruction instanceof TypeB) {
			int [] bImm12 = {instr[8], instr[9], instr[10], instr[11], instr[25], instr[26], instr[27], instr[28], instr[29], instr[30], instr[7], instr[31]};
			((TypeB) instruction).setImm12(bImm12);
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
	
	}
}
