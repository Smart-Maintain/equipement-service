package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.BugFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface BugFeedbackRepository extends JpaRepository<BugFeedback, UUID> {
}
