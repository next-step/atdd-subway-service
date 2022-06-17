package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @DisplayName("역이 등록되어 있다면, 등록된 역을 조회한다.")
    @Test
    void getStationsWithStation() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Line line = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);

        assertThat(line.getStations()).hasSize(2);
        assertThat(line.getStations()).contains(강남역, 광교역);
    }

    @DisplayName("역이 등록되어 있지 않다면, 빈 리스트를 반환한다.")
    @Test
    void getStationsWithoutStation() {
        Line line = new Line("신분당선", "bg-red-600");

        assertThat(line.getStations()).hasSize(0);
    }

    @DisplayName("구간을 추가할 수 있다.")
    @Test
    void addSection() {
        Line line = new Line("신분당선", "bg-red-600");
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        int distance = 10;
        line.addSection(강남역, 광교역, distance);

        assertThat(line.getSections().getValues()).hasSize(1);
        assertThat(line.getSections().getValues()).contains(new Section(line, 강남역, 광교역, distance));
    }

    @DisplayName("구간을 삭제할 수 있다.")
    @Test
    void removeSection() {
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Station 양재역 = new Station("양재역");
        Line line = new Line("신분당선", "bg-red-600", 강남역, 광교역, 10);
        line.addSection(광교역, 양재역, 15);

        line.removeSection(양재역);

        assertThat(line.getStations()).doesNotContain(양재역);
    }
}
