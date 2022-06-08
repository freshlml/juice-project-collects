package com.juice.normal.test.xml;


import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class StaxParserTest {


    public static void main(String argv[]) throws Exception {

        XMLInputFactory factory = XMLInputFactory.newInstance();
        InputStream in1 = GenericXmlParserTest.class.getClassLoader().getResourceAsStream("com/freshjuice/fl/xml/validate_no_namespace_test.xml");
        XMLStreamReader reader = factory.createXMLStreamReader(in1);

        while(reader.hasNext()) {
            int type = reader.next();
            switch(type) {
                case XMLStreamReader.START_DOCUMENT:
                    System.out.println("start_document: version=" + reader.getVersion() + ";encoding=" + reader.getEncoding());
                    break;
                case XMLStreamReader.END_DOCUMENT:
                    System.out.println("end_document");
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    System.out.println("start_element: prefix=" + reader.getPrefix() + ";localName=" + reader.getLocalName() + ";namespaceURI=" + reader.getNamespaceURI());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    System.out.println("end_element: prefix=" + reader.getPrefix() + ";localName=" + reader.getLocalName() + ";namespaceURI=" + reader.getNamespaceURI());
                    break;
                case XMLStreamConstants.CHARACTERS:
                    System.out.println("CHARACTERS: text=" + reader.getText());
                    break;
                case XMLStreamConstants.CDATA:
                    System.out.println("CDATA: text=" + reader.getText());
                    break;
                case XMLStreamConstants.SPACE:
                    System.out.println("space: text=" + reader.getText());
                    break;
                case XMLStreamConstants.NAMESPACE:
                    System.out.println("namespace: xmlns=" + reader.getAttributeNamespace(0) + ";localName=" + reader.getAttributeLocalName(0) + ";prefix=" +reader.getAttributePrefix(0));
                    break;


            }
        }



    }



}
