package patmal.workshops.logic.impl;

import java.util.List;
import org.junit.jupiter.api.Test;
import patmal.workshops.api.logic.WorkshopSystem;
import patmal.workshops.api.model.Instructor;
import patmal.workshops.api.model.Workshop;

import static org.junit.jupiter.api.Assertions.*;

class InstructorWorkshopTest {
    @Test
    void instructorRegistrationUsesItsOwnIdNamespaceAndRejectsDuplicates() {
        WorkshopSystem system = new WorkshopSystemImpl();
        system.createStudent("same-id", "Alice", "alice@example.com");
        Instructor dana = system.createInstructor("same-id", "Dana", "dana@example.com");
        assertSame(dana, system.getInstructor("same-id"));
        assertEquals("same-id", dana.getId());
        assertEquals("Dana", dana.getName());
        assertEquals("dana@example.com", dana.getEmail());
        assertTrue(dana.getWorkshops().isEmpty());
        assertThrows(IllegalArgumentException.class,
                () -> system.createInstructor("same-id", "Other", "other@example.com"));
        assertSame(dana, system.getInstructor("same-id"));
        assertEquals(1, system.getInstructors().size());
    }

    @Test
    void workshopIdsStartAtOneAndIncreaseWithinEachSystem() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Workshop java = system.createWorkshop("Java", "Core Java");
        Workshop web = system.createWorkshop("Web", "Web development");
        assertEquals(1, java.getId());
        assertEquals(2, web.getId());
        assertSame(java, system.getWorkshop(1));
        assertSame(web, system.getWorkshop(2));
        assertEquals("Java", java.getName());
        assertEquals("Core Java", java.getDescription());
        assertNull(java.getInstructor());
        WorkshopSystem other = new WorkshopSystemImpl();
        assertEquals(1, other.createWorkshop("Other", "Another system").getId());
        assertTrue(other.getInstructors().isEmpty());
    }

    @Test
    void missingLookupsAndEmptyRegistriesFollowOurConvention() {
        WorkshopSystem system = new WorkshopSystemImpl();
        assertNull(system.getInstructor("missing"));
        assertNull(system.getWorkshop(999));
        assertTrue(system.getInstructors().isEmpty());
        assertTrue(system.getWorkshops().isEmpty());
    }

    @Test
    void associationUpdatesBothDirectionsWithoutDuplicateEntries() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Instructor dana = system.createInstructor("i1", "Dana", "dana@example.com");
        Workshop java = system.createWorkshop("Java", "Core");
        Workshop web = system.createWorkshop("Web", "Web development");
        system.associateWorkshopWithInstructor(dana, java);
        system.associateWorkshopWithInstructor(dana, web);
        system.associateWorkshopWithInstructor(dana, java);
        assertSame(dana, java.getInstructor());
        assertSame(dana, web.getInstructor());
        assertEquals(2, dana.getWorkshops().size());
        assertTrue(dana.getWorkshops().containsAll(List.of(java, web)));
    }

    @Test
    void reassociationKeepsBothInstructorListsConsistent() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Instructor dana = system.createInstructor("i1", "Dana", "dana@example.com");
        Instructor noa = system.createInstructor("i2", "Noa", "noa@example.com");
        Workshop java = system.createWorkshop("Java", "Core");
        Workshop web = system.createWorkshop("Web", "Existing workshop");
        system.associateWorkshopWithInstructor(noa, web);
        system.associateWorkshopWithInstructor(dana, java);
        system.associateWorkshopWithInstructor(noa, java);
        assertSame(noa, java.getInstructor());
        assertTrue(dana.getWorkshops().isEmpty());
        assertEquals(2, noa.getWorkshops().size());
        assertTrue(noa.getWorkshops().containsAll(List.of(java, web)));
        assertSame(noa, web.getInstructor());
    }

    @Test
    void returnedListsProtectRegistriesAndRelationships() {
        WorkshopSystem system = new WorkshopSystemImpl();
        Instructor dana = system.createInstructor("i1", "Dana", "dana@example.com");
        Workshop java = system.createWorkshop("Java", "Core");
        system.associateWorkshopWithInstructor(dana, java);
        system.getInstructors().clear();
        system.getWorkshops().clear();
        dana.getWorkshops().clear();
        assertSame(dana, system.getInstructor("i1"));
        assertSame(java, system.getWorkshop(1));
        assertEquals(1, system.getInstructors().size());
        assertEquals(1, system.getWorkshops().size());
        assertEquals(List.of(java), dana.getWorkshops());
        assertSame(dana, java.getInstructor());
    }
}
