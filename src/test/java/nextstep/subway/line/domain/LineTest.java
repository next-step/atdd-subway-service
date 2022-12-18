package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 노선 도메인 모델 단위 테스트")
class LineTest {
    private static final Station head = new Station("강남역");
    private static final Station middle = new Station("판교역");
    private static final Station tail = new Station("광교역");

    private Line line;

    @BeforeEach
    void setup() {
        line = Line.from("신분당선", "RED");
        line.addSection(new Section(head, middle, Distance.from(10)));
        line.addSection(new Section(middle, tail, Distance.from(10)));
    }

    @DisplayName("노선에 등록 되어있지 않은 역은 제거할 수 없다")
    @Test
    void removeLineStation_notExistsLine() {
        line.removeLineStation(new Station("수원역"));

        assertThat(line.getStations()).containsExactly(head, middle, tail);
    }

    @DisplayName("오직 하나의 구간만 존재 할 경우, 해당 구간은 제거 할 수 없다")
    @Test
    void removeLineStation_oneLineRemaining() {
        Line lineWithOneSection = Line.from("새로운라인", "GREEN");
        lineWithOneSection.addSection(new Section(head, middle, Distance.from(10)));

        assertThatThrownBy(() -> lineWithOneSection.removeLineStation(head))
            .isInstanceOf(CannotRemoveSectionException.class)
            .hasMessageContaining("지울 수 없는 구간 입니다");
    }

    @DisplayName("노선 중 첫 역 구간을 제거할경우, 해당 구간만 성공적으로 제거 할 수 있어야 한다")
    @Test
    void removeLineStation_head() {
        line.removeLineStation(head);

        assertThat(line.getStations()).containsExactly(middle, tail);
    }

    @DisplayName("노선 중 중간 역 구간을 제거할경우, 해당 구간만 성공적으로 제거 할 수 있어야 한다")
    @Test
    void removeLineStation_middle() {
        line.removeLineStation(middle);

        assertThat(line.getStations()).containsExactly(head, tail);
    }

    @DisplayName("노선 중 마지막 역 구간을 제거할경우, 해당 구간만 성공적으로 제거 할 수 있어야 한다")
    @Test
    void removeLineStation_tail() {
        line.removeLineStation(tail);

        assertThat(line.getStations()).containsExactly(head, middle);
    }

}