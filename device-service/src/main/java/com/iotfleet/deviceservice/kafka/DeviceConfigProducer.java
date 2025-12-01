package com.iotfleet.deviceservice.kafka;

import com.iotfleet.deviceservice.dto.DeviceConfigDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeviceConfigProducer {
    private final KafkaTemplate<String, DeviceConfigDTO> kafkaTemplate;
    private static final String TOPIC = "device-create-events";
    public void publishDeviceCreated(DeviceConfigDTO event){
        try{
            kafkaTemplate.send(
                    TOPIC,
                    event.getDeviceId().toString(),
                    event
            );

    } catch(Exception e)

    {
        log.error("!!!!!!!!!!!!!!!!!!Error {}",e);
    }
        log.info("Published DEVICE-CREATED event for {}", event.getDeviceId());


    }
}
