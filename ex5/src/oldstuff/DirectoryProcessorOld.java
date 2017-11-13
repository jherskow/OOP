package oldstuff;

import filesprocessing.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;


/**
 * Created by jherskow on 5/24/17.
 */
public class DirectoryProcessorOld {
	private static final String
			ERROR = "DEFAULT DirectoryProcessor ERROR",//todo fix
			BAD_SECTION_NAME = "Sub-sections must be names FILTER and ORDER.\t(case sensitive)", // todo maybe do prinf %s floop
			INVALID_USAGE = "Usage: DirectoryProcessor sourceDirectory commandFile",
			BAD_COMMAND_FILE = "Bad command file."; // todo make specific error messages
	private static Order secondaryOrder;

	//==================================   main   ===============================================
	public static void main(String[] args) throws Type2Error, Type1Error {

		if(args.length != 2) throw new Type2Error(ERROR);
		checkValidInput(args[1]);
		Explorable[] files = getFileList(args[0]);
		String[] commandPairs = getCommandPairs(args[1]);
		Command[] commands = getCommandList(commandPairs) ;

		secondaryOrder = new Order("abs");

		for (Command command: commands) {
			Explorable[] filteredList = command.filter.filtered(files);
			secondaryOrder.sort(filteredList); //todo secondary sort
			command.order.sort(filteredList);
			for (Explorable file: filteredList) {
				System.out.println(file.getName());
			}
		}


	}

	static Explorable[] getFileList(String sourcedir) throws Type2Error{
		File[] sourceFiles;
		try {
			sourceFiles = new File(sourcedir).listFiles();
			if(sourceFiles == null){
				throw new Type2Error(ERROR);
			}
		} catch (Exception e){
			throw new Type2Error(ERROR);
		}
		int numOfFiles = sourceFiles.length;
		Explorable[] explorables = new Explorable[numOfFiles];
		int i = 0;
		for (File file: sourceFiles){  //todo maybe implement linked list for sort.
			if (file.isFile()){
				explorables[i] = new FileFacadeExplorable(sourceFiles[i]);
				i++;
			}
		}
		return Arrays.copyOf(explorables,i);
	}

	static String[] getCommandPairs(String commandFile) throws Type2Error{
		String returnString = "";
		try(BufferedReader reader = new BufferedReader(new FileReader(commandFile))){
			String currentLine;
			while ((currentLine = reader.readLine()) != null){
				returnString += currentLine;
			}
		}catch (IOException e ){
			throw new Type2Error(ERROR);
			//todo
		}
		//returnString = returnString.replace("\n","");
		String[] commandSets = returnString.split("FILTER");
		return Arrays.copyOfRange(commandSets, 1, commandSets.length);

	}

	static Command[] getCommandList(String[] commandPairs) throws Type1Error{
		int numCommands = commandPairs.length;
		Command[] commands = new Command[numCommands];
		for (int i = 0; i <numCommands ; i++) {
			commands[i] = new Command(commandPairs[i]);
		}
		return commands;
	}

	static void checkValidInput(String input) throws Type2Error{
		//todo
	}

}
