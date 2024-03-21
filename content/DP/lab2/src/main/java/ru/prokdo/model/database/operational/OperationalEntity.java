package ru.prokdo.model.database.operational;

import ru.prokdo.model.database.Entity;


public abstract class OperationalEntity extends Entity implements Operational {
    protected int id;
    protected String name; 

    @Override
    public int getId() {
        return this.id;
    }
 
    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        if (name == null) throw new NullPointerException("Name cannot be null");

        this.name = name;
    }
}
