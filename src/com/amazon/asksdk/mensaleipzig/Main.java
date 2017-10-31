package com.amazon.asksdk.mensaleipzig;

import com.amazon.asksdk.mensaleipzig.helper.SpeechTextHelper;
import com.amazon.asksdk.mensaleipzig.model.Gericht;
import com.amazon.asksdk.mensaleipzig.network.MensaLeipzigRequest;

import java.util.LinkedList;

/**
 * Created by danielmertens on 25.10.17.
 */
public class Main {
    public static void main(String[] args) {
        LinkedList<Gericht> gerichte = MensaLeipzigRequest.getSpeiseplan("mensa academica", "2017-11-01");
        System.out.println(SpeechTextHelper.getSpeechTextFromSpeiseplan(gerichte, ""));
    }
}
