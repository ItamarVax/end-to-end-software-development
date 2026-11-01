package patmal.workshops.model.impl;

import patmal.workshops.api.model.Person;

public abstract class PersonImpl implements Person {
    private final String id;
    private final String name;
    private final String email;

    protected PersonImpl(String id, String name, String email) {
        if (id == null || name == null || email == null) {
            throw new IllegalArgumentException("Person ID, name and email must not be null");
        }
        this.id = id;
        this.name = name;
        this.email = email;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getEmail() {
        return email;
    }
}
