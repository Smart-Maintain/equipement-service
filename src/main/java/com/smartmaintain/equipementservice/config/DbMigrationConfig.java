package com.smartmaintain.equipementservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbMigrationConfig {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void migrate() {
        try {
            jdbcTemplate.execute("ALTER TABLE rapport_attachments ALTER COLUMN attachment_url TYPE TEXT");
            System.out.println("===> MIGRATION SUCCESSFUL: ALTERED attachment_url to TEXT");
        } catch (Exception e) {
            System.out.println("===> MIGRATION FAILED OR ALREADY APPLIED: " + e.getMessage());
        }
    }
}
