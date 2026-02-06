package fr.fruityhedgeh0g.enums;

import jdk.jfr.Description;

public enum RoleTypeEnum {
    ORGANIZATIONAL("organizational_role"),
    LEGAL("legal_role");

    String name;

    RoleTypeEnum(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
