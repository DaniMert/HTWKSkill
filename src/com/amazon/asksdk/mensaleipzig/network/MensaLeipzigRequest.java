package com.amazon.asksdk.mensaleipzig.network;

import com.amazon.asksdk.mensaleipzig.model.Gericht;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by danielmertens on 25.10.17.
 */
public class MensaLeipzigRequest {
    private static HashMap<String, Integer> mensaCodes = new HashMap<>();

    static {
        mensaCodes.put("cafeteria dittrichring", 153);
        mensaCodes.put("cafeteria philipp-rosenthal-straße", 127);
        mensaCodes.put("mensa academica", 118);
        mensaCodes.put("mensa am park", 106);
        mensaCodes.put("mensa am elsterbecken", 115);
        mensaCodes.put("mensaria liebigstraße", 162);
        mensaCodes.put("mensa peterssteinweg", 111);
        mensaCodes.put("mensa schönauer straße", 140);
        mensaCodes.put("mensa tierklinik", 170);
    }

    public static LinkedList<Gericht> getSpeiseplan(String mensa, String datum) {
        String xmlResponse = null;
        try {
            String url = "https://www.studentenwerk-leipzig.de/XMLInterface/request?location=" + mensaCodes.get(mensa) + "&date=" + datum;
            xmlResponse = Unirest.get(url).asString().getBody();
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        return parseXML(xmlResponse);
    }

    private static LinkedList<Gericht> parseXML(String xml) {
        LinkedList<Gericht> gerichte = new LinkedList<>();
        if (xml != null) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;
            try {
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(xml)));

                NodeList groups = document.getElementsByTagName("group");
                for (int i = 0; i < groups.getLength(); i++){
                    Element group = (Element) groups.item(i);
                    Element kategorie = (Element) group.getElementsByTagName("name").item(0);
                    Element preis = (Element) group.getElementsByTagName("price").item(0);
                    NodeList komponentenList = group.getElementsByTagName("name1");
                    NodeList tagList = group.getElementsByTagName("tagging");

                    Gericht gericht = new Gericht();
                    gericht.setKategorie(kategorie.getTextContent());
                    gericht.setPreis(Double.parseDouble(preis.getTextContent()));
                    LinkedList<String> komponenten = new LinkedList<>();
                    for (int k = 0; k < komponentenList.getLength(); k++){
                        komponenten.add(komponentenList.item(k).getTextContent());
                    }
                    gericht.setKomponenten(komponenten);
                    LinkedList<String> tags = new LinkedList<>();
                    for (int k = 0; k < tagList.getLength(); k++){
                        tags.add(tagList.item(k).getTextContent());
                    }
                    gericht.setTags(tags);
                    gerichte.add(gericht);
                }

            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
        }
        return gerichte;
    }
}