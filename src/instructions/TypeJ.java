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
}
