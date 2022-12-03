package nextstep.subway.path.dto;

import static nextstep.subway.path.domain.PathTest.경로_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathResponseTest {

    private Path path;

    @BeforeEach
    void setUp() {
        Station 교대역 = 지하철역_생성("교대역");
        Station 강남역 = 지하철역_생성("강남역");
        Station 양재역 = 지하철역_생성("양재역");
        path = 경로_생성(Arrays.asList(교대역, 강남역, 양재역), 20, 1000);
    }

    @Test
    @DisplayName("지하철역 목록, 거리, 요금을 받아 생성")
    void fromPath() {
        // when
        PathResponse pathResponse = PathResponse.from(path.getStations(), path.getDistance(), 1250);

        // then
        assertThat(pathResponse).isNotNull();
        assertThat(pathResponse.getStations()).hasSize(3);
        assertThat(pathResponse.getDistance()).isEqualTo(20);
    }

    private Station 지하철역_생성(String name) {
        return new Station.Builder()
                .name(name)
                .build();
    }
}
