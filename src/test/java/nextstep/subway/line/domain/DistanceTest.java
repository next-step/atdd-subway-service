package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;

@DisplayName("Distance 클래스")
public class DistanceTest {
    public static Distance 거리_생성(final int distance) {
        return new Distance(distance);
    }
}
