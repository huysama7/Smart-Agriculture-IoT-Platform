package com.huysama.deviceService.controller;

import com.huysama.builderDto.config.HAMA_INSTANCE;
import com.huysama.builderDto.enitties.iot.Tbiot_Devices;
import com.huysama.deviceService.service.DeviceService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Log4j2
@RequestMapping(HAMA_INSTANCE.CONTROLLER_DEVICE_REQUEST_PATH)
public class DeviceController {


    @Autowired
    private DeviceService deviceService;

    @GetMapping("/devices")
    public List<Tbiot_Devices> getDevices(@RequestBody Tbiot_Devices filter) {
        log.info("Fetching devices with filter: {}", filter);
        return deviceService.getDevices(filter);
    }
}
