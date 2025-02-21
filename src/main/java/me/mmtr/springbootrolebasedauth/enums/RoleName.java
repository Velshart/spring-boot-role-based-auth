package me.mmtr.springbootrolebasedauth.enums;

public enum RoleName {
    ADMIN,
    USER;

    public String getFormattedName() {
        return "ROLE_" + this.name();
    }
}
