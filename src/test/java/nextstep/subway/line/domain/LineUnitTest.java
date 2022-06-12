package nextstep.subway.line.domain;

import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.SectionAddException;
import nextstep.subway.line.exception.SectionSizeMinimunException;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class LineUnitTest {
    private Station 청담역;
    private Station 뚝섬유원지역;
    private Station 건대입구역;
    private Station 어린이대공원역;
    private Station 군자역;
    private Line 칠호선;

    @BeforeEach
    void init() {
        청담역 = new Station("청담역");
        뚝섬유원지역 = new Station("뚝섬유원지역");
        건대입구역 = new Station("건대입구역");
        어린이대공원역 = new Station("어린이대공원역");
        군자역 = new Station("군자역");

        칠호선 = new Line("칠호선", "yellow", 청담역, 건대입구역, 20);
    }

    @Test
    @DisplayName("노선에 포함된 역 리스트 가져오기")
    void getStations() {
        assertThat(칠호선.getStations()).containsExactly(청담역, 건대입구역);
    }

    @Test
    @DisplayName("노선에 포함된 역 Response 리스트 가져오기")
    void getStationResponses() {
        assertThat(칠호선.getStationResponses()
                        .stream()
                        .map(StationResponse::getId))
                        .containsExactly(청담역.getId(), 건대입구역.getId());
    }

    @Test
    @DisplayName("노선 Response 가져오기")
    void findLineResponses() {
        LineResponse lineResponse = 칠호선.findLineResponses();
        assertThat(lineResponse.getId()).isEqualTo(칠호선.getId());
        assertThat(lineResponse.getStations()
                               .stream()
                               .map(StationResponse::getId))
                               .containsExactly(청담역.getId(), 건대입구역.getId());
    }

    @Test
    @DisplayName("노선에 구간 추가 (HappyPath)")
    void addLineStation() {
        칠호선.addLineStation(건대입구역, 군자역, 20);
        assertThat(칠호선.getStations()).containsExactly(청담역, 건대입구역, 군자역);
    }

    @Test
    @DisplayName("추가하려는 구간이 특정 구간의 상행과 하행을 모두 포함하면 추가 불가")
    void addLineStationHasUpAndDownStation() {
        assertThatThrownBy(() -> {
            칠호선.addLineStation(청담역, 건대입구역, 20);
        }).isInstanceOf(SectionAddException.class)
        .hasMessageContaining(SectionAddException.SECTION_HAS_UP_AND_DOWN_STATION_MSG);
    }

    @Test
    @DisplayName("추가하려는 구간이 특정 구간의 상행과 하행 중 하나라도 포함하지 않으면 추가 불가")
    void addLineStationHasNotUpAndDownStation() {
        assertThatThrownBy(() -> {
            칠호선.addLineStation(어린이대공원역, 군자역, 20);
        }).isInstanceOf(SectionAddException.class)
        .hasMessageContaining(SectionAddException.SECTION_HAS_NOT_UP_AND_DOWN_STATION_MSG);
    }

    @Test
    @DisplayName("노선에 구간 제거 (HappyPath)")
    void removeLineStation() {
        칠호선.addLineStation(건대입구역, 군자역, 20);
        칠호선.removeLineStation(청담역);
        assertThat(칠호선.getStations()).containsExactly(건대입구역, 군자역);
    }

    @Test
    @DisplayName("노선에 구간이 하나 남았을 때 제거 불가")
    void removeLineStationWithOutSection() {
        assertThatThrownBy(() -> {
            칠호선.removeLineStation(건대입구역);
        }).isInstanceOf(SectionSizeMinimunException.class)
        .hasMessageContaining(SectionSizeMinimunException.SECTION_SIZE_MINIMUN_MSG);
    }
}
