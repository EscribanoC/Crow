package com.carlospi.crow.model.enumeration;

public enum GeneroEnum {
    MASCULINO,
    FEMENINO,
    OTRO;

    public static GeneroEnum transformToUpperCase(String genero) {
        return GeneroEnum.valueOf(genero.toUpperCase());
    }
}
