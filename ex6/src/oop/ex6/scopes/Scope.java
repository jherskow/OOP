package oop.ex6.scopes;

import oop.ex6.Variable;
import oop.ex6.main.CompileError;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a scope within an sjava code file.
 *
 * A scope represents a domain within the code.
 *
 * A scope can have other scopes, of different types,
 * as well as variables, assignments, and method calls/declarations
 * depending on the type.
 *
 * A scope also can have a parent, if it is nested within another.
 *
 * The first line of the scope is saved for precise pinpointing of the offending line,
 * in case of an error.
 */
public abstract class Scope {
	LinkedHashMap<String, Variable> variables;
	private HashSet<Scope> scopes;
	ScopeType scopeType;
	Scope parent;
	Global global;
	int startLine;

	static final String

			// Regular expressions
			REGEX_BLOCK= "^(if|while)\\s*\\(([^\\)]+)\\)\\s*\\{$",
			REGEX_STARTS_WITH_VARIABLE_TYPE = "^(final|int|double|char|boolean|String).*",
			REGEX_METHOD_CALL ="(\\w+)\\s*\\(([^\\)]*)\\)\\s*;",
			REGEX_VARIABLE_ASSIGNMENT = "(\\w+)\\s*=\\s*([^;]+);",
			REGEX_RETURN_STATEMENT= "^return;$",
			REGEX_ASSIGNMENT_PARSING="(\\w+)\\s*=\\s*([^;]+)",
			REGEX_METHOD_DECLARATION = "^void[^{]*\\{$",
			REGEX_SPLIT_DECLARATION_SPACES = " |;",
			REGEX_SPLIT_DECLARATION_EQUALS_COMMA = "((?<==)|(?==)|(?<=,)|(?=,))",
			REGEX_WHITESPACE = "\\s+",

			// Magic Strings
			FINAL = "final",
			EQUALS = "=",
			COMMA = ",",
			OPENING_BRACKET = "{",
			CLOSING_BRACKET = "}",
			SEMICOLON = "",
			EMPTY_STRING ="",
			COMMENT_LINE = "//",

			// Compile error messages
			METHOD_CALL_IN_GLOBAL="Method calls are not allowed in the global scope.",
			UNKNOWN_SCOPE_TYPE="This is not a recognized type of scope.",
			MISSING_RETURN_STATEMENT = "the last line in a method must be a return staement.",
			UNKNOWN_SYNTAX = "Unknown Syntax error.",
			NO_SEMICOLON = "Missing ; at end of line.",
			ILLEGAL_DYCK_SEQUENCE="Opened bracket is not matched by a legal Dyck pattern of nested brackets.",
			METHOD_DEFINED_NOT_IN_GLOBAL = "Methods can only be defined in the global scope.",
			UNDECLARED_VARIABLE = "This variable has not been declared.",
			INCOMPATIBLE_PARAMETER = "Parameters are not compatible with the method's definition.",
			INCOMPATIBLE_NUM_OF_PARAMETERS = "# of paramaters not compatible with the method's definition.",
			ASSIGN_FROM_NOT_INITILISED="The variable you are trying to assign from has not been initialised",
			METHOD_NOT_DEFINED = "This method has not been defined.",
			BAD_ASSIGNMENT_SYNTAX = "Incorrect syntax for variable assignment",
			BAD_TYPE_ASSIGNMENT = "The assigned value is not compatible with the variable's type.",
			ASSIGNMENT_to_FINAL = "You cannot assign a new value to a final variable.",
			VARIABLE_ALREADY_DEFINED_IN_SCOPE = "This variable has already been defined in this scope.",
			BLOCK_IN_GLOBAL = "Conditional blocks / loops are not allowed in the global scope.",
			UNRECOGNIZED_PARAMETER = "The paramater is not a valid literal or variable name.";

	private static final char
			OPENING_BRACKET_CHAR = '{';

	enum ScopeType {
		BLOCK, GLOBAL, METHOD
	}

	/**
	 * Initialises a scope and its' main variables.
	 * @param startLine The number of the first line of the scope
	 */
	Scope(int startLine){
		variables = new LinkedHashMap<String, Variable>();
		scopes = new HashSet<Scope>();
		this.startLine = startLine;
	}

	/**
	 * This constructor creates a general scope from code lines, using lineParser.
	 * @param scopeLines The lines of the scope.
	 * @param parent The Scope in which this Scope is nested.
	 * @throws CompileError
	 */
	void createScope(ArrayList<String> scopeLines, Scope parent)throws CompileError {
		this.parent = parent;
		if (scopeType == ScopeType.GLOBAL) this.global = null;
		else if (parent.scopeType == ScopeType.GLOBAL) global = (Global) parent;
		else global = parent.global;
		lineParser(scopeLines);
	}


