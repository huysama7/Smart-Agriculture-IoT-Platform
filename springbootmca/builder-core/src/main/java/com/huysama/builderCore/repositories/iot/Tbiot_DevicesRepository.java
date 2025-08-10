package com.huysama.builderCore.repositories.iot;

import com.huysama.builderCore.repositories.base.HamaRepository;
import com.huysama.builderCore.repositories.base.Hamatory;
import com.huysama.builderDto.enitties.iot.Tbiot_Devices;

@Hamatory("Tbiot_DevicesRepository")
public interface Tbiot_DevicesRepository extends HamaRepository<Tbiot_Devices, Long> {
}