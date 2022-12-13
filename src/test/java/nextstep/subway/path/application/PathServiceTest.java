package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.line.application.LineService;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.stream.Collectors;

import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    @Mock
    StationService stationService;
    @Mock
    LineService lineService;
    @InjectMocks
    PathService pathService;

    private Line 삼호선;
    private Line 이호선;
    private Line 신분당선;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;
    private Station 강남역;
    private Station 간이역;

    @BeforeEach
    void setup() {
        교대역 = 교대역();
        남부터미널역 = 남부터미널역();
        양재역 = 양재역();
        강남역 = 강남역();
        간이역 = 간이역();

        신분당선 = new Line("신분당선", "red", 강남역, 양재역, new Distance(10));
        이호선 = new Line("2호선", "Yellow-green", 교대역, 강남역, new Distance(10));
        삼호선 = new Line("3호선", "Orange", 교대역, 양재역, new Distance(5));
        삼호선.addLineStation(new Section(삼호선, 교대역, 남부터미널역, new Distance(3)));
    }

    @DisplayName("두 역의 최단 경로를 조회한다.")
    @Test
    void 역_사이의_최단_거리_조회_테스트() {
        // given
        given(stationService.findById(2L)).willReturn(강남역);
        given(stationService.findById(4L)).willReturn(남부터미널역);
        given(lineService.findAllLines()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        // when
        PathResponse pathResponse = pathService.findBestPath(LoginMember.GUEST, 강남역.getId(), 남부터미널역.getId());

        // then
        assertAll(
                () -> assertThat(pathResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList())).containsExactly(강남역.getName(), 양재역.getName(), 남부터미널역.getName()),
                () -> assertThat(pathResponse.getDistance()).isEqualTo(12)
        );
    }

    @DisplayName("같은 역 간의 최단 거리를 조회한다.")
    @Test
    void 동일역_사이의_최단_거리_조회_테스트() {
        // given
        given(stationService.findById(2L)).willReturn(강남역);
        given(stationService.findById(2L)).willReturn(강남역);
        given(lineService.findAllLines()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatThrownBy(
                () -> pathService.findBestPath(LoginMember.GUEST, 강남역.getId(), 강남역.getId())
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("연결되지 않은 역 사이의 최단 거리를 조회한다.")
    @Test
    void 연결되지_않은_역_사이의_최단_거리_조회_테스트() {
        // given
        given(stationService.findById(2L)).willReturn(강남역);
        given(stationService.findById(5L)).willReturn(간이역);
        given(lineService.findAllLines()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));

        // when & then
        assertThatThrownBy(
                () -> pathService.findBestPath(LoginMember.GUEST, 강남역.getId(), 간이역.getId())
        ).isInstanceOf(InvalidDataException.class);
    }
}
