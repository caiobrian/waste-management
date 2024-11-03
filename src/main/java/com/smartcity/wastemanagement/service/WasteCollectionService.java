package com.smartcity.wastemanagement.service;

import com.smartcity.wastemanagement.exception.ResourceNotFoundException;
import com.smartcity.wastemanagement.model.CollectionStatus;
import com.smartcity.wastemanagement.model.WasteCollection;
import com.smartcity.wastemanagement.repository.WasteCollectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WasteCollectionService {

    private final WasteCollectionRepository wasteCollectionRepository;

    @Transactional
    public WasteCollection scheduleCollection(WasteCollection wasteCollection) {
        validateScheduledTime(wasteCollection.getScheduledTime());
        wasteCollection.setStatus(CollectionStatus.SCHEDULED);
        return wasteCollectionRepository.save(wasteCollection);
    }

    @Transactional(readOnly = true)
    public WasteCollection getCollectionById(Long id) {
        return wasteCollectionRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Collection not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<WasteCollection> getAllCollections() {
        return wasteCollectionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<WasteCollection> getCollectionsByDateRange(LocalDateTime start, LocalDateTime end) {
        return wasteCollectionRepository.findByScheduledTimeBetween(start, end);
    }

    @Transactional
    public WasteCollection updateCollection(Long id, WasteCollection updatedCollection) {
        WasteCollection existingCollection = getCollectionById(id);
        
        if (updatedCollection.getScheduledTime() != null) {
            validateScheduledTime(updatedCollection.getScheduledTime());
            existingCollection.setScheduledTime(updatedCollection.getScheduledTime());
        }
        
        if (updatedCollection.getAddress() != null) {
            existingCollection.setAddress(updatedCollection.getAddress());
        }
        
        if (updatedCollection.getWasteType() != null) {
            existingCollection.setWasteType(updatedCollection.getWasteType());
        }
        
        if (updatedCollection.getNotes() != null) {
            existingCollection.setNotes(updatedCollection.getNotes());
        }

        return wasteCollectionRepository.save(existingCollection);
    }

    @Transactional
    public WasteCollection updateCollectionStatus(Long id, CollectionStatus newStatus) {
        WasteCollection collection = getCollectionById(id);
        validateStatusTransition(collection.getStatus(), newStatus);
        collection.setStatus(newStatus);
        return wasteCollectionRepository.save(collection);
    }

    @Transactional
    public void deleteCollection(Long id) {
        WasteCollection collection = getCollectionById(id);
        if (collection.getStatus() != CollectionStatus.SCHEDULED) {
            throw new IllegalStateException("Can only delete collections that are in SCHEDULED status");
        }
        wasteCollectionRepository.deleteById(id);
    }

    private void validateScheduledTime(LocalDateTime scheduledTime) {
        if (scheduledTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled time must be in the future");
        }
    }

    private void validateStatusTransition(CollectionStatus currentStatus, CollectionStatus newStatus) {
        if (currentStatus == CollectionStatus.COMPLETED || currentStatus == CollectionStatus.CANCELLED) {
            throw new IllegalStateException("Cannot change status of a completed or cancelled collection");
        }
        
        if (currentStatus == CollectionStatus.SCHEDULED && newStatus == CollectionStatus.COMPLETED) {
            throw new IllegalStateException("Collection must be IN_PROGRESS before being marked as COMPLETED");
        }
    }
}