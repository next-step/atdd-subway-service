package nextstep.subway.path.domain.calcurator;

import nextstep.subway.path.domain.Money;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-10
 */
public interface PriceCalculator<T> {

    Money calculate(Money money, T t);
}
