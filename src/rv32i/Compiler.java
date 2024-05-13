package rv32i;

import java.util.BitSet;
import instructions.*;

/*	TESTED INSTRUCTIONS

	lui: WORKING
	auipc: WORKING 
	jal: WORKING
	jalr: WORKING
	
	add: WORKING
	sub: WORKING
	sll: WORKING
	slt: WORKING
	sltu: WORKING
	xor: WORKING
	srl: WORKING
	sra: WORKING
	or: WORKING
	and: WORKING
	
	addi: WORKING
	slti: WORKING
	sltiu: WORKING
	xori: WORKING
	ori: WORKING
	andi: WORKING
	slli: WORKING
	srli: WORKING
	srai: WORKING
	
	sb: WORKING
	sh: WORKING
	sw: WORKING
	
	lb: WORKING
	lh: WORKING
	lw: WORKING
	lbu: WORKING
	lhu: WORKING
	
	beq: WORKING
	bne: WORKING
	blt: WORKING
	bge: WORKING
	bltu: WORKING
	bgeu: WORKING
	
	csrrw: TODO
	csrrs: TODO
	csrrc: TODO
	csrrwi: TODO
	csrrsi: TODO
	csrrci: TODO

*/

public class Compiler {

	public static int[] reg = new int[32];
	public static int pc;
	
	public static BitSet pm = new BitSet(1000); // Size TBD
	public static BitSet mem = new BitSet(1000); // Size TBD
	
	// MEMORY RELATED METHODS
	
	public static BitSet getInstr() {
		BitSet result = new BitSet();
		int start = pc;
		int finish = pc + 32;
		
		int j = 0;
		for (int i = start; i < finish; i++) {
			result.set(j, pm.get(i));
			j++;
		}
		
		return result;
	}

	public static BitSet loadMem(int address, int size) {
		BitSet result = new BitSet(size);
		int start = address;
		int finish = address + size;
		
		int j = 0;
		for (int i = start; i < finish; i++) {
			result.set(j, mem.get(i));
			j++;
		}
		return result;
	}
	
	
	public static void storeMem(BitSet segment, int address) {
		int start = address;
		int finish = address + segment.length();
		
		int j = 0;
		for (int i = start; i < finish; i++) {
			mem.set(i, segment.get(j));
			j++;
		}
	}
		
	
	public static void run() {
		BitSet instructionArray = getInstr();
		Instruction instruction = getInstrType(instructionArray);
		fillInstr(instructionArray, instruction);
		runInstruction(instruction);
	}
	
	// COMPILER FUNCTIONALITY FUNCTIONS
	
	public static BitSet fillSegment(BitSet instr, int first, int last) {
		
		// Gets an instruction array and a bit range and returns an array filtering out
		// the rest of the instruction. Used in fillInstr() for simplicity purposes.
		
		int length = last - first + 1;
		BitSet result = new BitSet(length);
		
		int o = 0;
		for (int i = first; i < last + 1; i++) {
			result.set(o, instr.get(i)); 
			o++;
		}
		
		return result;
	}
	
	public static BitSet bitExtension(BitSet segment, int resultBits) {
		
		// Extends a bit array to a desired bit ammount. PROBABLY REDUNDANT.
		
		BitSet result = new BitSet(resultBits);
		
		for (int i = 0; i < segment.length(); i++) {
			result.set(i, segment.get(i));
		}
		return result;
	}
	
	public static int btiu(BitSet segment) {
		
		// Binary to int unsigned.
		
		int result = 0;

		for (int i = 0; i < segment.length(); i++) {
			if (segment.get(i)) {
				result += 1 * Math.pow(2,i);
			}
		}
		return result;
	}
	
	public static int btis(BitSet segment, int size) {
		
		// Binary to int signed. Due to BitSize having
		// a dinamic length depending on the last bit set,
		// a size parameter is needed.
		
		int result = 0;
		
		if (segment.length() < size) {
		
			result = btiu(segment);
		} else {
			
			for (int i = 0; i < segment.length(); i++) {
				if (!segment.get(i)) {
					result = result + ((int) Math.pow(2,i));
				}
			}
			result++;
			result = 0 - result;
		}
		return result;
		
	}

	
	public static BitSet getReg(int regIndex, int size) {
		
		// Returns the sized array with the value of the
		// requested register.
		
		String binary = Integer.toBinaryString(reg[regIndex]);
		BitSet segment = new BitSet(32);
		for (int i = 0; i < binary.length(); i++) {
				segment.set(binary.length() - 1 - i, binary.charAt(i) == '1');
		}
		
		BitSet result = new BitSet(size);
		for (int i = 0; i < size; i++) {
			result.set(i, segment.get(i));
		}
		
		return result;
	}
	
