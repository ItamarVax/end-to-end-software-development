package patmal.workshops.logic.impl;

import java.util.List;
import org.junit.jupiter.api.Test;
import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Student;

import static org.junit.jupiter.api.Assertions.*;

class WorkshopSystemImplTest {
    @Test
    void freshSystemHasNoStudents() {
        WorkshopSystem system = new WorkshopSystemImpl();
        assertNotNull(system.getStudents());
        assertTrue(system.getStudents().isEmpty());
    }

    @Test
    void registersStudentsWithTheirDetailsAndRetrievesTheSameObjects() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Student alice = system.createStudent("s1", "Alice", "alice@example.com");
        Student ben = system.createStudent("s2", "Ben", "ben@example.com");

        assertEquals("s1", alice.getId());
        assertEquals("Alice", alice.getName());
        assertEquals("alice@example.com", alice.getEmail());
        assertEquals("s2", ben.getId());
        assertEquals("Ben", ben.getName());
        assertEquals("ben@example.com", ben.getEmail());
        assertSame(alice, system.getStudent("s1"));
        assertSame(ben, system.getStudent("s2"));
        assertNull(alice.getTeamName());
        assertNull(alice.getInstructor());
        assertTrue(alice.getTeamMates().isEmpty());
    }

    @Test
    void duplicateIdIsRejectedWithoutReplacingTheOriginalStudent() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Student alice = system.createStudent("s1", "Alice", "alice@example.com");

        assertThrows(IllegalArgumentException.class,
                () -> system.createStudent("s1", "Replacement", "other@example.com"));

        assertSame(alice, system.getStudent("s1"));
        assertEquals("Alice", system.getStudent("s1").getName());
        assertEquals("alice@example.com", system.getStudent("s1").getEmail());
        assertEquals(1, system.getStudents().size());
        Student ben = system.createStudent("s2", "Ben", "ben@example.com");
        assertSame(ben, system.getStudent("s2"));
        assertEquals(2, system.getStudents().size());
    }

    @Test
    void missingLookupReturnsNullWithoutChangingRegistrations() {
        WorkshopSystem system = new WorkshopSystemImpl();
        assertNull(system.getStudent("missing"));
        assertTrue(system.getStudents().isEmpty());
        Student alice = system.createStudent("s1", "Alice", "alice@example.com");
        assertNull(system.getStudent("missing"));
        assertEquals(1, system.getStudents().size());
        assertSame(alice, system.getStudent("s1"));
    }

    @Test
    void listingReturnsEveryStudentOnceAndProtectsRegistryMembership() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Student alice = system.createStudent("s1", "Alice", "alice@example.com");
        Student ben = system.createStudent("s2", "Ben", "ben@example.com");
        List<Student> result = system.getStudents();

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(student -> student == alice));
        assertTrue(result.stream().anyMatch(student -> student == ben));
        result.clear();
        result.add(alice);
        result.add(alice);

        assertEquals(2, system.getStudents().size());
        assertSame(alice, system.getStudent("s1"));
        assertSame(ben, system.getStudent("s2"));
    }

    @Test
    void systemsKeepRegistrationsIndependent() {
        WorkshopSystem first = new WorkshopSystemImpl();
        WorkshopSystem second = new WorkshopSystemImpl();
        Student alice = first.createStudent("s1", "Alice", "alice@example.com");
        assertNull(second.getStudent("s1"));
        assertTrue(second.getStudents().isEmpty());
        Student ben = second.createStudent("s1", "Ben", "ben@example.com");
        assertSame(alice, first.getStudent("s1"));
        assertSame(ben, second.getStudent("s1"));
        assertNotSame(alice, ben);
    }
}
