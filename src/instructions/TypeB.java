package instructions;

import java.util.BitSet;
import java.util.HashMap;

import rv32i.Compiler;
import utils.Utils;

public class TypeB extends Instruction {

	private BitSet imm13;
	private BitSet rs2;
	private BitSet rs1;
	private BitSet funct3;

	private final HashMap<Integer, Runnable> runTypeB = new HashMap<>() {{
		put(0, () -> runBeq(TypeB.this));
		put(1, () -> runBne(TypeB.this));
		put(4, () -> runBlt(TypeB.this));
		put(5, () -> runBge(TypeB.this));
		put(6, () -> runBltu(TypeB.this));
		put(7, () -> runBgeu(TypeB.this));
	}};

	private static final HashMap<String, Byte> stringToFunct3 = new HashMap<>() {{
		put("beq", (byte) 0);
		put("bne", (byte) 1);
		put("blt", (byte) 4);
		put("bge", (byte) 5);
		put("bltu", (byte) 6);
		put("bgeu", (byte) 7);
	}};

	public BitSet getImm13() {
		return imm13;
	}

    public BitSet getRs2() {
		return rs2;
	}

    public BitSet getRs1() {
		return rs1;
	}

    public BitSet getFunct3() {
		return funct3;
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


	private static void runBeq(TypeB instruction) {
		int rs1 = Utils.btiu(instruction.getRs1());
		int rs2 = Utils.btiu(instruction.getRs2());
		int imm13 = Utils.btis(instruction.getImm13(), 13);

		if (Compiler.reg[rs1] == Compiler.reg[rs2]) {
			Compiler.pc = Compiler.pc + imm13;
		} else {
			Compiler.pc = Compiler.pc + 32;
		}
	}

	private static void runBne(TypeB instruction) {
		int rs1 = Utils.btiu(instruction.getRs1());
		int rs2 = Utils.btiu(instruction.getRs2());
		int imm13 = Utils.btis(instruction.getImm13(), 13);

		if (Compiler.reg[rs1] != Compiler.reg[rs2]) {
			Compiler.pc = Compiler.pc + imm13;
		} else {
			Compiler.pc = Compiler.pc + 32;
		}
	}

	private static void runBlt(TypeB instruction) {
		int rs1 = Utils.btiu(instruction.getRs1());
		int rs2 = Utils.btiu(instruction.getRs2());
		int imm13 = Utils.btis(instruction.getImm13(), 13);

		if (Compiler.reg[rs1] < Compiler.reg[rs2]) {
			Compiler.pc = Compiler.pc + imm13;
		} else {
			Compiler.pc = Compiler.pc + 32;
		}
	}

	private static void runBge(TypeB instruction) {
		int rs1 = Utils.btiu(instruction.getRs1());
		int rs2 = Utils.btiu(instruction.getRs2());
		int imm13 = Utils.btis(instruction.getImm13(), 13);

		if (Compiler.reg[rs1] >= Compiler.reg[rs2]) {
			Compiler.pc = Compiler.pc + imm13;
		} else {
			Compiler.pc = Compiler.pc + 32;
		}
	}

	private static void runBltu(TypeB instruction) {
		int rs1 = Utils.btiu(instruction.getRs1());
		int rs2 = Utils.btiu(instruction.getRs2());
		int imm13 = Utils.btis(instruction.getImm13(), 13);

		if (Math.abs(Compiler.reg[rs1]) < Math.abs(Compiler.reg[rs2])) {
			Compiler.pc = Compiler.pc + imm13;
		} else {
			Compiler.pc = Compiler.pc + 32;
		}
	}

	private static void runBgeu(TypeB instruction) {
		int rs1 = Utils.btiu(instruction.getRs1());
		int rs2 = Utils.btiu(instruction.getRs2());
		int imm13 = Utils.btis(instruction.getImm13(), 13);

		if (Math.abs(Compiler.reg[rs1]) >= Math.abs(Compiler.reg[rs2])) {
			Compiler.pc = Compiler.pc + imm13;
		} else {
			Compiler.pc = Compiler.pc + 32;
		}
	}

	@Override
	public void run() {
		Runnable function = runTypeB.get(Utils.btiu(this.funct3));
		if (function != null) {
			function.run();
		} else {
			System.err.println("Error: Type B instruction not found");
		}
	}

	@Override
	public void fill(BitSet instructionArray) {
		BitSet bImm13 = new BitSet(13);

		int o = 8;
		for (int i = 1; i <= 4; i++) {
			bImm13.set(i, instructionArray.get(o));
			o++;
		}
		o = 25;
		for (int i = 5; i <= 10; i++) {
			bImm13.set(i, instructionArray.get(o));
			o++;
		}
		bImm13.set(11, instructionArray.get(7));
		bImm13.set(12, instructionArray.get(31));
		this.imm13 = bImm13;
		this.rs2 = Utils.fillSegment(instructionArray, 20, 24);
		this.rs1 = Utils.fillSegment(instructionArray, 15, 19);
		this.funct3 = Utils.fillSegment(instructionArray, 12, 14);
	}

	public static BitSet assemble(String[] instructionSplit) {
		BitSet result = new BitSet(32);
		// opcode
		result.or(BitSet.valueOf(new long[]{0b1100011}));

		int imm12 = Integer.parseInt(instructionSplit[3]);
		BitSet imm12Array = BitSet.valueOf(new long[]{imm12});

		// imm5
		result.set(7, imm12Array.get(11));
		int o = 1;
		for (int i = 8; i <= 11; i++) {
			result.set(i, imm12Array.get(o));
			o++;
		}

		// funct3
		byte funct3 = stringToFunct3.get(instructionSplit[0]);
		result.or(BitSet.valueOf(new long[]{funct3 << 12}));

		// rs1
		int rs1 = Integer.parseInt(instructionSplit[1]);
		result.or(BitSet.valueOf(new long[]{(long) rs1 << 15}));

		// rs2
		int rs2 = Integer.parseInt(instructionSplit[2]);
		result.or(BitSet.valueOf(new long[]{(long) rs2 << 20}));

		// imm7
		o = 5;
		for (int i = 25; i <= 30; i++) {
			result.set(i, imm12Array.get(o));
			o++;
		}

		result.set(31, imm12Array.get(12));

		return result;
	}
}
