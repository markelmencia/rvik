package instructions;

import java.util.BitSet;
import java.util.HashMap;
import rv32i.Compiler;
import utils.Utils;

public class TypeLoad extends Instruction {

	private BitSet imm12;
	private BitSet rs1;
	private BitSet funct3;
	private BitSet rd;

	private static final HashMap<String, Byte> stringToFunct3 = new HashMap<>() {{
		put("lb", (byte) 0);
		put("lh", (byte) 1);
		put("lw", (byte) 2);
		put("lbu", (byte) 4);
		put("lhu", (byte) 5);
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

	public TypeLoad(BitSet imm12, BitSet rs1, BitSet funct3, BitSet rd) {
		super();
		this.imm12 = imm12;
		this.rs1 = rs1;
		this.funct3 = funct3;
		this.rd = rd;
	}
	
	public TypeLoad() {
		super();
		this.imm12 = new BitSet(12);
		this.rs1 = new BitSet(5);
		this.funct3 = new BitSet(3);
		this.rd = new BitSet(5);
	}

	@Override
	public void run() {
		HashMap<Integer, Integer> typeLoadSizes = new HashMap<>() {{ // TODO: move this
			put(0, 16);
			put(1, 32);
			put(2, 8);
			put(4, 8);
			put(5, 16);
		}};

		int size = typeLoadSizes.get(Utils.btiu(this.funct3));
		int rd = Utils.btiu(this.rd);
		int rs1 = Utils.btiu(this.rs1);
		int imm12 = Utils.btis(this.imm12, 12);

		Compiler.reg[rd] = Utils.btis(Compiler.loadMem((Compiler.reg[rs1] + imm12), size), size);
		Compiler.pc = Compiler.pc + 32;
	}

	@Override
	public void fill(BitSet instructionArray) {
		this.imm12 = Utils.fillSegment(instructionArray, 20, 31);
		this.rs1 = Utils.fillSegment(instructionArray, 15, 19);
		this.funct3 = Utils.fillSegment(instructionArray, 12, 14);
		this.rd = Utils.fillSegment(instructionArray, 7, 11);
	}

	public static BitSet assemble(String[] instructionSplit) {
		BitSet result = new BitSet(32);
		// opcode
		result.or(BitSet.valueOf(new long[]{0b0110111}));

		// rd
		long rd = Integer.parseInt(instructionSplit[1]);
		result.or(BitSet.valueOf(new long[]{rd << 7}));

		// imm12
		int imm12 = Integer.parseInt(instructionSplit[2]);
		result.or(BitSet.valueOf(new long[]{(long) imm12 << 20}));

		// funct3
		byte funct3 = stringToFunct3.get(instructionSplit[0]);
		result.or(BitSet.valueOf(new long[]{(long) funct3 << 12}));

		// rs1
		if (instructionSplit.length != 3) {
			int rs1 = Integer.parseInt(instructionSplit[3]);
			result.or(BitSet.valueOf(new long[]{(long) rs1 << 15}));
		}

		return result;
	}
}
