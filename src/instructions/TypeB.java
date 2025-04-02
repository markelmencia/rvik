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

	public BitSet getImm13() {
		return imm13;
	}
	
	public void setImm13(BitSet imm13) {
		this.imm13 = imm13;
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
}
