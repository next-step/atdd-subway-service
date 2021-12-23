package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    Station 교대역 = new Station(1L, "교대역");
    Station 양재역 = new Station(2L, "양재역");
    Station 강남역 = new Station(3L, "강남역");

    Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 10);
    Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 1);
    Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5);

    @Mock
    LineRepository lineRepository;
    @Mock
    StationRepository stationRepository;

    @Test
    @DisplayName("최단경로를 조회한다.")
    void findPath() {
        // Given
        PathService pathService = new PathService(lineRepository, stationRepository);
        // 노선을 전부 조회 한다.
        when(lineRepository.findAll()).thenReturn(Arrays.asList(신분당선, 이호선, 삼호선));
//        // 출발 역 조회한다.
        when(stationRepository.findById(양재역.getId())).thenReturn(java.util.Optional.of(양재역));
//        // 도착 역 조회한다.
        when(stationRepository.findById(강남역.getId())).thenReturn(java.util.Optional.of(강남역));

        // When
        PathResponse pathResponse = pathService.findPath(2L, 3L, new LoginMember());

        // Then
        Assertions.assertAll(
                () -> assertThat(pathResponse.getStations().stream()
                    .map(stationResponse -> stationResponse.getId())
                    .collect(Collectors.toList())).contains(양재역.getId(), 교대역.getId(), 강남역.getId()),
                () -> assertThat(pathResponse.getStations().stream()
                        .map(stationResponse -> stationResponse.getName())
                        .collect(Collectors.toList())).contains(양재역.getName(), 교대역.getName(), 강남역.getName())
        );
    }
}