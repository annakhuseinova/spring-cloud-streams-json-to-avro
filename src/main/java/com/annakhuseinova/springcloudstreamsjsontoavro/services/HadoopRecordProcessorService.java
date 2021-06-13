package com.annakhuseinova.springcloudstreamsjsontoavro.services;

import com.annakhuseinova.springcloudstreams.stream.json.avro.HadoopRecord;
import com.annakhuseinova.springcloudstreamsjsontoavro.bindings.PosListenerBinding;
import com.annakhuseinova.springcloudstreamsjsontoavro.model.PosInvoice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@EnableBinding(PosListenerBinding.class)
@RequiredArgsConstructor
public class HadoopRecordProcessorService {

    private final RecordBuilder recordBuilder;

    @StreamListener("hadoop-input-channel")
    @SendTo("hadoop-output-channel")
    public KStream<String, HadoopRecord> process(KStream<String, PosInvoice> input){
        KStream<String, HadoopRecord> hadoopRecordKStream = input.mapValues(recordBuilder::getMaskedInvoice)
                .flatMapValues(recordBuilder::getHadoopRecords);
        hadoopRecordKStream.foreach((k,v)-> log.info(String.format("Hadoop Record: - K: %s, Value: %s", k, v)));
        return hadoopRecordKStream;
    }
}
