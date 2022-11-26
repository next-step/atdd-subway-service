package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LineTest {

    @DisplayName("지하철 노선이름, 지하철 색상이 같으면 두 지하철 노선은 동등하다.")
    @Test
    void equals() {
        Line line1 = new Line("신분당선", "red");
        Line line2 = new Line("신분당선", "red");

        Assertions.assertThat(line1).isEqualTo(line2);
    }

    @DisplayName("지하철 노선 이름이 다르면 두 지하철 노선은 등동하지 않다.")
    @Test
    void notEquals1() {
        Line line1 = new Line("신분당선", "red");
        Line line2 = new Line("분당선", "red");

        Assertions.assertThat(line1).isNotEqualTo(line2);
    }

    @DisplayName("지하철 노선 색상이 다르면 두 지하철 노선은 동등하지 않다.")
    @Test
    void notEquals2() {
        Line line1 = new Line("신분당선", "red");
        Line line2 = new Line("신분당선", "yellow");

        Assertions.assertThat(line1).isNotEqualTo(line2);
    }

    @DisplayName("지하철 노선에 지하철 구간이 추가되었는지 확인한다.")
    @Test
    void addSection() {
        Line line = new Line("신분당선", "red");
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Section section = new Section(line, 강남역, 광교역, 10);

        line.addSection(section);

        Assertions.assertThat(line.getSections())
                .containsExactlyInAnyOrder(new Section(line, 강남역, 광교역, 10));
    }

    @DisplayName("지하철 노선에 지하철 구간이 없으면 빈 지하철역 목록을 반환한다.")
    @Test
    void emptyStations() {
        Line line = new Line("신분당선", "red");

        Assertions.assertThat(line.getStations()).isEmpty();
    }

    @DisplayName("지하철 노선에 지하철 구간을 등록하고 지하철 목록을 조회하면 상행역, 하행역 순으로 조회된다.")
    @Test
    void getStations() {
        Line line = new Line("신분당선", "red");

        Station 신사역 = new Station("신사역");
        Station 강남역 = new Station("강남역");
        Station 광교역 = new Station("광교역");
        Section 신사역_강남역_구간 = new Section(line, 신사역, 강남역, 10);
        Section 강남역_광교역_구간 = new Section(line, 강남역, 광교역, 10);

        line.addSection(강남역_광교역_구간);
        line.addSection(신사역_강남역_구간);

        Assertions.assertThat(line.getStations()).containsExactly(신사역, 강남역, 광교역);
    }
}
