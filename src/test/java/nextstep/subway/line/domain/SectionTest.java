package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionTest {

    private Station 강남역;
    private Station 양재역;
    private Line 신분당선;
    private Section 구간;

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        신분당선 = new Line("신분당선", "red", 강남역, 양재역, 10);
        구간 = new Section(신분당선, 강남역, 양재역, 10);
    }

    @DisplayName("구간에서 해당 역이 반대편 역인지 확인")
    @Test
    void 반대편_역_여부() {
        assertAll(
                () -> assertThat(구간.isStationOppositeOf(강남역, StationPosition.DOWN_STATION)).isTrue(),
                () -> assertThat(구간.isStationOppositeOf(강남역, StationPosition.UP_STATION)).isFalse(),
                () -> assertThat(구간.isStationOppositeOf(양재역, StationPosition.UP_STATION)).isTrue(),
                () -> assertThat(구간.isStationOppositeOf(양재역, StationPosition.DOWN_STATION)).isFalse()
        );
    }

    @DisplayName("구간에서 해당 위치의 지하철역 찾기")
    @Test
    void 위치로_역_찾기() {
        Station 찾은_상행역 = 구간.getStationByPosition(StationPosition.UP_STATION);
        Station 찾은_하행역 = 구간.getStationByPosition(StationPosition.DOWN_STATION);

        assertAll(
                () -> assertThat(찾은_상행역).isEqualTo(강남역),
                () -> assertThat(찾은_하행역).isEqualTo(양재역)
        );
    }

    @DisplayName("기존 구간 길이보다 크거나 같은 길이의 구간 등록 시도 시 예외 처리")
    @Test
    void 유효하지_않은_구간길이() {
        Station 정자역 = new Station("정자역");
        Section 구간2 = new Section(신분당선, 양재역, 정자역, 12);

        Station 강남_양재_사이_신설역 = new Station("강남_양재_사이_신설역");
        Section newSection1 = new Section(신분당선, 강남_양재_사이_신설역, 양재역, 6);

        Station 양재_정자_사이_신설역 = new Station("양재_정자_사이_신설역");
        Section newSection2 = new Section(신분당선, 양재역, 양재_정자_사이_신설역, 12);
        Section newSection3 = new Section(신분당선, 양재역, 양재_정자_사이_신설역, 15);
        Section newSection4 = new Section(신분당선, 양재_정자_사이_신설역, 정자역, 12);

        구간.splitSection(newSection1);

        assertAll(
                () -> assertThat(구간.getUpStation()).isEqualTo(강남역),
                () -> assertThat(구간.getDownStation()).isEqualTo(강남_양재_사이_신설역),
                () -> assertThat(구간.getDistanceValue()).isEqualTo(4),
                () -> assertThatThrownBy(() -> 구간2.splitSection(newSection2)).isInstanceOf(RuntimeException.class),
                () -> assertThatThrownBy(() -> 구간2.splitSection(newSection3)).isInstanceOf(RuntimeException.class),
                () -> assertThatThrownBy(() -> 구간2.splitSection(newSection4)).isInstanceOf(RuntimeException.class)
        );
    }
}
