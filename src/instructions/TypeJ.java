package instructions;

import java.util.BitSet;

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
}
