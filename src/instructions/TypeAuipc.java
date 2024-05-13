package instructions;
import java.util.BitSet;

public class TypeAuipc extends Instruction {

	private BitSet imm20;
	private BitSet rd;
	
	public BitSet getImm20() {
		return imm20;
	}
	
	public void setImm20(BitSet imm20) {
		this.imm20 = imm20;
	}
	
	public BitSet getRd() {
		return rd;
	}
	
	public void setRd(BitSet rd) {
		this.rd = rd;
	}

	public TypeAuipc(BitSet imm20, BitSet rd) {
		super();
		this.imm20 = imm20;
		this.rd = rd;
	}
	
	public TypeAuipc() {
		super();
		this.imm20 = new BitSet(20);
		this.rd = new BitSet(5);
	}

}
