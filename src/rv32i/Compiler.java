package rv32i;

import instructions.*;
import java.util.BitSet;
import java.util.HashMap;

/*	
	csrrw: TODO
	csrrs: TODO
	csrrc: TODO
	csrrwi: TODO
	csrrsi: TODO
	csrrci: TODO
*/

public class Compiler {

	public static int[] reg = new int[32];
	public static int pc = 0;
	
	public static BitSet pm = new BitSet(65536); // Size TBD
	public static BitSet mem = new BitSet(65536); // Size TBD

	public static HashMap<Integer, Instruction> codeopToInstr = new HashMap<Integer, Instruction>() {{
			put(0b0110111, new TypeLui()); 		  // (0110111)
			put(0b0010111, new TypeAuipc()); 	  // (0010111)
			put(0b1101111, new TypeJ()); 		  // (1101111)
			put(0b1100111, new TypeJalr()); 	  // (1100111)
			put(0b1100011, new TypeB()); 		  // (1100011)
			put(0b0000011, new TypeLoad());   	  // (0000011)
			put(0b0100011, new TypeS()); 		  // (0100011)
			put(0b0010011, new TypeImm()); 		  // (0010011)
			put(0b0110011, new TypeR());    	  // (0110011)
			put(0b1110011, new TypeCallAtomic()); // (1110011)
		}};
	
	// MEMORY RELATED METHODS
	
	/**
	 * Gets the BitSet the Program Counter is pointing to
	 */
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

	/**
	 * Gets a BitSet with the content of the specified address up to size
	 * @param address The first address that will be read
	 * @param size The amount of adresses that will be read after the first
	 */
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
	
	/**
	 * Stores a BitSet into the specified address
	 * @param segment The bit segment that will be stored
	 * @param address The first address in which the segment will be stored
	 */
	public static void storeMem(BitSet segment, int address) {
		int start = address;
		int finish = address + segment.length();
		
		int j = 0;
		for (int i = start; i < finish; i++) {
			mem.set(i, segment.get(j));
			j++;
		}
	}
		
