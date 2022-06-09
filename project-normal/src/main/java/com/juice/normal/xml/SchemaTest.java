package com.juice.normal.xml;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

public class SchemaTest {

    public static void main(String argv[]) throws Exception {
        InputStream xmlSource = GenericXmlParserTest.class.getClassLoader().getResourceAsStream("validate_schema_test_2.xml");
        InputStream rootsSource = GenericXmlParserTest.class.getClassLoader().getResourceAsStream("validate_schema_test_2.xml1.xsd");
        InputStream flSource = GenericXmlParserTest.class.getClassLoader().getResourceAsStream("validate_schema_test_2.xsd");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        Source[] sources = new StreamSource[2];
        sources[1] = new StreamSource(flSource);
        sources[0] = new StreamSource(rootsSource);
        Schema schema = schemaFactory.newSchema(sources);

        //当然xml文档中定义xsd文档的位置，直接用xml中定义的xsd文档位置即可,如下
        //Schema schema = schemaFactory.newSchema();//将使用xml中定义的xsd

        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlSource));


    }

}
