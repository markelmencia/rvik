package rv32i;

import instructions.*;

import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.BitSet;
import java.util.function.Function;

public class Assembler {

	private static final HashMap<String, Function<String[], BitSet>> splitToBitSet = new HashMap<>() {{
		put("lui",   TypeLui::assemble);
		put("auipc", TypeAuipc::assemble);
		put("jal",   TypeJ::assemble);
		put("jalr",  TypeJalr::assemble);
		put("beq",   TypeB::assemble);
		put("bne",   TypeB::assemble);
		put("blt",   TypeB::assemble);
		put("bge",   TypeB::assemble);
		put("bltu",  TypeB::assemble);
		put("bgeu",  TypeB::assemble);
		put("lb",    TypeLoad::assemble);
		put("lh",    TypeLoad::assemble);
		put("lw",    TypeLoad::assemble);
		put("lbu",   TypeLoad::assemble);
		put("lhu",   TypeLoad::assemble);
		put("sb",    TypeS::assemble);
		put("sh",    TypeS::assemble);
		put("sw",    TypeS::assemble);
		put("addi",  TypeImm::assemble);
		put("slti",  TypeImm::assemble);
		put("sltiu", TypeImm::assemble);
		put("xori",  TypeImm::assemble);
		put("ori",   TypeImm::assemble);
		put("andi",  TypeImm::assemble);
		put("slli",  TypeImm::assemble);
		put("srli",  TypeImm::assemble);
		put("srai",  TypeImm::assemble);
		put("add",   TypeR::assemble);
		put("sub",   TypeR::assemble);
		put("sll",   TypeR::assemble);
		put("slt",   TypeR::assemble);
		put("sltu",  TypeR::assemble);
		put("xor",   TypeR::assemble);
		put("srl",   TypeR::assemble);
		put("sra",   TypeR::assemble);
		put("or",    TypeR::assemble);
		put("and",   TypeR::assemble);
	}};

	/**
	 * Assembles a string, returning the equivalent instruction
	 * @param instructionString The instruction line
	 * @return A BitSet with the appropiate bits set
	 */
	public static BitSet assembleString(String instructionString) {
		String[] instructionSplit = instructionString.split("[ (]");
		
		for (int i = 1; i < instructionSplit.length; i++) {
			instructionSplit[i] = instructionSplit[i].replaceAll("[x,)]", "");
			instructionSplit[i] = instructionSplit[i].trim();
			if (instructionSplit[i].isEmpty()) {
				instructionSplit[i] = "0";
			}
		}
		return splitToBitSet.get(instructionSplit[0]).apply(instructionSplit);
	}

	/**
	 * Assembles a file, iterating through each line to assemble its instructions
	 * @param FILE_DIR The file path
	 */
	public static void assembleFile(String FILE_DIR) {
		
		File file = new File(FILE_DIR);
		int j = 0;
		try {
			Scanner scanner = new Scanner(file);
			BitSet instruction;
			Main.output += "Program file is: " + FILE_DIR + "\n";
			
			Main.output += "\nPROGRAM INSTRUCTIONS\n--------------------------";
			
			while (scanner.hasNextLine()) {
				String instructionString = scanner.nextLine();
				Main.output += "\n    " + instructionString;
				instruction = assembleString(instructionString);
				for (int i = 0; i < 32; i++) {
					Compiler.pm.set(j, instruction.get(i));
					j++;
				}
			}
			Main.output += "\n--------------------------\n";
			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
