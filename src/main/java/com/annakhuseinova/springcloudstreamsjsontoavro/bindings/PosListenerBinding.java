package com.annakhuseinova.springcloudstreamsjsontoavro.bindings;

import com.annakhuseinova.springcloudstreams.stream.json.avro.HadoopRecord;
import com.annakhuseinova.springcloudstreams.stream.json.avro.Notification;
import com.annakhuseinova.springcloudstreamsjsontoavro.model.PosInvoice;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface PosListenerBinding {

    @Input("notification-input-channel")
    KStream<String, PosInvoice> notificationInputStream();

    @Output("notification-output-channel")
    KStream<String, Notification> notificationOutputStream();

    @Input("hadoop-input-channel")
    KStream<String, PosInvoice> hadoopInputStream();

    @Output("hadoop-output-channel")
    KStream<String, HadoopRecord> hadoopOutputStream();
}
