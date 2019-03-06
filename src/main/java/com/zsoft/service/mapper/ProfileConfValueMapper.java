package com.zsoft.service.mapper;

import com.zsoft.domain.*;
import com.zsoft.service.dto.ProfileConfValueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ProfileConfValue and its DTO ProfileConfValueDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProfileConfValueMapper extends EntityMapper<ProfileConfValueDTO, ProfileConfValue> {


    @Mapping(target = "configs", ignore = true)
    @Mapping(target = "profiles", ignore = true)
    ProfileConfValue toEntity(ProfileConfValueDTO profileConfValueDTO);

    default ProfileConfValue fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfileConfValue profileConfValue = new ProfileConfValue();
        profileConfValue.setId(id);
        return profileConfValue;
    }
}
