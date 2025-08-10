package com.huysama.builderDto.enitties.iot;

import com.huysama.builderDto.enitties.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tbiot_devices")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tbiot_Devices extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long device_id;
    private String device_name;
    private String location;
    private String status;
    private String type;
}
