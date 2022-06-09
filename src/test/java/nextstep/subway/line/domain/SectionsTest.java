package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {
    private final static Line 이호선 = new Line("2호선", "green");
    private final static Station 강남역 = new Station(1L, "강남역");
    private final static Station 삼성역 = new Station(2L, "삼성역");
    private final static Station 잠실역 = new Station(3L, "잠실역");
    private final static Section 강남역_삼성역 = new Section(1L, 이호선, 강남역, 삼성역, 5);
    private final static Section 삼성역_잠실역 = new Section(2L, 이호선, 삼성역, 잠실역, 5);

    @Test
    @DisplayName("특정 노선의 구간 정보들을 출력시 상행종착역 부터 하행종착역 순서대로 출력한다")
    void getStations() {
        // given
        final Sections 이호선구간 = new Sections();
        이호선구간.addSection(강남역_삼성역);
        이호선구간.addSection(삼성역_잠실역);

        // when
        List<Station> actual = 이호선구간.getStations();

        // then
        assertThat(actual).containsExactly(강남역, 삼성역, 잠실역);
    }
}
