package instructions;

import java.util.BitSet;
import utils.Utils;
import rv32i.Compiler;

public class TypeJ extends Instruction {

	private BitSet imm21;
	private BitSet rd;
	
	public BitSet getImm21() {
		return imm21;
	}
	
	public void setImm21(BitSet imm21) {
		this.imm21 = imm21;
	}
	
	public BitSet getRd() {
		return rd;
	}
	
	public void setRd(BitSet rd) {
		this.rd = rd;
	}

	public TypeJ(BitSet imm21, BitSet rd) {
		super();
		this.imm21 = imm21;
		this.rd = rd;
	}
	
	public TypeJ() {
		super();
		this.imm21 = new BitSet(21);
		this.rd = new BitSet(5);
	}

	@Override
	public void run() {
		int rd = Utils.btiu(this.rd);
		int imm21 = Utils.btis(this.imm21, 21);

		Compiler.reg[rd] = Compiler.pc + 32; // rd <- pc + 32
		Compiler.pc = Compiler.pc + imm21; // pc <- pc + imm_j
	}

	@Override
	public void fill(BitSet instructionArray) {
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

		this.imm21 = jImm21;
		this.rd = Utils.fillSegment(instructionArray, 7, 11);
	}

	public static BitSet assemble(String[] instructionSplit) {
		BitSet result = new BitSet(32);
		// opcode
		result.or(BitSet.valueOf(new long[]{0b1101111}));

		// rd
		long rd = Integer.parseInt(instructionSplit[1]);
		result.or(BitSet.valueOf(new long[]{rd << 7}));

		// imm21
		int imm21 = Integer.parseInt(instructionSplit[2]);
		BitSet imm21Array = BitSet.valueOf(new long[]{imm21});

		for (int i = 12; i <= 19; i++) {
			result.set(i, imm21Array.get(i));
		}

		result.set(20, imm21Array.get(11));

		int o = 1;
		for (int i = 21; i <= 30; i++) {
			result.set(i, imm21Array.get(o));
			o++;
		}

		result.set(31, imm21Array.get(20));

		return result;
	}
}
