package nextstep.subway.common;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Money, Integer> {

    @Override
    public Integer convertToDatabaseColumn(final Money attribute) {
        return attribute.getAmount();
    }

    @Override
    public Money convertToEntityAttribute(final Integer dbData) {
        return Money.valueOf(dbData);
    }
}
