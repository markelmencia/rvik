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
		int imm20 = Utils.btis(Compiler.bitExtension(this.imm20, 32), 32);

		Compiler.reg[rd] = imm20 << 12; // rd <- imm_u
		Compiler.pc = Compiler.pc + 32;
	}
}
