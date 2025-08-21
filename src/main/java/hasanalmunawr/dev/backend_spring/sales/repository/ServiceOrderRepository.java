package hasanalmunawr.dev.backend_spring.sales.repository;

import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.sales.model.ServiceOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ServiceOrderRepository extends BaseRepository<ServiceOrder> {

    @Query(
            value = """
                    SELECT COUNT(s) FROM serviceOrder s WHERE DATE(s.created_at) = :date
            """,
            nativeQuery = true
    )
    long countByCreatedAt(@Param("date") LocalDate date);

    @Query(
            value = """
        SELECT * FROM serviceOrder 
        WHERE LOWER(serviceid) LIKE LOWER(CONCAT('%', :search, '%')) 
           OR LOWER(customername) LIKE LOWER(CONCAT('%', :search, '%'))
        """,
            countQuery = """
        SELECT COUNT(*) FROM serviceOrder 
        WHERE LOWER(serviceid) LIKE LOWER(CONCAT('%', :search, '%')) 
           OR LOWER(customername) LIKE LOWER(CONCAT('%', :search, '%'))
        """,
            nativeQuery = true
    )
    Page<ServiceOrder> searchServiceOrders(@Param("search") String search, Pageable pageable);

}
