package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class LineTest {

    @DisplayName("노선에 구간을 등록하면 지하철역이 정렬돼서 조회된다.")
    @Test
    void getStations() {
        Line line = new Line("신분당선", "red");

        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 정자역 = new Station("정자역");

        Section 역삼역_정자역_구간 = new Section(line, 역삼역, 정자역, 5);
        Section 강남역_역심역_구간 = new Section(line, 강남역, 역삼역, 10);

        line.addSection(강남역_역심역_구간);
        line.addSection(역삼역_정자역_구간);

        Assertions.assertThat(line.getStations()).containsExactly(강남역, 역삼역, 정자역);
    }

}
