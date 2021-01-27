package com.playtomic.tests.wallet.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.playtomic.tests.wallet.databinding.MoneyDeserializer;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
@Configuration
public class ObjectMapperConfig {

    final ObjectMapper mapper;

    public ObjectMapperConfig(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @PostConstruct
    public ObjectMapper configureMapper() {
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS, true);
        mapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        SimpleModule module = new SimpleModule();
        module.addDeserializer(BigDecimal.class, new MoneyDeserializer());
        mapper.registerModule(module);
        return mapper;
    }
}
