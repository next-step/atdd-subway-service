package nextstep.subway.path.domain.discount;

public class Default extends AgeDiscount {
    
    public Default(int fare) {
        super(fare);
    }

    @Override
    public int discount() {
        return getFare();
    }
}
