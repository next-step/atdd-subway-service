package nextstep.subway.path.domain.calcurator;

import java.util.Arrays;
import nextstep.subway.path.domain.Money;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-10
 */
public enum Discount {

    CHILD   ( 6, 12, Money.won(350L), 50.0),
    TEENAGER(13, 19, Money.won(350L), 20.0),
    NORMAL  (20, 64, Money.won(0L),    0.0),
    FREE    (65,  Integer.MAX_VALUE, Money.won(0L),  100.0);

    private static final Money basePrice = Money.won(1250L);

    private int minAge;
    private int maxAge;
    private final Money discountPrice;
    private final double discountPercent;


    private final static PriceCalculator<Discount> discountPriceCalculator = new DiscountPriceCalculator();
    private final static PriceCalculator<Integer> overFarePriceCalculator = new OverFarePriceCalculator();

    Discount(int minAge, int maxAge, Money discountPrice, double discountPercent) {
        this.maxAge = minAge;
        this.maxAge = maxAge;
        this.discountPrice = discountPrice;
        this.discountPercent = discountPercent;
    }

    public static Money discountCalculate(int distance, Money additionalFare, Integer age) {
        Money price = basePrice.plus(additionalFare);
        Discount discount = NORMAL;
        if (age != null) {
            discount = Arrays.stream(values())
                    .filter(it -> it.minAge <= age && age <= it.maxAge)
                    .findFirst()
                    .orElse(NORMAL);
        }
        price = overFarePriceCalculator.calculate(price, distance);
        if (discount != Discount.NORMAL) {
            price = discountPriceCalculator.calculate(price, discount);
        }
        return price;
    }

    public Money getDiscountPrice() {
        return discountPrice;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

}
