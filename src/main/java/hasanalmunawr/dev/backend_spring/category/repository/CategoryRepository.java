package hasanalmunawr.dev.backend_spring.category.repository;

import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends BaseRepository<CategoryModel> {

    @Query(
            value = """
        SELECT * FROM categories 
        WHERE LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) 
        """,
            countQuery = """
         SELECT * FROM categories 
        WHERE LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) 
        """,
            nativeQuery = true
    )
    Page<CategoryModel> searchCategory(@Param("search") String search, Pageable pageable);

    @Query(
            value = "SELECT * FROM categories WHERE user_id = :userId",
            countQuery = "SELECT count(*) FROM categories WHERE user_id = :userId",
            nativeQuery = true
    )
    Page<CategoryModel> findByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query(
            value = "SELECT * FROM categories WHERE user_id = :userId AND is_deleted = false",
            countQuery = "SELECT count(*) FROM categories WHERE user_id = :userId AND is_deleted = false",
            nativeQuery = true
    )
    List<CategoryModel> findByUserId(
            @Param("userId") Integer userId
    );
}
