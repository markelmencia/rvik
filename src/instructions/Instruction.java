package instructions;

import java.util.BitSet;

public abstract class Instruction {
    public abstract void run();
    public abstract void fill(BitSet instructionArray);
}
