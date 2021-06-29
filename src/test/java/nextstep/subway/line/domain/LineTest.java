package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @Test
    void addLineStation_라인에_지하철역_추가_역사이에성공케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 잠실역 = new Station("잠실역");
        Line secondLine = new Line("2호선", "green", 강남역, 잠실역, 10);

        //when
        secondLine.addStation(강남역, 역삼역, 5);

        //then
        assertThat(secondLine.getStations()).containsExactly(강남역, 역삼역, 잠실역);
    }

    @Test
    void addLineStation_라인에_지하철역_추가_상행역변경성공케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 잠실역 = new Station("잠실역");
         Line secondLine = new Line("2호선", "green", 역삼역, 잠실역, 5);

        //when
        secondLine.addStation(강남역, 역삼역, 5);

        //then
         assertThat(secondLine.getStations()).containsExactly(강남역, 역삼역, 잠실역);
    }

    @Test
    void addLineStation_라인에_지하철역_추가_하행역변경성공케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 잠실역 = new Station("잠실역");
        Line secondLine = new Line("2호선", "green", 강남역, 역삼역, 5);

        //when
        secondLine.addStation(역삼역, 잠실역, 5);

        //then
        assertThat(secondLine.getStations()).containsExactly(강남역, 역삼역, 잠실역);
    }

    @Test
    void addLineStation_라인에_지하철역_추가_기존구간보다길이긴_예외케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 잠실역 = new Station("잠실역");
        Line secondLine = new Line("2호선", "green", 강남역, 잠실역, 5);

        //when-then
        assertThatThrownBy(() -> secondLine.addStation(강남역, 역삼역, 10)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void addLineStation_라인에_지하철역_추가_같은역구간추가_예외케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line secondLine = new Line("2호선", "green", 강남역, 역삼역, 5);

        //when-then
        assertThatThrownBy(() -> secondLine.addStation(강남역, 역삼역, 5)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void addLineStation_라인에_지하철역_추가_없은역구간추가_예외케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 잠실역 = new Station("잠실역");
        Station 잠실나루역 = new Station("잠실나루역");
        Line secondLine = new Line("2호선", "green", 강남역, 역삼역, 5);

        //when-then
        assertThatThrownBy(() -> secondLine.addStation(잠실역, 잠실나루역, 5)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void removeLineStation_라인의_지하철역_제거_성공케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 잠실역 = new Station("잠실역");
        Line secondLine = new Line("2호선", "green", 강남역, 잠실역, 10);
         secondLine.addStation(강남역, 역삼역, 5);

        //when
        secondLine.removeStation(강남역);

        //then
        assertThat(secondLine.getStations()).containsExactly(역삼역, 잠실역);
    }

    @Test
    void addLineStation_라인에_지하철역_제거_없는역제거_예외케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 잠실역 = new Station("잠실역");
        Line secondLine = new Line("2호선", "green", 강남역, 역삼역, 5);

        //when-then
        assertThatThrownBy(() -> secondLine.removeStation(잠실역)).isInstanceOf(RuntimeException.class);
    }

    @Test
    void addLineStation_라인에_지하철역_제거_역이두개뿐인경우제거_예외케이스() {
        //given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Line secondLine = new Line("2호선", "green", 강남역, 역삼역, 5);

        //when-then
        assertThatThrownBy(() -> secondLine.removeStation(역삼역)).isInstanceOf(RuntimeException.class);
    }


}