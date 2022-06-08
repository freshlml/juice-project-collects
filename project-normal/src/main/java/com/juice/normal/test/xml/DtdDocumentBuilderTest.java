package com.juice.normal.test.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

public class DtdDocumentBuilderTest {

    public static void main(String argv[]) throws Exception {
        InputStream xmlIn = GenericXmlParserTest.class.getClassLoader().getResourceAsStream("com/freshjuice/fl/xml/validate_test.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        factory.setValidating(true);  //开启dtd校验
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "all"); //external DTD的允许加载协议
        factory.setNamespaceAware(true);

        factory.setCoalescing(false);
        factory.setIgnoringComments(true);
        factory.setExpandEntityReferences(false);
        factory.setIgnoringElementContentWhitespace(false);

        DocumentBuilder builder = factory.newDocumentBuilder();

        //使用EntityResolver加载external DTD
        builder.setEntityResolver(new ClasspathEntityResolver("com/freshjuice/fl/xml/validate_test.dtd"));
        builder.setErrorHandler(new DefaultErrorHandler());
        Document document = builder.parse(new InputSource(xmlIn));

        //xpath
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        Node result = evl(xpath, "/mapper/select", document, Node.class);
        System.out.println(result.getTextContent());

    }

    public static <T> T evl(XPath xpath, String expression, Object item, Class<T> clazz) throws Exception {
        QName qname = XPathConstants.STRING;
        if(clazz == Node.class)
           qname = XPathConstants.NODE;
        if(clazz == NodeList.class)
            qname = XPathConstants.NODESET;
        if(clazz == String.class)
            qname = XPathConstants.STRING;
        if(clazz == Boolean.class)
            qname = XPathConstants.BOOLEAN;
        if(Number.class.isAssignableFrom(clazz))
            qname = XPathConstants.NUMBER;
        return (T) xpath.evaluate(expression, item, qname);
    }
}
