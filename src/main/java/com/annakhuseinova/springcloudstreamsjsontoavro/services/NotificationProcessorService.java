package com.annakhuseinova.springcloudstreamsjsontoavro.services;

import com.annakhuseinova.springcloudstreams.stream.json.avro.Notification;
import com.annakhuseinova.springcloudstreamsjsontoavro.bindings.PosListenerBinding;
import com.annakhuseinova.springcloudstreamsjsontoavro.model.PosInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@EnableBinding(PosListenerBinding.class)
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationProcessorService {

    private final RecordBuilder recordBuilder;

    @StreamListener("notification-input-channel")
    @SendTo("notification-output-channel")
    public KStream<String, Notification> process(KStream<String, PosInvoice> input){
        KStream<String, Notification> notificationKStream = input.filter((k,v) -> v.getCustomerType()
                .equalsIgnoreCase("PRIME")).mapValues(recordBuilder::getNotification);

        notificationKStream.foreach((key, value)-> log.info(String.format("Notification: - Key: %s, Value: %s",
                key, value)));
        return notificationKStream;
    }
}
