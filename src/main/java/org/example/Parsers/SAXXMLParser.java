package org.example.Parsers;

import org.example.Models.Gun;
import org.example.Models.TTC;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


class GunsHandler extends DefaultHandler {
    private List<Gun> guns;
    private StringBuilder elementValue;

    private Gun gun;

    List<Gun> getGuns() {
        return guns;
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void startDocument() {
        guns = new ArrayList<>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        if (qName.equals(Gun.GUN)) {
            gun = new Gun();
            gun.setTtc(new TTC());
            gun.setId(Integer.parseInt(attributes.getValue(Gun.ID)));
        } else {
            this.elementValue = new StringBuilder();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        switch (qName) {
            case Gun.MODEL                -> { String data = elementValue.toString(); gun.setModel(data);                  }
            case Gun.HANDY                -> { String data = elementValue.toString(); gun.setHandy(data);                  }
            case Gun.ORIGIN               -> { String data = elementValue.toString(); gun.setOrigin(data);                 }
            case TTC.FIRING_RANGE         -> { String data = elementValue.toString(); gun.getTtc().setFiringRange(data);   }
            case TTC.SIGHTING_RANGE       -> { String data = elementValue.toString(); gun.getTtc().setSightingRange(data); }
            case TTC.MAGAZINE             -> { String data = elementValue.toString(); gun.getTtc().setMagazine(data);      }
            case TTC.SIGHT                -> { String data = elementValue.toString(); gun.getTtc().setSight(data);         }
            case Gun.MATERIAL             -> { String data = elementValue.toString(); gun.setMaterial(data);               }
            case Gun.GUN                  -> { guns.add(gun);                                                              }
        }
    }

    @Override
    public void endDocument() {
        gun = null;
        elementValue = null;
    }
}

public class SAXXMLParser extends XMLParser {
    private final SAXParser parser;
    private final GunsHandler handler;

    public SAXXMLParser() throws ParserConfigurationException, SAXException {
        parser = SAXParserFactory.newInstance().newSAXParser();
        handler = new GunsHandler();
    }

    @Override
    public List<Gun> parse(String path) {
        try {
            parser.parse(path, handler);
            return handler.getGuns();
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
