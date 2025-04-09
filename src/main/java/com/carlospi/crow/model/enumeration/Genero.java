package com.carlospi.crow.model.enumeration;

public enum Genero {
    MASCULINO,
    FEMENINO,
    NO_BINARIO,
    OTRO;

    public static Genero transformToUpperCase(String genero) {
        return Genero.valueOf(genero.toUpperCase());
    }
}
