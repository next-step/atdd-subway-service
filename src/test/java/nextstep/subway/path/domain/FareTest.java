package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FareTest {

    @DisplayName("기본거리 요금 생성")
    @Test
    void create() {
        Fare fare = Fare.from(10L);

        assertThat(fare).isNotNull();
        assertThat(fare.getFare()).isEqualTo(1250L);
    }

    @DisplayName("거리별 요금 정책 검증")
    @Test
    void faresByDistance() {
        Fare fare = Fare.from(11L);

        assertThat(fare.getFare()).isEqualTo(1350L);

        fare = Fare.from(23L);

        assertThat(fare.getFare()).isEqualTo(1550L);

        fare = Fare.from(66L);

        assertThat(fare.getFare()).isEqualTo(2250L);
    }

    @DisplayName("라인 추가요금 정책 검증")
    @Test
    void faresByLine() {
        Station upStation = new Station("건대역");
        Station downStation = new Station("용마산역");
        Line line = new Line("7호선", "bg-red-600", upStation, downStation, 10, 900L);

        Fare fare = Fare.of(10L, line.getSections());

        assertThat(fare.getFare()).isEqualTo(2150L);
    }
}
