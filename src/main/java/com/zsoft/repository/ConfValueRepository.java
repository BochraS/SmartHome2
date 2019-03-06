package com.zsoft.repository;

import com.zsoft.domain.ConfValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ConfValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfValueRepository extends JpaRepository<ConfValue, Long> {

}
