package fr.fruityhedgeh0g.enums;

public enum RoleTypeEnum {
    ORGANIZATIONAL("organizational_role"),
    LEGAL("legal_role");

    final String name;

    RoleTypeEnum(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
