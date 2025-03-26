package test.rv32i;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.BitSet;

import org.junit.Test;

import instructions.*;
import rv32i.Compiler;

public class CompilerTest {

    @Test
    public void testGetInstr() {
        BitSet bitSet = new BitSet(32);
        for (int i = 0; i < 10; i++) {
            bitSet.set(i);
        }

        Compiler.pc = 32;
        for (int i = 32; i < 42; i++) {
            Compiler.pm.set(i);
        }
        System.out.println(bitSet);
        assertEquals(bitSet, Compiler.getInstr());
    }

    @Test
    public void testLoadMem() {
       for (int i = 32; i < 42; i++) {
            Compiler.mem.set(i);
        }

       BitSet bitSet = new BitSet(32);
        for (int i = 0; i < 10; i++) {
            bitSet.set(i);
        }

        assertEquals(bitSet, Compiler.loadMem(32, 32));
    }
    
    @Test
    public void testStoreMem() {
        BitSet bitSet = new BitSet(32);
        for (int i = 0; i < 10; i++) {
            bitSet.set(i);
        }

        Compiler.storeMem(bitSet, 64);

        assertEquals(bitSet, Compiler.loadMem(64, 32));
    }

    @Test
    public void testBtiu() {
        BitSet segment = new BitSet(4);
        for (int i = 0; i < 4; i++) {
            segment.set(i); // 15
        }

        assertEquals(15, Compiler.btiu(segment));

        BitSet segment2 = new BitSet(4);
        for (int i = 0; i < 3; i++) {
            segment2.set(i); // 7
        }

        assertEquals(7, Compiler.btiu(segment2));
    }

    @Test
    public void testBtis() {
        BitSet segment = new BitSet(4);
        for (int i = 0; i < 4; i++) {
            segment.set(i); // -1
        }

        assertEquals(-1, Compiler.btis(segment, 4));

        BitSet segment2 = new BitSet(4);
        for (int i = 0; i < 3; i++) {
            segment2.set(i); // 7
        }

        assertEquals(7, Compiler.btis(segment2, 4));
    }

    @Test
    public void testInstructionIsEmpty() {
        BitSet bitSet = new BitSet(32);
        bitSet.set(4);
        assertFalse(Compiler.instructionIsEmpty(bitSet));
        bitSet = new BitSet(32);
        assertTrue(Compiler.instructionIsEmpty(bitSet));
    }

    @Test
    public void testFillSegment() {
        BitSet bitSet = new BitSet(15);
        for (int i = 0; i < 10; i++) {
            bitSet.set(i);
        }

        BitSet segment = new BitSet(7);
        segment.set(0);
        segment.set(1);

        assertEquals(segment, Compiler.fillSegment(bitSet, 8, 15));
    }

    @Test
    public void testBitExtension() {
        BitSet bitSet = new BitSet(10);
        for (int i = 0; i < 10; i++) {
            bitSet.set(i);
        }

        BitSet extendedBitSet = new BitSet(32);
        for (int i = 0; i < 10; i++) {
            extendedBitSet.set(i);
        }

        assertEquals(extendedBitSet, Compiler.bitExtension(bitSet, 32));
    }

    @Test
    public void testGetReg() {
        Compiler.reg[2] = 31;
        BitSet bitSet = new BitSet(4);
        for (int i = 0; i < 4; i++) {
            bitSet.set(i);
        }
        assertEquals(bitSet, Compiler.getReg(2, 4));
    }

    @Test
    public void testGetInstrType() {
        BitSet typeLui = BitSet.valueOf(new long[]{0b0110111});
        BitSet typeAuipc = BitSet.valueOf(new long[]{0b00010111});
        BitSet typeJ = BitSet.valueOf(new long[]{0b1101111});
        BitSet typeJalr = BitSet.valueOf(new long[]{0b1100111});
        BitSet typeB = BitSet.valueOf(new long[]{0b1100011});
        BitSet typeLoad = BitSet.valueOf(new long[]{0b0000011});
        BitSet typeS = BitSet.valueOf(new long[]{0b0100011});
        BitSet typeImm = BitSet.valueOf(new long[]{0b0010011});
        BitSet typeR = BitSet.valueOf(new long[]{0b0110011});
        BitSet typeCallAtomic = BitSet.valueOf(new long[]{0b1110011});

        assertEquals(TypeLui.class,         Compiler.getInstrType(typeLui).getClass());
        assertEquals(TypeAuipc.class,       Compiler.getInstrType(typeAuipc).getClass());
        assertEquals(TypeJ.class,           Compiler.getInstrType(typeJ).getClass());
        assertEquals(TypeJalr.class,        Compiler.getInstrType(typeJalr).getClass());
        assertEquals(TypeB.class,           Compiler.getInstrType(typeB).getClass());
        assertEquals(TypeLoad.class,        Compiler.getInstrType(typeLoad).getClass());
        assertEquals(TypeS.class,           Compiler.getInstrType(typeS).getClass());
        assertEquals(TypeImm.class,         Compiler.getInstrType(typeImm).getClass());
        assertEquals(TypeR.class,           Compiler.getInstrType(typeR).getClass());
        assertEquals(TypeCallAtomic.class,  Compiler.getInstrType(typeCallAtomic).getClass());
    }

    @Test
    public void testFillInstr() {
        TypeLui typeLui = new TypeLui();
        BitSet luiBitSet = new BitSet(32);
    }
}
