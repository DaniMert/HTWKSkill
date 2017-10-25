package com.amazon.asksdk.mensaleipzig.model;

import java.util.LinkedList;

/**
 * Created by danielmertens on 25.10.17.
 */
public class Gericht {
    private String kategorie;
    private LinkedList<String> komponenten;
    private double preis;

    public String getKategorie() {
        return kategorie;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
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
