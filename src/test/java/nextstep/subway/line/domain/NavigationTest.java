package nextstep.subway.line.domain;

import static java.util.Arrays.asList;
import static nextstep.subway.common.Message.MESSAGE_EQUALS_START_STATION_END_STATION;
import static nextstep.subway.common.Message.MESSAGE_NOT_CONNECTED_START_STATION_AND_END_STATION;
import static nextstep.subway.common.Message.MESSAGE_NOT_EXISTS_START_STATION_OR_END_STATION;
import static nextstep.subway.line.domain.fixture.LineFixture.*;
import static nextstep.subway.line.domain.fixture.SectionFixture.*;
import static nextstep.subway.station.domain.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import nextstep.subway.line.dto.PathResponse;
import nextstep.subway.station.dto.StationResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class NavigationTest {

    @Test
    void 지하철노선이_없는경우_예외() {
        // then
        Assertions.assertThatThrownBy(() -> {
            Navigation.of(null);
        }).isInstanceOf(RuntimeException.class);
    }

    @Test
    void 출발역과_도착역이_같은_경우_예외() {
        // then
        Assertions.assertThatThrownBy(() -> {
            Navigation navigation = Navigation.of(asList(일호선()));
            navigation.findFastPath(강남역(), 강남역());
        }).isInstanceOf(RuntimeException.class)
            .hasMessage(MESSAGE_EQUALS_START_STATION_END_STATION.getMessage());
    }

    @Test
    void 존재하지_않은_출발역이나_도착역을_조회_할_경우_예외() {
        // given
        List<Line> 전체노선 = createSubwayMap();

        // then
        Assertions.assertThatThrownBy(() -> {
            Navigation navigation = Navigation.of(전체노선);
            navigation.findFastPath(양평역(), 용문역());
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MESSAGE_NOT_EXISTS_START_STATION_OR_END_STATION.getMessage());
    }

    @Test
    void 출발역과_도착역이_연결이_되어_있지_않은_경우_예외() {

        // given
        List<Line> 전체노선 = createSubwayMap();

        // then
        Assertions.assertThatThrownBy(() -> {
                Navigation navigation = Navigation.of(전체노선);
                navigation.findFastPath(강남역(), 오이도역());
            }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(MESSAGE_NOT_CONNECTED_START_STATION_AND_END_STATION.getMessage());
    }


    /**
     *
     * 사호선 사당역 ---------오이도역
     *
     *                                    합정역
     *                                     |(10)
     *                                  홍대입구역
     *                                    |(5)
     * 강남역 ---(10)-*일호선* ---------  신촌역  --(5)----- 역삼역
     *   |                               |                 |
     *  (4)                           *2호선* (5)         (4)
     *  |                               |                 |
     * 안국역 -*3호선(2)*-종로3가역 -(2)-을지로3가역 --(1)-- 충무로역
     */
    @Test
    void 최단거리_경로_확인() {
        // given
        List<Line> subwayMap = createSubwayMap();

        // when
        Navigation navigation = Navigation.of(subwayMap);
        PathResponse fastPath = navigation.findFastPath(안국역(), 역삼역());


        // then
        List<StationResponse> stations = fastPath.getStations();
        Stream<String> stationNames = stations.stream().map(StationResponse::getName);
        assertThat(stationNames).containsExactly("안국역", "종로3가역", "을지로3가역", "충무로역", "역삼역");
        assertThat(fastPath.getDistance()).isEqualTo(9.0);
    }

    private List<Line> createSubwayMap() {
        // given
        // 강남역 -- 신촌역 -- 역삼역
        Line 일호선 = 일호선();
        일호선.addSection(일호선_구간_강남역_신촌역());
        일호선.addLineStation(일호선_구간_신촌역_역삼역());

        // 합저역 -- 홍대입구역 - 신촌역 -- 을지로3가역
        Line 이호선 = 이호선();
        이호선.addSection(이호선_구간_합정역_홍대입구역());
        이호선.addLineStation(이호선_구간_홍대입구역_신촌역());
        이호선.addLineStation(이호선_구간_신촌역_을지로3가역());

        // 안국역 - 종로 3가역 - 을지로3가역 - 충무로역
        Line 삼호선 = 삼호선();
        삼호선.addSection(삼호선_구간_안국역_종로3가역());
        삼호선.addLineStation(삼호선_구간_종로3가역_을지로3가역());
        삼호선.addLineStation(삼호선_구간_을지로3가역_충무로역());

        삼호선.addLineStation(삼호선_일호선_연결_안국역_강남역());
        삼호선.addLineStation(삼호선_일호선_연결_충무로역_역삼역());

        Line 사호선 = 사호선();
        사호선.addSection(사호선_구간_사당역_오이도역());


        return asList(일호선, 이호선, 삼호선, 사호선);
    }

}