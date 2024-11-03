package com.smartcity.wastemanagement.controller;

import com.smartcity.wastemanagement.dto.WasteCollectionDTO;
import com.smartcity.wastemanagement.mapper.WasteCollectionMapper;
import com.smartcity.wastemanagement.model.WasteCollection;
import com.smartcity.wastemanagement.service.WasteCollectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/waste-collections")
@RequiredArgsConstructor
@Tag(name = "Waste Collection", description = "Waste Collection management APIs")
public class WasteCollectionController {

    private final WasteCollectionService wasteCollectionService;
    private final WasteCollectionMapper wasteCollectionMapper;

    @Operation(summary = "Create a new waste collection", 
               description = "Creates a new waste collection schedule with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Collection successfully created",
                    content = @Content(schema = @Schema(implementation = WasteCollectionDTO.Response.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<WasteCollectionDTO.Response> createCollection(
            @Valid @RequestBody WasteCollectionDTO.Request request) {
        WasteCollection entity = wasteCollectionMapper.toEntity(request);
        WasteCollection savedEntity = wasteCollectionService.scheduleCollection(entity);
        return new ResponseEntity<>(wasteCollectionMapper.toResponseDto(savedEntity), HttpStatus.CREATED);
    }

    @Operation(summary = "Get a waste collection by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the waste collection",
                    content = @Content(schema = @Schema(implementation = WasteCollectionDTO.Response.class))),
        @ApiResponse(responseCode = "404", description = "Collection not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<WasteCollectionDTO.Response> getCollection(
            @Parameter(description = "ID of the collection to retrieve") @PathVariable Long id) {
        WasteCollection entity = wasteCollectionService.getCollectionById(id);
        return ResponseEntity.ok(wasteCollectionMapper.toResponseDto(entity));
    }

    @Operation(summary = "Get all waste collections", 
               description = "Retrieves all waste collections with optional date range filtering")
    @GetMapping
    public ResponseEntity<List<WasteCollectionDTO.ListResponse>> getAllCollections(
            @Parameter(description = "Start date for filtering (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date for filtering (ISO format)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<WasteCollection> collections;
        if (startDate != null && endDate != null) {
            collections = wasteCollectionService.getCollectionsByDateRange(startDate, endDate);
        } else {
            collections = wasteCollectionService.getAllCollections();
        }
        return ResponseEntity.ok(wasteCollectionMapper.toListDto(collections));
    }

    @Operation(summary = "Update a waste collection", 
               description = "Updates an existing waste collection with the provided details")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Collection successfully updated"),
        @ApiResponse(responseCode = "404", description = "Collection not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<WasteCollectionDTO.Response> updateCollection(
            @Parameter(description = "ID of the collection to update") @PathVariable Long id,
            @Valid @RequestBody WasteCollectionDTO.UpdateRequest request) {
        WasteCollection existingEntity = wasteCollectionService.getCollectionById(id);
        wasteCollectionMapper.updateEntityFromDto(request, existingEntity);
        WasteCollection updatedEntity = wasteCollectionService.updateCollection(id, existingEntity);
        return ResponseEntity.ok(wasteCollectionMapper.toResponseDto(updatedEntity));
    }

    @Operation(summary = "Update collection status", 
               description = "Updates only the status of an existing waste collection")
    @PatchMapping("/{id}/status")
    public ResponseEntity<WasteCollectionDTO.Response> updateCollectionStatus(
            @Parameter(description = "ID of the collection to update status") @PathVariable Long id,
            @Valid @RequestBody WasteCollectionDTO.StatusUpdateRequest request) {
        WasteCollection updatedEntity = wasteCollectionService.updateCollectionStatus(id, request.getStatus());
        return ResponseEntity.ok(wasteCollectionMapper.toResponseDto(updatedEntity));
    }

    @Operation(summary = "Delete a waste collection", 
               description = "Deletes an existing waste collection by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Collection successfully deleted"),
        @ApiResponse(responseCode = "404", description = "Collection not found"),
        @ApiResponse(responseCode = "409", description = "Cannot delete collection in current status")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCollection(
            @Parameter(description = "ID of the collection to delete") @PathVariable Long id) {
        wasteCollectionService.deleteCollection(id);
        return ResponseEntity.noContent().build();
    }
}