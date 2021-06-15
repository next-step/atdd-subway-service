package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    @DisplayName("지하철역 목록을 가져온다.")
    @Test
    void getStations() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line 이호선 = new Line("2호선", "green", 강남역, 역삼역, 10);

        assertThat(이호선.getStations()).hasSize(2);
        assertThat(이호선.getStations().get(0).getName()).isEqualTo(강남역.getName());
    }

    @DisplayName("지하철 노선에 구간을 추가한다.")
    @Test
    void addSection() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 양재역 = new Station("양재역");
        Line 이호선 = new Line("2호선", "green", 강남역, 역삼역, 10);
        Section newSection = new Section(이호선, 역삼역, 양재역, 10);

        이호선.addSection(newSection);

        assertAll(() -> assertThat(이호선.getSections()).hasSize(2),
                () -> assertThat(이호선.getSections().get(1).getDownStation()).isEqualTo(양재역));
    }
}
