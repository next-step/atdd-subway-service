package nextstep.subway.generic.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nextstep.subway.generic.domain.Price;

@Converter(autoApply = true)
public class PriceConverter implements AttributeConverter<Price, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Price attribute) {
        return attribute.getValue();
    }

    @Override
    public Price convertToEntityAttribute(Integer dbData) {
        return Price.valueOf(dbData);
    }
}
