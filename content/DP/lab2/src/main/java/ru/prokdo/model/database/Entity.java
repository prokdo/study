package ru.prokdo.model.database;


public abstract class Entity {
    protected EntityType entityType;

    public EntityType getEntityType() {
        return this.entityType;
    }
}
