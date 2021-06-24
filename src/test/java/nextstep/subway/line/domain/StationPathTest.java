package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

class StationPathTest {
    @DisplayName("라인 생성시 경로 추가")
    @Test
    void createLine() {
        //given
        Station 강남역 = new Station("강남역");
        Station 선릉역 = new Station("선릉역");

        new Line("신분당선"
                , "빨간색"
                , 강남역
                , 선릉역
                , new Distance(5));

        //when
        List<StationResponse> shortestPath = StationPath.findShortestPath(강남역, 선릉역);
        //then
        assertThat(shortestPath).isNotNull();
        assertThat(shortestPath.get(0).getName()).isEqualTo(강남역.getName());
        assertThat(shortestPath.get(1).getName()).isEqualTo(선릉역.getName());
    }
}