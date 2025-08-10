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
@Table(name = "tbiot_sensor_data")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tbiot_Sensor_Data extends BaseEntity {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long data_id;
    private Long device_id;
    private String sensor_type;
    private String value;
}
