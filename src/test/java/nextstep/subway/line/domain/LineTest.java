package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("지하철 구간을 관리")
class LineTest {

    @DisplayName("지하철 구간 등록")
    @Test
    void addSection() {
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Station 판교역 = new Station("판교역");
        Station 강남역 = new Station("강남역");
        Line 신분당선 = new Line("신분당선", "bg-red", 양재역, 정자역, 10);

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
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Station 판교역 = new Station("판교역");
        Station 강남역 = new Station("강남역");
        Line 신분당선 = new Line("신분당선", "bg-red", 양재역, 정자역, 10);

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
        // given
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Line 신분당선 = new Line("신분당선", "bg-red", 양재역, 정자역, 10);

        // when then
        assertThatThrownBy(() -> 신분당선.removeStation(정자역))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("구간 삭제 실패됨");
    }
}