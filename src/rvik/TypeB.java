package rvik;

public class TypeB {

	private int[] imm7;
	private int[] rs2;
	private int[] rs1;
	private int[] funct3;
	private int[] imm5;
	
	public int[] getImm7() {
		return imm7;
	}
	
	public void setImm7(int[] imm7) {
		this.imm7 = imm7;
	}
	
	public int[] getRs2() {
		return rs2;
	}
	
	public void setRs2(int[] rs2) {
		this.rs2 = rs2;
	}
	
	public int[] getRs1() {
		return rs1;
	}
	
	public void setRs1(int[] rs1) {
		this.rs1 = rs1;
	}
	
	public int[] getFunct3() {
		return funct3;
	}
	
	public void setFunct3(int[] funct3) {
		this.funct3 = funct3;
	}
	
	public int[] getImm5() {
		return imm5;
	}
	
	public void setImm5(int[] imm5) {
		this.imm5 = imm5;
	}

	public TypeB(int[] imm7, int[] rs2, int[] rs1, int[] funct3, int[] imm5) {
		super();
		this.imm7 = imm7;
		this.rs2 = rs2;
		this.rs1 = rs1;
		this.funct3 = funct3;
		this.imm5 = imm5;
	}
	
	public TypeB() {
		super();
		this.imm7 = new int[7];
		this.rs2 = new int[5];
		this.rs1 = new int[5];
		this.funct3 = new int[3];
		this.imm5 = new int[5];
	}

}
