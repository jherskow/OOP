package oop.ex6.scopes;

import oop.ex6.Variable;
import oop.ex6.main.CompileError;

import java.util.ArrayList;

/**
 * A class that represents a Block. Blocks are scopes that they start with a condition and cannot
 *  be in global
 */
class Block extends Scope {
	//private int startLine, endline;
	private static final String
			REGEX_BLOCK_CONDITION_SPLITTER = "\\|\\||\\&\\&",
			OPEN_PAREN = "\\(",
			CLOSED_PAREN = "\\)",

			VARIABLE_IS_NOT_VALID_AS_BOOLEAN
					="The value or variable in the condition is not a legal boolean value",
			CONDITION_NOT_ASSIGNED = "The variable in the condition has not been assigned";


	/**
	 * Constructs a method allowing the program to enter its scope.
	 * @param codeLines the lines representing the method
	 * @param lineNumber the number used to print readable error messages
	 * @param parent the parent scope so that it knows where to look for other variables
	 * @throws CompileError thrown if invalid syntax
	 */
	public Block(ArrayList<String> codeLines, int lineNumber, Scope parent) throws CompileError {
		super(lineNumber);
		scopeType = ScopeType.BLOCK;
		this.parent = parent;

		// take line opening the block
		String firstLine = codeLines.get(0);

		// crab the string in the brackets
		String inBrackets = firstLine.split(OPEN_PAREN)[1];
		inBrackets = inBrackets.split(CLOSED_PAREN)[0];

		// iterate over the conditions, ensuring the validity of each as a bool literal / variable
		String[] condition = inBrackets.split(REGEX_BLOCK_CONDITION_SPLITTER);
		for (int i = 0; i < condition.length; i++){

			condition[i] = condition[i].trim();

			// if not a boolean value,
			if (!Variable.checkTypeCompatibility(condition[i],Variable.VariableType.BOOLEAN,startLine)){

				// check if a boolean variable
				Variable boolVal = getVariable(condition[i], startLine);

				// check if it's assigned
				if(!boolVal.hasValue){
					throw new CompileError(CONDITION_NOT_ASSIGNED, startLine);
				}
				// and make sure it is a compatible type
				if (!Variable.checkTypeCompatibility
						(Variable.VariableType.BOOLEAN, boolVal.getType(), startLine)){
					throw new CompileError(VARIABLE_IS_NOT_VALID_AS_BOOLEAN, startLine);
				}
			}
		}

		// create rest of scope
		createScope(codeLines, parent);
	}
}