	/**
	 * The heavy lifter for main, lineParse iterates over the lines of code,
	 * creating sub-scopes when they are declared, and checking the legality of the rest
	 * of the code lines.
	 *
	 * @param inputArray List of code lines.
	 * @throws CompileError if code is not legal sjava.
	 */
	private void lineParser(ArrayList<String> inputArray) throws CompileError {
		String line;
		ArrayList<String> codeSnippet;
		int i = 0;

		while (i < inputArray.size()) {

			line = inputArray.get(i);

			//if line is comment or empty, ignore
			if (line.startsWith(COMMENT_LINE)) {
				i++;
				continue;
			}

			// otherwise remove leading and trailing whitespace
			line = line.trim();

			//if line was whitespace, ignore
			if (line.equals(EMPTY_STRING)) {
				i++;
				continue;
			}

			// if line is first line of inner scope, skip it.
			if (i==0 && scopeType != ScopeType.GLOBAL){
				i++;
				continue;
			}

			// if line is start of scope
			if (line.charAt(line.length() - 1) == OPENING_BRACKET_CHAR) {

				//get the entire scope's Dyck pattern of "{}{}"'s
				codeSnippet = snipDyck(inputArray, i);

				// make if and while loops if not in global
				if (line.matches(REGEX_BLOCK)) {
					makeBlock(codeSnippet, i);

				} else if (line.matches(REGEX_METHOD_DECLARATION)) {
					makeMethod(codeSnippet, i);
				} else {
					throw new CompileError(UNKNOWN_SCOPE_TYPE, i + startLine);
				}
				i = i + codeSnippet.size() +1;
				continue;
			}

			if (!line.endsWith(SEMICOLON))
				throw new CompileError(NO_SEMICOLON, i + startLine);

			// if the line stars with a variable type name, it must be a declaraition
			if (line.matches(REGEX_STARTS_WITH_VARIABLE_TYPE)) {
				if(scopeType!= ScopeType.GLOBAL) declareVariables(line, i);

			// regular assignment to a variable
			} else if (line.matches(REGEX_VARIABLE_ASSIGNMENT)) {
				assignVariables(line, i);

			// if this is the second-to-last line in a method - ensure line is a RETURN statement.
			}else if (i == inputArray.size() - 1 && scopeType == ScopeType.METHOD){
				if(! line.matches(REGEX_RETURN_STATEMENT)){
					throw new CompileError(MISSING_RETURN_STATEMENT, i + startLine);
				}

			// if this is some other return statement - make sure it is in method.
			} else if (line.matches(REGEX_RETURN_STATEMENT)) {
				i++;
				continue;

			// if line is a method call, ensure it is valid
			} else if (line.matches(REGEX_METHOD_CALL)) {
				CheckMethodCall(line, i);

			// nothing is matched
			} else{
				throw new CompileError(UNKNOWN_SYNTAX, i+ startLine);
			}
			// next line
			i++;
		}
	}


	/**
	 * This algorithm starts at an '{'
	 * And grabs all the lines until the next *correct* '}'
	 *
	 * After the lines are found, the method snips out a sub-list of these lines,
	 * and returns it.
	 *
	 *
	 * This method makes use of the mathematical propreties of a legal Dyck word
	 * or Dyck pattern.
	 * For more information, see:
	 * https://en.wikipedia.org/wiki/Dyck_language
	 *
	 * @param file Codelines of outer scope
	 * @param i Line at which the Dyck pattern begins.
	 * @return Sub-list of inner scope.
	 * @throws CompileError If the code has a bad Dyck.
	 */
	private ArrayList<String> snipDyck(ArrayList<String> file, int i ) throws CompileError{
		// find the line with the matching }  , using Dyck pattern counting
		// and return the entire scope (with first line)

		int dyckCount = 1;
		String line;
		for (int j = i+1; j < file.size() ; j++) {
			line = file.get(j).trim();
			if (line.endsWith(OPENING_BRACKET)) dyckCount++;
			if (line.equals(CLOSING_BRACKET)) dyckCount--;
			if (dyckCount == 0){
				return new ArrayList<String>(file.subList(i,j));
			}
		}
		throw new CompileError(ILLEGAL_DYCK_SEQUENCE,i+ startLine);
	}

