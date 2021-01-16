package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PathServiceTest {

    @InjectMocks
    private PathService pathService;

    @Mock
    private LineRepository lineRepository;

    @Mock
    private StationRepository stationRepository;

    private Station 강남역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 양재역;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;

    /**
     *              거리 10
     * 교대역    --- *2호선* ---   강남역
     * |                        |
     * *3호선*                   *신분당선*
     * 거리 3                     거리 10
     * |                        |
     * 남부터미널역  --- *3호선* --- 양재역
     *              거리 2
     */

    @BeforeEach
    void setUp() {
        강남역 = makeMockStation(1L, "강남역");
        교대역 = makeMockStation(2L, "교대역");
        양재역 = makeMockStation(3L, "양재역");
        남부터미널역 = makeMockStation(4L, "남부터미널역");

        이호선 = makeMockLine("이호선", 0, Arrays.asList(makeMockSection(교대역, 강남역, 10, 이호선, 0)));
        삼호선 = makeMockLine("삼호선", 500, Arrays.asList(makeMockSection(교대역, 남부터미널역, 3, 삼호선, 500),
                makeMockSection(남부터미널역, 양재역, 2, 삼호선, 500)));
        신분당선 = makeMockLine("신분당선", 1000, Arrays.asList(makeMockSection(강남역, 양재역, 10, 신분당선, 1000)));

        given(lineRepository.findAll()).willReturn(Arrays.asList(신분당선, 이호선, 삼호선));
        given(stationRepository.findById(강남역.getId())).willReturn(Optional.of(강남역));
        given(stationRepository.findById(교대역.getId())).willReturn(Optional.of(교대역));
        given(stationRepository.findById(양재역.getId())).willReturn(Optional.of(양재역));
        given(stationRepository.findById(남부터미널역.getId())).willReturn(Optional.of(남부터미널역));
    }

    @DisplayName("로그인 사용자 최단 경로 조회시 나이를 사용")
    @Test
    void findShortestPath_로그인_사용자() {
        // given
        given(stationRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(강남역, 남부터미널역));
        LoginMember loginMember = mock(LoginMember.class);
        given(loginMember.hasAuthentication()).willReturn(true);
        given(loginMember.getAge()).willReturn(20);

        // when
        PathRequest pathRequest = new PathRequest(강남역.getId(), 남부터미널역.getId());
        PathResponse pathResponse = pathService.findShortestPath(pathRequest, loginMember);

        // then
        assertThat(pathResponse.getStations())
                .map(StationResponse::getName)
                .containsExactly("강남역", "양재역", "남부터미널역");

        assertThat(pathResponse.getDistance()).isEqualTo(12);
        verify(loginMember, times(1)).getAge();
    }

    @DisplayName("로그인 없이 최단 경로를 조회할 수 있음")
    @Test
    void calculatePath_비로그인_사용자() {
        // given
        given(stationRepository.findAllByIdIn(anyList())).willReturn(Arrays.asList(강남역, 남부터미널역));
        LoginMember loginMember = mock(LoginMember.class);
        given(loginMember.hasAuthentication()).willReturn(false);

        // when
        PathRequest pathRequest = new PathRequest(강남역.getId(), 남부터미널역.getId());
        PathResponse pathResponse = pathService.findShortestPath(pathRequest, loginMember);

        // then
        assertThat(pathResponse.getStations())
                .map(StationResponse::getName)
                .containsExactly("강남역", "양재역", "남부터미널역");
        assertThat(pathResponse.getDistance()).isEqualTo(12);
        verify(loginMember, never()).getAge();
    }

    @DisplayName("존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findNotExistStations() {
        // given
        given(stationRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        PathRequest pathRequest = new PathRequest(강남역.getId(), 남부터미널역.getId());
        assertThatThrownBy(() -> pathService.findShortestPath(pathRequest, new LoginMember()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는");
    }

    private Line makeMockLine(String name, int fare, List<Section> sections) {
        Line line = mock(Line.class);
        given(line.getName()).willReturn(name);
        given(line.getFare()).willReturn(Fare.of(fare));
        given(line.getSections()).willReturn(sections);
        return line;
    }

    private Section makeMockSection(Station upStation, Station downStation, int distance, Line line, int fare) {
        Section section = mock(Section.class);
        given(section.getUpStation()).willReturn(upStation);
        given(section.getDownStation()).willReturn(downStation);
        given(section.getDistance()).willReturn(new Distance(distance));
        given(section.getFare()).willReturn(Fare.of(fare));
        return section;
    }

    private Station makeMockStation(Long id, String name) {
        Station station = mock(Station.class);
        given(station.getId()).willReturn(id);
        given(station.getName()).willReturn(name);
        ReflectionTestUtils.setField(station,"id", id);
        given(station.isSameStation(anyLong())).willCallRealMethod();
        return station;
    }
}
