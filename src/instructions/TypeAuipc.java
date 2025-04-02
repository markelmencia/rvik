package instructions;
import java.util.BitSet;
import utils.Utils;
import rv32i.Compiler;

import static utils.Utils.*;

public class TypeAuipc extends Instruction {

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

	public TypeAuipc(BitSet imm20, BitSet rd) {
		super();
		this.imm20 = imm20;
		this.rd = rd;
	}
	
	public TypeAuipc() {
		super();
		this.imm20 = new BitSet(20);
		this.rd = new BitSet(5);
	}

	@Override
	public void run() {
		int rd = btiu(this.rd);
		int imm20 = btis(bitExtension(this.imm20, 32), 32);

		Compiler.reg[rd] = Compiler.pc + (imm20 << 12); // rd <- pc + imm_u
		Compiler.pc = Compiler.pc + 32;
	}

	@Override
	public void fill(BitSet instructionArray) {
		this.imm20 = fillSegment(instructionArray, 12, 31);
		this.rd = fillSegment(instructionArray, 7, 11);
	}
}
