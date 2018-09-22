package com.uga.gastorama.repository;

import com.uga.gastorama.domain.Confectioner;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Confectioner entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfectionerRepository extends JpaRepository<Confectioner, Long> {

}
