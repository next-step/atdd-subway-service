package nextstep.subway.path.domain.price;

public interface PriceCalculator {

	int calculatePrice(int pathDistance);

	int adjustAgeDiscount(int price, Integer age);
}