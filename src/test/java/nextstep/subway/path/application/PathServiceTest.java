package nextstep.subway.path.application;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.Money;
import nextstep.subway.path.dto.PathRequest;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-11
 */
@SpringBootTest
@ActiveProfiles("test")
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private DatabaseCleanup databaseCleanup;


    private Station 교대역;
    private Station 강남역;
    private Station 양재역;
    private Station 남부터미널역;

    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;



    @BeforeEach
    void setup() {
        databaseCleanup.execute();

        교대역 = stationRepository.save(new Station("교대역"));
        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));
        이호선 = new Line("이호선", "green", Money.ZERO);
        삼호선 = new Line("삼호선", "yellow", Money.ZERO);
        신분당선 = new Line("신분당선", "red", Money.won(800L));
        lineRepository.save(이호선);
        lineRepository.save(삼호선);
        lineRepository.save(신분당선);

        이호선.addSection(new Section(이호선, 교대역, 강남역, 100));
        삼호선.addSection(new Section(삼호선, 교대역, 양재역, 300));
        삼호선.addSection(new Section(삼호선, 교대역, 남부터미널역, 100));
        신분당선.addSection(new Section(신분당선, 강남역, 양재역, 100));

        lineRepository.save(이호선);
        lineRepository.save(삼호선);
        lineRepository.save(신분당선);
    }



    @DisplayName("경로 탐색 테스트")
    @Test
    void pathServiceTest() {

        PathRequest request = new PathRequest(교대역.getId(), 양재역.getId());

        LoginMember loginMember = new LoginMember(1L, "email", 20);
        PathResponse shortestPath = pathService.getShortestPath(request, loginMember);

        assertThat(shortestPath.getDistance()).isEqualTo(200);
        assertThat(shortestPath.getPrice()).isEqualTo( (long)(1250 + (200 / 8 * 100) + 800) );
        assertThat(shortestPath.getStations().get(0).getName()).isEqualTo("교대역");
        assertThat(shortestPath.getStations().get(1).getName()).isEqualTo("강남역");
        assertThat(shortestPath.getStations().get(2).getName()).isEqualTo("양재역");

    }

    @DisplayName("청소년 경로 탐색 테스트")
    @Test
    void pathServiceWithTeenagerTest() {

        PathRequest request = new PathRequest(교대역.getId(), 양재역.getId());

        LoginMember loginMember = new LoginMember(1L, "email", 19);
        PathResponse shortestPath = pathService.getShortestPath(request, loginMember);

        assertThat(shortestPath.getDistance()).isEqualTo(200);
        assertThat(shortestPath.getPrice()).isEqualTo( (long)((1250 + (200 / 8 * 100) + 800 - 350) * 0.8) );
        assertThat(shortestPath.getStations().get(0).getName()).isEqualTo("교대역");
        assertThat(shortestPath.getStations().get(1).getName()).isEqualTo("강남역");
        assertThat(shortestPath.getStations().get(2).getName()).isEqualTo("양재역");

    }

    @DisplayName("어린이 경로 탐색 테스트")
    @Test
    void pathServiceWithChildTest() {

        PathRequest request = new PathRequest(교대역.getId(), 양재역.getId());

        LoginMember loginMember = new LoginMember(1L, "email", 10);
        PathResponse shortestPath = pathService.getShortestPath(request, loginMember);

        assertThat(shortestPath.getDistance()).isEqualTo(200);
        assertThat(shortestPath.getPrice()).isEqualTo( (long)((1250 + (200 / 8 * 100) + 800 - 350) * 0.5) );
        assertThat(shortestPath.getStations().get(0).getName()).isEqualTo("교대역");
        assertThat(shortestPath.getStations().get(1).getName()).isEqualTo("강남역");
        assertThat(shortestPath.getStations().get(2).getName()).isEqualTo("양재역");

    }

    @DisplayName("노약자 경로 탐색 테스트")
    @Test
    void pathServiceWithOlderTest() {

        PathRequest request = new PathRequest(교대역.getId(), 양재역.getId());

        LoginMember loginMember = new LoginMember(1L, "email", 70);
        PathResponse shortestPath = pathService.getShortestPath(request, loginMember);

        assertThat(shortestPath.getDistance()).isEqualTo(200);
        assertThat(shortestPath.getPrice()).isEqualTo( 0 );
        assertThat(shortestPath.getStations().get(0).getName()).isEqualTo("교대역");
        assertThat(shortestPath.getStations().get(1).getName()).isEqualTo("강남역");
        assertThat(shortestPath.getStations().get(2).getName()).isEqualTo("양재역");

    }

}
