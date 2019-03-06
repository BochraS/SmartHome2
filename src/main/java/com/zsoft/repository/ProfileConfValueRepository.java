package com.zsoft.repository;

import com.zsoft.domain.ProfileConfValue;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ProfileConfValue entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileConfValueRepository extends JpaRepository<ProfileConfValue, Long> {

}
