package com.amazon.asksdk.mensaleipzig;

import com.amazon.asksdk.mensaleipzig.model.Gericht;

import java.util.LinkedList;

/**
 * Created by danielmertens on 25.10.17.
 */
public class Main {
    public static void main(String[] args) {
        LinkedList<Gericht> gerichte = MensaLeipzigRequest.getSpeiseplan("Mensa Tierklinik", "2017-10-25");
        String speechText = "";
        for (Gericht gericht : gerichte) {
            speechText = speechText + "Als " + gericht.getKategorie() + " gibt es ";

            for (String komponente : gericht.getKomponenten()) {
                speechText = speechText + komponente + ", ";
            }
            String[] preis = String.valueOf(gericht.getPreis()).split("\\.");
            speechText = speechText.substring(0, speechText.length() - 1) + " für " + preis[0] + " Euro " + preis[1] + ". ";
        }
        System.out.println(speechText);
    }
}
