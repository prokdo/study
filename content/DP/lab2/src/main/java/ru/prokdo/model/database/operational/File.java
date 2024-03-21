package ru.prokdo.model.database.operational;

import ru.prokdo.model.database.EntityType;


public final class File extends OperationalEntity {
    public File(int id, String name) {
        this.id = id;
        this.name = name;

        this.entityType = EntityType.FILE;
    }

    @Override
    public String toString() {
        return String.format("File[id=%d, name=%s]", this.id, this.name);
    }
}
