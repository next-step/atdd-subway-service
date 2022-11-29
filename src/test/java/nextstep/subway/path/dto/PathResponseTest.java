package nextstep.subway.path.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
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
        path = Path.of(Arrays.asList(교대역, 강남역, 양재역), 20);
    }

    @Test
    @DisplayName("Path를 받아 생성")
    void fromPath() {
        // when
        PathResponse pathResponse = PathResponse.from(path);

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
