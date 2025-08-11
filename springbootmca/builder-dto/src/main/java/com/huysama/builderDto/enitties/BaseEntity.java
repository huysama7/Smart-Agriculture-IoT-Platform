package com.huysama.builderDto.enitties;

import com.huysama.builderDto.config.UserContextHolder;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;

import java.util.Date;

@MappedSuperclass
@Data
public abstract class BaseEntity {


    @Column(name = "organization_id", nullable = false)
    private String organization_id;

    @Column(updatable = false)
    private Date create_date;

    @Column(updatable = false)
    private String create_by;

    @Column(name = "modify_date")
    private Date modify_date;

    @Column(name = "modify_by")
    private String modify_by;

    @PrePersist
    protected void onCreate() {
        this.create_date = new Date();
        this.create_by = UserContextHolder.getCurrentUserId() != null
                ? UserContextHolder.getCurrentUserId()
                : "system";
        if (this.organization_id == null) {
            this.organization_id = UserContextHolder.getCurrentOrgId() != null
                    ? UserContextHolder.getCurrentOrgId()
                    : "default-org";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.modify_date = new Date();
        this.modify_by = UserContextHolder.getCurrentUserId() != null
                ? UserContextHolder.getCurrentUserId()
                : "system";
    }

    private String getCurrentUsername() {
        // TODO: Lấy từ SecurityContextHolder
        return "system";
    }
    private String getCurrentOrganizationId() {
        // TODO: Lấy từ claim của Keycloak token
        return "default-org";
    }
}
