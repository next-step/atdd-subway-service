package nextstep.subway.path.service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.fare.domain.FareLine;
import nextstep.subway.fare.dto.FareRequest;
import nextstep.subway.fare.dto.PathWithFareResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Lines;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
public class PathIntegrationTest {

    @Autowired
    PathService pathService;

    @Autowired
    LineRepository lineRepository;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    SectionRepository sectionRepository;

    @Autowired
    MemberRepository memberRepository;

    private Station 교대역;
    private Station 양재역;
    private Station 사당역;
    private Station 양재시민의숲;
    private Lines lineGroup;
    private Line 신분당선;
    private Member adult;

    @BeforeEach
    void setUp() {
        // given
        교대역 = new Station("교대역");
        양재역 = new Station("양재역");
        양재시민의숲 = new Station("양재시민의숲");
        Station 남부터미널 = new Station("남부터미널");
        Station 강남역 = new Station("강남역");
        사당역 = new Station("이수역");
        Station 이수역 = new Station("사당역");

        신분당선 = new Line("신분당선", "orange", 강남역, 양재역, 10, 2000);
        신분당선.add(양재역, 양재시민의숲, 5);
        Line 삼호선 = new Line("3호선", "orange", 교대역, 남부터미널, 10, 1000);
        삼호선.add(남부터미널, 양재역, 1);
        Line 이호선 = new Line("2호선", "orange", 교대역, 강남역, 5);
        Line 사호선 = new Line("사호선", "orange", 사당역, 이수역, 5);

        List<Line> lines = Arrays.asList(신분당선, 삼호선, 이호선, 사호선);
        lineRepository.saveAll(lines);
        lineGroup = new Lines(lines);
        adult = memberRepository.save(new Member("adult", "adult@adult", 19));
    }

    @AfterEach
    void tearDown() {
        sectionRepository.deleteAllInBatch();
        lineRepository.deleteAllInBatch();
        stationRepository.deleteAllInBatch();
        memberRepository.deleteAllInBatch();
    }

    @DisplayName("지하철 경로를 조회하고 순서, 거리가 일치하는지 확인한다.")
    @Test
    void findPath() {
        PathWithFareResponse pathResponse = findPath(adult, 교대역, 양재역);

        assertThat(pathResponse.getDistance()).isEqualTo(11);
        assertThat(pathResponse.getStations())
                .extracting("name")
                .containsExactly("교대역", "남부터미널", "양재역");
    }

    @DisplayName("지하철 경로를 조회하고 순서, 환승 노선의 추가요금을 반환한다.")
    @Test
    void findPathExchangeLine() {
        // given
        PathWithFareResponse pathResponse = findPath(adult, 교대역, 양재시민의숲);
        PathWithFareResponse pathResponseReverse = findPath(adult, 양재시민의숲, 교대역);

        // when then
        assertThat(pathResponse.getFare()).isEqualTo(3650);
        assertThat(pathResponse.getFare())
                .isEqualTo(pathResponseReverse.getFare());
    }

    @DisplayName("경로의 모든 추가 요금을 반환한다.")
    @Test
    void findPathOverCharge() {
        // given
        Member children = memberRepository.save(new Member("children", "children@children", 7));
        Member teenager = memberRepository.save(new Member("teenager", "teenager@teenager", 14));

        // when then
        assertThat(findPath(adult, 교대역, 양재시민의숲).getFare()).isEqualTo(3650);
        assertThat(findPath(new Member(), 교대역, 양재시민의숲).getFare()).isEqualTo(3650);
        assertThat(findPath(teenager, 교대역, 양재시민의숲).getFare()).isEqualTo(2640);
        assertThat(findPath(children, 교대역, 양재시민의숲).getFare()).isEqualTo(1650);
    }

    private PathWithFareResponse findPath(Member member, Station source, Station target) {
        return pathService.findPath(new LoginMember(member.getId(), member.getEmail(), member.getAge()), source.getId(), target.getId());
    }

    @DisplayName("경로 조회시 존재하지 않은 출발역이나 도착역을 조회 할 경우")
    @Test
    void findPathWhenNotExistStation() {
        // given
        Station 서울역 = stationRepository.save(new Station("서울역"));

        // when then
        assertThatThrownBy(() -> findPath(adult, 교대역, 서울역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않은 출발역이나 도착역입니다.");
    }

    @DisplayName("경로 조회시 출발역과 도착역이 같은 경우")
    @Test
    void findPathWhenEqualStation() {
        // when then
        assertThatThrownBy(() -> findPath(adult, 교대역, 교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 같습니다.");
    }

    @DisplayName("출발역과 도착역이 연결이 되어 있지 않은 경우")
    @Test
    void findPathWhenNotConnectedStation() {
        // when then
        assertThatThrownBy(() -> findPath(adult, 사당역, 교대역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발역과 도착역이 연결 되어 있지 않습니다.");
    }
}
