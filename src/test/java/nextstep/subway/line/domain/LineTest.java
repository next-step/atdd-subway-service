package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class LineTest {

    @DisplayName("지정위치에 따라 다음역 찾기")
    @Test
    void 다음역_찾기() {
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Line line = new Line("신분당선", "red", 강남역, 양재역, 10);

        assertAll(
            () -> assertThat(line.nextStationOf(강남역, StationPosition.DOWN_STATION)).isEqualTo(양재역),
            () -> assertThat(line.nextStationOf(양재역, StationPosition.UP_STATION)).isEqualTo(강남역)
        );
    }

    @DisplayName("상행종점 찾기")
    @Test
    void 상행종점_찾기() {
        Station 신논현역 = new Station("신논현역");
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Line line = new Line("신분당선", "red", 양재역, 정자역, 10);
        Section section1 = new Section(line, 강남역, 양재역, 10);
        Section section2 = new Section(line, 신논현역, 강남역, 10);
        line.getSections().add(section1);
        line.getSections().add(section2);

        assertThat(line.findFinalUpStation()).isEqualTo(신논현역);
    }
}
