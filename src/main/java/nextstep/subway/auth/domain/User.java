package nextstep.subway.auth.domain;

import nextstep.subway.path.domain.DiscountStrategy;

public interface User {

    Long getId();

    String getEmail();

    Integer getAge();

    DiscountStrategy getDiscountStrategy();
}
