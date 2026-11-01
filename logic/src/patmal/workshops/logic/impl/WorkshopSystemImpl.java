package patmal.workshops.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;
import patmal.workshops.model.impl.StudentImpl;
import patmal.workshops.model.impl.TeamImpl;
import patmal.workshops.model.impl.InstructorImpl;
import patmal.workshops.model.impl.WorkshopImpl;

/**
 * Missing single-object lookups return null; missing collection queries return empty lists.
 * Required command arguments reject null. All returned lists are mutable shallow copies.
 */
public class WorkshopSystemImpl implements WorkshopSystem {
    private final Map<String, StudentImpl> students = new HashMap<>();

    private final Map<String, InstructorImpl> instructors = new HashMap<>();
    private final Map<Integer, WorkshopImpl> workshops = new HashMap<>();
    private int nextWorkshopId = 1;
    private final Map<Integer, Team> teams = new HashMap<>();
    private int nextTeamId = 1;

    public WorkshopSystemImpl() {
    }

    @Override
    public Instructor createInstructor(String id, String name, String email) {
        if (instructors.containsKey(id)) {
            throw new IllegalArgumentException("Instructor ID already exists: " + id);
        }
        InstructorImpl instructor = new InstructorImpl(id, name, email);
        instructors.put(id, instructor);
        return instructor;
    }

    @Override
    public Workshop createWorkshop(String name, String description) {
        WorkshopImpl workshop = new WorkshopImpl(nextWorkshopId, name, description);
        workshops.put(nextWorkshopId, workshop);
        nextWorkshopId++;
        return workshop;
    }

    @Override
    public void associateWorkshopWithInstructor(Instructor instructor, Workshop workshop) {
        if (instructor == null || workshop == null
                || instructors.get(instructor.getId()) != instructor
                || workshops.get(workshop.getId()) != workshop) {
            throw new IllegalArgumentException("Instructor and workshop must belong to this system");
        }
        WorkshopImpl registeredWorkshop = workshops.get(workshop.getId());
        InstructorImpl registeredInstructor = instructors.get(instructor.getId());
        registeredWorkshop.assignInstructor(registeredInstructor);
    }

    @Override
    public Student createStudent(String id, String name, String email) {
        if (students.containsKey(id)) {
            throw new IllegalArgumentException("Student ID already exists: " + id);
        }
        StudentImpl student = new StudentImpl(id, name, email);
        students.put(id, student);
        return student;
    }

    @Override
    public Team createTeam(String name, Workshop workshop, List<String> members) {
        if (name == null || workshop == null || members == null) {
            throw new IllegalArgumentException("Team name, workshop and members must not be null");
        }
        if (workshops.get(workshop.getId()) != workshop) {
            throw new IllegalArgumentException("Workshop must belong to this system");
        }
        List<StudentImpl> resolvedMembers = new ArrayList<>();
        for (String studentId : members) {
            if (studentId == null) {
                throw new IllegalArgumentException("Student IDs must not be null");
            }
            StudentImpl student = students.get(studentId);
            if (student == null) {
                throw new IllegalArgumentException("Unknown student ID: " + studentId);
            }
            resolvedMembers.add(student);
        }
        Team team = new TeamImpl(nextTeamId, name, workshop, resolvedMembers);
        teams.put(nextTeamId, team);
        nextTeamId++;
        return team;
    }

    @Override
    public List<Team> getTeams() {
        return new ArrayList<>(teams.values());
    }

    @Override
    public Team getTeam(Integer id) {
        return teams.get(id);
    }

    @Override
    public List<Student> getStudents() {
        return new ArrayList<>(students.values());
    }

    @Override
    // Unknown IDs return null, as permitted by the assignment.
    public Student getStudent(String id) {
        return students.get(id);
    }

    @Override
    public List<Workshop> getWorkshops() {
        return new ArrayList<>(workshops.values());
    }

    @Override
    public Workshop getWorkshop(Integer id) {
        return workshops.get(id);
    }

    @Override
    public List<Instructor> getInstructors() {
        return new ArrayList<>(instructors.values());
    }

    @Override
    public Instructor getInstructor(String id) {
        return instructors.get(id);
    }

    @Override
    public List<Student> getFreeStudents() {
        List<Student> freeStudents = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.getTeamName() == null) {
                freeStudents.add(student);
            }
        }
        return freeStudents;
    }

    @Override
    public List<Student> getStudentsOfInstructor(String instructorId) {
        List<Student> result = new ArrayList<>();
        Instructor instructor = instructors.get(instructorId);
        if (instructor == null) {
            return result;
        }
        for (Student student : students.values()) {
            if (student.getInstructor() == instructor) {
                result.add(student);
            }
        }
        return result;
    }

    @Override
    public List<Student> getStudentsOfWorkshop(Integer workshopId) {
        List<Student> result = new ArrayList<>();
        Workshop workshop = workshops.get(workshopId);
        if (workshop == null) {
            return result;
        }
        for (Team team : teams.values()) {
            if (team.getWorkshop() == workshop) {
                result.addAll(team.getStudents());
            }
        }
        return result;
    }
}
