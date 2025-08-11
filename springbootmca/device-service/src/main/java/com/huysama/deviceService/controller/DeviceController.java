package com.huysama.deviceService.controller;

import com.huysama.builderDto.config.HAMA_INSTANCE;
import com.huysama.builderDto.enitties.iot.Tbiot_Devices;
import com.huysama.deviceService.service.DeviceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Log4j2
@RequestMapping(HAMA_INSTANCE.CONTROLLER_DEVICE_REQUEST_PATH)
public class DeviceController {



    @Autowired
    HttpServletRequest request;
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/devices")
    public List<Tbiot_Devices> getDevices(@RequestBody Tbiot_Devices filter,@RequestHeader Map<String, String> headers) {
        log.info("Instance ID: {}", headers);
        log.info("Fetching devices with filter: {}", filter);
        deviceService.addDevice(Tbiot_Devices.builder()
                .device_name("Device 1")
                .location("Location 1")
                .status("Active")
                .type("Type A")
                .build());
        return deviceService.getDevices(filter);
    }
}
