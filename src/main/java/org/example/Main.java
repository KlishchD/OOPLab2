package org.example;

import org.example.Models.Gun;
import org.example.Parsers.StAXXMLParser;
import org.example.Parsers.XMLParser;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        XMLParser parser = new StAXXMLParser();
        List<Gun> guns = parser.parse("src/main/resources/gun.xml", "src/main/resources/gun.xsd");

        if (guns != null) {
            guns.sort(new Gun.GunComparator());
            System.out.println(guns.stream().map(gun -> gun.toString() + "\n").reduce((a, b) -> a + b).get());
        } else {
            System.out.println("Was not able to load xml file");
        }
    }
}