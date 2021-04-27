package ies.lab3.datatopresentation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IssueRepository extends JpaRepository<IssueReport,Long> {
    @Query(value = "SELECT i FROM IssueReport i WHERE marked_as_private = false")
    List<IssueReport> findAllButPrivate();
    List<IssueReport> findAllByEmail(String email);
}
