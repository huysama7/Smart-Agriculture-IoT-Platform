package com.huysama.builderDto.enitties.iot;

import com.huysama.builderDto.enitties.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tbiot_user_interactions")
public class Tbiot_User_Interactions extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long interaction_id;
    private String username;
    private Long device_id;
    private String action;
}
