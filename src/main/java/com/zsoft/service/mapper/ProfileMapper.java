package com.zsoft.service.mapper;

import com.zsoft.domain.*;
import com.zsoft.service.dto.ProfileDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Profile and its DTO ProfileDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, ProfileConfValueMapper.class})
public interface ProfileMapper extends EntityMapper<ProfileDTO, Profile> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "profileConfValue.id", target = "profileConfValueId")
    ProfileDTO toDto(Profile profile);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "profileConfValueId", target = "profileConfValue")
    Profile toEntity(ProfileDTO profileDTO);

    default Profile fromId(Long id) {
        if (id == null) {
            return null;
        }
        Profile profile = new Profile();
        profile.setId(id);
        return profile;
    }
}
