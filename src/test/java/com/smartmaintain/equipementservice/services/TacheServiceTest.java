package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.entities.Maintenance;
import com.smartmaintain.equipementservice.entities.Tache;
import com.smartmaintain.equipementservice.repositories.EquipeRepository;
import com.smartmaintain.equipementservice.repositories.MaintenanceRepository;
import com.smartmaintain.equipementservice.repositories.TacheRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TacheServiceTest {

    @Test
    void technicianCanAddNoteAndCheckTaskDone() {
        TacheRepository tacheRepository = mock(TacheRepository.class);
        MaintenanceRepository maintenanceRepository = mock(MaintenanceRepository.class);
        when(tacheRepository.save(any(Tache.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID taskId = UUID.randomUUID();
        Tache task = new Tache();
        task.setId(taskId);
        task.setStatus("in_progress");
        when(tacheRepository.findById(taskId)).thenReturn(Optional.of(task));

        TacheService service = new TacheService(tacheRepository, mock(EquipeRepository.class), maintenanceRepository);

        Tache noted = service.addTechnicianNote(taskId, "Hydraulic line inspected.");
        Tache checked = service.checkTaskDone(taskId);

        assertEquals("Hydraulic line inspected.", noted.getTechnicianNote());
        assertEquals("completed", checked.getStatus());
    }

    @Test
    void taskCanBeAttachedToMaintenanceAssignment() {
        TacheRepository tacheRepository = mock(TacheRepository.class);
        MaintenanceRepository maintenanceRepository = mock(MaintenanceRepository.class);
        when(tacheRepository.save(any(Tache.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID taskId = UUID.randomUUID();
        UUID maintenanceId = UUID.randomUUID();
        Tache task = new Tache();
        task.setId(taskId);
        Maintenance maintenance = new Maintenance();
        maintenance.setId(maintenanceId);
        when(tacheRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(maintenanceRepository.findById(maintenanceId)).thenReturn(Optional.of(maintenance));

        TacheService service = new TacheService(tacheRepository, mock(EquipeRepository.class), maintenanceRepository);

        Tache assigned = service.attachToMaintenance(taskId, maintenanceId);

        assertEquals(maintenanceId, assigned.getMaintenance().getId());
    }
}
