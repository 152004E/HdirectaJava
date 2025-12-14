package com.exe.Huerta_directa.Strategy;


public enum Localidad {

    CHAPINERO(1.00),
    TEUSAQUILLO(1.05),
    BARRIOS_UNIDOS(1.10),
    USAQUEN(1.20),
    SUBA(1.30),
    ENGATIVA(1.35),
    FONTIBON(1.40),
    KENNEDY(1.50),
    BOSA(1.60),
    CIUDAD_BOLIVAR(1.70),
    USME(1.75),
    SUMAPAZ(2.50);

    private final double factor;

    Localidad(double factor) {
        this.factor = factor;
    }

    public double getFactor() {
        return factor;
    }
}
