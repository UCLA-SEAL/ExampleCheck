package edu.ucla.cs.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import edu.ucla.cs.search.APIOracleAccess;

public class ExtractAPIOracleFromJarFile {
	private static final String CLASS_SUFFIX = ".class";
	private String jar;
	private URLClassLoader cl;
	
	String getShortName(String name) {
		String shortName = name;
		if(name.contains(".")) {
			shortName = name.substring(name.lastIndexOf('.') + 1);
		}
		
		return shortName;
	}

	public ExtractAPIOracleFromJarFile(String jarPath) {
		this.jar = jarPath;
		URL url;
		try {
			File file = new File(jarPath);
			URL url0 = file.toURI().toURL();
			url = new URL("jar:file://" + jarPath + "!/");
			cl = URLClassLoader.newInstance(new URL[] { url });
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public void extract() {
		APIOracleAccess access = new APIOracleAccess();
		access.connect();
		try {
			JarFile jf = new JarFile(jar);
			Enumeration<JarEntry> entryEnum = jf.entries();
			while (entryEnum.hasMoreElements()) {
				JarEntry je = entryEnum.nextElement();
				String name = je.getName();
				if (name.endsWith(CLASS_SUFFIX) && !name.contains("$")) { 
					// we do not process inner class here
					name = name.replaceAll("/", ".").substring(0,
							name.lastIndexOf("."));
					ClassNode node = readByteCodeFromJar(je, jf);
					if (node.superName != null
							&& node.superName.equals("java/lang/Enum")) {
						continue;
					}
					// extract methods
					if((node.access & Opcodes.ACC_PUBLIC) != 0) {
						extractMethods(node, name, access);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			access.close();
		}
	}

	public ClassNode readByteCodeFromJar(JarEntry entry, JarFile jar) {
		try {
			InputStream is = jar.getInputStream(entry);
			ClassReader reader = new ClassReader(is);
			ClassNode node = new ClassNode();
			reader.accept(node, 0);
			return node;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void extractMethods(ClassNode node, String className, APIOracleAccess access) {
		try {
			List<MethodNode> ms = node.methods;
			for (MethodNode m : ms) {
				if((node.access & Opcodes.ACC_PUBLIC) == 0) {
					continue;
				}
				String methodName = m.name;
				Type[] argTypes = Type.getArgumentTypes(m.desc);
//				String returnType = Type.getReturnType(m.desc).toString();
//				if (mname.equals("<init>") || mname.equals("<clinit>")) {
//					mname = node.name.substring(node.name.lastIndexOf('/') + 1);
//					returnType = "L" + node.name + ";";
//				}
				
				if(methodName.contains("$")) continue;
				
				className = getShortName(className);
				if (methodName.equals("<init>") || methodName.equals("<clinit>")) {
					methodName = "new " + className;
				}
				
				ArrayList<String> arguments = new ArrayList<String>();
				for(Type t : argTypes) {
					String type = ASMTypeTranslater.translate(t.toString());
					arguments.add(getShortName(type));
				}
				
				access.insertAPIOracle(className, methodName, arguments);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ExtractAPIOracleFromJarFile e = new ExtractAPIOracleFromJarFile(
				"/home/troy/research/Baker/inconsistencyinspectorresources/rt.jar");
		e.extract();
	}
}
