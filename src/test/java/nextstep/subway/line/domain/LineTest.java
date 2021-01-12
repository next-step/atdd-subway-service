package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Line 도메인 단위 테스트")
class LineTest {

    private Line 이호선;
    private Station 강남역;
    private Station 역삼역;
    private int 강남역_역삼역_거리;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");
        강남역_역삼역_거리 = 3;
        이호선 = new Line("2호선", "green", 강남역, 역삼역, 강남역_역삼역_거리);
    }

    @DisplayName("역의 구간 목록을 순서대로 가져온다")
    @Test
    void getStations() {
        List<StationResponse> 구간_목록 = 이호선.getStationResponse();

        List<String> 구간_역이름_목록 = 구간_목록.stream()
                .map(response -> response.getName())
                .collect(Collectors.toList());

        assertThat(구간_역이름_목록).containsExactlyElementsOf(Arrays.asList("강남역", "역삼역"));
    }
}