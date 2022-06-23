package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("구간 일급 컬렉션")
class SectionsTest {
    private Line 노선;

    @BeforeEach
    void setUp() {
        노선 = Line.of("2호선", "green");
    }

    @Test
    @DisplayName("구간을 추가할 수 있다.")
    void 추가() {
        Section 구간 = 구간_생성("강남역", "역삼역", 5);

        Sections sections = new Sections();
        sections.add(구간);

        assertThat(sections.getStations()).hasSize(2);
    }

    @Test
    @DisplayName("노선의 상행 종점역을 찾을 수 있다.")
    void 상행_종점역_찾기() {
        Sections sections = new Sections();
        sections.add(구간_생성("강남역", "역삼역", 4));
        sections.add(구간_생성("역삼역", "선릉역", 5));
        sections.add(구간_생성("선릉역", "삼성역", 4));
        sections.add(구간_생성("서초역", "강남역", 4));

        assertThat(sections.findFinalUpStation()).isNotNull().isEqualTo(Station.from("서초역"));
    }

    @Test
    @DisplayName("노선의 모든 역을 조회할 수 있다.")
    void 노선의_모든_역_조회() {
        Sections sections = new Sections();
        sections.add(구간_생성("서초역", "강남역", 4));
        sections.add(구간_생성("강남역", "역삼역", 4));
        sections.add(구간_생성("역삼역", "선릉역", 5));
        sections.add(구간_생성("선릉역", "삼성역", 4));

        assertThat(sections.getStations()).containsExactly(
                Station.from("서초역"),
                Station.from("강남역"),
                Station.from("역삼역"),
                Station.from("선릉역"),
                Station.from("삼성역"));
    }

    private Section 구간_생성(String upStation, String downStation, int distance) {
        return Section.of(노선, Station.from(upStation), Station.from(downStation), distance);
    }
}
