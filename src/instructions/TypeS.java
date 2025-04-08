package instructions;

import java.util.BitSet;
import java.util.HashMap;
import utils.Utils;
import rv32i.Compiler;

public class TypeS extends Instruction {

	private BitSet imm12;
	private BitSet rs2;
	private BitSet rs1;
	private BitSet funct3;

	private static final HashMap<String, Byte> stringToFunct3 = new HashMap<>() {{
		put("sb", (byte) 0);
		put("sh", (byte) 1);
		put("sw", (byte) 2);
	}};

	public BitSet getImm12() {
		return imm12;
	}

	public void setImm12(BitSet imm12) {
		this.imm12 = imm12;
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

	public TypeS(BitSet imm12, BitSet rs2, BitSet rs1, BitSet funct3) {
		super();
		this.imm12 = imm12;
		this.rs2 = rs2;
		this.rs1 = rs1;
		this.funct3 = funct3;
	}

	public TypeS() {
		super();
		this.imm12 = new BitSet(7);
		this.rs2 = new BitSet(5);
		this.rs1 = new BitSet(5);
		this.funct3 = new BitSet(3);
	}

	@Override
	public void run() {
		final HashMap<Integer, Integer> typeStoreSizes = new HashMap<>() {{
			put(0, 8);
			put(1, 16);
			put(2, 32);
		}};

		int size = typeStoreSizes.get(Utils.btiu(this.funct3));
		int rs1 = Utils.btiu(this.rs1);
		int rs2 = Utils.btiu(this.rs2);
		int imm12 = Utils.btis(this.imm12, 12);

		Compiler.storeMem(Compiler.getReg(rs2, size), Compiler.reg[rs1] + imm12);
		Compiler.pc = Compiler.pc + 32;
	}

	@Override
	public void fill(BitSet instructionArray) {
		BitSet sImm12 = new BitSet(12);

		int o = 7;
		for (int i = 0; i <= 4; i++) {
			sImm12.set(i, instructionArray.get(o));
			o++;
		}

		o = 25;
		for (int i = 5; i <= 11; i++) {
			sImm12.set(i, instructionArray.get(o));
			o++;
		}

		this.imm12 = sImm12;
		this.rs2 = Utils.fillSegment(instructionArray, 20, 24);
		this.rs1 = Utils.fillSegment(instructionArray, 15, 19);
		this.funct3 = Utils.fillSegment(instructionArray, 12, 14);
	}

	public static BitSet assemble(String[] instructionSplit) {
		BitSet result = new BitSet(32);
		// opcode
		result.or(BitSet.valueOf(new long[]{0b0110111}));

		// imm12
		int imm12 = Integer.parseInt(instructionSplit[2]);
		BitSet imm12Array = BitSet.valueOf(new long[]{imm12});
		int o = 0;
		for (int i = 7; i <= 11; i++) {
			result.set(i, imm12Array.get(o));
			o++;
		}
		// funct3
		byte funct3 = stringToFunct3.get(instructionSplit[0]);
		result.or(BitSet.valueOf(new long[]{funct3 << 12}));

		// rs1
		if (instructionSplit.length != 3) {
			int rs1 = Integer.parseInt(instructionSplit[3]);
			result.or(BitSet.valueOf(new long[]{(long) rs1 << 15}));
		}

		// rs2
		int rs2 = Integer.parseInt(instructionSplit[1]);
		result.or(BitSet.valueOf(new long[]{(long) rs2 << 20}));

		// imm7
		for (int i = 25; i <= 30; i++) {
			result.set(i, imm12Array.get(o));
		}

		result.set(31, imm12Array.get(11));

		return result;
	}
}
