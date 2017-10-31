package com.amazon.asksdk.mensaleipzig.helper;

import com.amazon.asksdk.mensaleipzig.model.Gericht;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SpeechTextHelper {
    public static String getSpeechTextFromSpeiseplan(LinkedList<Gericht> speiseplan, String requestedKategorie){
        if(speiseplan == null){
            return "Ich habe keinen Speiseplan gefunden.";
        }

        List<Gericht> removeThese = new ArrayList<>();
        for (Gericht gericht : speiseplan) {
            String kategorie = gericht.getKategorie();
            if (requestedKategorie != null && !requestedKategorie.isEmpty()) {
                boolean keep = false;
                for(String tag : gericht.getTags()){
                    if(tag.toLowerCase().equals(requestedKategorie))
                        keep = true;
                }
                if (!kategorie.toLowerCase().equals(requestedKategorie) && !keep) {
                    removeThese.add(gericht);
                }
            }
        }
        speiseplan.removeAll(removeThese);

        if(speiseplan.isEmpty()){
            return "Ich habe keine passenden Gerichte gefunden.";
        }

        StringBuilder speechText = new StringBuilder();
        String lastKategorie = null;
        for (Gericht gericht : speiseplan) {
            String kategorie = gericht.getKategorie();
            if(kategorie.equals(lastKategorie)){
                speechText.append(" oder ");
            } else {
                speechText.append(". Als ").append(kategorie).append(" gibt es ");
            }

            for (String komponente : gericht.getKomponenten()) {
                speechText.append(komponente).append(", ");
            }
            speechText = new StringBuilder(speechText.substring(0, speechText.length() - 1) + " für " + gericht.getPreis() + "€");

            lastKategorie = kategorie;
        }

        speechText = new StringBuilder(speechText.substring(2).replace("&", "und") + ".");
        return speechText.toString();
    }
}
