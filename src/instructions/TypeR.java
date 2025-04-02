package instructions;

import java.util.BitSet;
import java.util.HashMap;
import rv32i.Compiler;
import utils.Utils;

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

	private static int addsub(TypeR instruction, int rs1, int rs2) {
		if (Utils.btiu(instruction.getFunct7()) == 0) {
			return Compiler.reg[rs1] + Compiler.reg[rs2];
		} else {
			return Compiler.reg[rs1] - Compiler.reg[rs2];
		}
	}

	private static int srlsra(TypeR instruction, int rs1, int rs2) {
		if (Utils.btiu(instruction.getFunct7()) == 0) { // srl
			return Compiler.reg[rs1] >>> Compiler.reg[rs2];
		} else { // sra
			return Compiler.reg[rs1] >> Compiler.reg[rs2];
		}
	}

	@Override
	public void run() {
		HashMap<Integer, Integer> typeROperations = new HashMap<>() {{ // TODO: move this

			int rs1 = Utils.btiu(TypeR.this.getRs1());
			int rs2 = Utils.btiu(TypeR.this.getRs2());

			put(0, addsub(TypeR.this, rs1, rs2)); // addi
			put(1, Compiler.reg[rs1] << Compiler.reg[rs2]); // sll
			put(2, Compiler.reg[rs1] < Compiler.reg[rs2] ? 1 : 0); // slt
			put(3, Math.abs(Compiler.reg[rs1]) < Math.abs(Compiler.reg[rs2]) ? 1 : 0); // slit
			put(4, Compiler.reg[rs1] ^ Compiler.reg[rs2]); // xor
			put(5, srlsra(TypeR.this, rs1, rs2)); // srl / sra
			put(6, Compiler.reg[rs1] | Compiler.reg[rs2]); // or
			put(7, Compiler.reg[rs1] & Compiler.reg[rs2]); // and
		}};

		Compiler.reg[Utils.btiu(this.rd)] = typeROperations.get(Utils.btiu(this.rd));
		Compiler.pc = Compiler.pc + 32;
	}

	@Override
	public void fill(BitSet instructionArray) {
		this.funct7 = Utils.fillSegment(instructionArray, 25, 31);
		this.rs1 = Utils.fillSegment(instructionArray, 15, 19);
		this.rs2 = Utils.fillSegment(instructionArray, 20, 24);
		this.funct3 = Utils.fillSegment(instructionArray, 12, 14);
		this.rd = Utils.fillSegment(instructionArray, 7, 11);
	}
}
