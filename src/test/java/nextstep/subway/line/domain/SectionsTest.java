package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("지하철 구간 관련 기능")
class SectionsTest {
    Sections sections = new Sections();
    Station station0;
    Station station1;
    Station station2;
    Station station3;
    Station station4;
    Line line;

    @BeforeEach
    void setUp() {
        station0 = new Station("서초역");
        station1 = new Station("강남역");
        station2 = new Station("력삼역");
        station3 = new Station("선릉역");
        station4 = new Station("삼성역");
        line = new Line("2호선", "bg-100", station1, station3, 100);
        sections.add(line, station1, station3, 50);
    }

    @Test
    @DisplayName("강남역-역삼역 구간을 등록한다")
    void addBetweenStation() {
        sections.add(line, station1, station2, 40);
        assertThat(sections.stations()).hasSize(3);
        assertThat(sections.stations()).contains(station2);
    }

    @Test
    @DisplayName("강남역 상행으로 서초역을 등록한다")
    void addUpStation() {
        sections.add(line, station0, station1, 40);
        assertThat(sections.stations()).hasSize(3);
        assertThat(sections.stations()).contains(station0);
    }

    @Test
    @DisplayName("선릉역 하행으로 삼성역을 등록한다")
    void addDownStation() {
        sections.add(line, station3, station4, 40);
        assertThat(sections.stations()).hasSize(3);
        assertThat(sections.stations()).contains(station4);
    }

    @Test
    @DisplayName("동일한 구간을 등록하면 예외가 발생한다")
    void addSameSectionException() {
        assertThatThrownBy(() -> sections.add(line, station1, station3, 40))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("이미 등록된 구간 입니다.");
    }

    @Test
    @DisplayName("연결된 구간이 없으면 예외가 발생한다")
    void addNoMatchedSectionException() {
        assertThatThrownBy(() -> sections.add(line, station2, station4, 40))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("등록할 수 없는 구간 입니다.");
    }

    @Test
    @DisplayName("상행->하행 순서로 지하철역 리스트를 가져온다")
    void stations() {
        sections.add(line, station1, station2, 40);
        sections.add(line, station3, station4, 40);
        assertThat(sections.stations().get(0).getName()).isEqualTo("강남역");
        assertThat(sections.stations().get(1).getName()).isEqualTo("력삼역");
        assertThat(sections.stations().get(2).getName()).isEqualTo("선릉역");
        assertThat(sections.stations().get(3).getName()).isEqualTo("삼성역");
    }

    @Test
    @DisplayName("역을 제거하면 상행 역간 구간거리는 삭제된 구간 거리만큼 늘어난다")
    void remove() {
        sections.add(line, station1, station2, 10);
        sections.remove(line, station2);
        assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(station3);
        assertThat(sections.getSections().get(0).getDistance()).isEqualTo(new Distance(50));
    }

    @Test
    @DisplayName("첫번째 역을 제거한다")
    void removeFirstStation() {
        sections.add(line, station1, station2, 10);
        sections.remove(line, station1);
        assertThat(sections.getSections().get(0).getUpStation()).isEqualTo(station2);
    }

    @Test
    @DisplayName("마지막 역을 제거한다")
    void removeLastStation() {
        sections.add(line, station1, station2, 10);
        sections.remove(line, station3);
        assertThat(sections.getSections().get(0).getDownStation()).isEqualTo(station2);
    }

    @Test
    @DisplayName("라인의 구간이 하나일 경우 역을 삭제하면 예외가 발생한다")
    void deleteException() {
        assertThatThrownBy(() -> sections.remove(line, station3)).isInstanceOf(RuntimeException.class);
    }

}
