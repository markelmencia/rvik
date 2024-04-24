package rvik;

public class TypeAuipc {

	private int[] imm;
	private int[] rd;
	
	public int[] getImm() {
		return imm;
	}
	
	public void setImm(int[] imm) {
		this.imm = imm;
	}
	
	public int[] getRd() {
		return rd;
	}
	
	public void setRd(int[] rd) {
		this.rd = rd;
	}

	public TypeAuipc(int[] imm, int[] rd) {
		super();
		this.imm = imm;
		this.rd = rd;
	}
	
	public TypeAuipc() {
		super();
		this.imm = new int[20];
		this.rd = new int[5];
	}

}
