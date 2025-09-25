package de.bayer.pharmacy.productservice.product.repository;

import de.bayer.pharmacy.productservice.product.Product;
import de.bayer.pharmacy.productservice.product.ProductStatus;
import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository // optional; Spring Data will detect it either way
public interface ProductRepository
        extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    // --- basics
    Optional<Product> findBySku(Long sku);

    List<Product> findByNameContainingIgnoreCase(String q);

    boolean existsBySku(Long sku);

    Page<Product> findByStatus(ProductStatus status, Pageable pageable);
    Page<Product> findByStatusInAndNameContainingIgnoreCase(
            Collection<ProductStatus> statuses, String namePart, Pageable pageable);

    // Eagerly fetch availability when needed (adjust attribute name to your model)
    @EntityGraph(attributePaths = {"availabilities"})
    Optional<Product> findWithAvailabilitiesBySku(Long sku);

    // Pessimistic lock variant for workflows that must serialize updates (e.g., approval)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
    @Query("select p from Product p where p.sku = :sku")
    Optional<Product> findBySkuForUpdate(@Param("sku") Long sku);

    // ------ Choose ONE of the two blocks below, matching your mapping ------

    // If you used: Set<ProductAvailability> availabilities (value object with branchCode, quantity)
    @Query("""
           select p from Product p 
           join p.availabilities a 
           where a.branchCode = :branch and a.availableQuantity > :minQty
           """)
    Page<Product> findAvailableInBranchSet(@Param("branch") String branch,
                                           @Param("minQty") int minQty,
                                           Pageable pageable);



    // Projection example for lightweight lists
//    @Query("""
//           select p.id as id, p.sku as sku, p.name as name, p.status as status
//           from Product p
//           where p.status = :status
//           """)
//    Page<LiteProductProjection> findSummariesByStatus(@Param("status") ProductStatus status,
//                                                      Pageable pageable);
}