package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @DisplayName("구간을 추가한다.")
    @Test
    void add() {
        Line line = new Line("2호선", "green");
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 양재역 = new Station("양재역");
        Sections sections = new Sections();

        sections.add(new Section(line, 강남역, 역삼역, 10));
        sections.add(new Section(line, 역삼역, 양재역, 10));

        assertThat(sections.getSections()).hasSize(2);
    }

    @DisplayName("구간 추가를 실패한다. - 이미 등록된 구간")
    @Test
    void add_fail_for_already_added() {

    }

    @Test
    void getStations() {
    }

    @Test
    void removeStation() {
    }
}