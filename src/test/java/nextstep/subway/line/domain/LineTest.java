package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("지하철 노선 관련 기능")
class LineTest {
    private Line _2호선;
    private Station 삼성역;
    private Station 종합운동장역;
    private Station 잠실새내역;
    private Station 잠실역;

    @BeforeEach
    void beforeEach() {
        삼성역 = new Station("삼성역");
        종합운동장역 = new Station("종합운동장역");
        잠실새내역 = new Station("잠실새내역");
        잠실역 = new Station("잠실역");
        _2호선 = new Line("2호선", "bg-green", 삼성역, 종합운동장역, 10);
    }

    @DisplayName("노선의 구간 `Section` 등록")
    @Test
    void addSection() {
        // When
        _2호선.addSection(잠실새내역, 종합운동장역, 5);
        _2호선.addSection(잠실역, 삼성역, 5);
        // Then
        assertThat(_2호선.getStations())
                .extracting("name")
                .containsExactly("잠실역", "삼성역", "잠실새내역", "종합운동장역");
    }

    @DisplayName("노선의 지하철역 `Station` 제거")
    @Test
    void deleteStation() {
        // Given
        _2호선.addSection(잠실새내역, 종합운동장역, 5);
        _2호선.addSection(잠실역, 삼성역, 5);
        // When
        _2호선.deleteStation(잠실새내역);
        // Then
        assertThat(_2호선.getStations())
                .extracting("name")
                .containsExactly("잠실역", "삼성역", "종합운동장역");
    }

    @DisplayName("노선의 지하철역 `Station` 삭제 예외 발생 확")
    @Test
    void checkExceptionToDeleteStationInLine() {
        // When & Then
        assertThatThrownBy(() -> _2호선.deleteStation(종합운동장역))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("구간이 1개이면 역을 삭제할 수 없습니다.");
    }
}
