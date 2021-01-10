package nextstep.subway.path.domain.calcurator;

import nextstep.subway.path.domain.Money;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-10
 */
public class DiscountPriceCalculator implements PriceCalculator<Discount> {

    @Override
    public Money calculate(Money money, Discount discount) {
        Money calculatedMoney = calculateDiscountAmount(money, discount.getDiscountPrice());
        return calculatePercent(calculatedMoney, discount.getDiscountPercent());
    }

    private Money calculateDiscountAmount(Money money, Money discountPrice) {
        return money.minus(discountPrice);
    }

    private Money calculatePercent(Money money, double percent) {
        return money.minus(money.times(percent / 100));
    }
}
