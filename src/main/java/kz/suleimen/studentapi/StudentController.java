package kz.suleimen.studentapi;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentRepository students;

    public StudentController(StudentRepository students) {
        this.students = students;
    }

    @GetMapping
    public List<Student> all() {
        return students.findAll();
    }

    @GetMapping("/{id}")
    public Student one(@PathVariable Long id) {
        return findStudent(id);
    }

    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody Student student) {
        Student saved = students.save(student);
        return ResponseEntity.created(URI.create("/api/students/" + saved.getId())).body(saved);
    }

    @PutMapping("/{id}")
    public Student update(@PathVariable Long id, @Valid @RequestBody Student changes) {
        Student current = findStudent(id);
        current.setFullName(changes.getFullName());
        current.setEmail(changes.getEmail());
        current.setGroupName(changes.getGroupName());
        return students.save(current);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Student current = findStudent(id);
        students.delete(current);
        return ResponseEntity.noContent().build();
    }

    private Student findStudent(Long id) {
        return students.findById(id)
            .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Student " + id + " not found"));
    }
}
