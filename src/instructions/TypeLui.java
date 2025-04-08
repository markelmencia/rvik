package instructions;
import java.util.BitSet;
import rv32i.Compiler;
import utils.Utils;

public class TypeLui extends Instruction {

	private BitSet imm20;
	private BitSet rd;
	
	public BitSet getImm20() {
		return imm20;
	}
	
	public void setImm20(BitSet imm20) {
		this.imm20 = imm20;
	}
	
	public BitSet getRd() {
		return rd;
	}
	
	public void setRd(BitSet rd) {
		this.rd = rd;
	}

	public TypeLui(BitSet imm20, BitSet rd) {
		super();
		this.imm20 = imm20;
		this.rd = rd;
	}
	
	public TypeLui() {
		super();
		this.imm20 = new BitSet(20);
		this.rd = new BitSet(5);
	}

	@Override
	public void run() {
		int rd = Utils.btiu(this.rd);
		int imm20 = Utils.btis(Utils.bitExtension(this.imm20, 32), 32);

		Compiler.reg[rd] = imm20 << 12; // rd <- imm_u
		Compiler.pc = Compiler.pc + 32;
	}

	@Override
	public void fill(BitSet instructionArray) {
		this.imm20 = Utils.fillSegment(instructionArray, 12, 31);
		this.rd = Utils.fillSegment(instructionArray, 7, 11);
	}


	public static BitSet assemble(String[] instructionSplit) {
		BitSet result = new BitSet(32);
		// opcode
		result.or(BitSet.valueOf(new long[]{0b0110111}));

		// rd
		long rd = Integer.parseInt(instructionSplit[1]);
		result.or(BitSet.valueOf(new long[]{rd << 7}));

		// imm20
		long imm20 = Integer.parseInt(instructionSplit[2]);
		result.or(BitSet.valueOf(new long[]{imm20 << 12}));

		return result;
	}
}
