package com.zsoft.repository;

import com.zsoft.domain.Profile;
import com.zsoft.domain.User;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Profile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

  /*  @Query("select profile from Profile profile where profile.user.login = ?#{principal.username}")
    List<Profile> findByUserIsCurrentUser();*/
  List<Profile> findAllByUser(User user);

}
