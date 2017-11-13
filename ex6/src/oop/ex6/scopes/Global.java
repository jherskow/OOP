package oop.ex6.scopes;

import oop.ex6.main.CompileError;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Represents a global scope.
 * The global scope is the main driver - since all other scopes exist within.
 * Additionally, due to the special conditions for global variables and methods -
 * Global's constructor runs over the code twice
 * - once for the global objects, and again to check the inner scopes
 * This scope can have methods but cannot have while/if blocks.
 */

public class Global extends Scope {
	HashMap<String,Method> methods;

	private static final String
		METHOD_ALREADY_DEFINED ="A method with this name is already defined in the global scope.";


    /**
     * Constructs a Global class
     * @param code the lines of code
     * @throws CompileError the error thrown if the code is bad
     */
	public Global(ArrayList<String> code)throws CompileError {
		super(0);
		methods = new HashMap<String,Method>();
		this.parent = null;
		this.global = null;
		scopeType = ScopeType.GLOBAL;
		listMethodsAndVariables(code);
		createScope(code, this);
	}

    /**
     * Creates a list of global methods and variables
     * @param inputArray the array of the code
     * @throws CompileError the error thrown if the code is bad
     */
	private void listMethodsAndVariables(ArrayList<String> inputArray) throws CompileError{
		String line;
		int i = 0;

		while (i < inputArray.size()) {

			line = inputArray.get(i);

			//if line is comment, ignore
			if (line.startsWith(COMMENT_LINE)) {
				i++;
				continue;
			}

			// otherwise remove leading and trailing whitespace
			else line = line.trim();

			// if line is empty, skip
			if(line.equals(EMPTY_STRING)){
				i++;
				continue;
			}

			// if line is a global variable declaration ,declare it and add to list.
			if (line.matches(REGEX_STARTS_WITH_VARIABLE_TYPE)) {
				declareVariables(line, i);

			// regular assignment to a variable
			} else if (line.matches(REGEX_VARIABLE_ASSIGNMENT)) {
				assignVariables(line, i);

			// if line is a method declaration ,add it's name to the list of methods.
			}else if (line.matches(REGEX_METHOD_DECLARATION)) {
				Method newMethod = new Method(line, i);
				if(methods.containsKey(newMethod.name)){
					throw new CompileError(METHOD_ALREADY_DEFINED,i);
				}
				methods.put(newMethod.name, newMethod);
			}

			// skip the rest of the scope
			if (line.charAt(line.length() - 1) == '{') {
				i = skipDyck(inputArray, i + startLine);
				continue;
			}

			// otherwise next line
			i++;
		}
	}


    /**
     * find the line with the matching }  , using Dyck pattern counting
     * and return the length of the encapsulated Dyck pattern.
     * @param file the file being read
     * @param lineNum the start line
     * @return the length of the dyck
     * @throws CompileError the error thrown if there is not a complete dyck
     */
	private int skipDyck(ArrayList<String> file, int lineNum) throws CompileError{
		int dyckCount = 1;
		String line;
		// Iterate over the lines, usign the algorithm for a Dyck pattern.
		for (int i = lineNum+1; i < file.size() ; i++) {
			line = file.get(i).trim();
			if (line.endsWith(OPENING_BRACKET)) dyckCount++;
			if (line.equals(CLOSING_BRACKET)) dyckCount--;
			if (dyckCount == 0){
				return i;
			}
		}
		throw new CompileError(ILLEGAL_DYCK_SEQUENCE,lineNum);
	}

}
