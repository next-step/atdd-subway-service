package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class AddedPrice {

    private int price;

    public AddedPrice() {
    }

    public AddedPrice(int price) {
        this.price = price;
    }

    public int value() {
        return price;
    }

}
