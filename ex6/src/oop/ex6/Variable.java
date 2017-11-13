package oop.ex6;


import oop.ex6.main.CompileError;

/**
 * Represents a variable. A variable can be final, assigned, and it has a type.
 */
public class Variable{
	public enum VariableType {
		INT, DOUBLE, STRING, CHAR, BOOLEAN, UNKNOWN
	}


	private static final String
			REGEX_INT = "-??\\d+",
			REGEX_DOUBLE = "-??\\d+\\.\\d+",
			REGEX_CHAR = "'[^']'$",
			REGEX_STRING = "\"[^\"]*\"",
			REGEX_BOOLEAN = "true|false",
			REGEX_VARIABLE_NAME = "[a-zA-Z]\\w*|_\\w+",

			ILLEGAL_VARIABLE_NAME = "Variable name is not a valid variable name.",
			INCOMPATIBLE_TYPE_ASSIGNMENT = "Value or variable is not a compatible type for this assignment.",
			UNKNOWN_VARIABLE_TYPE = "Variable type is misspelled or not supported",
			FINAL_VARIABLE_NOT_ASSIGNED = "A final variable must be assigned a value at declaration time.";


	public boolean _final = false, hasValue = false;
	private int lineDeclared;
	private VariableType type;
	public String name;

    /**
     * Constructor that receives a type
     * @param isFinal if it is final
     * @param type the variable type
     * @param name the variable name
     * @param lineDeclared the line variable was declared
     * @param direct if this was called directly
     * @throws CompileError thrown if the variable is bad
     */
	public Variable(boolean isFinal, VariableType type, String name, int lineDeclared, boolean direct)
			throws CompileError{
		_final = isFinal;
		if(_final && direct)
			throw new CompileError(FINAL_VARIABLE_NOT_ASSIGNED,lineDeclared);
		this.lineDeclared = lineDeclared;
		this.type = type;
		if (!isNameLegal(name)) throw new CompileError(ILLEGAL_VARIABLE_NAME, lineDeclared);
		this.name = name;
	}

    /**
     * Constructor called when giving a literal value
     * @param isFinal if the var is final
     * @param type the type of var
     * @param name the name of var
     * @param literalValue the literal value being assigned
     * @param lineDeclared the line declared
     * @throws CompileError thrown if illegal deceleration
     */
	public Variable(boolean isFinal, VariableType type, String name, String literalValue, int lineDeclared)
			throws CompileError {
		this(isFinal, type, name, lineDeclared, false);
		if(!checkTypeCompatibility(literalValue, type, lineDeclared)){
			throw new CompileError(INCOMPATIBLE_TYPE_ASSIGNMENT, lineDeclared);
		}
		hasValue = true;
	}

	public Variable(Variable other)	throws CompileError {
		this(other._final, other.type, other.name, other.lineDeclared, false);
		hasValue = other.hasValue;
	}

    /**
     * constructor that receives a variable assignment
     * @param isFinal if the var is final
     * @param type the type of var
     * @param name the name of var
     * @param rightVar the variable being assigned to it
     * @param lineDeclared the line declared
     * @throws CompileError thrown if illegal assignment
     */
	public Variable(boolean isFinal, VariableType type, String name, Variable rightVar, int lineDeclared)
			throws CompileError{
		this(isFinal, type, name, lineDeclared, false);
		if(!checkTypeCompatibility(type, rightVar.getType(), lineDeclared)){
			throw new CompileError(INCOMPATIBLE_TYPE_ASSIGNMENT, lineDeclared);
		}
		hasValue = true;
	}


    /**
     * @return the type
     */
	public VariableType getType(){
		return type;
	}

    /**
     * Converts the string to a valid variable
     * @param typeName the name of the type
     * @param lineNum the line declared
     * @return the variable type
     * @throws CompileError thrown if illegal type
     */
	public static VariableType typeNameToType(String typeName, int lineNum)throws CompileError{
		switch (typeName) {
			case "int":
				return VariableType.INT;
			case "double":
				return VariableType.DOUBLE;
			case "String":
				return VariableType.STRING;
			case "char":
				return VariableType.CHAR;
			case "boolean":
				return VariableType.BOOLEAN;
			default:
				throw new CompileError(UNKNOWN_VARIABLE_TYPE, lineNum);
		}
	}


