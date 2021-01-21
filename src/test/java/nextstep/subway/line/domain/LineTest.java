package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @Test
    @DisplayName("구간 추가 - 새로운 구간의 하행역과 기존 구간의 상행역이 같은 경우")
    void addSection4() {
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 광교역 = new Station("광교역");
        //given
        Line line = new Line("2호선", "green", 양재역, 광교역, 7);
        //when
        line.addLineSection(line, 강남역, 양재역, 3);

        //then
        assertThat(line.getStations(line)).containsExactly(강남역, 양재역, 광교역);
    }

    @Test
    @DisplayName("구간 추가 - 새로운 구간의 상행역과 기족 구간의 하행역이 같은 경우")
    void addSection5() {
        Station 강남역 = new Station("강남역");
        Station 정자역 = new Station("정자역");
        Station 광교역 = new Station("광교역");
        //given
        Line line = new Line("2호선", "green", 강남역, 정자역, 8);
        //when
        line.addLineSection(line, 정자역, 광교역, 2);
        //then
        assertThat(line.getStations(line)).containsExactly(강남역, 정자역, 광교역);
    }

    @Test
    @DisplayName("구간 제거 - 구간이 하나거나 없을 경우 삭제")
    void addSection6() {
        //given
        Station 강남역 = new Station("강남역");
        Line line = new Line("신분당선", "pink");
        //when
        //then
        assertThatThrownBy(() -> {
            line.removeLineSection(line, 강남역);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("제거할 구간이 없습니다!");
    }

    @Test
    @DisplayName("구간 제거")
    void addSection7() {
        //given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Station 정자역 = new Station("정자역");
        Line line = new Line("신분당선", "pink", 강남역, 정자역, 8);
        //when then
        assertThatThrownBy(() -> {
                    line.removeLineSection(line, 정자역);
        }).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("제거할 구간이 없습니다!");


        line.addLineSection(line, 양재역, 정자역, 5);
        line.removeLineSection(line, 양재역);
    }
}