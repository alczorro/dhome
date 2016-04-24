/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.dhome.crawler.parser;



import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import nu.xom.XPathContext;

public abstract class XmlParser implements Parser{
	XPathContext context; 
    protected final String DEFAULT_SEPERATOR= ";";
	public Document doc;

	public XmlParser() {
		context = new XPathContext("ns", "http://www.w3.org/1999/xhtml");
	}

	public void addNamespace(String prefix, String uri) {
		context.addNamespace(prefix, uri);
	}
	public abstract Object parse();
	
	public Nodes getNodes(Node node, String xpath) {
		return node.query(xpath, context);
	}
	public Node getFirstNode(Node node, String xpath) {
		Nodes nodes = node.query(xpath, context);
		return (nodes.size() > 0) ? nodes.get(0):null;
	}
	public Node getNode(Node node, String xpath,int n) {
		Nodes nodes = node.query(xpath, context);
		return (nodes.size() > n) ? nodes.get(n):null;
	}

	public String getNodesValue(Node node, String xpath, String separator) {
		Nodes nodes = node.query(xpath, context);
		int size = nodes.size();
		if (size == 0)
			return "";
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			if(!nodes.get(i).getValue().equals("")){
			sb.append(nodes.get(i).getValue().trim());
			if (i != size-1)
				sb.append(separator);
			}
		}
		return sb.toString();		
	}	

	public String getFirstNodeValue(Node node, String xpath) {
		Nodes nodes = node.query(xpath, context);
		return (nodes.size() > 0) ? nodes.get(0).getValue().trim() : "";		
	}	
	public String getNodeValue(Node node, String xpath,int n) {
		Nodes nodes = node.query(xpath, context);
		return (nodes.size() > n) ? nodes.get(n).getValue().trim() : "";		
	}
	public String getFirstNodeAttributeValue(Node node, String xpath, String attributeName) {
		Nodes nodes = node.query(xpath, context);
		return (nodes.size() > 0) ? ((Element)nodes.get(0)).getAttributeValue(attributeName).trim() : "";
	}
	
	public  void setDocument(String filename) throws Exception {
		XMLReader parser = XMLReaderFactory.createXMLReader("org.ccil.cowan.tagsoup.Parser");
		Builder builder = new Builder(parser);
		doc = builder.build(new File(filename));
	}
	public void setDocument(InputStream in, String charset,String hostUrl ) throws Exception {
		Reader reader = new InputStreamReader(in, charset);
		XMLReader parser = XMLReaderFactory
				.createXMLReader("org.ccil.cowan.tagsoup.Parser");
		Builder builder = new Builder(parser);
		doc = builder.build(reader, hostUrl);
		reader.close();
	}
	public  Document getDocument() {
		return doc;
	}
	
	
}
