package com.huysama.builderDto.enitties.iot;

import com.huysama.builderDto.enitties.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private Long device_id;
    @NotBlank
    private String device_name;
    @NotBlank
    private String location;
    @NotBlank
    private String status;
    @NotBlank
    private String type;
}
