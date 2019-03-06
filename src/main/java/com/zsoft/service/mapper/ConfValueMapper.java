package com.zsoft.service.mapper;

import com.zsoft.domain.*;
import com.zsoft.service.dto.ConfValueDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ConfValue and its DTO ConfValueDTO.
 */
@Mapper(componentModel = "spring", uses = {ConfigMapper.class})
public interface ConfValueMapper extends EntityMapper<ConfValueDTO, ConfValue> {

    @Mapping(source = "config.id", target = "configId")
    ConfValueDTO toDto(ConfValue confValue);

    @Mapping(source = "configId", target = "config")
    ConfValue toEntity(ConfValueDTO confValueDTO);

    default ConfValue fromId(Long id) {
        if (id == null) {
            return null;
        }
        ConfValue confValue = new ConfValue();
        confValue.setId(id);
        return confValue;
    }
}
