package instructions;

import java.util.BitSet;

public class TypeCallAtomic extends Instruction {

	private BitSet csr12;
	private BitSet rs1;
	private BitSet funct3;
	private BitSet rd;
	
	public BitSet getCsr12() {
		return csr12;
	}
	
	public void setCsr12(BitSet csr12) {
		this.csr12 = csr12;
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

	public TypeCallAtomic() {
		super();
		this.csr12 = new BitSet(12);
		this.rs1 = new BitSet(5);
		this.funct3 = new BitSet(3);
		this.rd = new BitSet(5);
	}
	
	public TypeCallAtomic(BitSet csr12, BitSet rs1, BitSet funct3, BitSet rd) {
		super();
		this.csr12 = csr12;
		this.rs1 = rs1;
		this.funct3 = funct3;
		this.rd = rd;
	}
	
	
	
}
