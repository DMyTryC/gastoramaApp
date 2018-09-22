package com.uga.gastorama.repository;

import com.uga.gastorama.domain.ConfectionerPhoto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConfectionerPhoto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfectionerPhotoRepository extends JpaRepository<ConfectionerPhoto, Long> {

}
