package com.smartcity.wastemanagement.dto;

import com.smartcity.wastemanagement.model.CollectionStatus;
import com.smartcity.wastemanagement.model.WasteType;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

public class WasteCollectionDTO {

    @Data
    public static class Request {
        @NotBlank(message = "Address is required")
        private String address;

        @NotNull(message = "Waste type is required")
        private WasteType wasteType;

        @NotNull(message = "Scheduled time is required")
        @FutureOrPresent(message = "Scheduled time must be in the present or future")
        private LocalDateTime scheduledTime;

        private String notes;
    }

    @Data
    public static class UpdateRequest {
        private String address;
        private WasteType wasteType;
        
        @FutureOrPresent(message = "Scheduled time must be in the present or future")
        private LocalDateTime scheduledTime;
        private String notes;
    }

    @Data
    public static class StatusUpdateRequest {
        @NotNull(message = "Status is required")
        private CollectionStatus status;
    }

    @Data
    public static class Response {
        private Long id;
        private String address;
        private WasteType wasteType;
        private LocalDateTime scheduledTime;
        private CollectionStatus status;
        private String notes;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    public static class ListResponse {
        private Long id;
        private String address;
        private WasteType wasteType;
        private LocalDateTime scheduledTime;
        private CollectionStatus status;
    }
}