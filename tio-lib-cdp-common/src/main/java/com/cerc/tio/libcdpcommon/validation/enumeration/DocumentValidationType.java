package com.cerc.tio.libcdpcommon.validation.enumeration;

public enum DocumentValidationType {
    CPF, CNPJ;

    public static DocumentValidationType fromString(String value) {
        for (DocumentValidationType type : DocumentValidationType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        return null;
    }
}
