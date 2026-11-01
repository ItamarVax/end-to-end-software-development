package patmal.workshops.logic.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Workshop;

import static org.junit.jupiter.api.Assertions.*;

class InputValidationTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void rejectsNullStudentArgumentsWithoutRegistering(int index) {
        WorkshopSystem system = new WorkshopSystemImpl();
        String[] arguments = {"s1", "Alice", "alice@example.com"};
        arguments[index] = null;
        assertThrows(IllegalArgumentException.class,
                () -> system.createStudent(arguments[0], arguments[1], arguments[2]));
        assertTrue(system.getStudents().isEmpty());
        assertNotNull(system.createStudent("s1", "Alice", "alice@example.com"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2})
    void rejectsNullInstructorArgumentsWithoutRegistering(int index) {
        WorkshopSystem system = new WorkshopSystemImpl();
        String[] arguments = {"i1", "Dana", "dana@example.com"};
        arguments[index] = null;
        assertThrows(IllegalArgumentException.class,
                () -> system.createInstructor(arguments[0], arguments[1], arguments[2]));
        assertTrue(system.getInstructors().isEmpty());
        assertNotNull(system.createInstructor("i1", "Dana", "dana@example.com"));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void rejectedWorkshopCreationDoesNotConsumeId(int index) {
        WorkshopSystem system = new WorkshopSystemImpl();
        Workshop first = system.createWorkshop("First", "Description");
        String[] arguments = {"Java", "Core Java"};
        arguments[index] = null;
        assertThrows(IllegalArgumentException.class,
                () -> system.createWorkshop(arguments[0], arguments[1]));
        assertEquals(1, system.getWorkshops().size());
        assertSame(first, system.getWorkshop(1));
        assertEquals(2, system.createWorkshop("Second", "Description").getId());
    }

    @Test
    void nullLookupsAreAbsentAndDoNotChangeRegistrations() {
        WorkshopSystem system = new WorkshopSystemImpl();
        system.createStudent("s1", "Alice", "alice@example.com");
        system.createInstructor("i1", "Dana", "dana@example.com");
        system.createWorkshop("Java", "Core");
        assertNull(system.getStudent(null));
        assertNull(system.getInstructor(null));
        assertNull(system.getWorkshop(null));
        assertEquals(1, system.getStudents().size());
        assertEquals(1, system.getInstructors().size());
        assertEquals(1, system.getWorkshops().size());
    }

    @Test
    void invalidAssociationLeavesExistingRelationshipUntouched() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Instructor dana = system.createInstructor("i1", "Dana", "dana@example.com");
        Workshop java = system.createWorkshop("Java", "Core");
        system.associateWorkshopWithInstructor(dana, java);
        WorkshopSystem other = new WorkshopSystemImpl();
        Instructor foreign = other.createInstructor("i1", "Other", "other@example.com");
        Workshop foreignWorkshop = other.createWorkshop("Other", "Foreign");
        assertThrows(IllegalArgumentException.class,
                () -> system.associateWorkshopWithInstructor(null, java));
        assertThrows(IllegalArgumentException.class,
                () -> system.associateWorkshopWithInstructor(dana, null));
        assertThrows(IllegalArgumentException.class,
                () -> system.associateWorkshopWithInstructor(foreign, java));
        assertThrows(IllegalArgumentException.class,
                () -> system.associateWorkshopWithInstructor(dana, foreignWorkshop));
        assertSame(dana, java.getInstructor());
        assertEquals(1, dana.getWorkshops().size());
        assertSame(java, dana.getWorkshops().get(0));
        assertTrue(foreign.getWorkshops().isEmpty());
        assertNull(foreignWorkshop.getInstructor());
    }

    @Test
    void nonNullBlankValuesAreNotRejectedByExtraBusinessRules() {
        WorkshopSystem system = new WorkshopSystemImpl();
        assertNotNull(system.createStudent("", "", ""));
        assertNotNull(system.createInstructor("", "", ""));
        assertEquals(1, system.createWorkshop("", "").getId());
    }
}
