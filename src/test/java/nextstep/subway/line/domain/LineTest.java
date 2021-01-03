package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 노선 구간 관리")
class LineTest {

    private Station 양재역;
    private Station 정자역;
    private Line 신분당선;
    private Station 판교역;
    private Station 강남역;

    @BeforeEach
    void setUp() {
        양재역 = new Station("양재역");
        정자역 = new Station("정자역");
        판교역 = new Station("판교역");
        강남역 = new Station("강남역");
        신분당선 = new Line("신분당선", "bg-red", 양재역, 정자역, 10);
    }

    @DisplayName("지하철 구간 등록")
    @Test
    void addSection() {
        // when
        신분당선.add(판교역, 정자역, 5);
        신분당선.add(강남역, 양재역, 5);

        // then
        assertThat(신분당선.getStations())
                .extracting("name")
                .containsExactly("강남역","양재역", "판교역", "정자역");
    }

    @DisplayName("지하철 구간 삭제")
    @Test
    void removeSection() {
        // given
        신분당선.add(판교역, 정자역, 5);
        신분당선.add(강남역, 양재역, 5);

        // when
        신분당선.removeStation(판교역);

        // then
        assertThat(신분당선.getStations())
                .extracting("name")
                .containsExactly("강남역","양재역", "정자역");
    }

    @DisplayName("지하철 구간 삭제")
    @Test
    void removeSectionExpectedException() {
        // when then
        assertThatThrownBy(() -> 신분당선.removeStation(정자역))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("구간 삭제 실패됨");
    }
}