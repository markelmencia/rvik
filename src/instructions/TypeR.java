package instructions;

import java.util.BitSet;

public class TypeR extends Instruction {

	private BitSet funct7;
	private BitSet rs1;
	private BitSet rs2;
	private BitSet funct3;
	private BitSet rd;
	
	public BitSet getFunct7() {
		return funct7;
	}
	
	public BitSet getRs1() {
		return rs1;
	}
	
	public BitSet getRs2() {
		return rs2;
	}
	
	public BitSet getFunct3() {
		return funct3;
	}
	
	public BitSet getRd() {
		return rd;
	}

	public void setFunct7(BitSet funct7) {
		this.funct7 = funct7;
	}

	public void setRs1(BitSet rs1) {
		this.rs1 = rs1;
	}

	public void setRs2(BitSet rs2) {
		this.rs2 = rs2;
	}

	public void setFunct3(BitSet funct3) {
		this.funct3 = funct3;
	}

	public void setRd(BitSet rd) {
		this.rd = rd;
	}
	
	
	public TypeR(BitSet funct7, BitSet rs1, BitSet rs2, BitSet funct3, BitSet rd) {
		super();
		this.funct7 = funct7;
		this.rs1 = rs1;
		this.rs2 = rs2;
		this.funct3 = funct3;
		this.rd = rd;
	}

	public TypeR() {
		super();
		this.funct7 = new BitSet(7);
		this.rs1 = new BitSet(5);
		this.rs2 = new BitSet(5);
		this.funct3 = new BitSet(3);
		this.rd = new BitSet(5);
	}
}
