package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
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


@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    private Line 일호선;
    private Line 신분당선;

    private Station 판교역;
    private Station 성남역;
    private Station 광교역;
    private Station 인천역;
    private Station 동인천역;
    private Station 주안역;

    @Mock
    SectionRepository sectionRepository;

    @Mock
    StationRepository stationRepository;

    @InjectMocks
    PathService pathService;



    @BeforeEach
    void setUp() {


        판교역 = new Station("판교역");
        성남역 = new Station("성남역");
        광교역 = new Station("광교역");

        인천역 = new Station("인천역");
        동인천역 = new Station("동인천역");
        주안역 = new Station("주안역");
        // --  판교 - 주안 - 성남 - 광교
        신분당선 = new Line("신분당선", "레드", 판교역, 주안역, 10);
        신분당선.addSection(new Section(신분당선, 주안역, 성남역, 100));
        신분당선.addSection(new Section(신분당선, 성남역, 광교역, 42));
        신분당선.changeFare(2_000);

        // -- 인천 - 판교 -- 동인천  -- 광교
        일호선 = new Line("일호선", "블루", 인천역, 판교역, 33);
        일호선.addSection(new Section(일호선, 판교역, 동인천역, 31));
        일호선.addSection(new Section(일호선, 동인천역, 광교역, 41));

        일호선.changeFare(3_000);

    /*
        인천 - 판교 -- *일호선* -- 동인천  -- 광교
              |*신분당선                    | *신분당선
              주안 ----------------------성남


     */
    }

    @Test
    @DisplayName("비회원 최적 경로 찾기 - 장거리 금액")
    void findShortPathLongDistance() {
        PathRequest pathRequest = new PathRequest(1L, 2L);

        given(sectionRepository.findAll()).willReturn(stubSections());
        given(stationRepository.getById(pathRequest.getSource())).willReturn(판교역);
        given(stationRepository.getById(pathRequest.getTarget())).willReturn(광교역);

        final PathResponse pathResponse = pathService.findShortPath(LoginMember.guestLogin(), new PathRequest(1L, 2L));

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(StationResponse.of(판교역), StationResponse.of(동인천역), StationResponse.of(광교역)),
                () -> assertThat(pathResponse.getFare()).isEqualTo(5350)
        );
    }

    @Test
    @DisplayName("비회원 최적 경로 찾기 - 초과 거리 금액")
    void findShortPathMidDistance() {
        PathRequest pathRequest = new PathRequest(1L, 2L);

        given(sectionRepository.findAll()).willReturn(stubSections());
        given(stationRepository.getById(pathRequest.getSource())).willReturn(동인천역);
        given(stationRepository.getById(pathRequest.getTarget())).willReturn(광교역);

        final PathResponse pathResponse = pathService.findShortPath(LoginMember.guestLogin(), new PathRequest(1L, 2L));

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(StationResponse.of(동인천역), StationResponse.of(광교역)),
                () -> assertThat(pathResponse.getFare()).isEqualTo(4_950)
        );


    }

    @Test
    @DisplayName("비회원 최적 경로 찾기 - 디폴트 금액")
    void findShortPathDefaultDistance() {
        PathRequest pathRequest = new PathRequest(1L, 2L);

        given(sectionRepository.findAll()).willReturn(stubSections());
        given(stationRepository.getById(pathRequest.getSource())).willReturn(판교역);
        given(stationRepository.getById(pathRequest.getTarget())).willReturn(주안역);

        final PathResponse pathResponse = pathService.findShortPath(LoginMember.guestLogin(), new PathRequest(1L, 2L));

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(StationResponse.of(판교역), StationResponse.of(주안역)),
                () -> assertThat(pathResponse.getFare()).isEqualTo(3_250)
        );
    }

    @Test
    @DisplayName("어린이 회원 최적 경로 찾기 - 장거리 금액")
    void findShortPathLongDistanceChildren() {
        PathRequest pathRequest = new PathRequest(1L, 2L);
        LoginMember loginMember = new LoginMember(1L, "asda@gmail.com", 10);
        given(sectionRepository.findAll()).willReturn(stubSections());
        given(stationRepository.getById(pathRequest.getSource())).willReturn(판교역);
        given(stationRepository.getById(pathRequest.getTarget())).willReturn(광교역);


        final PathResponse pathResponse = pathService.findShortPath(loginMember, new PathRequest(1L, 2L));

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(StationResponse.of(판교역), StationResponse.of(동인천역), StationResponse.of(광교역)),
                () -> assertThat(pathResponse.getFare()).isEqualTo(2500)
        );
    }

    @Test
    @DisplayName("청소년 회원 최적 경로 찾기 - 장거리 금액")
    void findShortPathLongDistanceThenAger() {
        PathRequest pathRequest = new PathRequest(1L, 2L);
        LoginMember loginMember = new LoginMember(1L, "asda@gmail.com", 18);
        given(sectionRepository.findAll()).willReturn(stubSections());
        given(stationRepository.getById(pathRequest.getSource())).willReturn(판교역);
        given(stationRepository.getById(pathRequest.getTarget())).willReturn(광교역);


        final PathResponse pathResponse = pathService.findShortPath(loginMember, new PathRequest(1L, 2L));

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(StationResponse.of(판교역), StationResponse.of(동인천역), StationResponse.of(광교역)),
                () -> assertThat(pathResponse.getFare()).isEqualTo(4000)
        );
    }

    @Test
    @DisplayName("어른 회원 최적 경로 찾기 - 장거리 금액")
    void findShortPathLongDistanceAdult() {
        PathRequest pathRequest = new PathRequest(1L, 2L);
        LoginMember loginMember = new LoginMember(1L, "asda@gmail.com", 25);
        given(sectionRepository.findAll()).willReturn(stubSections());
        given(stationRepository.getById(pathRequest.getSource())).willReturn(판교역);
        given(stationRepository.getById(pathRequest.getTarget())).willReturn(광교역);


        final PathResponse pathResponse = pathService.findShortPath(loginMember, new PathRequest(1L, 2L));

        assertAll(
                () -> assertThat(pathResponse.getStations()).containsExactly(StationResponse.of(판교역), StationResponse.of(동인천역), StationResponse.of(광교역)),
                () -> assertThat(pathResponse.getFare()).isEqualTo(5350)
        );
    }

    private List<Section> stubSections() {
        List<Section> stubSections = new ArrayList<>();

        final Sections 신분당선구간 = 신분당선.getSections();
        final Sections 일호선구간 = 일호선.getSections();
        stubSections.addAll(신분당선구간.getSectionElements());
        stubSections.addAll(일호선구간.getSectionElements());

        return stubSections;
    }




}
