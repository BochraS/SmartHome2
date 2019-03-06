package com.zsoft.repository;

import com.zsoft.domain.SmartObject;
import com.zsoft.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the SmartObject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SmartObjectRepository extends JpaRepository<SmartObject, Long> {

    List<SmartObject> findAllByUser(User user);
}
