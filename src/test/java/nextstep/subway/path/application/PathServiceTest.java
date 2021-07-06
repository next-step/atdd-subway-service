package nextstep.subway.path.application;

import nextstep.subway.auth.Policy.MemberPolicy;
import nextstep.subway.auth.domain.BasicMember;
import nextstep.subway.auth.domain.ChildMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Excetion.NotConnectStationException;
import nextstep.subway.line.collection.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("지하철 경로 조회 테스트")
@ExtendWith(MockitoExtension.class)
public class PathServiceTest {
    private Station 문래역;
    private Station 신도림역;
    private Station 영등포구청역;
    private Station 합정역;
    private Station 홍대역;
    private Station 부산역;
    private Station 까치산역;
    private Line 이호선;

    private PathService pathService;

    @Mock
    private SectionRepository sectionRepository;

    @Mock
    private StationRepository stationRepository;

    @BeforeEach
    public void setUp() {
        pathService = new PathService(sectionRepository, stationRepository);

        // given
        // 지하철역_등록_되어_있음
        신도림역 = new Station(1L, "신도림역");
        문래역 = new Station(2L, "문래역");
        영등포구청역 = new Station(3L, "영등포구청역");
        합정역 = new Station(4L, "합정역");
        홍대역 = new Station(5L, "홍대역");
        부산역 = new Station(6L, "부산역");
        까치산역 = new Station(7L, "까치산역");

        // and
        // 노선 등록되어 있음
        이호선 = new Line("이호선", "GREEN", 신도림역, 문래역, 10, 500);

        when(sectionRepository.findAll()).thenReturn(
                Arrays.asList(new Section(신도림역, 문래역, new Distance(10)),
                        new Section(문래역, 영등포구청역, new Distance(5)),
                        new Section(문래역, 합정역, new Distance(5)),
                        new Section(영등포구청역, 홍대역, new Distance(10)),
                        new Section(합정역, 홍대역, new Distance(11)),
                        new Section(부산역, 까치산역, new Distance(11))
                ));
    }

    @Test
    @DisplayName("지하철 경로 조회")
    void 지하철_경로_조회() {
        // given
        // 노선에 구간 등록되어 있음
        노선_구간_등록_되어_있음();

        // when
        // 신도림에서 홍대역까지의 경로를 구한다.
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(신도림역));
        when(stationRepository.findById(5L)).thenReturn(Optional.ofNullable(홍대역));

        // then
        PathResponse pathResponse = pathService.findOptimalPath(new BasicMember(), 1L, 5L);
        List<StationResponse> stations = pathResponse.getStations();
        List<Station> expectedStations = Arrays.asList(
                신도림역, 문래역, 영등포구청역, 홍대역
        );

        List<String> response = stations.stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        List<String> expected = expectedStations.stream()
                .map(it -> it.getName())
                .collect(Collectors.toList());

        assertThat(response).containsExactlyElementsOf(expected);
    }

    @Test
    @DisplayName("출발역과 도착역이 같은 예외 오류 발생")
    void exception_equals_sourceId_and_targetId() {
        // when
        // 신도림에서 신도림역까지의 경로를 구한다.
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(신도림역));

        // then
        // 예외 발생
        assertThatThrownBy(() -> pathService.findOptimalPath(new BasicMember(),1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("연결되어 있지 않은 경로 예외 오류 발생")
    void exception_nonConnected_sourceId_and_targetId() {
        // when
        // 신도림에서 부산역까지의 경로를 구한다.
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(신도림역));
        when(stationRepository.findById(6L)).thenReturn(Optional.ofNullable(부산역));

        // then
        // 예외 발생
        assertThatThrownBy(() -> pathService.findOptimalPath(new BasicMember(),1L, 6L))
                .isInstanceOf(NotConnectStationException.class);
    }

    @Test
    @DisplayName("지하철 거리 조회")
    void 지하철_거리_조회() {
        // given
        // 지하철 구간 등록되어 있음
        노선_구간_등록_되어_있음();

        // when
        // 최단 거리 조회함
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(신도림역));
        when(stationRepository.findById(5L)).thenReturn(Optional.ofNullable(홍대역));

        // then
        // 거리가 응답됨
        PathResponse pathResponse = pathService.findOptimalPath(new BasicMember(),1L, 5L);
        assertThat(pathResponse.getDistance()).isEqualTo(25);
    }

    @Test
    @DisplayName("지하철 요금 조회")
    void 지하철_요금_조회() {
        // given
        // 지하철 구간 등록되어 있음
        노선_구간_등록_되어_있음();

        // when
        // 신도림에서 홍대역까지의 경로를 구한다.
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(신도림역));
        when(stationRepository.findById(5L)).thenReturn(Optional.ofNullable(홍대역));

        // then
        // 거리가 응답됨 30km -> 1,550 + 500
        PathResponse pathResponse = pathService.findOptimalPath(new BasicMember(),1L, 5L);
        assertThat(pathResponse.getCharge()).isEqualTo(2050);
    }

    @Test
    @DisplayName("유아용 지하철 요금 조회")
    void 유아용_지하철_요금_조회() {
        // given
        // 지하철 구간 등록되어 있음
        노선_구간_등록_되어_있음();

        // and
        // 유아 로그인 되어 있음
        LoginMember loginMember = ChildMember.of(new Member(1L, "child@test.com", "1234", 7), MemberPolicy.CHILD_MEMBER);

        // when
        // 신도림에서 홍대역까지의 경로를 구한다.
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(신도림역));
        when(stationRepository.findById(5L)).thenReturn(Optional.ofNullable(홍대역));

        // then
        // 거리가 응답됨 30km -> (1,550 + 500 - 350) * 0.5 = 850
        PathResponse pathResponse = pathService.findOptimalPath(loginMember,1L, 5L);
        assertThat(pathResponse.getCharge()).isEqualTo(850);
    }

    @Test
    @DisplayName("청소년 지하철 요금 조회")
    void 청소년_지하철_요금_조회() {
        // given
        // 지하철 구간 등록되어 있음
        노선_구간_등록_되어_있음();

        // and
        // 청소년 로그인 되어 있음
        LoginMember loginMember = ChildMember.of(new Member(1L, "teenager@test.com", "1234", 15), MemberPolicy.TEENAGER_MEMBER);

        // when
        // 신도림에서 홍대역까지의 경로를 구한다.
        when(stationRepository.findById(1L)).thenReturn(Optional.ofNullable(신도림역));
        when(stationRepository.findById(5L)).thenReturn(Optional.ofNullable(홍대역));

        // then
        // 거리가 응답됨 30km -> (1,550 + 500 - 350) * 0.8 = 850
        PathResponse pathResponse = pathService.findOptimalPath(loginMember,1L, 5L);
        assertThat(pathResponse.getCharge()).isEqualTo(1360);
    }

    private void 노선_구간_등록_되어_있음() {
        when(sectionRepository.findByUpStationIdAndDownStationId(1L, 2L)).thenReturn(new Section(이호선, 신도림역, 문래역, new Distance(10)));
        when(sectionRepository.findByUpStationIdAndDownStationId(2L, 3L)).thenReturn(new Section(이호선, 문래역, 영등포구청역, new Distance(10)));
        when(sectionRepository.findByUpStationIdAndDownStationId(3L, 5L)).thenReturn(new Section(이호선, 영등포구청역, 홍대역, new Distance(10)));
    }
}
