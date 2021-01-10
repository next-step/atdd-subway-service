package nextstep.subway.path.domain.calcurator;


import nextstep.subway.path.domain.Money;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-10
 */
public class OverFarePriceCalculator implements PriceCalculator<Integer> {

    @Override
    public Money calculate(Money money, Integer distance) {
        return money.plus(calculateOverFare(distance));
    }

    private Money calculateOverFare(int distance) {
        return DistanceAmount.calculateAdditionalAmount(distance);
    }
}
