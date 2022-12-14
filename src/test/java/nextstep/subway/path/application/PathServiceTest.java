package nextstep.subway.path.application;

import nextstep.subway.fare.application.FareService;
import nextstep.subway.fixture.LineFixture;
import nextstep.subway.fixture.StationFixture;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private FareService fareService;

    @InjectMocks
    private PathService pathService;


    /**
     * Given 시작역과 도착역이 주어진경우
     * When 최단 경로 조회시
     * Then 최단 경로를 반환한다
     */
    @DisplayName("강남역 - 삼성역 최단경로 조회")
    @Test
    void save_line_with_request_test() {
        // given
        long sourceStationId = 1L;
        long targetStationId = 2L;

        // 이호선 : 강남역 - 교대역 - 삼성역
        given(stationRepository.findById(sourceStationId)).willReturn(Optional.of(StationFixture.강남역));
        given(stationRepository.findById(targetStationId)).willReturn(Optional.of(StationFixture.삼성역));
        given(lineRepository.findAllWithSections()).willReturn(Arrays.asList(LineFixture.이호선));
        given(fareService.calculateFare(any())).willReturn(Fare.of(1_250));

        // when
        PathResponse pathResponse = pathService.getShortestDistance(sourceStationId, targetStationId);

        // then
        then(stationRepository).should(times(2)).findById(any());
        then(lineRepository).should(times(1)).findAllWithSections();
        assertAll(
                () -> assertThat(getStationNames(pathResponse)).containsExactly("강남역", "교대역", "삼성역"),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(10),
                () -> assertThat(pathResponse.getFare()).isEqualTo(1_250)
        );
    }

    private List<String> getStationNames(PathResponse pathResponse) {
        return pathResponse.getStations().stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
    }
}
