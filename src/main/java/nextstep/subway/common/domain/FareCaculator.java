package nextstep.subway.common.domain;


import java.math.BigDecimal;

public interface FareCaculator<T1, T2> {
    BigDecimal calculate(T1 subwayFare, T2 policy);
}
