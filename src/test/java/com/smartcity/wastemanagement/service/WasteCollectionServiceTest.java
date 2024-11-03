package com.smartcity.wastemanagement.service;

import com.smartcity.wastemanagement.exception.ResourceNotFoundException;
import com.smartcity.wastemanagement.model.CollectionStatus;
import com.smartcity.wastemanagement.model.WasteCollection;
import com.smartcity.wastemanagement.model.WasteType;
import com.smartcity.wastemanagement.repository.WasteCollectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class WasteCollectionServiceTest {

    @Mock
    private WasteCollectionRepository repository;

    @InjectMocks
    private WasteCollectionService service;

    private WasteCollection sampleCollection;

    @BeforeEach
    void setUp() {
        // Configuração comum para todos os testes
        sampleCollection = new WasteCollection();
        sampleCollection.setId(1L);
        sampleCollection.setAddress("Rua Teste");
        sampleCollection.setWasteType(WasteType.RECYCLABLE);
        sampleCollection.setScheduledTime(LocalDateTime.now().plusDays(1));
        sampleCollection.setStatus(CollectionStatus.SCHEDULED);
    }

    @Test
    @DisplayName("Deve criar uma nova coleta com sucesso")
    void shouldCreateCollection() {
        // Arrange
        when(repository.save(any(WasteCollection.class))).thenReturn(sampleCollection);

        // Act
        WasteCollection result = service.scheduleCollection(sampleCollection);

        // Assert
        assertNotNull(result);
        assertEquals("Rua Teste", result.getAddress());
        assertEquals(WasteType.RECYCLABLE, result.getWasteType());
        assertEquals(CollectionStatus.SCHEDULED, result.getStatus());
    }

    @Test
    @DisplayName("Deve buscar uma coleta por ID com sucesso")
    void shouldFindCollectionById() {
        // Arrange
        when(repository.findById(1L)).thenReturn(Optional.of(sampleCollection));

        // Act
        WasteCollection result = service.getCollectionById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Rua Teste", result.getAddress());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar coleta inexistente")
    void shouldThrowExceptionWhenCollectionNotFound() {
        // Arrange
        when(repository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            service.getCollectionById(999L);
        });
    }

    @Test
    @DisplayName("Deve listar todas as coletas")
    void shouldListAllCollections() {
        // Arrange
        List<WasteCollection> collections = Arrays.asList(sampleCollection);
        when(repository.findAll()).thenReturn(collections);

        // Act
        List<WasteCollection> result = service.getAllCollections();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleCollection.getAddress(), result.get(0).getAddress());
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver coletas")
    void shouldReturnEmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<WasteCollection> result = service.getAllCollections();

        // Assert
        assertTrue(result.isEmpty());
    }
}