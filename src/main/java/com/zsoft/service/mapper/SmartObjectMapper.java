package com.zsoft.service.mapper;

import com.zsoft.domain.*;
import com.zsoft.service.dto.SmartObjectDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity SmartObject and its DTO SmartObjectDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface SmartObjectMapper extends EntityMapper<SmartObjectDTO, SmartObject> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    SmartObjectDTO toDto(SmartObject smartObject);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "configs", ignore = true)
    SmartObject toEntity(SmartObjectDTO smartObjectDTO);

    default SmartObject fromId(Long id) {
        if (id == null) {
            return null;
        }
        SmartObject smartObject = new SmartObject();
        smartObject.setId(id);
        return smartObject;
    }
}
