package nextstep.subway.path.domain.fee.discount;

import nextstep.subway.path.domain.fee.CalculatedFee;

public interface Discount {

  Long discount(Long fee, Class<? extends CalculatedFee> clazz);
}
