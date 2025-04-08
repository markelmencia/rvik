package instructions;

import java.util.BitSet;

public abstract class Instruction implements Assemblable {
    public abstract void run();
    public abstract void fill(BitSet instructionArray);
}
