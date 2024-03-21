package ru.prokdo.model.database.operational;

import ru.prokdo.model.database.EntityType;


public final class User extends OperationalEntity {
    private String password;
    private boolean isSuper;

    public User(int id, String name, String password, boolean isSuper) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.isSuper = isSuper;

        this.entityType = EntityType.USER;
    }
    public String getPassword() {
        return this.password;
    }

    public boolean isSuper() {
        return this.isSuper;
    }

    public void setPassword(String password) {
        if (password == null) throw new NullPointerException("User password cannot be null");

        this.password = password;
    }

    public void setIsSuper(boolean isSuper) {
        this.isSuper = isSuper;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, name=%s, password=%s, isSuper=%b]", this.id, this.name, this.password, this.isSuper);
    }
}
