package nextstep.subway.line;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class LineTest {

    Station 강남역 = new Station("강남역");
    Station 양재역 = new Station("양재역");
    Line line = new Line("2호선", "green", 강남역, 양재역, 10);

    @DisplayName("라인에 구간을 등록한다")
    @Test
    void addLineStation() {
        //given
        Station station = new Station("판교역");
        //when
        line.addStations(양재역, station, 4);
        //then
        assertThat(line.getSections().size()).isEqualTo(2);
    }

    @DisplayName("라인에 역을 제거한다")
    @Test
    void removeLineStation() {
        //given
        Station station = new Station("판교역");
        line.addStations(양재역, station, 2);
        //when
        line.removeStation(양재역);
        //then
        assertThat(line.getSections().size()).isEqualTo(1);
        assertThat(line.getStations().size()).isEqualTo(2);
    }
}
