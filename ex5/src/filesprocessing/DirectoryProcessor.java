package filesprocessing;

import java.io.*;
import java.util.*;

/**
 * The main class, which takes the given arguments and displays the given directory
 * to the user, according to the specified parameters in the filter file.
 *
 * @author jherskow
 * @author felber
 */
public class DirectoryProcessor {
	private static final String
			INVALID_USAGE = "Usage: DirectoryProcessor sourceDirectory commandFile",
			IO_FAIL = "IO error",
			DEFAULT_ORDER ="abs",
			NEWLINE = "\n";


//==================================   Main   ===============================================

	/**
	 * The main method.
	 * @param args [0] The path of the source directory. [1] - The path to the filter file.
	 */
	public static void main(String[] args){
		try {

			if (args.length != 2) throw new Type2Error(INVALID_USAGE);

			Explorable[] files = getFileList(args[0]);
			String[] lineArray = linesToArray(args[1]);
			Command[] commands = CommandFactory.makeCommandsFromLines(lineArray);

			DisplayDirectory(commands, files);

		} catch (Type2Error e) {
			System.err.println(e.getMessage());
		}

	}

//==================================   helper methods   ===============================================

	/**
	 * Iterates over the command array,
	 * and prints the contents of the directory in accordance with each command.
	 *
	 * @param commands An array of Commands.
	 * @param files An array of Explorables.
	 */
	private static void DisplayDirectory(Command[] commands, Explorable[] files){
		Order secondaryOrder = new Order(DEFAULT_ORDER,0);
		for (Command command: commands) {
			Explorable[] filteredList = command.filter.filtered(files);
			secondaryOrder.sort(filteredList);
			command.order.sort(filteredList);
			System.err.print(command.filter.getErrorMessage());
			System.err.print(command.order.getErrorMessage());
			for (Explorable file: filteredList) {
				System.out.println(file.getName());
			}
		}
	}

	/**
	 * Creates an array of commands from an array of Strings.
	 * @param pathToFilterFile The path to the .flt file.
	 * @return A command array.
	 * @throws Type2Error If the file cannot be read properly.
	 */
	private static String[] linesToArray(String pathToFilterFile) throws Type2Error{
		String returnString = "";
		try(BufferedReader reader = new BufferedReader(new FileReader(pathToFilterFile))){
			String currentLine;
			while ((currentLine = reader.readLine()) != null){
				returnString += (currentLine + '\n');
			}
		}catch (IOException e ){
			throw new Type2Error(IO_FAIL);
		}
		return returnString.split(NEWLINE);
	}

	/**
	 * Creates an array of Explorables from a given directory.
	 * @param pathToDirectory The path to the directory.
	 * @return An Explorables array.
	 * @throws Type2Error If the directory cannot be read properly.
	 */
	private static Explorable[] getFileList(String pathToDirectory) throws Type2Error{
		File[] sourceFiles;
		sourceFiles = new File(pathToDirectory).listFiles();
		if(sourceFiles == null){
			throw new Type2Error(IO_FAIL);
		}
		int numOfFiles = sourceFiles.length;
		Explorable[] explorables = new Explorable[numOfFiles];
		int i = 0;
		// Wrap each file in an Explorable for compatibility and modularity.
		for (File file: sourceFiles){
			if (file.isFile()){
				explorables[i] = new FileFacadeExplorable(file);
				i++;
			}
		}
		return Arrays.copyOf(explorables,i);
	}
}
