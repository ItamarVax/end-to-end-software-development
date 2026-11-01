package patmal.workshops.logic.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Student;
import patmal.workshops.api.model.Team;
import patmal.workshops.api.model.Workshop;

import static org.junit.jupiter.api.Assertions.*;

class TeamRegistrationTest {
    @Test
    void registersTeamsAndDerivesMembershipUsingIdentityRatherThanNames() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Workshop workshop = system.createWorkshop("Java", "Core");
        system.createWorkshop("Web", "Web");
        Student first = system.createStudent("s1", "Alex", "first@example.com");
        Student second = system.createStudent("s2", "Alex", "second@example.com");
        Student solo = system.createStudent("s3", "Solo", "solo@example.com");
        Team alpha = system.createTeam("Alpha", workshop, List.of("s1", "s2"));
        Team beta = system.createTeam("Beta", workshop, List.of("s3"));
        assertEquals(1, alpha.getId());
        assertEquals(2, beta.getId());
        assertSame(alpha, system.getTeam(1));
        assertSame(workshop, alpha.getWorkshop());
        assertEquals("Alpha", first.getTeamName());
        assertEquals("Alpha", second.getTeamName());
        assertEquals(List.of(second), first.getTeamMates());
        assertEquals(List.of(first), second.getTeamMates());
        assertTrue(solo.getTeamMates().isEmpty());
        assertEquals(2, alpha.getStudents().size());
        assertTrue(alpha.getStudents().containsAll(List.of(first, second)));
        assertEquals(2, system.getTeams().size());
        assertEquals("Beta", beta.getName());
    }

    @Test
    void studentInstructorReflectsLaterAssociationAndReassignment() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Workshop workshop = system.createWorkshop("Java", "Core");
        Student student = system.createStudent("s1", "Alice", "alice@example.com");
        system.createTeam("Alpha", workshop, List.of("s1"));
        assertNull(student.getInstructor());
        Instructor dana = system.createInstructor("i1", "Dana", "dana@example.com");
        Instructor noa = system.createInstructor("i2", "Noa", "noa@example.com");
        system.associateWorkshopWithInstructor(dana, workshop);
        assertSame(dana, student.getInstructor());
        system.associateWorkshopWithInstructor(noa, workshop);
        assertSame(noa, student.getInstructor());
        assertEquals("Alpha", student.getTeamName());
    }

    @Test
    void listCopiesProtectRegistryAndTeamMembershipInBothDirections() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Workshop workshop = system.createWorkshop("Java", "Core");
        Student first = system.createStudent("s1", "Alice", "alice@example.com");
        Student second = system.createStudent("s2", "Ben", "ben@example.com");
        List<String> input = new ArrayList<>(List.of("s1", "s2"));
        Team team = system.createTeam("Alpha", workshop, input);
        input.clear();
        team.getStudents().clear();
        first.getTeamMates().clear();
        system.getTeams().clear();
        assertSame(team, system.getTeam(1));
        assertEquals(1, system.getTeams().size());
        assertEquals(2, team.getStudents().size());
        assertEquals(List.of(second), first.getTeamMates());
        assertEquals(List.of(first), second.getTeamMates());
    }

    @ParameterizedTest
    @ValueSource(strings = {"unknown", "duplicate", "assigned", "null-member", "empty",
            "null-list", "null-name", "null-workshop", "foreign"})
    void rejectedTeamRequestsPreserveMembershipAndNextId(String invalidCase) {
        WorkshopSystem system = new WorkshopSystemImpl();
        Workshop workshop = system.createWorkshop("Java", "Core");
        Student first = system.createStudent("s1", "Alice", "alice@example.com");
        Student second = system.createStudent("s2", "Ben", "ben@example.com");
        Student assigned = system.createStudent("s3", "Dana", "dana@example.com");
        Team original = system.createTeam("Original", workshop, List.of("s3"));
        Workshop foreign = new WorkshopSystemImpl().createWorkshop("Foreign", "Other system");
        List<String> members = switch (invalidCase) {
            case "unknown" -> List.of("s1", "missing");
            case "duplicate" -> List.of("s1", "s1");
            case "assigned" -> List.of("s1", "s3");
            case "null-member" -> Arrays.asList("s1", null);
            case "empty" -> List.of();
            case "null-list" -> null;
            default -> List.of("s1", "s2");
        };
        String name = invalidCase.equals("null-name") ? null : "Rejected";
        Workshop candidate = switch (invalidCase) {
            case "null-workshop" -> null;
            case "foreign" -> foreign;
            default -> workshop;
        };
        assertThrows(IllegalArgumentException.class,
                () -> system.createTeam(name, candidate, members));
        assertNull(first.getTeamName());
        assertNull(second.getTeamName());
        assertNull(first.getInstructor());
        assertTrue(first.getTeamMates().isEmpty());
        assertEquals("Original", assigned.getTeamName());
        assertEquals(List.of(assigned), original.getStudents());
        assertEquals(1, system.getTeams().size());
        assertSame(original, system.getTeam(1));
        Team next = system.createTeam("Valid", workshop, List.of("s1", "s2"));
        assertEquals(2, next.getId());
        assertEquals("Valid", first.getTeamName());
        assertEquals("Valid", second.getTeamName());
    }

    @Test
    void emptyAndUnknownTeamQueriesFollowTheAgreedConvention() {
        WorkshopSystem system = new WorkshopSystemImpl();
        assertTrue(system.getTeams().isEmpty());
        assertNull(system.getTeam(999));
        assertNull(system.getTeam(null));
    }

    @Test
    void teamCountersBelongToEachSystemInstance() {
        for (int i = 0; i < 2; i++) {
            WorkshopSystem system = new WorkshopSystemImpl();
            Workshop workshop = system.createWorkshop("Java", "Core");
            system.createStudent("s1", "Alice", "alice@example.com");
            assertEquals(1, system.createTeam("Alpha", workshop, List.of("s1")).getId());
        }
    }
}
