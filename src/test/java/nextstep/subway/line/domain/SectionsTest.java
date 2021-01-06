package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 구간 목록 관련 기능")
class SectionsTest {

    private Station 삼성역;
    private Station 종합운동장역;
    private Station 잠실새내역;
    private Line _2호선;
    private Sections sections;

    @BeforeEach
    void beforeEach() {
        삼성역 = new Station("삼성역");
        종합운동장역 = new Station("종합운동장역");
        잠실새내역 = new Station("잠실새내역");
        _2호선 = new Line("2호선", "bg-green", 삼성역, 종합운동장역, 10);
        sections = new Sections(new Section(_2호선, 삼성역, 종합운동장역, 10));
    }

    @DisplayName("구간 `Section` 추가")
    @Test
    void addSection() {
        // When
        sections.add(new Section(_2호선, 종합운동장역, 잠실새내역, 10));
        // Then
        assertThat(sections.getStations())
                .extracting("name")
                .containsExactly("삼성역", "종합운동장역", "잠실새내역");
    }

    @DisplayName("구간 `Section`에서 `Station` 삭제")
    @Test
    void deleteStation() {
        // Given
        sections.add(new Section(_2호선, 종합운동장역, 잠실새내역, 10));
        // When
        sections.deleteStation(종합운동장역);
        // Then
        assertThat(sections.getStations())
                .extracting("name")
                .containsExactly("삼성역", "잠실새내역");
    }
}