	/**
	 * Returns true if the BitSet is empty (has no set bits).
	 * @param instr The BitSet that will be checked
	 */
	public static boolean instructionIsEmpty(BitSet instr) {
		for (int i = 0; i < 32; i++) {
			if (instr.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Runs a program. The instructions will execute until an empty instruction is found.
	 */
	public static void run() {
		BitSet instructionArray = getInstr();
		Instruction instruction;
			
		while(!instructionIsEmpty(instructionArray)) {
			instruction = getInstrType(instructionArray);
			fillInstr(instructionArray, instruction);
			runInstruction(instruction);
			reg[0] = 0; // The first register is hardcoded to 0
			instructionArray = getInstr();
		}
	}
	
	// COMPILER FUNCTIONALITY FUNCTIONS
	
	/**
	 * Gets an instruction array and a bit range and returns an array filtering 
	 * out the rest of the instruction. Used in fillInstr() for simplicity purposes.
	 * @param instr The instruction array that will be filtered.
	 * @param first the first bit of the range.
	 * @param last The last bit of the range.
	 * @return An array of the size of the range, with the bits that haven't been filtered out.
	 */
	public static BitSet fillSegment(BitSet instr, int first, int last) {
		int length = last - first + 1;
		BitSet result = new BitSet(length);
		
		int o = 0;
		for (int i = first; i < last + 1; i++) {
			result.set(o, instr.get(i)); 
			o++;
		}	
		return result;
	}
	
	/**
	 * Extends a bit array to a desired bit ammount.
	 * @param segment The instruction array that will be filtered.
	 * @param resultBits The desired bit length.
	 * @return The extended BitSet.
	 */
	public static BitSet bitExtension(BitSet segment, int resultBits) {
		BitSet result = new BitSet(resultBits);
		
		for (int i = 0; i < segment.length(); i++) {
			result.set(i, segment.get(i));
		}
		return result;
	}
	
	/**
	 * Converts a binary array to a base-10 integer (unsigned)
	 */
	public static int btiu(BitSet segment) {
		
		// Binary to int unsigned.
		
		int result = 0;

		for (int i = 0; i < segment.length(); i++) {
			if (segment.get(i)) {
				result |= (1 << i);
			}
		}
		return result;
	}
	
	/**
	 * Converts a binary array to a base-10 integer (unsigned)
	 */
	public static int btis(BitSet segment, int size) {
		
		// Binary to int signed. Due to BitSize having
		// a dinamic length depending on the last bit set,
		// a size parameter is needed.

		int result = 0;

		for (int i = 0; i < size; i++) {
			if (segment.get(i)) {
				result |= (1 << i);
			}
		}
		if (segment.get(size - 1)) {
			result -= (1 << size);
		}
		return result;
	}

	/**
	 * Gets the value of a register up to size
	 */
	public static BitSet getReg(int regIndex, int size) {
    	int regValue = reg[regIndex];
    	BitSet result = new BitSet(size);
    
    	for (int i = 0; i < size; i++) {
        	// Masks the specific bit that is about to be set/unset and checks if it's set
        	result.set(i, (regValue & (1 << (size - 1 - i))) != 0);
   		}
    	return result;
	}
	
	// TODO: Perhaps move the run functions to each class or w/e
	public static void runTypeLui(TypeLui instruction) {
		int rd = btiu(instruction.getRd());
		int imm20 = btis(bitExtension(instruction.getImm20(), 32), 32);

		reg[rd] = imm20 << 12; // rd <- imm_u
		pc = pc + 32;
	}

	public static void runTypeAuipc(TypeAuipc instruction) {		
		int rd = btiu(instruction.getRd());
		int imm20 = btis(bitExtension(instruction.getImm20(), 32), 32);

		reg[rd] = pc + (imm20 << 12); // rd <- pc + imm_u
		pc = pc + 32;
	}

	public static void runTypeJ(TypeJ instruction) {
		int rd = btiu(instruction.getRd());
		int imm21 = btis(instruction.getImm21(), 21);

		reg[rd] = pc + 32; // rd <- pc + 32
		pc = pc + imm21; // pc <- pc + imm_j
	}
	
	public static void runTypeJalr(TypeJalr instruction) {
		int rd = btiu(instruction.getRd());
		int rs1 = btiu(instruction.getRs1());
		int imm12 = btis(instruction.getImm12(), 12);

		reg[rd] = pc + 32; // rd <- pc + 32
		pc = reg[rs1] + imm12; // pc < rs1 + imm_i
	}

	public static void runBeq(TypeB instruction) {
		int rs1 = btiu(instruction.getRs1());
		int rs2 = btiu(instruction.getRs2());
		int imm13 = btis(instruction.getImm13(), 13);

		if (reg[rs1] == reg[rs2]) {
			pc = pc + imm13;
		} else {
			pc = pc + 32;
		}
	}

	public static void runBne(TypeB instruction) {
		int rs1 = btiu(instruction.getRs1());
		int rs2 = btiu(instruction.getRs2());
		int imm13 = btis(instruction.getImm13(), 13);

		if (reg[rs1] != reg[rs2]) {
			pc = pc + imm13;
		} else {
			pc = pc + 32;
		}
	}
	// TODO: consider creating new files to shrink size of this one

	public static void runBlt(TypeB instruction) {
		int rs1 = btiu(instruction.getRs1());
		int rs2 = btiu(instruction.getRs2());
		int imm13 = btis(instruction.getImm13(), 13);

		if (reg[rs1] < reg[rs2]) {
			pc = pc + imm13;
		} else {
			pc = pc + 32;
		}		
	}

	public static void runBge(TypeB instruction) {
		int rs1 = btiu(instruction.getRs1());
		int rs2 = btiu(instruction.getRs2());
		int imm13 = btis(instruction.getImm13(), 13);

		if (reg[rs1] >= reg[rs2]) {
			pc = pc + imm13;
		} else {
			pc = pc + 32;
		}
	}

	public static void runBltu(TypeB instruction) {
		int rs1 = btiu(instruction.getRs1());
		int rs2 = btiu(instruction.getRs2());
		int imm13 = btis(instruction.getImm13(), 13);

		if (Math.abs(reg[rs1]) < Math.abs(reg[rs2])) {
			pc = pc + imm13;
		} else {
			pc = pc + 32;
		}
	}

	public static void runBgeu(TypeB instruction) {
		int rs1 = btiu(instruction.getRs1());
		int rs2 = btiu(instruction.getRs2());
		int imm13 = btis(instruction.getImm13(), 13);

		if (Math.abs(reg[rs1]) >= Math.abs(reg[rs2])) {
			pc = pc + imm13;
		} else {
			pc = pc + 32;
		}
	}

	public static void runTypeB(TypeB instruction) {
		HashMap<Integer, Runnable> runTypeB = new HashMap<>() {{ // TODO: move this
			put(0, () -> runBeq(instruction));
			put(1, () -> runBne(instruction));
			put(4, () -> runBlt(instruction));
			put(5, () -> runBge(instruction));
			put(6, () -> runBltu(instruction));
			put(7, () -> runBgeu(instruction));
		}};

		Runnable function = runTypeB.get(btiu(instruction.getFunct3()));
		if (function != null) {
			function.run();
		} else {
			System.err.println("Error: Type B instruction not found");
		}
	}

/*
// TODO: comments
	public static void runLb(TypeLoad instruction) {
		reg[btiu(instruction.getRd())] = btis(loadMem((reg[btiu(instruction.getRs1())] + btis(instruction.getImm12(), 12)), 8), 8);
		pc = pc + 32;
	}

	public static void runLh(TypeLoad instruction) {
		reg[btiu(instruction.getRd())] = btis(loadMem((reg[btiu(instruction.getRs1())] + btis(instruction.getImm12(), 12)), 16), 16);
		pc = pc + 32;
	}

	public static void runLw(TypeLoad instruction) {
		reg[btiu(instruction.getRd())] = btis(loadMem((reg[btiu(instruction.getRs1())] + btis(instruction.getImm12(), 12)), 32), 32);
		pc = pc + 32;
	}

	public static void runLbu(TypeLoad instruction) {
		reg[btiu(instruction.getRd())] = btiu(loadMem((reg[btiu(instruction.getRs1())] + btis(instruction.getImm12(), 8)), 8)); // TODO: ? first 8
		pc = pc + 32;
	}

	public static void runLhu(TypeLoad instruction) {
		reg[btiu(instruction.getRd())] = btiu(loadMem((reg[btiu(instruction.getRs1())] + btis(instruction.getImm12(), 12)), 16));
		pc = pc + 32;
	}

	// TODO: change int sizes to be more optimized
*/
	public static void runTypeLoad(TypeLoad instruction) {
		/*
		HashMap<Integer, Runnable> runTypeLoad = new HashMap<>() {{ // TODO: move this
			put(0, () -> runLb(instruction));
			put(1, () -> runLh(instruction));
			put(2, () -> runLw(instruction));
			put(4, () -> runLbu(instruction));
			put(5, () -> runLhu(instruction));
		}};

		Runnable function = runTypeLoad.get(btiu(instruction.getFunct3()));
		if (function != null) {
			function.run();
		} else {
			System.err.println("Error: Type Load instruction not found");
		}
		*/
		HashMap<Integer, Integer> typeLoadSizes = new HashMap<>() {{ // TODO: move this
			put(0, 16);
			put(1, 32);
			put(2, 8);
			put(4, 8);
			put(5, 16);
		}};

		int size = typeLoadSizes.get(btiu(instruction.getFunct3()));
		int rd = btiu(instruction.getRd());
		int rs1 = btiu(instruction.getRs1());
		int imm12 = btis(instruction.getImm12(), 12);
		
		reg[rd] = btis(loadMem((reg[rs1] + imm12), size), size);
		pc = pc + 32;		
	}

	/*
	public static void runSb(TypeS instruction) {
		storeMem(getReg(btiu(instruction.getRs2()), 8), reg[btiu(instruction.getRs1())] + btis(instruction.getImm12(), 12));
		pc = pc + 32;
	}

	public static void runSh(TypeS instruction) {
		storeMem(getReg(btiu(instruction.getRs2()), 16), reg[btiu(instruction.getRs1())] + btis(instruction.getImm12(), 12));
		pc = pc + 32;
	}

	public static void runSw(TypeS instruction) {
		storeMem(getReg(btiu(instruction.getRs2()), 32), reg[btiu(instruction.getRs1())] + btis(instruction.getImm12(), 12));
		pc = pc + 32;
	}
	*/

	public static void runTypeS(TypeS instruction) {
		/*
		HashMap<Integer, Runnable> runTypeS = new HashMap<>() {{ // TODO: move this
			put(0, () -> runSb(instruction));
			put(1, () -> runSh(instruction));
			put(2, () -> runSw(instruction));
		}};
		*/
		HashMap<Integer, Integer> typeStoreSizes = new HashMap<>() {{ // TODO: move this
			put(0, 8);
			put(1, 16);
			put(2, 32);
		}};

		int size = typeStoreSizes.get(btiu(instruction.getFunct3()));
		int rs1 = btiu(instruction.getRs1());
		int rs2 = btiu(instruction.getRs2());
		int imm12 = btis(instruction.getImm12(), 12);

		storeMem(getReg(rs2, size), reg[rs1] + imm12);
		pc = pc + 32;
	}

	// TODO: check public/private what to choose
	public static int srlisrai(TypeImm instruction, int rs1, int imm12) {
		if (!instruction.getInstr30().get(0)) {
			return reg[rs1] >>> imm12;
		} else {
			return reg[rs1] >> imm12;
		}
	}

	public static void runTypeImm(TypeImm instruction) {

		HashMap<Integer, Integer> typeImmOperations = new HashMap<>() {{ // TODO: move this

			int rs1 = btiu(instruction.getRs1());
			int imm12 = btis(instruction.getImm12(), 12);

			put(0, reg[rs1] + imm12); // addi
			put(1, reg[rs1] << imm12); // slli
			put(2, reg[rs1] < imm12 ? 1 : 0); // slti
			put(3, Math.abs(reg[rs1]) < Math.abs(imm12) ? 1 : 0); // slitu
			put(4, reg[rs1] ^ imm12); // xori
			put(5, srlisrai(instruction, rs1, imm12)); // srli / srai
			put(6, reg[rs1] | imm12); // ori
			put(7, reg[rs1] & imm12); // andi
		}};

		reg[btiu(instruction.getRd())] = typeImmOperations.get(btiu(instruction.getFunct3()));
		pc = pc + 32;
	}

	public static int addsub(TypeR instruction, int rs1, int rs2) {
		if (btiu(instruction.getFunct7()) == 0) {
			return reg[rs1] + reg[rs2];
		} else {
			return reg[rs1] - reg[rs2];
		}
	}

	public static int srlsra(TypeR instruction, int rs1, int rs2) {
		if (btiu(instruction.getFunct7()) == 0) { // srl
			return reg[rs1] >>> reg[rs2];
		} else { // sra
			return reg[rs1] >> reg[rs2];
		}
	}

	// TODO: fix that you acces btiu multiple tkimes when you can just do it int he beginnign of the funciton

	public static void runTypeR(TypeR instruction) {

		HashMap<Integer, Integer> typeROperations = new HashMap<>() {{ // TODO: move this

			int rs1 = btiu(instruction.getRs1());
			int rs2 = btiu(instruction.getRs2());

			put(0, addsub(instruction, rs1, rs2)); // addi
			put(1, reg[rs1] << reg[rs2]); // sll
			put(2, reg[rs1] < reg[rs2] ? 1 : 0); // slt
			put(3, Math.abs(reg[rs1]) < Math.abs(reg[rs2]) ? 1 : 0); // slit
			put(4, reg[rs1] ^ reg[rs2]); // xor
			put(5, srlsra(instruction, rs1, rs2)); // srl / sra
			put(6, reg[rs1] | reg[rs2]); // or
			put(7, reg[rs1] & reg[rs2]); // and
		}};

		reg[btiu(instruction.getRd())] = typeROperations.get(btiu(instruction.getFunct3()));
		pc = pc + 32;
	}

	public static void runInstruction(Instruction instruction) {
		
		HashMap<Class<? extends Instruction>, Runnable> run = new HashMap<>() {{ // TODO: Perhaps move this to the global scope
			put(TypeLui.class,   () -> runTypeLui((TypeLui) instruction));
			put(TypeAuipc.class, () -> runTypeAuipc((TypeAuipc) instruction));
			put(TypeJ.class,     () -> runTypeJ((TypeJ) instruction));
			put(TypeJalr.class,  () -> runTypeJalr((TypeJalr) instruction));
			put(TypeB.class,     () -> runTypeB((TypeB) instruction));
			put(TypeLoad.class,  () -> runTypeLoad((TypeLoad) instruction));
			put(TypeS.class,     () -> runTypeS((TypeS) instruction));
			put(TypeImm.class,   () -> runTypeImm((TypeImm) instruction));
			put(TypeR.class,     () -> runTypeR((TypeR) instruction));
		}};

		Runnable function = run.get(instruction.getClass());  // Get the Runnable based on the instruction's class
		if (function != null) {
			function.run();
		} else {
			System.out.println("Error: Instruction type not found");
		}

		/*
		// Runs the given instruction.
		
		if (instruction instanceof TypeLui) {
			TypeLui typeLui = (TypeLui) instruction;
			
			reg[btiu(((TypeLui) instruction).getRd())] = btis(bitExtension(typeLui.getImm20(), 32), 32) << 12; // rd <- imm_u
			pc = pc + 32; // pc <- pc + 16
		}
		
		if (instruction instanceof TypeAuipc) {
			TypeAuipc typeAuipc = (TypeAuipc) instruction;
			
			reg[btiu(((TypeAuipc) instruction).getRd())] = pc + btis(bitExtension(typeAuipc.getImm20(), 32), 32) << 12;; // rd <- pc + imm_u
			pc = pc + 32; // pc <- pc + 16
		}
		
		if (instruction instanceof TypeJ) {
			TypeJ typeJ = (TypeJ) instruction;
			
			reg[btiu(typeJ.getRd())] = pc + 16; // rd <- pc + 16
			pc = pc + btis(typeJ.getImm21(), 21); // pc <- pc + imm_j
		}
		
		if (instruction instanceof TypeJalr) {
			TypeJalr typeJalr = (TypeJalr) instruction;
			
			reg[btiu(typeJalr.getRd())] = pc + 32; // rd <- pc + 16
			pc = reg[btiu(typeJalr.getRs1())] + btis(typeJalr.getImm12(), 12); // pc < rs1 + imm_i
		}
		
		if (instruction instanceof TypeB) {
			TypeB typeB = (TypeB) instruction;
			
			if (btiu(typeB.getFunct3()) == 0) { // beq
				if (reg[btiu(typeB.getRs1())] == reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 32;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 1) { // bne
				if (reg[btiu(typeB.getRs1())] != reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 32;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 4) { // blt
				if (reg[btiu(typeB.getRs1())] < reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 32;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 5) { // bge				
				if (reg[btiu(typeB.getRs1())] >= reg[btiu(typeB.getRs2())]) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 32;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 6) { // btlu
				if (Math.abs(reg[btiu(((TypeB) instruction).getRs1())]) < Math.abs(reg[btiu(((TypeB) instruction).getRs2())])) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 32;
				}
			}
			
			if (btiu(typeB.getFunct3()) == 7) { // bgeu
				if (Math.abs(reg[btiu(((TypeB) instruction).getRs1())]) >= Math.abs(reg[btiu(((TypeB) instruction).getRs2())])) {
					pc = pc + btis(typeB.getImm13(), 13);
				} else {
					pc = pc + 32;
				}
			}
		}
		
		if (instruction instanceof TypeLoad typeLoad) {
			
			if (btiu(typeLoad.getFunct3()) == 0) { // lb
				reg[btiu(typeLoad.getRd())] = btis(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 12)), 8), 8);
				pc = pc + 32;
			}
			
			if (btiu(typeLoad.getFunct3()) == 1) { // lh
				reg[btiu(typeLoad.getRd())] = btis(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 12)), 16), 16);
				pc = pc + 32;
			}
			
			if (btiu(typeLoad.getFunct3()) == 2) { // lw
				reg[btiu(typeLoad.getRd())] = btis(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 12)), 32), 32);
				pc = pc + 32;
			}
			
			if (btiu(typeLoad.getFunct3()) == 4) { // lbu
				reg[btiu(typeLoad.getRd())] = btiu(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 8)), 8));
				pc = pc + 32;
			}
			
			if (btiu(typeLoad.getFunct3()) == 5) { // lhu
				reg[btiu(typeLoad.getRd())] = btiu(loadMem((reg[btiu(typeLoad.getRs1())] + btis(typeLoad.getImm12(), 12)), 16));
				pc = pc + 32;
			}
		}
		
		if (instruction instanceof TypeS) {
			TypeS typeS = (TypeS) instruction;
			
			if (btiu(typeS.getFunct3()) == 0) { // sb
				storeMem(getReg(btiu(typeS.getRs2()), 8), reg[btiu(typeS.getRs1())] + btis(typeS.getImm12(), 12));
				pc = pc + 32;
			}
			
			if (btiu(typeS.getFunct3()) == 1) { // sh
				storeMem(getReg(btiu(typeS.getRs2()), 16), reg[btiu(typeS.getRs1())] + btis(typeS.getImm12(), 12));
				pc = pc + 32;
			}
			
			if (btiu(typeS.getFunct3()) == 2) { // sw
				storeMem(getReg(btiu(typeS.getRs2()), 32), reg[btiu(typeS.getRs1())] + btis(typeS.getImm12(), 12));
				pc = pc + 32;
			}
		}
		
		if (instruction instanceof TypeImm) {
			TypeImm typeImm = (TypeImm) instruction;
			
			if (btiu(typeImm.getFunct3()) == 0) { // addi
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] + btis(typeImm.getImm12(), 12); // rd <- rs1 + imm12
				pc = pc + 32;
			}
			
			if (btiu(typeImm.getFunct3()) == 2) { // slti
				if (reg[btiu(typeImm.getRs1())] < btis(typeImm.getImm12(), 12)) {
					reg[btiu(typeImm.getRd())] = 1;
				} else {
					reg[btiu(typeImm.getRd())] = 0;
				}
				pc = pc + 32;
			}
			
			if (btiu(typeImm.getFunct3()) == 3) { // sltiu
				if (Math.abs(reg[btiu(typeImm.getRs1())]) < Math.abs(btis(typeImm.getImm12(), 12))) {
					reg[btiu(typeImm.getRd())] = 1;
				} else {
					reg[btiu(typeImm.getRd())] = 0;
				}
				pc = pc + 32;
			}
			
			if (btiu(typeImm.getFunct3()) == 4) { // xori
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] ^ btis(typeImm.getImm12(), 12);
				pc = pc + 32;
			}
			
			if (btiu(typeImm.getFunct3()) == 6) { // ori
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] | btis(typeImm.getImm12(), 12);
				pc = pc + 32;
			}
			
			if (btiu(typeImm.getFunct3()) == 7) { // andi
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] & btis(typeImm.getImm12(), 12);
				pc = pc + 32;
			}
			
			if (btiu(typeImm.getFunct3()) == 1) { // slli
				reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] << btis(typeImm.getImm12(), 12);
				pc = pc + 32;
			}
			
			if (btiu(typeImm.getFunct3()) == 5) { // srli / srai
				if (typeImm.getInstr30() == 0) { // srli
					reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] >>> btis(typeImm.getImm12(), 12);
				} else { // srai
					reg[btiu(typeImm.getRd())] = reg[btiu(typeImm.getRs1())] >> btis(typeImm.getImm12(), 12);
				}
				pc = pc + 32;
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
				
				pc = pc + 32;
			}
			
			if (btiu(typeR.getFunct3()) == 1) { // sll
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] << reg[btiu(typeR.getRs2())];
				pc = pc + 32;
			}
			
			if (btiu(typeR.getFunct3()) == 2) { // slt
				if (reg[btiu(typeR.getRs1())] < reg[btiu(typeR.getRs2())]) {
					reg[btiu(typeR.getRd())] = 1;
				} else {
					reg[btiu(typeR.getRd())] = 0;
				}
				pc = pc + 32;	
			}
			
			if (btiu(typeR.getFunct3()) == 3) { // sltu
				if (Math.abs(reg[btiu(typeR.getRs1())]) < Math.abs(reg[btiu(typeR.getRs2())])) {
					reg[btiu(typeR.getRd())] = 1;
				} else {
					reg[btiu(typeR.getRd())] = 0;
				}
				pc = pc + 32;		
			}
			
			if (btiu(typeR.getFunct3()) == 4) { // xor
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] ^ reg[btiu(typeR.getRs2())];
				pc = pc + 32;
			}
			
			if (btiu(typeR.getFunct3()) == 5) { // srl / sra
				if (btiu(typeR.getFunct7()) == 0) { // srl
					reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] >>> reg[btiu(typeR.getRs2())];
				} else { // sra
					reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] >> reg[btiu(typeR.getRs2())];
				}
				pc = pc + 32;
			}
			
			if (btiu(typeR.getFunct3()) == 6) { // or
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] | reg[btiu(typeR.getRs2())];
				pc = pc + 32;
			}
			
			if (btiu(typeR.getFunct3()) == 7) { // and
				reg[btiu(typeR.getRd())] = reg[btiu(typeR.getRs1())] & reg[btiu(typeR.getRs2())];
				pc = pc + 32;
			}	
		}
		*/
	}
	
	// Returns the specific instruction object (its type) via the given instruction's code operation.
	public static Instruction getInstrType(BitSet instr) {
		
		BitSet codeop = new BitSet(7); // Little endian
		for (int i = 0; i < 7; i++) {
			codeop.set(i, instr.get(i));
		}

		return codeopToInstr.get(btiu(codeop));
		
		/*
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
		*/
	}
	

	public static void fillTypeLui(BitSet instructionArray, Instruction instruction) {
		((TypeLui) instruction).setImm20(fillSegment(instructionArray, 12, 31));
		((TypeLui) instruction).setRd(fillSegment(instructionArray, 7, 11));
	}

	public static void fillTypeAuipc(BitSet instructionArray, Instruction instruction) {
		((TypeAuipc) instruction).setImm20(fillSegment(instructionArray, 12, 31));
		((TypeAuipc) instruction).setRd(fillSegment(instructionArray, 7, 11));
	}

	public static void fillTypeJ(BitSet instructionArray, Instruction instruction) {
		BitSet jImm21 = new BitSet(21);
		jImm21.set(0, false);

		// Fills the instruction according to the J Type bit structure
		int o = 21;
		for (int i = 1; i <= 10; i++) {
			jImm21.set(i, instructionArray.get(o));
			o++;
		}
		jImm21.set(11, instructionArray.get(20));
		o = 12;
		for (int i = 12; i <= 19; i++) {
			jImm21.set(i, instructionArray.get(o));
			o++;
		}
		jImm21.set(20, instructionArray.get(31));
		/*
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
		*/
		((TypeJ) instruction).setImm21(jImm21);
		((TypeJ) instruction).setRd(fillSegment(instructionArray, 7, 11));
	}

	public static void fillTypeJalr(BitSet instructionArray, Instruction instruction) {
		((TypeJalr) instruction).setImm12(fillSegment(instructionArray, 20, 31));
		((TypeJalr) instruction).setRs1(fillSegment(instructionArray, 15, 19));
		((TypeJalr) instruction).setFunct3(fillSegment(instructionArray, 12, 14));
		((TypeJalr) instruction).setRd(fillSegment(instructionArray, 7, 11));
	}

	public static void fillTypeB(BitSet instructionArray, Instruction instruction) {
		BitSet bImm13 = new BitSet(13);

		int o = 8;
		for (int i = 1; i <= 4; i++) {
			bImm13.set(i, instructionArray.get(o));
			o++;
		}
		o = 25;
		for (int i = 5; i <= 10; i++) {
			bImm13.set(i, instructionArray.get(o));
			o++;
		}
		bImm13.set(11, instructionArray.get(7));
		bImm13.set(12, instructionArray.get(31));
		/*
		bImm13.set(0, false);
		bImm13.set(1, instruction.get(8));
		bImm13.set(2, instruction.get(9));
		bImm13.set(3, instruction.get(10));
		bImm13.set(4, instruction.get(11));
		bImm13.set(5, instruction.get(25));
		bImm13.set(6, instruction.get(26));
		bImm13.set(7, instruction.get(27));
		bImm13.set(8, instruction.get(28));
		bImm13.set(9, instruction.get(29));
		bImm13.set(10, instruction.get(30));
		bImm13.set(11, instruction.get(7));
			bImm13.set(12, instruction.get(31));
		*/
		((TypeB) instruction).setImm13(bImm13);
		((TypeB) instruction).setRs2(fillSegment(instructionArray, 20, 24));
		((TypeB) instruction).setRs1(fillSegment(instructionArray, 15, 19));
		((TypeB) instruction).setFunct3(fillSegment(instructionArray, 12, 14));
	}

	public static void fillTypeLoad(BitSet instructionArray, Instruction instruction) {
		((TypeLoad) instruction).setImm12(fillSegment(instructionArray, 20, 31));
		((TypeLoad) instruction).setRs1(fillSegment(instructionArray, 15, 19));
		((TypeLoad) instruction).setFunct3(fillSegment(instructionArray, 12, 14));
		((TypeLoad) instruction).setRd(fillSegment(instructionArray, 7, 11));
	}

	public static void fillTypeS(BitSet instructionArray, Instruction instruction) {
		BitSet sImm12 = new BitSet(12);

		int o = 7;
		for (int i = 0; i <= 4; i++) {
			sImm12.set(i, instructionArray.get(o));
			o++;
			}

		o = 25;
		for (int i = 5; i <= 11; i++) {
			sImm12.set(i, instructionArray.get(o));
			o++;
		}
		/*
		sImm12.set(0, instruction.get(7));
		sImm12.set(1, instruction.get(8));
		sImm12.set(2, instruction.get(9));
		sImm12.set(3, instruction.get(10));
		sImm12.set(4, instruction.get(11));
		sImm12.set(5, instruction.get(25));
		sImm12.set(6, instruction.get(26));
		sImm12.set(7, instruction.get(27));
		sImm12.set(8, instruction.get(28));
		sImm12.set(9, instruction.get(29));
		sImm12.set(10, instruction.get(30));
		sImm12.set(11, instruction.get(31));
		*/
		((TypeS) instruction).setImm12(sImm12);
		((TypeS) instruction).setRs2(fillSegment(instructionArray, 20, 24));
		((TypeS) instruction).setRs1(fillSegment(instructionArray, 15, 19));
		((TypeS) instruction).setFunct3(fillSegment(instructionArray, 12, 14));
	}

	public static void fillTypeImm(BitSet instructionArray, Instruction instruction) {
		((TypeImm) instruction).setImm12(fillSegment(instructionArray, 20, 31));
		((TypeImm) instruction).setRs1(fillSegment(instructionArray, 15, 19));
		((TypeImm) instruction).setFunct3(fillSegment(instructionArray, 12, 14));
		((TypeImm) instruction).setRd(fillSegment(instructionArray, 7, 11));
		((TypeImm) instruction).setInstr30(instructionArray.get(30));
	}

	public static void fillTypeR(BitSet instructionArray, Instruction instruction) {
		((TypeR) instruction).setFunct7(fillSegment(instructionArray, 25, 31));
		((TypeR) instruction).setRs1(fillSegment(instructionArray, 15, 19));
		((TypeR) instruction).setRs2(fillSegment(instructionArray, 20, 24));
		((TypeR) instruction).setFunct3(fillSegment(instructionArray, 12, 14));
		((TypeR) instruction).setRd(fillSegment(instructionArray, 7, 11));
	}

	public static void fillTypeCallAtomic(BitSet instructionArray, Instruction instruction) {
		((TypeCallAtomic) instruction).setCsr12(fillSegment(instructionArray, 20, 31));
		((TypeCallAtomic) instruction).setRs1(fillSegment(instructionArray, 15, 19));
		((TypeCallAtomic) instruction).setFunct3(fillSegment(instructionArray, 12, 14));
		((TypeCallAtomic) instruction).setRd(fillSegment(instructionArray, 7, 11));
	}

	public static void fillInstr(BitSet instructionArray, Instruction instruction) {
		
		// Fills in the attributes of an instruction according to its 
		// instruction type.

		HashMap<Class<? extends Instruction>, Runnable> fillerMap = new HashMap<>() {{
			put(TypeLui.class,        () -> fillTypeLui(instructionArray,   	 (TypeLui) instruction));
			put(TypeAuipc.class,      () -> fillTypeAuipc(instructionArray, 	 (TypeAuipc) instruction));
			put(TypeJ.class,          () -> fillTypeJ(instructionArray, 		 (TypeJ) instruction));
			put(TypeJalr.class,       () -> fillTypeJalr(instructionArray, 		 (TypeJalr) instruction));
			put(TypeB.class,          () -> fillTypeB(instructionArray, 		 (TypeB) instruction));
			put(TypeLoad.class,       () -> fillTypeLoad(instructionArray, 		 (TypeLoad) instruction));
			put(TypeS.class,          () -> fillTypeS(instructionArray, 		 (TypeS) instruction));
			put(TypeImm.class,        () -> fillTypeImm(instructionArray, 		 (TypeImm) instruction));
			put(TypeR.class,          () -> fillTypeR(instructionArray, 		 (TypeR) instruction));
			put(TypeCallAtomic.class, () -> fillTypeCallAtomic(instructionArray, (TypeCallAtomic) instruction));
		}};

		Runnable filler = fillerMap.get(instruction.getClass());
		if (filler != null) {
			filler.run();
		} else {
			System.err.println("Error: Instruction instance not found");
		}
		/*
		if (instruction instanceof TypeLui) {
			(TypeLui) instruction.setImm20(fillSegment(instr, 12, 31));
			(TypeLui) instruction.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeAuipc) {
			(TypeAuipc) instruction.setImm20(fillSegment(instr, 12, 31));
			(TypeAuipc) instruction.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeJ) {
			BitSet jImm21 = new BitSet(21);
			jImm21.set(0, false);

			// Fills the instruction according to the J Type bit structure
			int o = 21;
			for (int i = 1; i <= 10; i++) {
				jImm21.set(i, instr.get(o));
				o++;
			}
			jImm21.set(11, instr.get(20));
			o = 12;
			for (int i = 11; i <= 20; i++) {
				jImm21.set(i, instr.get(o));
				o++;
			}

			jImm21.set(0), false);
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

			(TypeJ) instruction.setImm21(jImm21);
			(TypeJ) instruction.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeJalr) {	
			(TypeJalr) instruction.setImm12(fillSegment(instr, 20, 31));
			(TypeJalr) instruction.setRs1(fillSegment(instr, 15, 19));
			(TypeJalr) instruction.setFunct3(fillSegment(instr, 12, 14));
			(TypeJalr) instruction.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeB) {
			BitSet bImm13 = new BitSet(13);

			int o = 8;
			for (int i = 1; i <= 4; i++) {
				bImm13.set(i, instr.get(o));
				o++;
			}
			o = 25;
			for (int i = 5; i <= 10; i++) {
				bImm13.set(i, instr.get(o));
				o++;
			}
			bImm13.set(11, instr.get(7));
			bImm13.set(12, instr.get(31));

			
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
			

			(TypeB) instruction.setImm13(bImm13);
			(TypeB) instruction.setRs2(fillSegment(instr, 20, 24));
			(TypeB) instruction.setRs1(fillSegment(instr, 15, 19));
			(TypeB) instruction.setFunct3(fillSegment(instr, 12, 14));
		}
		
		if (instruction instanceof TypeLoad) {
			(TypeLoad) instruction.setImm12(fillSegment(instr, 20, 31));
			(TypeLoad) instruction.setRs1(fillSegment(instr, 15, 19));
			(TypeLoad) instruction.setFunct3(fillSegment(instr, 12, 14));
			(TypeLoad) instruction.setRd(fillSegment(instr, 7, 11));
		}
		
		if (instruction instanceof TypeS) {
			BitSet sImm12 = new BitSet(12);

			int o = 7;
			for (int i = 0; i <= 4; i++) {
				sImm12(i, instr.get(o));
			}

			int o = 25;
			for (int i = 5; i <= 11; i++) {
				sImm12(i, instr.get(o));
			}

			
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
		*/
	}
}
