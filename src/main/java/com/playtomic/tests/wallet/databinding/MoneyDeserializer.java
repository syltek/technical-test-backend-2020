package com.playtomic.tests.wallet.databinding;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.playtomic.tests.wallet.ex.ErrorException;
import com.playtomic.tests.wallet.ex.ErrorType;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author : Andrey Kolchev
 * @since : 01/27/2021
 */
public class MoneyDeserializer extends JsonDeserializer<BigDecimal> {

    final NumberDeserializers.BigDecimalDeserializer delegate = NumberDeserializers.BigDecimalDeserializer.instance;

    @Override
    public BigDecimal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        if (jsonParser.currentToken().equals(JsonToken.VALUE_STRING)) {
            throw new ErrorException(ErrorType.INVALID_JSON_TYPE, jsonParser.currentName());
        }
        BigDecimal value = delegate.deserialize(jsonParser, deserializationContext);
        value = value.setScale(2, RoundingMode.HALF_UP);
        return value;
    }
}