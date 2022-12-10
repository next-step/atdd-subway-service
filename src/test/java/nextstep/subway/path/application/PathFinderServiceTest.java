package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("경로 조회 서비스 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class PathFinderServiceTest {
    @Mock
    private SectionRepository sectionRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    private PathFinderService pathFinderService;

    private final Line 이호선 = Line.ofNameAndColor("2호선", "초록색");
    private final Line 신분당선 = Line.ofNameAndColor("신분당선", "빨간색");
    private final Line 삼호선 = Line.ofNameAndColor("3호선", "주황색");
    private final Station 강남역 = new Station("강남역");
    private final Station 양재역 = new Station("양재역");
    private final Station 교대역 = new Station("교대역");
    private final Station 남부터미널역 = new Station("남부터미널역");
    private final Section 강남역_양재역 = Section.of(신분당선, 강남역, 양재역, 10);
    private final Section 교대역_강남역 = Section.of(이호선, 교대역, 강남역, 10);
    private final Section 교대역_남부터미널역 = Section.of(삼호선, 교대역, 남부터미널역, 3);
    private final Section 남부터미널역_양재역 = Section.of(삼호선, 남부터미널역, 양재역, 5);
    private final LoginMember 로그인_유저 = new LoginMember(1L, "email@email", 10);

    @DisplayName("게스트 최단거리 경로 조회")
    @Test
    void findShortestPath_guest() {
        // given
        Long 강남역_ID = 1L;
        Long 남부터미널역_ID = 2L;
        given(stationService.findStationById(강남역_ID)).willReturn(강남역);
        given(stationService.findStationById(남부터미널역_ID)).willReturn(남부터미널역);
        given(sectionRepository.findAll()).willReturn(Arrays.asList(
                강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역));
        // when

        PathResponse 경로_조회_결과 = pathFinderService.getShortestPath(강남역_ID, 남부터미널역_ID, LoginMember.GUEST);

        // then
        assertAll(
                ()->assertThat(경로_조회_결과.getStations().stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("강남역", "교대역", "남부터미널역"),
                ()->assertThat(경로_조회_결과.getDistance()).isEqualTo(13),
                ()->assertThat(경로_조회_결과.getFare()).isEqualTo(1250)
        );
    }

    @DisplayName("로그인유저 최단거리 경로 조회")
    @Test
    void findShortestPath_user() {
        // given
        Long 강남역_ID = 1L;
        Long 남부터미널역_ID = 2L;
        given(stationService.findStationById(강남역_ID)).willReturn(강남역);
        given(stationService.findStationById(남부터미널역_ID)).willReturn(남부터미널역);
        given(sectionRepository.findAll()).willReturn(Arrays.asList(
                강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역));
        // when

        PathResponse 경로_조회_결과 = pathFinderService.getShortestPath(강남역_ID, 남부터미널역_ID, 로그인_유저);

        // then
        assertAll(
                ()->assertThat(경로_조회_결과.getStations().stream().map(StationResponse::getName).collect(Collectors.toList())).containsExactly("강남역", "교대역", "남부터미널역"),
                ()->assertThat(경로_조회_결과.getDistance()).isEqualTo(13),
                ()->assertThat(경로_조회_결과.getFare()).isEqualTo(450)
        );
    }

}
