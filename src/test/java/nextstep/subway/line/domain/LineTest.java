package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    private final static Station 강남역 = new Station(1L, "강남역");
    private final static Station 삼성역 = new Station(2L, "삼성역");
    private final static Station 잠실역 = new Station(3L, "잠실역");

    @Test
    @DisplayName("노선은 구간 정보를 저장한다")
    void addSection() {
        // given
        final Line 이호선 = new Line("2호선", "green");
        final Section 강남역_삼성역 = new Section(1L, 이호선, 강남역, 삼성역, 5);

        // when
        이호선.addSection(강남역_삼성역);
        final List<Station> actual = 이호선.getStations();

        // then
        assertThat(actual).containsExactly(강남역, 삼성역);
    }

    @Test
    @DisplayName("노선은 역 정보 출력시 상행종착역 부터 하행종착역 순서대로 반환한다")
    void getStations() {
        // given
        final Line 이호선 = new Line("2호선", "green");
        이호선.addSection(new Section(1L, 이호선, 강남역, 삼성역, 5));
        이호선.addSection(new Section(2L, 이호선, 삼성역, 잠실역, 5));

        // when
        final List<Station> actual = 이호선.getStations();

        // then
        assertThat(actual).containsExactly(강남역, 삼성역, 잠실역);
    }
}
