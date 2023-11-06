package org.example.Parsers;

import org.example.Models.Gun;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class XMLParser {
    abstract public List<Gun> parse(String xmlPath);

    public List<Gun> parse(String xmlPath, String xsdPath) {
        return validate(xmlPath, xsdPath) ? parse(xmlPath) : null;
    }

    protected boolean validate(String xmlPath, String xsdPath) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdPath));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlPath)));
        } catch (SAXException | IOException e) {
            System.out.println("Error" + e.getMessage().toString());
            return false;
        }
        return true;
    }
}
