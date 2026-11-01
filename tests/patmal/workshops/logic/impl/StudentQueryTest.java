package patmal.workshops.logic.impl;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Workshop;

import static org.junit.jupiter.api.Assertions.*;

class StudentQueryTest {
    @Test
    void freeStudentQueryReflectsRegistrationsAndMembershipChanges() {
        WorkshopSystem system = new WorkshopSystemImpl();
        assertTrue(system.getFreeStudents().isEmpty());
        Workshop workshop = system.createWorkshop("Java", "Core");
        Student first = system.createStudent("s1", "Alice", "alice@example.com");
        Student second = system.createStudent("s2", "Ben", "ben@example.com");
        assertStudents(system.getFreeStudents(), first, second);
        system.getFreeStudents().clear();
        assertStudents(system.getFreeStudents(), first, second);
        system.createTeam("", workshop, List.of("s1"));
        assertStudents(system.getFreeStudents(), second);
        Student third = system.createStudent("s3", "Dana", "dana@example.com");
        assertStudents(system.getFreeStudents(), second, third);
        system.createTeam("Beta", workshop, List.of("s2", "s3"));
        assertTrue(system.getFreeStudents().isEmpty());
    }

    @Test
    void workshopQueryIncludesAllItsTeamsAndExcludesOtherWorkshopsAndFreeStudents() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Workshop java = system.createWorkshop("Java", "Core");
        Workshop web = system.createWorkshop("Web", "Other");
        Workshop empty = system.createWorkshop("Empty", "No teams");
        Student first = system.createStudent("s1", "Alice", "alice@example.com");
        Student second = system.createStudent("s2", "Ben", "ben@example.com");
        Student third = system.createStudent("s3", "Dana", "dana@example.com");
        system.createStudent("free", "Free", "free@example.com");
        system.createTeam("Alpha", java, List.of("s1"));
        system.createTeam("Beta", java, List.of("s2"));
        system.createTeam("Gamma", web, List.of("s3"));
        assertStudents(system.getStudentsOfWorkshop(java.getId()), first, second);
        assertStudents(system.getStudentsOfWorkshop(web.getId()), third);
        assertTrue(system.getStudentsOfWorkshop(empty.getId()).isEmpty());
        system.getStudentsOfWorkshop(java.getId()).clear();
        assertStudents(system.getStudentsOfWorkshop(java.getId()), first, second);
    }

    @Test
    void instructorQuerySpansWorkshopsAndReflectsAssociationChangesAndLaterTeams() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Instructor dana = system.createInstructor("i1", "Dana", "dana@example.com");
        Instructor noa = system.createInstructor("i2", "Noa", "noa@example.com");
        Workshop java = system.createWorkshop("Java", "Core");
        Workshop web = system.createWorkshop("Web", "Other");
        Student first = system.createStudent("s1", "Alice", "alice@example.com");
        Student second = system.createStudent("s2", "Ben", "ben@example.com");
        system.createTeam("Alpha", java, List.of("s1"));
        system.createTeam("Beta", web, List.of("s2"));
        assertTrue(system.getStudentsOfInstructor("i1").isEmpty());
        system.associateWorkshopWithInstructor(dana, java);
        system.associateWorkshopWithInstructor(dana, web);
        assertStudents(system.getStudentsOfInstructor("i1"), first, second);
        assertTrue(system.getStudentsOfInstructor("i2").isEmpty());
        system.getStudentsOfInstructor("i1").clear();
        assertStudents(system.getStudentsOfInstructor("i1"), first, second);
        system.associateWorkshopWithInstructor(noa, web);
        assertStudents(system.getStudentsOfInstructor("i1"), first);
        assertStudents(system.getStudentsOfInstructor("i2"), second);
        Student third = system.createStudent("s3", "Chris", "chris@example.com");
        system.createTeam("Gamma", web, List.of("s3"));
        assertStudents(system.getStudentsOfInstructor("i2"), second, third);
    }

    @Test
    void nullAndUnknownQueryIdsReturnMutableEmptyLists() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Student student = system.createStudent("s1", "Alice", "alice@example.com");
        for (List<Student> result : List.of(system.getStudentsOfInstructor(null),
                system.getStudentsOfInstructor("missing"), system.getStudentsOfWorkshop(null),
                system.getStudentsOfWorkshop(999))) {
            assertNotNull(result);
            assertTrue(result.isEmpty());
            result.add(student);
        }
        assertTrue(system.getStudentsOfInstructor(null).isEmpty());
        assertTrue(system.getStudentsOfWorkshop(null).isEmpty());
    }

    private static void assertStudents(List<Student> actual, Student... expected) {
        assertNotNull(actual);
        assertEquals(expected.length, actual.size());
        assertTrue(actual.containsAll(Arrays.asList(expected)));
    }
}
