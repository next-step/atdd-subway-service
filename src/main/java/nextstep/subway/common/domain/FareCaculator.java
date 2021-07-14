package nextstep.subway.common.domain;


public interface FareCaculator<T1, T2> {
    SubwayFare calculate(T1 t1, T2 t2);
}
