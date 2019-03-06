package com.zsoft.service.mapper;

import com.zsoft.domain.*;
import com.zsoft.service.dto.ConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Config and its DTO ConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {SmartObjectMapper.class, ProfileConfValueMapper.class})
public interface ConfigMapper extends EntityMapper<ConfigDTO, Config> {

    @Mapping(source = "smartObject.id", target = "smartObjectId")
    @Mapping(source = "profileConfValue.id", target = "profileConfValueId")
    ConfigDTO toDto(Config config);

    @Mapping(source = "smartObjectId", target = "smartObject")
    @Mapping(source = "profileConfValueId", target = "profileConfValue")
    @Mapping(target = "confValues", ignore = true)
    Config toEntity(ConfigDTO configDTO);

    default Config fromId(Long id) {
        if (id == null) {
            return null;
        }
        Config config = new Config();
        config.setId(id);
        return config;
    }
}
