package instructions;

import java.util.BitSet;
import java.util.HashMap;
import utils.Utils;
import rv32i.Compiler;

public class TypeImm extends Instruction {

	private BitSet imm12;
	private BitSet rs1;
	private BitSet funct3;
	private BitSet rd;
	private BitSet instr30;

	
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

	public BitSet getInstr30() {
		return instr30;
	}

	public void setInstr30(boolean instr30) {
		if (instr30) {
			this.instr30.set(0);
		} else {
			this.instr30.clear();
		}
	}

	public TypeImm(BitSet imm12, BitSet rs1, BitSet funct3, BitSet rd, BitSet instr30) {
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
		this.instr30 = new BitSet(1);
	}

	public static int srlisrai(TypeImm instruction, int rs1, int imm12) {
		if (!instruction.getInstr30().get(0)) {
			return Compiler.reg[rs1] >>> imm12;
		} else {
			return Compiler.reg[rs1] >> imm12;
		}
	}

	@Override
	public void run() {

		final HashMap<Integer, Integer> typeImmOperations = new HashMap<>() {{

			int rs1 = Utils.btiu(TypeImm.this.rs1);
			int imm12 = Utils.btis(TypeImm.this.imm12, 12);

			put(0, Compiler.reg[rs1] + imm12); // addi
			put(1, Compiler.reg[rs1] << imm12); // slli
			put(2, Compiler.reg[rs1] < imm12 ? 1 : 0); // slti
			put(3, Math.abs(Compiler.reg[rs1]) < Math.abs(imm12) ? 1 : 0); // slitu
			put(4, Compiler.reg[rs1] ^ imm12); // xori
			put(5, srlisrai(TypeImm.this, rs1, imm12)); // srli / srai
			put(6, Compiler.reg[rs1] | imm12); // ori
			put(7, Compiler.reg[rs1] & imm12); // andi
		}};

		Compiler.reg[Utils.btiu(this.rd)] = typeImmOperations.get(Utils.btiu(this.funct3));
		Compiler.pc = Compiler.pc + 32;
	}
}
