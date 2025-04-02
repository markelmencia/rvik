package rv32i;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import utils.Utils;

import java.util.BitSet;

import org.junit.Test;

import instructions.*;

public class CompilerTest {

    @Test
    public void testGetInstr() {
        BitSet bitset = BitSet.valueOf(new long[]{0b00000000000100000000000001110011}); // ebreak instruction (arbitrary)
        int j = 0;
        for (int i = 32; i < 64; i++) {
            Compiler.pm.set(i, bitset.get(j));
            j++;
        }
        Compiler.pc = 32;
        assertEquals(bitset, Compiler.getInstr());
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

        assertEquals(15, Utils.btiu(segment));

        BitSet segment2 = new BitSet(4);
        for (int i = 0; i < 3; i++) {
            segment2.set(i); // 7
        }

        assertEquals(7, Utils.btiu(segment2));
    }

    @Test
    public void testBtis() {
        BitSet segment = new BitSet(4);
        for (int i = 0; i < 4; i++) {
            segment.set(i); // -1
        }

        assertEquals(-1, Utils.btis(segment, 4));

        BitSet segment2 = new BitSet(4);
        for (int i = 0; i < 3; i++) {
            segment2.set(i); // 7
        }

        assertEquals(7, Utils.btis(segment2, 4));
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

        assertEquals(extendedBitSet, Utils.bitExtension(bitSet, 32));
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
        // Lui Type
        TypeLui typeLui = new TypeLui();
        BitSet luiBitSet = new BitSet() {{
            // imm20
            set(12);
            set(13);
            set(14);
            // rd
            set(7);
            set(9);
        }};
        typeLui.fill(luiBitSet);
        assertEquals(BitSet.valueOf(new long[]{7}), typeLui.getImm20());
        assertEquals(BitSet.valueOf(new long[]{5}), typeLui.getRd());
        
        // Auipc Type
        TypeAuipc typeAuipc = new TypeAuipc();
        BitSet auipcBitSet = new BitSet() {{
             // imm20
             set(12);
             set(14);
             // rd
             set(8);
        }};
        typeAuipc.fill(auipcBitSet);
        assertEquals(BitSet.valueOf(new long[]{5}), typeAuipc.getImm20());
        assertEquals(BitSet.valueOf(new long[]{2}), typeAuipc.getRd());

        // J Type
        TypeJ typeJ = new TypeJ();
        BitSet jBitSet = new BitSet() {{
            // imm21
            set(12);
            set(31);
            set(21);
            // rd
            set(9);
       }};
       typeJ.fill(jBitSet);
       // The purpose of this number in particular is to test
       // the bit reallocation that occurs in the jal assembly
       assertEquals(BitSet.valueOf(new long[]{1052674}), typeJ.getImm21());
       assertEquals(BitSet.valueOf(new long[]{4}), typeJ.getRd());

        // Jalr Type
        TypeJalr typeJalr = new TypeJalr();
        BitSet jalrBitSet = new BitSet() {{
            // rd
            set(7);
            set(8);
            // rs1
            set(18);
            // imm12
            set(20);
        }};
        typeJalr.fill(jalrBitSet);

        assertEquals(BitSet.valueOf(new long[]{3}), typeJalr.getRd());
        assertEquals(BitSet.valueOf(new long[]{8}), typeJalr.getRs1());
        assertEquals(BitSet.valueOf(new long[]{1}), typeJalr.getImm12());

        // B Type
        TypeB typeB = new TypeB();
        BitSet bBitSet = new BitSet() {{
            // imm13
            set(7);
            set(8);
            set(25);
            set(31);
            // funct3
            set(14);
            // rs1
            set(16);
            // rs2
            set(22);
        }};
        typeB.fill(bBitSet);

        assertEquals(BitSet.valueOf(new long[]{6178}), typeB.getImm13());
        assertEquals(BitSet.valueOf(new long[]{4}), typeB.getFunct3());
        assertEquals(BitSet.valueOf(new long[]{2}), typeB.getRs1());
        assertEquals(BitSet.valueOf(new long[]{4}), typeB.getRs2());

        // Load Type
        TypeLoad typeLoad = new TypeLoad();
        BitSet loadBitSet = new BitSet() {{
            // rd
            set(8);
            // funct3
            set(13);
            // rs1
            set(17);
            // imm12
            set(22);
        }};
        typeLoad.fill(loadBitSet);

        assertEquals(BitSet.valueOf(new long[]{4}), typeLoad.getImm12());
        assertEquals(BitSet.valueOf(new long[]{2}), typeLoad.getFunct3());
        assertEquals(BitSet.valueOf(new long[]{4}), typeLoad.getRs1());
        assertEquals(BitSet.valueOf(new long[]{2}), typeLoad.getRd());

        // S Type
        TypeS typeS = new TypeS();
        BitSet sBitSet = new BitSet() {{
            // funct3
            set(13);
            // rs1
            set(17);
            // rs2
            set(22);
            // imm12
            set(8);
            set(25);
        }};
        typeS.fill(sBitSet);

        assertEquals(BitSet.valueOf(new long[]{34}), typeS.getImm12());
        assertEquals(BitSet.valueOf(new long[]{2}), typeS.getFunct3());
        assertEquals(BitSet.valueOf(new long[]{4}), typeS.getRs1());
        assertEquals(BitSet.valueOf(new long[]{4}), typeS.getRs2());

        // Imm Type
        TypeImm typeImm = new TypeImm();
        BitSet immBitSet = new BitSet() {{
            // rd
            set(8);
            // funct3
            set(13);
            // rs1
            set(17);
            // imm12
            set(22);
        }};
        typeImm.fill(immBitSet);

        assertEquals(BitSet.valueOf(new long[]{4}), typeImm.getImm12());
        assertEquals(BitSet.valueOf(new long[]{2}), typeImm.getFunct3());
        assertEquals(BitSet.valueOf(new long[]{4}), typeImm.getRs1());
        assertEquals(BitSet.valueOf(new long[]{2}), typeImm.getRd());

        // R Type
        TypeR typeR = new TypeR();
        BitSet rBitSet = new BitSet() {{
            // rd
            set(8);
            // funct3
            set(13);
            // rs1
            set(17);
            // rs2
            set(22);
            // funct7
            set(25);
        }};
        typeR.fill(rBitSet);

        assertEquals(BitSet.valueOf(new long[]{1}), typeR.getFunct7());
        assertEquals(BitSet.valueOf(new long[]{2}), typeR.getFunct3());
        assertEquals(BitSet.valueOf(new long[]{4}), typeR.getRs1());
        assertEquals(BitSet.valueOf(new long[]{4}), typeR.getRs2());
        assertEquals(BitSet.valueOf(new long[]{2}), typeR.getRd());

        // CallAtomic Type
        TypeCallAtomic typeCallAtomic = new TypeCallAtomic();
        BitSet callAtomicBitSet = new BitSet() {{
            // rd
            set(8);
            // funct3
            set(13);
            // rs1
            set(17);
            // csr12
            set(22);
        }};
        typeCallAtomic.fill(callAtomicBitSet);

        assertEquals(BitSet.valueOf(new long[]{4}), typeCallAtomic.getCsr12());
        assertEquals(BitSet.valueOf(new long[]{2}), typeCallAtomic.getFunct3());
        assertEquals(BitSet.valueOf(new long[]{4}), typeCallAtomic.getRs1());
        assertEquals(BitSet.valueOf(new long[]{2}), typeCallAtomic.getRd());
    }

