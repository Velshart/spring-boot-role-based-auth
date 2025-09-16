package me.mmtr.springbootrolebasedauth.enums;

public enum RoleName {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String formattedName;

    RoleName(String formattedName) {
        this.formattedName = formattedName;
    }

    public String getFormattedName() {
        return formattedName;
    }
}
