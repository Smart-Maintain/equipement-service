package com.smartmaintain.equipementservice.services;

import com.smartmaintain.equipementservice.dto.EquipeRequest;
import com.smartmaintain.equipementservice.entities.Equipe;
import com.smartmaintain.equipementservice.repositories.EquipeRepository;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EquipeServiceTest {

    @Test
    void createsTeamWithLeaderEngineerAndTechnicians() {
        EquipeRepository repository = mock(EquipeRepository.class);
        when(repository.save(any(Equipe.class))).thenAnswer(invocation -> invocation.getArgument(0));
        EquipeService service = new EquipeService(repository);

        UUID engineerId = UUID.randomUUID();
        UUID technicianId = UUID.randomUUID();
        EquipeRequest request = new EquipeRequest(
                "Team Nacelle",
                "Engine inspection",
                engineerId,
                "Engineer Test",
                List.of(technicianId),
                List.of("Tech One")
        );

        Equipe saved = service.createTeam(request);

        assertEquals(engineerId, saved.getLeaderEngineerId());
        assertEquals(List.of(technicianId), saved.getTechnicianIds());
        assertEquals(List.of("Tech One"), saved.getTechnicianNames());
    }

    @Test
    void rejectsTeamWithoutTechnicians() {
        EquipeService service = new EquipeService(mock(EquipeRepository.class));
        EquipeRequest request = new EquipeRequest(
                "Empty Team",
                "Inspection",
                UUID.randomUUID(),
                "Engineer Test",
                List.of(),
                List.of()
        );

        assertThrows(IllegalArgumentException.class, () -> service.createTeam(request));
    }
}
