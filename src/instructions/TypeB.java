package instructions;

import java.util.BitSet;

public class TypeB extends Instruction {

	private BitSet imm13;
	private BitSet rs2;
	private BitSet rs1;
	private BitSet funct3;
	
	public BitSet getImm13() {
		return imm13;
	}
	
	public void setImm13(BitSet imm13) {
		this.imm13 = imm13;
	}
	
	public BitSet getRs2() {
		return rs2;
	}
	
	public void setRs2(BitSet rs2) {
		this.rs2 = rs2;
	}
	
	public BitSet getRs1() {
		return rs1;
	}
	
	public void setRs1(BitSet rs1) {
		this.rs1 = rs1;
	}
	
	public BitSet getFunct3() {
		return funct3;
	}
	
	public void setFunct3(BitSet funct3) {
		this.funct3 = funct3;
	}


	public TypeB(BitSet imm13, BitSet rs2, BitSet rs1, BitSet funct3) {
		super();
		this.imm13 = imm13;
		this.rs2 = rs2;
		this.rs1 = rs1;
		this.funct3 = funct3;
	}
	
	public TypeB() {
		super();
		this.imm13 = new BitSet(13);
		this.rs2 = new BitSet(5);
		this.rs1 = new BitSet(5);
		this.funct3 = new BitSet(3);
	}

}