	/**
	 * Uses regex to split a variable declaraion line into components.
	 * @param declarationLine The line of the Variable Declaration.
	 * @return List of separate regex capture groups.
	 */
	private ArrayList<String> splitDeclaration(String declarationLine){
		ArrayList<String> b =
				new ArrayList<String>(Arrays.asList(declarationLine.split(REGEX_SPLIT_DECLARATION_SPACES)));
		ArrayList<String> done = new ArrayList<String>();
		for(int i=0;i<b.size();i++){
		    if(b.get(i).equals(EMPTY_STRING)) continue;
			ArrayList<String> c = new ArrayList<String>(Arrays.asList(b.get(i).split
                    (REGEX_SPLIT_DECLARATION_EQUALS_COMMA)));
			for (String d:c) {
				done.add(d);
			}
		}
		return done;
	}

	/**
	 * @return true iff is name of local variable
	 */
	private boolean isLocalVariable(String varName){
		return  (variables.containsKey(varName));
	}

	/**
	 *
	 * @param varName A variable name.
	 * @param i Line in question.
	 * @return Variable object if this name has been declared above.
	 * @throws CompileError if variable is not declared.
	 */
	private Variable getVariableAbove(String varName, int i) throws CompileError{
		if (scopeType == ScopeType.GLOBAL) throw new CompileError(UNDECLARED_VARIABLE,i);
		if (parent.variables.containsKey(varName)) return parent.variables.get(varName);
		return parent.getVariableAbove(varName, i);
	}

	/**
	 * @param varName A variable name.
	 * @param i Line in question.
	 * @return Variable object if this name is a legal variable for this scope.
	 * @throws CompileError if variable is not declared.
	 */
	Variable getVariable(String varName, int i) throws CompileError{
		if (variables.containsKey(varName)) return variables.get(varName);
		return getVariableAbove(varName , i+ startLine);
	}

	/**
	 * @param varName A variable name.
	 * @return True iff this is name of a global variable.
	 */
	private boolean isGlobalVariable(String varName){
		if (scopeType == ScopeType.GLOBAL) return variables.containsKey(varName);
		if (parent.variables.containsKey(varName)) return parent.scopeType == ScopeType.GLOBAL;
		return parent.isGlobalVariable(varName);
	}

	/**
	 * Creates a new Scope of type Block
	 * @param codeSnippet Lines of code for the new scope.
	 * @param i First line of inner scope.
	 * @throws CompileError If scope creations fails.
	 */
	private void makeBlock(ArrayList<String> codeSnippet,int i) throws CompileError{
		if (scopeType == ScopeType.GLOBAL) {
			throw new CompileError(BLOCK_IN_GLOBAL, i+ startLine);
		} else {
			this.scopes.add(new Block(codeSnippet, i+ startLine, this));
		}
	}

	/**
	 * Creates a new Scope of type Method
	 * @param codeSnippet Lines of code for the new scope.
	 * @param i First line of inner scope.
	 * @throws CompileError If scope creations fails.
	 */
	private void makeMethod(ArrayList<String> codeSnippet,int i) throws CompileError{
		if (scopeType != ScopeType.GLOBAL) {
			throw new CompileError(METHOD_DEFINED_NOT_IN_GLOBAL, i+ startLine);
		} else {
			this.scopes.add(new Method(codeSnippet, i+ startLine, this));
		}
	}

	/**
	 * Implements the logic for declaring variables
	 * @param line line of call
	 * @param i line number within scope for errors
	 * @throws CompileError if illegal code
	 */
	void declareVariables(String line,int i) throws CompileError{
		ArrayList<String> splitlines = splitDeclaration(line);
		int j = 0;
		boolean _final = false;

		// add final modifier if specified
		if (splitlines.get(j).equals(FINAL)) {
			_final = true;
			j++;
		}
		// get the type
		Variable.VariableType variableType = Variable.typeNameToType(splitlines.get(j), i);
		j++;

		for (; j < splitlines.size(); j++) {
			// remove whitespace from the expressions
			String name = splitlines.get(j).trim();

			// ensure variable is not already locally defined
			if (isLocalVariable(name))
				throw new CompileError(VARIABLE_ALREADY_DEFINED_IN_SCOPE, i+ startLine);

			// see if this is an assignment declaration
			if (splitlines.size() > j+1 && splitlines.get(j + 1).equals(EQUALS)) {
				String assignedValue = splitlines.get(j + 2);
				j+=2;

				// if assignment of literal:
				if (Variable.isVariableLiteral(assignedValue)) {
					variables.put(name, new Variable(_final, variableType, name, assignedValue, i));

				} else {
					Variable rightVar = getVariable(assignedValue, i);
					// ensure source variable has been assigned
					if (!rightVar.hasValue){
						throw new CompileError(ASSIGN_FROM_NOT_INITILISED,i);
					}
					Variable newVar = new Variable(_final, variableType, name, rightVar, i);
					variables.put(name,newVar);
				}
			} else {
				variables.put(name, new Variable(_final, variableType, name, i+ startLine, true));
			}
			// if a single comma-spaced argument is too long, throw an error
			if(splitlines.size() > j + 1 && !splitlines.get(j+1).equals(COMMA)){
			    throw new CompileError(BAD_ASSIGNMENT_SYNTAX, i+startLine);
            }
            j++;
		}
	}

