package com.huysama.deviceService.service;

import com.huysama.builderCore.repositories.iot.Tbiot_DevicesRepository;
import com.huysama.builderDto.enitties.iot.Tbiot_Devices;
import com.huysama.builderDto.exception.CoreException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {

    @Autowired
    Tbiot_DevicesRepository tbiot_devicesRepository;

    public Tbiot_Devices addDevice(Tbiot_Devices device) {
        return tbiot_devicesRepository.save(device);
    }

    public void updateDevice(Tbiot_Devices deviceUpdate) {
        // Logic to update a device
        Tbiot_Devices existingDevice = tbiot_devicesRepository.findById(deviceUpdate.getDevice_id())
                .orElseThrow(() -> new CoreException(HttpStatus.NOT_FOUND, "Device not found"));
        existingDevice.setDevice_name(deviceUpdate.getDevice_name());
        existingDevice.setLocation(deviceUpdate.getLocation());
        existingDevice.setStatus(deviceUpdate.getStatus());
        existingDevice.setType(deviceUpdate.getType());
        tbiot_devicesRepository.save(existingDevice);
    }


    public void deleteDevice(Long deviceId) {
        Tbiot_Devices existingDevice = tbiot_devicesRepository.findById(deviceId)
                .orElseThrow(() -> new CoreException(HttpStatus.NOT_FOUND, "Device not found"));
        existingDevice.setStatus("C");
        tbiot_devicesRepository.save(existingDevice);
    }

    public List<Tbiot_Devices> getDevices(Tbiot_Devices filter) {
        return tbiot_devicesRepository.findAll(filter);
    }
}