	public static void runInstruction(Instruction instruction) {
		
		// Runs the given instruction.
		
		if (instruction instanceof TypeLui) {
			TypeLui typeLui = (TypeLui) instruction;
			
			reg[btiu(((TypeLui) instruction).getRd())] = btis(bitExtension(typeLui.getImm20(), 32), 32) << 12; // rd <- imm_u
			pc = pc + 16; // pc <- pc + 16
		}
		
		if (instruction instanceof TypeAuipc) {
			TypeAuipc typeAuipc = (TypeAuipc) instruction;
			
			reg[btiu(((TypeAuipc) instruction).getRd())] = pc + btis(bitExtension(typeAuipc.getImm20(), 32), 32) << 12;; // rd <- pc + imm_u
			pc = pc + 16; // pc <- pc + 16
		}
		
		if (instruction instanceof TypeJ) {
			TypeJ typeJ = (TypeJ) instruction;
			
			reg[btiu(typeJ.getRd())] = pc + 16; // rd <- pc + 16
			pc = pc + btis(typeJ.getImm21(), 21); // pc <- pc + imm_j
		}
		
		if (instruction instanceof TypeJalr) {
			TypeJalr typeJalr = (TypeJalr) instruction;
			
			reg[btiu(typeJalr.getRd())] = pc + 16; // rd <- pc + 16
			pc = reg[btiu(typeJalr.getRs1())] + btis(typeJalr.getImm12(), 12); // pc < rs1 + imm_i
		}
		
		if (instruction instanceof TypeB) {
			TypeB typeB = (TypeB) instruction;
			
			if (btiu(typeB.getFunct3()) == 0) { // beq
				if (reg[btiu(typeB.getRs1())] == reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 16;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 1) { // bne
				if (reg[btiu(typeB.getRs1())] != reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 16;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 4) { // blt
				if (reg[btiu(typeB.getRs1())] < reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 16;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 5) { // bge
				if (reg[btiu(typeB.getRs1())] >= reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 16;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 6) { // btlu
				if (Math.abs(reg[btiu(((TypeB) instruction).getRs1())]) < Math.abs(reg[btiu(((TypeB) instruction).getRs2())])) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 16;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 7) { // bgeu
				if (Math.abs(reg[btiu(((TypeB) instruction).getRs1())]) >= Math.abs(reg[btiu(((TypeB) instruction).getRs2())])) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 16;
				}
			}
		}
		
		if (instruction instanceof TypeLoad) {
			TypeLoad typeLoad = (TypeLoad) instruction;
			
			if (btiu(typeLoad.getFunct3()) == 0) { // lb
				reg[btiu(typeLoad.getRd())] = btis(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 12)), 8), 8);
				pc = pc + 16;
			}
			
			if (btiu(typeLoad.getFunct3()) == 1) { // lh
				reg[btiu(typeLoad.getRd())] = btis(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 12)), 16), 16);
				pc = pc + 16;
			}
			
			if (btiu(typeLoad.getFunct3()) == 2) { // lw
				reg[btiu(typeLoad.getRd())] = btis(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 12)), 32), 32);
				pc = pc + 16;
			}
			
			if (btiu(typeLoad.getFunct3()) == 4) { // lbu
				reg[btiu(typeLoad.getRd())] = btiu(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 8)), 8));
				pc = pc + 16;
			}
			
			if (btiu(typeLoad.getFunct3()) == 5) { // lhu
				reg[btiu(typeLoad.getRd())] = btiu(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 12)), 16));
				pc = pc + 16;
			}
		}
		
		if (instruction instanceof TypeS) {
			TypeS typeS = (TypeS) instruction;
			
			if (btiu(typeS.getFunct3()) == 0) { // sb
				storeMem(getReg(btiu(typeS.getRs2()), 8), reg[btiu(typeS.getRs1())] + btis(typeS.getImm12(), 12));
				pc = pc + 16;
			}
			
			if (btiu(typeS.getFunct3()) == 1) { // sh
				storeMem(getReg(btiu(typeS.getRs2()), 16), reg[btiu(typeS.getRs1())] + btis(typeS.getImm12(), 12));
				pc = pc + 16;
			}
			
			if (btiu(typeS.getFunct3()) == 2) { // sw
				storeMem(getReg(btiu(typeS.getRs2()), 32), reg[btiu(typeS.getRs1())] + btis(typeS.getImm12(), 12));
				pc = pc + 16;
			}
		}
		
		if (instruction instanceof TypeImm) {
			TypeImm typeImm = (TypeImm) instruction;
			
			if (btiu(typeImm.getFunct3()) == 0) { // addi
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] + btis(typeImm.getImm12(), 12); // rd <- rs1 + imm12
				pc = pc + 16;
			}
			
			if (btiu(typeImm.getFunct3()) == 2) { // slti
				if (reg[btiu(typeImm.getRs1())] < btis(typeImm.getImm12(), 12)) {
					reg[btiu(typeImm.getRd())] = 1;
				} else {
					reg[btiu(typeImm.getRd())] = 0;
				}
				pc = pc + 16;
			}
			
			if (btiu(typeImm.getFunct3()) == 3) { // sltiu
				if (Math.abs(reg[btiu(typeImm.getRs1())]) < Math.abs(btis(typeImm.getImm12(), 12))) {
					reg[btiu(typeImm.getRd())] = 1;
				} else {
					reg[btiu(typeImm.getRd())] = 0;
				}
				pc = pc + 16;
			}
			
			if (btiu(typeImm.getFunct3()) == 4) { // xori
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] ^ btis(typeImm.getImm12(), 12);
				pc = pc + 16;
			}
			
			if (btiu(typeImm.getFunct3()) == 6) { // ori
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] | btis(typeImm.getImm12(), 12);
				pc = pc + 16;
			}
			
			if (btiu(typeImm.getFunct3()) == 7) { // andi
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] & btis(typeImm.getImm12(), 12);
				pc = pc + 16;
			}
			
			if (btiu(typeImm.getFunct3()) == 1) { // slli
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] << btis(typeImm.getImm12(), 12);
				pc = pc + 16;
			}
			
			if (btiu(typeImm.getFunct3()) == 5) { // srli / srai
				if (typeImm.getInstr30() == 0) { // srli
					reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] >>> btis(typeImm.getImm12(), 12);
				} else { // srai
					reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] >> btis(typeImm.getImm12(), 12);
				}
				pc = pc + 16;
			}
			
		}
		
		if (instruction instanceof TypeR) {
			TypeR typeR = (TypeR) instruction;
			
			if (btiu(typeR.getFunct3()) == 0) { // add / sub
				if (btiu(typeR.getFunct7()) == 0) { // add
					reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] + reg[btiu(typeR.getRs2())];
				} else { // sub
					reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] - reg[btiu(typeR.getRs2())];
				}
				
				pc = pc + 16;
			}
			
			if (btiu(typeR.getFunct3()) == 1) { // sll
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] << reg[btiu(typeR.getRs2())];
				pc = pc + 16;
			}
			
			if (btiu(typeR.getFunct3()) == 2) { // slt
				if (reg[btiu(typeR.getRs1())] < reg[btiu(typeR.getRs2())]) {
					reg[btiu(typeR.getRd())] = 1;
				} else {
					reg[btiu(typeR.getRd())] = 0;
				}
				pc = pc + 16;	
			}
			
			if (btiu(typeR.getFunct3()) == 3) { // sltu
				if (Math.abs(reg[btiu(typeR.getRs1())]) < Math.abs(reg[btiu(typeR.getRs2())])) {
					reg[btiu(typeR.getRd())] = 1;
				} else {
					reg[btiu(typeR.getRd())] = 0;
				}
				pc = pc + 16;		
			}
			
			if (btiu(typeR.getFunct3()) == 4) { // xor
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] ^ reg[btiu(typeR.getRs2())];
				pc = pc + 16;
			}
			
			if (btiu(typeR.getFunct3()) == 5) { // srl / sra
				if (btiu(typeR.getFunct7()) == 0) { // srl
					reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] >>> reg[btiu(typeR.getRs2())];
				} else { // sra
					reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] >> reg[btiu(typeR.getRs2())];
				}
				pc = pc + 16;
			}
			
			if (btiu(typeR.getFunct3()) == 6) { // or
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] | reg[btiu(typeR.getRs2())];
				pc = pc + 16;
			}
			
			if (btiu(typeR.getFunct3()) == 7) { // and
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] & reg[btiu(typeR.getRs2())];
				pc = pc + 16;
			}	
		}
		
		if (instruction instanceof TypeCallAtomic) {
			TypeCallAtomic typeCallAtomic = (TypeCallAtomic) instruction; // TODO
		}
	}
	
	public static Instruction getInstrType(BitSet instr) {
		
		// Returns the specific instruction object (its type)
		// via the given instruction's code operation.
		
		BitSet codeop = new BitSet(7); // Little endian
		for (int i = 0; i < 7; i++) {
			codeop.set(i, instr.get(i));
		}
		
		Instruction result = null;
		
		if (btiu(codeop) == 55) { // lui (0110111)
			result = (TypeLui) new TypeLui();
		}
		
		if (btiu(codeop) == 23) { // auipc (0010111)
			result = (TypeAuipc) new TypeAuipc();
		}
		
		if (btiu(codeop) == 111) { // J (1101111)
			result = (TypeJ) new TypeJ();
		}
		
		if (btiu(codeop) == 103) { // jalr (1100111)
			result = (TypeJalr) new TypeJalr();
		}
		
		if (btiu(codeop) == 99) { // B (1100011)
			result = (TypeB) new TypeB();
		}
		
		if (btiu(codeop) == 3) { // load (0000011)
			result = (TypeLoad) new TypeLoad();
		}
		
		if (btiu(codeop) == 35) { // S (0100011)
			result = (TypeS) new TypeS();
		}
		
		if (btiu(codeop) == 19) { // Immediate (0010011)
			result = (TypeImm) new TypeImm();
		}
		
		if (btiu(codeop) == 51) { // R (0110011)
			result = (TypeR) new TypeR();
		}
		
		if (btiu(codeop) == 115) { // calls / atomic (1110011)
			result = (TypeCallAtomic) new TypeCallAtomic();
		}
	
		return result;
	}
	
	public static void fillInstr(BitSet instr, Instruction instruction) {
		
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
			BitSet jImm21 = new BitSet(21);
			jImm21.set(0, false);
			jImm21.set(1, instr.get(21));
			jImm21.set(2, instr.get(22));
			jImm21.set(3, instr.get(23));
			jImm21.set(4, instr.get(24));
			jImm21.set(5, instr.get(25));
			jImm21.set(6, instr.get(26));
			jImm21.set(7, instr.get(27));
			jImm21.set(8, instr.get(28));
			jImm21.set(9, instr.get(29));
			jImm21.set(10, instr.get(30));
			jImm21.set(11, instr.get(20));
			jImm21.set(12, instr.get(12));
			jImm21.set(13, instr.get(13));
			jImm21.set(14, instr.get(14));
			jImm21.set(15, instr.get(15));
			jImm21.set(16, instr.get(16));
			jImm21.set(17, instr.get(17));
			jImm21.set(18, instr.get(18));
			jImm21.set(19, instr.get(19));
			jImm21.set(20, instr.get(31));
			
			TypeJ typeJ = (TypeJ) instruction;
			
			typeJ.setImm21(jImm21);
			typeJ.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeJalr) {
			TypeJalr typeJalr = (TypeJalr) instruction;
			
			typeJalr.setImm12(fillSegment(instr, 20, 31));
			typeJalr.setRs1(fillSegment(instr, 15, 19));
			typeJalr.setFunct3(fillSegment(instr, 12, 14));
			typeJalr.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeB) {
			BitSet bImm13 = new BitSet(13);
			bImm13.set(0, false);
			bImm13.set(1, instr.get(8));
			bImm13.set(2, instr.get(9));
			bImm13.set(3, instr.get(10));
			bImm13.set(4, instr.get(11));
			bImm13.set(5, instr.get(25));
			bImm13.set(6, instr.get(26));
			bImm13.set(7, instr.get(27));
			bImm13.set(8, instr.get(28));
			bImm13.set(9, instr.get(29));
			bImm13.set(10, instr.get(30));
			bImm13.set(11, instr.get(7));
			bImm13.set(12, instr.get(31));
			
			TypeB typeB = (TypeB) instruction;
			
			typeB.setImm13(bImm13);
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
			BitSet sImm12 = new BitSet(12);
			sImm12.set(0, instr.get(7));
			sImm12.set(1, instr.get(8));
			sImm12.set(2, instr.get(9));
			sImm12.set(3, instr.get(10));
			sImm12.set(4, instr.get(11));
			sImm12.set(5, instr.get(25));
			sImm12.set(6, instr.get(26));
			sImm12.set(7, instr.get(27));
			sImm12.set(8, instr.get(28));
			sImm12.set(9, instr.get(29));
			sImm12.set(10, instr.get(30));
			sImm12.set(11, instr.get(31));
			
			((TypeS) instruction).setImm12(sImm12);
			((TypeS) instruction).setRs2(fillSegment(instr, 20, 24));
			((TypeS) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeS) instruction).setFunct3(fillSegment(instr, 12, 14));
		}
		
		if (instruction instanceof TypeImm) {
			((TypeImm) instruction).setImm12(fillSegment(instr, 20, 31));
			((TypeImm) instruction).setRs1(fillSegment(instr, 15, 19));
			((TypeImm) instruction).setFunct3(fillSegment(instr, 12, 14));
			((TypeImm) instruction).setRd(fillSegment(instr, 7, 11));
			((TypeImm) instruction).setInstr30(instr.get(30));
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
}
