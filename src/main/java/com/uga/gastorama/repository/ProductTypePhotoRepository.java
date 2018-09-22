package com.uga.gastorama.repository;

import com.uga.gastorama.domain.ProductTypePhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductTypePhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductTypePhotoRepository extends JpaRepository<ProductTypePhoto, Long> {

}
