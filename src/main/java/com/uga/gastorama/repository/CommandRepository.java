package com.uga.gastorama.repository;

import com.uga.gastorama.domain.Command;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Command entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommandRepository extends JpaRepository<Command, Long> {

    @Query("select command from Command command where command.userId.login = ?#{principal.username}")
    List<Command> findByUserIdIsCurrentUser();

}
