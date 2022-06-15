package nextstep.subway.generic.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import nextstep.subway.generic.domain.Distance;

@Converter(autoApply = true)
public class DistanceConverter implements AttributeConverter<Distance, Integer> {
    @Override
    public Integer convertToDatabaseColumn(final Distance attribute) {
        return attribute.getValue();
    }

    @Override
    public Distance convertToEntityAttribute(final Integer dbData) {
        return Distance.valueOf(dbData);
    }
}
