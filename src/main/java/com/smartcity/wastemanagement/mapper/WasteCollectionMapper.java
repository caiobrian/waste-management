package com.smartcity.wastemanagement.mapper;

import com.smartcity.wastemanagement.dto.WasteCollectionDTO;
import com.smartcity.wastemanagement.model.WasteCollection;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class WasteCollectionMapper {

    public WasteCollection toEntity(WasteCollectionDTO.Request dto) {
        WasteCollection entity = new WasteCollection();
        entity.setAddress(dto.getAddress());
        entity.setWasteType(dto.getWasteType());
        entity.setScheduledTime(dto.getScheduledTime());
        entity.setNotes(dto.getNotes());
        return entity;
    }

    public void updateEntityFromDto(WasteCollectionDTO.UpdateRequest dto, WasteCollection entity) {
        if (dto.getAddress() != null) {
            entity.setAddress(dto.getAddress());
        }
        if (dto.getWasteType() != null) {
            entity.setWasteType(dto.getWasteType());
        }
        if (dto.getScheduledTime() != null) {
            entity.setScheduledTime(dto.getScheduledTime());
        }
        if (dto.getNotes() != null) {
            entity.setNotes(dto.getNotes());
        }
    }

    public WasteCollectionDTO.Response toResponseDto(WasteCollection entity) {
        WasteCollectionDTO.Response dto = new WasteCollectionDTO.Response();
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setWasteType(entity.getWasteType());
        dto.setScheduledTime(entity.getScheduledTime());
        dto.setStatus(entity.getStatus());
        dto.setNotes(entity.getNotes());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    public WasteCollectionDTO.ListResponse toListDto(WasteCollection entity) {
        WasteCollectionDTO.ListResponse dto = new WasteCollectionDTO.ListResponse();
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setWasteType(entity.getWasteType());
        dto.setScheduledTime(entity.getScheduledTime());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public List<WasteCollectionDTO.ListResponse> toListDto(List<WasteCollection> entities) {
        return entities.stream()
                .map(this::toListDto)
                .collect(Collectors.toList());
    }
}