package com.uga.gastorama.repository;

import com.uga.gastorama.domain.CommandLine;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CommandLine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandLineRepository extends JpaRepository<CommandLine, Long> {

}
