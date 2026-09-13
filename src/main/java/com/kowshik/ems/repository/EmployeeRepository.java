package com.kowshik.ems.repository;
import com.kowshik.ems.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Employee> findTop5ByOrderByJoiningDateDesc();

    Page<Employee> findByDepartment(String department, Pageable pageable);

    @Query("""
    SELECT e FROM Employee e
    WHERE (
        LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
    )
    AND e.department = :department
    """)
    Page<Employee> searchByKeywordAndDepartment(
            @Param("keyword") String keyword,
            @Param("department") String department,
            Pageable pageable
    );

    List<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email
    );

    Page<Employee> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String firstName,
            String lastName,
            String email,
            Pageable pageable
    );

    @Query("""
       SELECT e.department, COUNT(e)
       FROM Employee e
       GROUP BY e.department
       """)
    List<Object[]> countEmployeesByDepartment();
}
