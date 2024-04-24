package rvik;

public class TypeJ {

	private int[] imm;
	private int[] rs1;
	private int[] funct3;
	private int[] rd;
	
	public int[] getImm() {
		return imm;
	}
	
	public void setImm(int[] imm) {
		this.imm = imm;
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
	
	public int[] getRd() {
		return rd;
	}
	
	public void setRd(int[] rd) {
		this.rd = rd;
	}

	public TypeJ(int[] imm, int[] rs1, int[] funct3, int[] rd) {
		super();
		this.imm = imm;
		this.rs1 = rs1;
		this.funct3 = funct3;
		this.rd = rd;
	}
	
	public TypeJ() {
		super();
		this.imm = new int[12];
		this.rs1 = new int[5];
		this.funct3 = new int[3];
		this.rd = new int[5];
		
	}
	
}
