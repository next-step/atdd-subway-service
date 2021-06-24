package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.DiscountStrategy;
import nextstep.subway.path.domain.NoDiscountStrategy;

public class Guest implements User {

    private static final Guest instance = new Guest();

    private Guest() { }

    public static Guest getInstance() {
        return instance;
    }

    @Override
    public Long getId() {
        return 0L;
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public Integer getAge() {
        return 0;
    }

    @Override
    public DiscountStrategy getDiscountStrategy() {
        return new NoDiscountStrategy();
    }
}
