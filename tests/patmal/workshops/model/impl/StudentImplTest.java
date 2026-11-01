package patmal.workshops.model.impl;

import org.junit.jupiter.api.Test;
import patmal.workshops.api.model.Student;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentImplTest {
    @Test
    void newStudentHasNoTeamRelationships() {
        Student student = new StudentImpl("s1", "Alice", "alice@example.com");

        assertNull(student.getTeamName());
        assertNull(student.getInstructor());
        assertNotNull(student.getTeamMates());
        assertTrue(student.getTeamMates().isEmpty());
    }
}
