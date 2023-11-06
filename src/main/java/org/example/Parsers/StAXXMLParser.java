package org.example.Parsers;

import org.example.Models.Gun;
import org.example.Models.TTC;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class StAXXMLParser extends XMLParser{
    private Gun gun;

    @Override
    public List<Gun> parse(String path) {
        try {
            XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(new FileInputStream(path));
            List<Gun> guns = new ArrayList<>();

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                if (event.isStartDocument()) {
                    startDocument(event, reader, guns);
                } else if (event.isStartElement()) {
                    startElement(event, reader, guns);
                } else if (event.isEndElement()) {
                    endElement(event, reader, guns);
                } else if (event.isEndDocument()) {
                    endDocument(event, reader, guns);
                }
            }

            return guns;
        } catch (XMLStreamException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void startDocument(XMLEvent event, XMLEventReader reader, List<Gun> guns) {
    }

    private void startElement(XMLEvent event, XMLEventReader reader, List<Gun> guns) throws XMLStreamException {
        StartElement element = event.asStartElement();
        switch (element.getName().getLocalPart()) {
            case Gun.MODEL             -> { event = reader.nextEvent(); String data = event.asCharacters().getData(); gun.setModel(data);                  }
            case Gun.HANDY             -> { event = reader.nextEvent(); String data = event.asCharacters().getData(); gun.setHandy(data);                  }
            case Gun.ORIGIN            -> { event = reader.nextEvent(); String data = event.asCharacters().getData(); gun.setOrigin(data);                 }
            case Gun.MATERIAL          -> { event = reader.nextEvent(); String data = event.asCharacters().getData(); gun.setMaterial(data);               }
            case TTC.FIRING_RANGE      -> { event = reader.nextEvent(); String data = event.asCharacters().getData(); gun.getTtc().setFiringRange(data);   }
            case TTC.SIGHTING_RANGE    -> { event = reader.nextEvent(); String data = event.asCharacters().getData(); gun.getTtc().setSightingRange(data); }
            case TTC.MAGAZINE          -> { event = reader.nextEvent(); String data = event.asCharacters().getData(); gun.getTtc().setMagazine(data);      }
            case TTC.SIGHT             -> { event = reader.nextEvent(); String data = event.asCharacters().getData(); gun.getTtc().setSight(data);         }
            case Gun.GUN -> {
                gun = new Gun();
                gun.setTtc(new TTC());

                String data = element.getAttributeByName(QName.valueOf(Gun.ID)).getValue();
                gun.setId(data);
            }
        }
    }

    private void endElement(XMLEvent event, XMLEventReader reader, List<Gun> guns) {
        EndElement element = event.asEndElement();
        String name = element.getName().getLocalPart();
        if (name.equals(Gun.GUN) && gun != null) {
            guns.add(gun);
            gun = null;
        }
    }

    private void endDocument(XMLEvent event, XMLEventReader reader, List<Gun> guns) {
    }
}