    /**
     * Checks compatibility of right and left var
     * @param leftVar right variable
     * @param rightVar left variable
     * @param lineDeclared line in question
     * @return iff they are compatible
     * @throws CompileError thrown if not compatible
     */
	public static boolean checkTypeCompatibility
			(Variable leftVar, Variable rightVar, int lineDeclared) throws CompileError{
		return checkTypeCompatibility(leftVar.getType(), rightVar.getType(), lineDeclared);
	}

    /**
     *
     * Checks compatibility of right and left var
     * @param rightLiteral right literal
     * @param leftType left type
     * @param lineDeclared line in question
     * @return iff they are compatible
     * @throws CompileError thrown if not compatible
     */
	public static boolean checkTypeCompatibility
			(String rightLiteral, VariableType leftType, int lineDeclared) throws CompileError{
		VariableType rightType = checkType(rightLiteral);
		return checkTypeCompatibility(leftType, rightType, lineDeclared);
	}

    /**
     * Checks compatibility of right and left var
     * @param rightLiteral right literal
     * @param leftVar the left var
     * @param lineDeclared line in question
     * @return true iff they are compatible
     * @throws CompileError thrown if not compatible
     */
	public static boolean checkTypeCompatibility
			(String rightLiteral, Variable leftVar, int lineDeclared) throws CompileError{
		return checkTypeCompatibility(rightLiteral,leftVar.type,lineDeclared);
	}

	/**
	 * Checks compatibility of right and left type
	 * @param leftType type being assigned to
	 * @param rightType type being assigned to
	 * @param lineDeclared  line in question
	 * @return true iff they are compatible
	 * @throws CompileError thrown if not compatible
	 */
	public static boolean checkTypeCompatibility
			(VariableType leftType, VariableType rightType, int lineDeclared) throws CompileError{
		if (rightType == VariableType.UNKNOWN) return false;
		if (leftType == VariableType.INT){
			return (rightType == VariableType.INT);
		}else if (leftType == VariableType.DOUBLE){
			return (rightType == VariableType.INT || rightType == VariableType.DOUBLE );
		}else if (leftType == VariableType.BOOLEAN) {
			return (rightType == VariableType.INT ||
					rightType == VariableType.DOUBLE || rightType == VariableType. BOOLEAN);
		}else if (leftType == VariableType.CHAR){
			return (rightType == VariableType.CHAR);
		}else if (leftType == VariableType.STRING) {
			return (rightType == VariableType.STRING);
		}
		else throw new CompileError(INCOMPATIBLE_TYPE_ASSIGNMENT, lineDeclared);

	}

	/**
	 *
	 * @param literalValue A literal value.
	 * @return The type of value specified by the literal.
	 */
	private static VariableType checkType(String literalValue){
		if (literalValue.matches(REGEX_INT)){
			return VariableType.INT;
		}if (literalValue.matches(REGEX_DOUBLE)){
			return VariableType.DOUBLE;
		}if (literalValue.matches(REGEX_STRING)) {
			return VariableType.STRING;
		}if (literalValue.matches(REGEX_CHAR)) {
			return VariableType.CHAR;
		}if (literalValue.matches(REGEX_BOOLEAN)) {
			return VariableType.BOOLEAN;
		}
		return VariableType.UNKNOWN;

	}

	/**
	 * @param literalValue A string
	 * @return True if the string is a literal value of a variable type.
	 */
	public static boolean isVariableLiteral(String literalValue) {
		return (literalValue.matches(REGEX_INT) ||
				literalValue.matches(REGEX_DOUBLE) ||
				literalValue.matches(REGEX_STRING) ||
				literalValue.matches(REGEX_CHAR) ||
				literalValue.matches(REGEX_BOOLEAN));
	}

	/**
	 * @return true iff the string is a legal variable name
	 */
	public static boolean isNameLegal(String name) {
		return name.matches(REGEX_VARIABLE_NAME);
	}

}