    @Test
    public void testRunInstruction() {
        Compiler.pc = 0;
        TypeLui typeLui = new TypeLui(BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{5}));
        typeLui.run();
        assertEquals(32, Compiler.pc);
        assertEquals(8192, Compiler.reg[5]);

        TypeAuipc typeAuipc = new TypeAuipc(BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{3}));
        typeAuipc.run();
        assertEquals(64, Compiler.pc);
        assertEquals(8224, Compiler.reg[3]);

        TypeJ typeJ = new TypeJ(BitSet.valueOf(new long[]{32}), BitSet.valueOf(new long[]{1}));
        typeJ.run();
        assertEquals(96, Compiler.pc);
        assertEquals(96, Compiler.reg[1]);

        TypeJalr typeJalr = new TypeJalr(BitSet.valueOf(new long[]{32}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{0}), BitSet.valueOf(new long[]{2}));
        typeJalr.run();
        assertEquals(128, Compiler.pc);
        assertEquals(128, Compiler.reg[2]);

        // B Type
        Compiler.reg[1] = 1;
        Compiler.reg[2] = 2;

        TypeB typeBeq = new TypeB(BitSet.valueOf(new long[]{64}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{0}));
        typeBeq.run();
        assertEquals(160, Compiler.pc);

        TypeB typeBne = new TypeB(BitSet.valueOf(new long[]{64}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{1}));
        typeBne.run();
        assertEquals(224, Compiler.pc);

        TypeB typeBlt = new TypeB(BitSet.valueOf(new long[]{64}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{4}));
        typeBlt.run();
        assertEquals(288, Compiler.pc);

        TypeB typeBge = new TypeB(BitSet.valueOf(new long[]{64}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{5}));
        typeBge.run();
        assertEquals(320, Compiler.pc);

        TypeB typeBltu = new TypeB(BitSet.valueOf(new long[]{64}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{6}));
        typeBltu.run();
        assertEquals(384, Compiler.pc);

        TypeB typeBgeu = new TypeB(BitSet.valueOf(new long[]{64}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{7}));
        typeBgeu.run();
        assertEquals(416, Compiler.pc);

        TypeLoad typeLoad = new TypeLoad(BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{3}));
        Compiler.reg[1] = 1;
        Compiler.storeMem(BitSet.valueOf(new long[]{1023}), 2);
        typeLoad.run();
        assertEquals(-1, Compiler.reg[3]);
        assertEquals(448, Compiler.pc);

        TypeS typeS = new TypeS(BitSet.valueOf(new long[]{0}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{0}));
        Compiler.reg[1] = 256;
        Compiler.reg[2] = 2;
       typeS.run();
        assertEquals(-1, Utils.btis(Compiler.loadMem(2, 8), 8));
        assertEquals(480, Compiler.pc);

        TypeImm typeImm = new TypeImm(BitSet.valueOf(new long[]{-1}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{0}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{0}));
        Compiler.reg[1] = 1;
        typeImm.run();
        assertEquals(0, Compiler.reg[1]);
        assertEquals(512, Compiler.pc);

        TypeR typeR = new TypeR(BitSet.valueOf(new long[]{0b0100000}), BitSet.valueOf(new long[]{1}), BitSet.valueOf(new long[]{2}), BitSet.valueOf(new long[]{0}), BitSet.valueOf(new long[3]));
        Compiler.reg[1] = 1;
        Compiler.reg[2] = 2;
        typeR.run();
        assertEquals(-1, Compiler.reg[3]);
        assertEquals(544, Compiler.pc);
    }

    @Test
    public void testRun() {
        // TODO
    }
}
