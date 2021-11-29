package nextstep.subway.path.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PathFinderTest {

    private static final Line 이호선 = new Line("이호선", "red");
    private static final Station 교대역 = new Station("교대역");
    private static final Station 양재역 = new Station("양재역");
    private static final Station 선릉역 = new Station("선릉역");
    private static final Sections 구간 = new Sections(Arrays.asList(
            new Section(이호선, 교대역, 선릉역, 10),
            new Section(이호선, 선릉역, 양재역, 10)));

    @Test
    void findShortestPath_최단_경로를_조회한다() {
        PathFinder pathFinder = new PathFinder();
        PathResponse response = pathFinder.findShortestPath(구간, 교대역, 양재역);
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(20),
                () -> assertThat(response.getStations()).containsExactly(
                        StationResponse.of(교대역),
                        StationResponse.of(선릉역),
                        StationResponse.of(양재역)));
    }
}