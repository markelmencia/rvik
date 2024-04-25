package rvik;

public class TypeJ extends Instruction {

	private int[] imm20;
	private int[] rd;
	
	public int[] getImm20() {
		return imm20;
	}
	
	public void setImm20(int[] imm20) {
		this.imm20 = imm20;
	}
	
	public int[] getRd() {
		return rd;
	}
	
	public void setRd(int[] rd) {
		this.rd = rd;
	}

	public TypeJ(int[] imm20, int[] rd) {
		super();
		this.imm20 = imm20;
		this.rd = rd;
	}
	
	public TypeJ() {
		super();
		this.imm20 = new int[20];
		this.rd = new int[5];
	}
}
