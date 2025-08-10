package com.huysama.builderCore.repositories.iot;

import com.huysama.builderCore.repositories.base.HamaRepository;
import com.huysama.builderCore.repositories.base.Hamatory;
import com.huysama.builderDto.enitties.iot.Tbiot_Sensor_Data;

@Hamatory("Tbiot_Sensor_DataRepository")
public interface Tbiot_Sensor_DataRepository extends HamaRepository<Tbiot_Sensor_Data, Long> {
}