package edu.ucla.cs.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.ucla.cs.search.APIOracleAccess;

public class ExtractAPIOracle {
	
	private static String getShortName(String name) {
		String shortName = name;
		if(name.contains(".")) {
			shortName = name.substring(name.lastIndexOf('.') + 1);
		}
		
		return shortName;
	}
	
	public static void main(String[] args) throws XMLStreamException, SAXException, IOException, ParserConfigurationException {
		String xml = "/home/troy/research/Baker/inconsistencyinspectorresources/report/static/static_latest.xml";
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(new File(xml));
		NodeList nList = doc.getElementsByTagName("classDetails");
		APIOracleAccess assess = new APIOracleAccess();
		assess.connect();
		if(nList.getLength() > 0) {
			Node cdNode = nList.item(0);
			if(cdNode.getNodeType() == Node.ELEMENT_NODE) {
				Element cdElement = (Element) cdNode;
				NodeList nList2 = cdElement.getElementsByTagName("ce");
				for(int i = 0; i < nList2.getLength(); i++) {
					Node ceNode = nList2.item(i);
					if(ceNode.getNodeType() == Node.ELEMENT_NODE) {
						Element ceElement = (Element) ceNode;
						String className = ceElement.getAttribute("id");
						className = StringEscapeUtils.unescapeHtml4(className);
						if(!className.contains("$")) {
							// ignore inner class
							className = getShortName(className);
							NodeList nList3 = ceElement.getElementsByTagName("me");
							for(int j = 0; j < nList3.getLength(); j++) {
								Node meNode = nList3.item(j);
								if(meNode.getNodeType() == Node.ELEMENT_NODE) {
									Element meElement = (Element) meNode;
									String signature = meElement.getAttribute("id");
									signature = StringEscapeUtils.unescapeHtml4(signature);
									if(!signature.contains("(")) continue;
									String methodName = signature.substring(0, signature.indexOf('('));
									String arguments = signature.substring(signature.indexOf('(') + 1, signature.indexOf(')'));
									// remove the package qualifier
									methodName = getShortName(methodName);
									if(methodName.contains("$")) continue;
									if(methodName.equals("<init>")) {
										methodName = "new " + className;
									}
										
									ArrayList<String> argTypes = new ArrayList<String>();
									if(!arguments.isEmpty()) {
										for(String arg : arguments.split(", ")) {
											arg = getShortName(arg);
											argTypes.add(arg);
										}
									}
								
									// insert this oracle to the database
									assess.insertAPIOracle(className, methodName, argTypes);
								} 
							}
						}
					}
				}
			}
		}
		assess.close();
	}
}
