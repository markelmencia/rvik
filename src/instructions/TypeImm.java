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

	private static final HashMap<String, Byte> stringToFunct3 = new HashMap<>() {{
		put("addi", (byte) 0);
		put("slti", (byte) 2);
		put("sltiu", (byte) 3);
		put("xori", (byte) 4);
		put("ori", (byte) 6);
		put("andi", (byte) 7);
		put("slli", (byte) 1);
		put("srli", (byte) 5);
		put("srai", (byte) 5);
	}};
	
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

	@Override
	public void fill(BitSet instructionArray) {
		this.imm12 = Utils.fillSegment(instructionArray, 20, 31);
		this.rs1 = Utils.fillSegment(instructionArray, 15, 19);
		this.funct3 = Utils.fillSegment(instructionArray, 12, 14);
		this.rd = Utils.fillSegment(instructionArray, 7, 11);
		this.setInstr30(instructionArray.get(30));
	}


	public static BitSet assemble(String[] instructionSplit) {
		BitSet result = new BitSet(32);
		// opcode
		result.or(BitSet.valueOf(new long[]{0b0010011}));

		// rd
		long rd = Integer.parseInt(instructionSplit[1]);
		result.or(BitSet.valueOf(new long[]{rd << 7}));

		// funct3
		byte funct3 = stringToFunct3.get(instructionSplit[0]);
		result.or(BitSet.valueOf(new long[]{funct3 << 12}));

		// rs1
		int rs1 = Integer.parseInt(instructionSplit[2]);
		result.or(BitSet.valueOf(new long[]{(long) rs1 << 15}));

		// imm12
		int imm12 = Integer.parseInt(instructionSplit[3]);
		result.or(BitSet.valueOf(new long[]{(long) imm12 << 20}));

		// instr30
		if (instructionSplit[0].equals("srai")) {
			result.set(30);
		}

		return result;
	}
}
