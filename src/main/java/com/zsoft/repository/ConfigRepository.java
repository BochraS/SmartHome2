package com.zsoft.repository;

import com.zsoft.domain.Config;
import com.zsoft.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import com.zsoft.domain.SmartObject;

import java.util.List;

/**
 * Spring Data  repository for the Config entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfigRepository extends JpaRepository<Config, Long> {
    List<Config> findAllBySmartObject(long id);
}
