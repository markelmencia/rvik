package rvik;

public class TypeR extends Instruction {

	private int[] funct7;
	private int[] rs1;
	private int[] rs2;
	private int[] funct3;
	private int[] rd;
	
	public int[] getFunct7() {
		return funct7;
	}
	
	public int[] getRs1() {
		return rs1;
	}
	
	public int[] getRs2() {
		return rs2;
	}
	
	public int[] getFunct3() {
		return funct3;
	}
	
	public int[] getRd() {
		return rd;
	}

	public void setFunct7(int[] funct7) {
		this.funct7 = funct7;
	}

	public void setRs1(int[] rs1) {
		this.rs1 = rs1;
	}

	public void setRs2(int[] rs2) {
		this.rs2 = rs2;
	}

	public void setFunct3(int[] funct3) {
		this.funct3 = funct3;
	}

	public void setRd(int[] rd) {
		this.rd = rd;
	}
	
	
	public TypeR(int[] funct7, int[] rs1, int[] rs2, int[] funct3, int[] rd) {
		super();
		this.funct7 = funct7;
		this.rs1 = rs1;
		this.rs2 = rs2;
		this.funct3 = funct3;
		this.rd = rd;
	}

	public TypeR() {
		super();
		this.funct7 = new int[7];
		this.rs1 = new int[5];
		this.rs2 = new int[5];
		this.funct3 = new int[3];
		this.rd = new int[5];
	}

}