	/**
	 * Implements the logic for checking a variable assignment
	 * @param line line of assignment
	 * @param i line number within scope for errors
	 * @throws CompileError if illegal code
	 */
	void assignVariables(String line,int i) throws CompileError{
		ArrayList<String> parsedLine = parseAssignment(line, i);
		Variable leftVar = getVariable(parsedLine.get(0), i);
		if(leftVar._final){
			throw new CompileError(ASSIGNMENT_to_FINAL, i+ startLine);
		}
		if (isGlobalVariable(leftVar.name)){
			// replace leftVar with new identical variable
			leftVar = new Variable(leftVar);

			// add it to this scopes variables - so that we have a local copy
			// this way initialization within a method, will not affect global.
			variables.put(leftVar.name, leftVar);

		}
		if(!Variable.checkTypeCompatibility(parsedLine.get(1), leftVar.getType() , i)){
			Variable rightVar = getVariable(parsedLine.get(1), i);
			if (!rightVar.hasValue) {
				throw new CompileError(ASSIGN_FROM_NOT_INITILISED, i + startLine);
			}
			if (! Variable.checkTypeCompatibility(rightVar.getType(), leftVar.getType(), i)){
				throw new CompileError(BAD_TYPE_ASSIGNMENT, i+ startLine);
			}
		}
		// mark the variable as initialised
		leftVar.hasValue=true;
	}

	/**
	 * Implements the logic for checking a method call
	 * @param line line of call
	 * @param i line number within scope for errors
	 * @throws CompileError if illegal code
	 */
	private void CheckMethodCall(String line, int i) throws CompileError{
		if (scopeType == ScopeType.GLOBAL) {
			throw new CompileError(METHOD_CALL_IN_GLOBAL, i + startLine);
		} else {
			// parse to get the method name and parameters
			ArrayList<String> list = Method.regexStringToMatchedList(line, REGEX_METHOD_CALL, i+ startLine);
			String name = list.get(0);


			// get the method's info from global
			if (!global.methods.containsKey(name)) throw new CompileError(METHOD_NOT_DEFINED, i+ startLine);
			Method method = global.methods.get(name);

			String[] parameters = list.get(1).split(COMMA);

			// iterate over the parameters & ensure all parameters given match the method
			Variable neededParameter;
			Iterator<Variable> neededParamIterator = method.variables.values().iterator();

			// if no parameters given, ensure none are required
			if(parameters.length == 1 && parameters[0].trim().equals(EMPTY_STRING)){
				if(neededParamIterator.hasNext()){
					throw new CompileError(INCOMPATIBLE_NUM_OF_PARAMETERS, i+ startLine);
				}
			}else{

				for (int j = 0; j < parameters.length ; j++) {
					String parameter = parameters[j].trim();

					if(!neededParamIterator.hasNext()){
						throw new CompileError(INCOMPATIBLE_NUM_OF_PARAMETERS, i+ startLine);
					}
					neededParameter = neededParamIterator.next();

					// first assume a literal value
					if(!Variable.checkTypeCompatibility(parameter, neededParameter, i)){

						// otherwise check if an actual variable
						if (!Variable.isNameLegal(parameters[j])){
							throw new CompileError(UNRECOGNIZED_PARAMETER, i+ startLine);
						}
						Variable varParam = getVariable(parameters[j], i);

						if(! Variable.checkTypeCompatibility(neededParameter,varParam,i )){
							throw new CompileError(INCOMPATIBLE_PARAMETER, i+ startLine);
						}
					}
				}
			}
		}
	}

	/**
	 * Recieves an assignment line and parses it
	 * @param line assignment line
	 * @param i Line in question
	 * @return List of matched regex groups
	 * @throws CompileError thrown if not legal syntax.
	 */
	private ArrayList<String> parseAssignment(String line, int i)throws CompileError{
		Pattern p = Pattern.compile(REGEX_ASSIGNMENT_PARSING);
		Matcher m = p.matcher(line);
		ArrayList<String> matches = new ArrayList<>();
		if(m.find()){
			for(int j = 0; j < m.groupCount(); j++){
				matches.add(m.group(j+1));
			}
		} else {
			throw new CompileError(BAD_ASSIGNMENT_SYNTAX, i+ startLine);
		}
		return matches;
	}
}