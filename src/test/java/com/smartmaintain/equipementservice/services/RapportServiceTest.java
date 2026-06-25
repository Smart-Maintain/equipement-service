package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.RapportRequest;
import com.smartmaintain.equipementservice.dto.RapportReviewRequest;
import com.smartmaintain.equipementservice.entities.Rapport;
import com.smartmaintain.equipementservice.entities.Tache;
import com.smartmaintain.equipementservice.repositories.MaintenanceRepository;
import com.smartmaintain.equipementservice.repositories.RapportRepository;
import com.smartmaintain.equipementservice.repositories.TacheRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RapportServiceTest {

    @Test
    void technicianSubmitsTaskReportToEngineerForApproval() {
        RapportRepository rapportRepository = mock(RapportRepository.class);
        TacheRepository tacheRepository = mock(TacheRepository.class);
        MaintenanceRepository maintenanceRepository = mock(MaintenanceRepository.class);
        when(rapportRepository.save(any(Rapport.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID taskId = UUID.randomUUID();
        Tache task = new Tache();
        task.setId(taskId);
        when(tacheRepository.findById(taskId)).thenReturn(Optional.of(task));

        RapportService service = new RapportService(rapportRepository, tacheRepository, maintenanceRepository);
        RapportRequest request = new RapportRequest(
                "TASK",
                taskId,
                null,
                "Blade check",
                "No cracks detected.",
                "technician",
                "tech1@test.com",
                null
        );

        Rapport draft = service.create(request);
        Rapport submitted = service.submit(draft);

        assertEquals("TASK", submitted.getType());
        assertEquals("SUBMITTED", submitted.getStatus());
        assertEquals("ENGINEER", submitted.getReviewerRole());
        assertEquals(taskId, submitted.getTache().getId());
    }

    @Test
    void reviewerCanRequestModificationWithNote() {
        RapportRepository rapportRepository = mock(RapportRepository.class);
        when(rapportRepository.save(any(Rapport.class))).thenAnswer(invocation -> invocation.getArgument(0));
        RapportService service = new RapportService(rapportRepository, mock(TacheRepository.class), mock(MaintenanceRepository.class));

        Rapport submitted = new Rapport();
        submitted.setId(UUID.randomUUID());
        submitted.setStatus("SUBMITTED");
        when(rapportRepository.findById(submitted.getId())).thenReturn(Optional.of(submitted));

        Rapport reviewed = service.review(
                submitted.getId(),
                new RapportReviewRequest("MODIFICATION_REQUESTED", "Add torque values.")
        );

        assertEquals("MODIFICATION_REQUESTED", reviewed.getStatus());
        assertEquals("Add torque values.", reviewed.getReviewerNote());
    }
}
