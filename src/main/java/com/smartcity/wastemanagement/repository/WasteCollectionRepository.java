package com.smartcity.wastemanagement.repository;

import com.smartcity.wastemanagement.model.WasteCollection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WasteCollectionRepository extends JpaRepository<WasteCollection, Long> {
    List<WasteCollection> findByScheduledTimeBetween(LocalDateTime start, LocalDateTime end);
    List<WasteCollection> findByAddressContainingIgnoreCase(String address);
}