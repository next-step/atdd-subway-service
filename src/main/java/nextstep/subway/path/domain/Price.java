package nextstep.subway.path.domain;

import org.springframework.stereotype.Component;

@Component
public class Price {
    public static final int DEFAULT_PRICE = 1250;
    private static final double TEN_KM = 10;
    private static final double FIFTY_KM = 50;

    private int price;

    public Price() {
    }

    public void calculatePrice(double distance) {
        if (distance < TEN_KM) {
            price = DEFAULT_PRICE;
            return;
        }

        if (distance < FIFTY_KM) {
            price = DEFAULT_PRICE + calculateOverFareTen(distance - TEN_KM);
            return;
        }

        price = DEFAULT_PRICE + calculateOverFareTen(distance - TEN_KM) + calculateOverFareFifty(distance - FIFTY_KM);
    }

    private int calculateOverFareTen(double distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateOverFareFifty(double distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    public int getPrice() {
        return price;
    }
}