package com.uga.gastorama.repository;

import com.uga.gastorama.domain.ProductPhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProductPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {

}
