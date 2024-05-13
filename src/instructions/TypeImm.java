package instructions;

import java.util.BitSet;

public class TypeImm extends Instruction {

	private BitSet imm12;
	private BitSet rs1;
	private BitSet funct3;
	private BitSet rd;
	private int instr30;
	
	public BitSet getImm12() {
		return imm12;
	}
	
	public void setImm12(BitSet imm12) {
		this.imm12 = imm12;
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
	
	public BitSet getRd() {
		return rd;
	}
	
	public void setRd(BitSet rd) {
		this.rd = rd;
	}
	
	public int getInstr30() {
		return instr30;
	}
	
	public void setInstr30(boolean instr30) {
		if (instr30) {
			this.instr30 = 1;
		} else {
			this.instr30 = 0;
		}
	}

	public TypeImm(BitSet instruction, BitSet imm12, BitSet rs1, BitSet funct3, BitSet rd, int instr30) {
		super();
		this.imm12 = imm12;
		this.rs1 = rs1;
		this.funct3 = funct3;
		this.rd = rd;
		this.instr30 = instr30;
	}
	
	public TypeImm() {
		super();
		this.imm12 = new BitSet(12);
		this.rs1 = new BitSet(5);
		this.funct3 = new BitSet(3);
		this.rd = new BitSet(5);
		this.instr30 = 1;
	}
}
