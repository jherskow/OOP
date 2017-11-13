package oop.ex6.scopes;

import oop.ex6.Variable;
import oop.ex6.main.CompileError;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class that represents a method. Methods are scopes except that they start with variables and can
 * only be in global
 */
class Method extends Scope {
	String name;
	private final static String
				REGEX_METHOD_DEF_LINE_PARSE = "void\\s+(\\w+)\\s*\\(([^\\)]*)\\)\\s*\\{",
				REGEX_METHOD_LEGAL_NAME = "[a-zA-Z]\\w*",

				BAD_METHOD_PARAMETER_DECLARATION = "Declaration of method parameter incomplete or illegal",
				ILLEGAL_METHOD_NAME = "Method name is not a valid method name.";

    /**
     * Constructs a method allowing the program to enter its scope.
     * @param codeLines the lines representing the method
     * @param lineNumber the number used to print readable error messages
     * @param parent the parent scope so that it knows where to look for other variables
     * @throws CompileError thrown if invalid syntax
     */
    Method(ArrayList<String> codeLines, int lineNumber, Scope parent) throws CompileError {
		super(lineNumber);
		this.parent = parent;
		this.global = parent.global;
		scopeType = ScopeType.METHOD;

		ArrayList<String> matched =
				regexStringToMatchedList(codeLines.get(0), REGEX_METHOD_DEF_LINE_PARSE, lineNumber);

		name = matched.get(0);
		if (!isLegalName(name)) throw new CompileError(ILLEGAL_METHOD_NAME, startLine);

		// grab parameter variables and add them to variables
		String paramDeclarationLine = matched.get(1).trim();
		if(!paramDeclarationLine.equals(EMPTY_STRING)) {
			addParameterVaraibles(paramDeclarationLine);
		}
		createScope(codeLines, parent);

	}


    /** A constructor for use by global during method caching
     * @param firstLine the first line of the method
     * @param i the line number
     * @throws CompileError the error thrown if syntax is wrong
     */
	Method(String firstLine, int i) throws CompileError{
		super(i);
		ArrayList<String> matched =
				regexStringToMatchedList(firstLine, REGEX_METHOD_DEF_LINE_PARSE, i + startLine);

		name = matched.get(0);
		if (!isLegalName(name)) throw new CompileError(ILLEGAL_METHOD_NAME, startLine);
		String paramDeclarationLine = matched.get(1).trim();
		if(!paramDeclarationLine.equals(EMPTY_STRING)) {
			addParameterVaraibles(paramDeclarationLine);
		}
	}

    /**
     * Checks if the name is legal
     * @param name the name
     * @return iff the name is legal
     */
	private boolean isLegalName(String name){
		return name.matches(REGEX_METHOD_LEGAL_NAME);
	}

    /**
     * Uses a regex to parse the line
     * @param line the line being parsed
     * @param regex the regex being used
     * @param i the line number
     * @return the parsed arraylist
     * @throws CompileError the error thrown if there are no matches
     */
	static ArrayList<String> regexStringToMatchedList(String line, String regex, int i)
			throws CompileError {
		ArrayList<String> matchedList = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(line);
		if(m.find()){
			for(int j = 0; j < m.groupCount(); j++){
				matchedList.add(m.group(j+1));
			}
		} else {
			throw new CompileError(UNKNOWN_SYNTAX, i);
		}
		return matchedList;
	}


    /**
     * Adds a parameter to the list of variables
     * @param paramLine the line
     * @throws CompileError the error thrown if it is not a variable
     */
	private void addParameterVaraibles(String paramLine) throws CompileError{
		String[] inputVariables = paramLine.split(COMMA);
		for (String parameter: inputVariables ) {
			String[] pair = parameter.trim().split(REGEX_WHITESPACE);
			int i = 0;
			boolean isFinal = false;
			if(pair[0].equals(FINAL)){
				isFinal = true;
				i++;
			}
			if(pair.length > i +2){
				throw new CompileError(BAD_METHOD_PARAMETER_DECLARATION, startLine);
			}
			try {
				Variable newVar =
						new Variable(isFinal, Variable.typeNameToType(pair[i], startLine),
								pair[i+1], startLine, false);
				newVar.hasValue = true;
				if(this.variables.containsKey(newVar.name)){
					throw new CompileError(VARIABLE_ALREADY_DEFINED_IN_SCOPE, startLine);
				}
				this.variables.put(newVar.name, newVar);
			} catch (ArrayIndexOutOfBoundsException e){
				throw new CompileError(BAD_METHOD_PARAMETER_DECLARATION, startLine);
			}
		}
	}



}
