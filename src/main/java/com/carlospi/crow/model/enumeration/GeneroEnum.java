package com.carlospi.crow.model.enumeration;

public enum GeneroEnum {
    MASCULINO,
    FEMENINO,
    NO_BINARIO,
    OTRO;

    public static GeneroEnum transformToUpperCase(String genero) {
        return GeneroEnum.valueOf(genero.toUpperCase());
    }
}
