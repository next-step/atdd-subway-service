package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Line 도메인 로직 단위 테스트")
class LineTest {

    @DisplayName("역 목록 조회 시 상행역에서 하행역 순으로 정렬된 Station 목록 생성")
    @Test
    void getStationsTest() {

        Station upStation = new Station("상행역");
        Station downStation = new Station("하행역");

        Line line = new Line("name", "color", upStation, downStation, 100);

        assertThat(line.getStations()).containsExactly(upStation, downStation);
    }
}
