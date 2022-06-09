package com.juice.normal.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;

public class SchemaDocumentBuilderTest {

    public static void main(String argv[]) throws Exception {
        InputStream xmlIn = GenericXmlParserTest.class.getClassLoader().getResourceAsStream("validate_schema_test_2.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

        factory.setValidating(false);//此属性只控制DTD的校验,如果是使用Schema校验，设置成false即可
        factory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "all"); //schema的加载协议
        factory.setNamespaceAware(true); //schema 必须namespaceAware=true

        /*Schema校验*/
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(); //使用xml中定义的xsd
        factory.setSchema(schema);

        factory.setCoalescing(false);
        factory.setIgnoringComments(true);
        factory.setExpandEntityReferences(false);
        factory.setIgnoringElementContentWhitespace(false);

        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new DefaultErrorHandler());
        //builder.setEntityResolver(new ClasspathEntityResolver("com/freshjuice/fl/xml/*.xsd"));
        Document document = builder.parse(new InputSource(xmlIn));


        System.out.println("########XPath与名称空间###########");
        //名称空间与 XPath
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        xpath.setNamespaceContext(new DefaultNamespaceResolver(document));
        Node result = evlNode(xpath, "/edx:roots/edx:bb", document);
        System.out.println(result);
    }

    public static Node evlNode(XPath xpath, String expression, Object item) throws Exception {
        QName qname = XPathConstants.NODE;
        return (Node)xpath.evaluate(expression, item, qname);
    }
}
