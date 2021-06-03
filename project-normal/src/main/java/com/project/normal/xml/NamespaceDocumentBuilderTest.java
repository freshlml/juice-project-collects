package com.project.normal.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class NamespaceDocumentBuilderTest {

    public static void main(String argv[]) throws Exception {
        InputStream xmlIn = GenericXmlParserTest.class.getClassLoader().getResourceAsStream("namespace_test.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        factory.setValidating(false);
        factory.setNamespaceAware(true); //是否感知名称空间

        factory.setCoalescing(false); //是否将CDATA节点转换为Text节点，并将其附加到相邻（如果有）的 Text 节点，false
        factory.setIgnoringComments(true);                  //忽略comments节点
        factory.setExpandEntityReferences(false); //不允许实体引用
        factory.setIgnoringElementContentWhitespace(false); //不消除元素内容中的空格
        factory.setXIncludeAware(false); //不处理XInclude markup

        DocumentBuilder builder = factory.newDocumentBuilder();

        builder.setErrorHandler(new DefaultErrorHandler());
        Document document = builder.parse(new InputSource(xmlIn));

        NodeList nodes = document.getDocumentElement().getChildNodes();
        for(int i=0; i<nodes.getLength(); i++) {
            Node node = nodes.item(i);
            System.out.println(node.getNodeName() + "=" + node.getTextContent() + ";" + node.getNamespaceURI());
        }


    }


}
