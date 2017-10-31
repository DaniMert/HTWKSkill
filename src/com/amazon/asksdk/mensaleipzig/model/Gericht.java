package com.amazon.asksdk.mensaleipzig.model;


import java.util.LinkedList;

/**
 * Created by danielmertens on 25.10.17.
 */
public class Gericht {
    private String kategorie;
    private LinkedList<String> tags;
    private LinkedList<String> komponenten;
    private double preis;

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public LinkedList<String> getTags() {
        return tags;
    }

    public void setTags(LinkedList<String> tags) {
        this.tags = tags;
    }

    public LinkedList<String> getKomponenten() {
        return komponenten;
    }

    public void setKomponenten(LinkedList<String> komponenten) {
        this.komponenten = komponenten;
    }

    public double getPreis() {
        return preis;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }
}
