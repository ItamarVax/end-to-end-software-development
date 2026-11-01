package patmal.workshops.model.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;

public class TeamImpl implements Team {
    private final Integer id;
    private final String name;
    private final Workshop workshop;
    private final List<StudentImpl> students;

    public TeamImpl(Integer id, String name, Workshop workshop, List<StudentImpl> members) {
        if (id == null || name == null || workshop == null || members == null) {
            throw new IllegalArgumentException("Team arguments must not be null");
        }
        List<StudentImpl> validatedMembers = new ArrayList<>(members);
        if (validatedMembers.isEmpty()) {
            throw new IllegalArgumentException("A team must contain at least one student");
        }
        Set<String> memberIds = new HashSet<>();
        for (StudentImpl student : validatedMembers) {
            if (student == null) {
                throw new IllegalArgumentException("Team members must not be null");
            }
            if (!memberIds.add(student.getId())) {
                throw new IllegalArgumentException("Duplicate team member: " + student.getId());
            }
            if (student.getTeam() != null) {
                throw new IllegalArgumentException("Student already belongs to a team: " + student.getId());
            }
        }

        this.id = id;
        this.name = name;
        this.workshop = workshop;
        this.students = validatedMembers;
        // Every member is valid before either side of membership is changed.
        for (StudentImpl student : students) {
            student.joinTeam(this);
        }
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<Student> getStudents() {
        return new ArrayList<>(students);
    }

    @Override
    public Workshop getWorkshop() {
        return workshop;
    }
}
