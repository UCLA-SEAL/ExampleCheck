package edu.ucla.cs.utils;

public class ASMTypeTranslater {

	public static String translate(String asmType){
		String type;
		
		if(asmType.startsWith("[")){
			type = translateArrayTypes(asmType);
		} else if(asmType.startsWith("L")){
			type = translateReferenceTypes(asmType);
		} else if(asmType.isEmpty()){
			return "";
		} else{
			type = translatePrimitiveTypes(asmType);
		}
		
		return type;
	}
	
	private static String translatePrimitiveTypes(String asmType){
		switch (asmType) {
		case "Z": return "boolean";
		case "C": return "char";
		case "B": return "byte";
		case "I": return "int";
		case "F": return "float";
		case "J": return "long";
		case "D": return "double";
		case "V": return "void";
		case "S": return "short";
		default: return null;
		}
	}
	
	private static String translateReferenceTypes(String asmType){
		String type = asmType.substring(1);
		return type.replace("/", ".").substring(0, type.length() - 1);
	}
	
	private static String translateArrayTypes(String asmType){
		int dims = 0;
		char temp;
		
		for(int i = 0; i < asmType.length(); i++){
			temp = asmType.charAt(i);
			
			if(temp == '['){
				dims++;
			}else{
				break;
			}
		}
		
		String type = asmType.substring(dims);
		type = translate(type);
		
		for(int i = 0; i < dims; i++){
			type += "[]";
		}
		
		return type;
	}
}
