package rv32i;

import instructions.*;
import java.util.BitSet;
import java.util.HashMap;
import utils.Utils;

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

	public static HashMap<Integer, Instruction> codeopToInstr = new HashMap<>() {{
        put(0b0110111, new TypeLui());
        put(0b0010111, new TypeAuipc());
        put(0b1101111, new TypeJ());
        put(0b1100111, new TypeJalr());
        put(0b1100011, new TypeB());
        put(0b0000011, new TypeLoad());
        put(0b0100011, new TypeS());
        put(0b0010011, new TypeImm());
        put(0b0110011, new TypeR());
        put(0b1110011, new TypeCallAtomic());
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
        int finish = address + size;

		int j = 0;
		for (int i = address; i < finish; i++) {
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
        int finish = address + segment.length();
		
		int j = 0;
		for (int i = address; i < finish; i++) {
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
			instruction.fill(instructionArray);
			instruction.run();
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
	
	// Returns the specific instruction object (its type) via the given instruction's code operation.
	public static Instruction getInstrType(BitSet instr) {
		
		BitSet codeop = new BitSet(7); // Little endian
		for (int i = 0; i < 7; i++) {
			codeop.set(i, instr.get(i));
		}

		return codeopToInstr.get(Utils.btiu(codeop));
	}
}
