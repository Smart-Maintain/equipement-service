package com.smartmaintain.equipementservice.repositories;

import com.smartmaintain.equipementservice.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    
    @Query("SELECT n FROM Notification n WHERE n.recipientEmail = :email OR n.recipientRole = :role ORDER BY n.createdAt DESC")
    List<Notification> findByRecipient(@Param("email") String email, @Param("role") String role);
}
