package patmal.workshops.model.impl;

import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Workshop;

public class WorkshopImpl implements Workshop {
    private final Integer id;
    private final String name;
    private final String description;
    private InstructorImpl instructor;

    public WorkshopImpl(Integer id, String name, String description) {
        if (id == null || name == null || description == null) {
            throw new IllegalArgumentException("Workshop ID, name and description must not be null");
        }
        this.id = id;
        this.name = name;
        this.description = description;
    }

    @Override
    public Integer getId() { return id; }

    @Override
    public String getName() { return name; }

    @Override
    public String getDescription() { return description; }

    @Override
    public Instructor getInstructor() { return instructor; }

    // Keep both sides consistent, including repeated assignment or reassignment.
    public void assignInstructor(InstructorImpl newInstructor) {
        if (newInstructor == null) {
            throw new IllegalArgumentException("Instructor must not be null");
        }
        if (instructor == newInstructor) {
            return;
        }
        if (instructor != null) {
            instructor.removeWorkshop(this);
        }
        instructor = newInstructor;
        instructor.addWorkshop(this);
    }
}
