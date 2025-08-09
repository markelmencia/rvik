package instructions;

import utils.Utils;

import java.util.BitSet;

public class TypeCallAtomic extends Instruction {

	private BitSet csr12;
	private BitSet rs1;
	private BitSet funct3;
	private BitSet rd;
	
	public BitSet getCsr12() {
		return csr12;
	}

    public BitSet getRs1() {
		return rs1;
	}

    public BitSet getFunct3() {
		return funct3;
	}

    public BitSet getRd() {
		return rd;
	}

    public TypeCallAtomic() {
		super();
		this.csr12 = new BitSet(12);
		this.rs1 = new BitSet(5);
		this.funct3 = new BitSet(3);
		this.rd = new BitSet(5);
	}

    @Override
	public void run() {

	}

	@Override
	public void fill(BitSet instructionArray) {
		this.csr12 = Utils.fillSegment(instructionArray, 20, 31);
		this.rs1 = Utils.fillSegment(instructionArray, 15, 19);
		this.funct3 = Utils.fillSegment(instructionArray, 12, 14);
		this.rd = Utils.fillSegment(instructionArray, 7, 11);
	}
}
