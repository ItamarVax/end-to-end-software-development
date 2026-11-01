package patmal.workshops.model.impl;

import java.util.ArrayList;
import java.util.List;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;

public class StudentImpl extends PersonImpl implements Student {
    private TeamImpl team;

    public StudentImpl(String id, String name, String email) {
        super(id, name, email);
    }

    @Override
    public String getTeamName() {
        return team == null ? null : team.getName();
    }

    @Override
    public Instructor getInstructor() {
        return team == null ? null : team.getWorkshop().getInstructor();
    }

    @Override
    public List<Student> getTeamMates() {
        List<Student> teamMates = new ArrayList<>();
        if (team != null) {
            for (Student member : team.getStudents()) {
                if (member != this) {
                    teamMates.add(member);
                }
            }
        }
        return teamMates;
    }

    TeamImpl getTeam() {
        return team;
    }

    // Called only after TeamImpl has validated every member.
    void joinTeam(TeamImpl team) {
        this.team = team;
    }
}
