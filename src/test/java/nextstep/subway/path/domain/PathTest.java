package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AnonymousMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.domain.Fare;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class PathTest {
    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;
    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Station 신도림역;
    private Station 광교역;
    private AnonymousMember 익명사용자;
    private LoginMember 일반회원;
    private LoginMember 청소년회원;
    private LoginMember 어린이회원;
    private PathFinder pathFinder;

    /**
     * 교대역    --- *2호선*(10) ---  강남역 --- *2호선*(36) --- 신도림역
     * |                            |
     * *3호선*(12)               *신분당선*(10)
     * |                            |
     * 남부터미널역 --- *3호선*(15) --- 양재역 --- *신분당선*(21)  --- 광교역
     */
    @BeforeEach
    public void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
        교대역 = Station.of(3L, "교대역");
        남부터미널역 = Station.of(4L, "남부터미널역");
        신도림역 = Station.of(5L, "신도림역");
        광교역 = Station.of(6L, "광교역");

        신분당선 = new Line.Builder("신분당선", "bg-red-600")
                .extraFare(900)
                .section(Section.of(강남역, 양재역, 10))
                .build();
        이호선 = new Line.Builder("이호선", "bg-green-600")
                .section(Section.of(교대역, 강남역, 10))
                .build();
        삼호선 = new Line.Builder("삼호선", "bg-orange-600")
                .section(Section.of(교대역, 양재역, 27))
                .build();

        삼호선.addSection(Section.of(교대역, 남부터미널역, 12));
        이호선.addSection(Section.of(강남역, 신도림역, 36));
        신분당선.addSection(Section.of(양재역, 광교역, 21));

        익명사용자 = new AnonymousMember();
        일반회원 = new LoginMember(1L, "basic@email.com", 19);
        청소년회원 = new LoginMember(2L, "basic@email.com", 13);
        어린이회원 = new LoginMember(3L, "basic@email.com", 6);

        pathFinder = new DijkstraShortestPathFinder(Arrays.asList(신분당선, 이호선, 삼호선));
    }

    @DisplayName("총 거리가 10km 이내인 경우 요금을 조회한다.")
    @Test
    void less_than_10km() {
        // given
        Path path = pathFinder.findShortestPath(교대역, 강남역);

        // when
        Fare fare = path.calculateFare(익명사용자);

        // then
        assertThat(fare.get()).isEqualTo(1250);
    }

    @DisplayName("총 거리가 15km 인 경우 요금을 조회한다.")
    @Test
    void greater_than_10km() {
        // given
        Path path = pathFinder.findShortestPath(남부터미널역, 양재역);

        // when
        Fare fare = path.calculateFare(익명사용자);

        // then
        assertThat(fare.get()).isEqualTo(1350);
    }

    @DisplayName("총 거리가 58km 인 경우 요금을 조회한다.")
    @Test
    void greater_than_50km() {
        // given
        Path path = pathFinder.findShortestPath(남부터미널역, 신도림역);

        // when
        Fare fare = path.calculateFare(익명사용자);

        // then
        assertThat(fare.get()).isEqualTo(2150);
    }

    @DisplayName("경로 중 추가 요금이 있는 노선 포함 시 요금을 조회한다.")
    @Test
    void extra_line() {
        // given
        Path path = pathFinder.findShortestPath(교대역, 양재역);

        // when
        Fare fare = path.calculateFare(익명사용자);

        // then
        assertThat(fare.get()).isEqualTo(2350);
    }

    @DisplayName("일반(19세)인 경우 최단 경로와 거리, 요금을 조회한다.")
    @Test
    void basic_fare() {
        // given
        Path path = pathFinder.findShortestPath(교대역, 양재역);

        // when
        Fare fare = path.calculateFare(일반회원);

        // then
        assertThat(fare.get()).isEqualTo(2350);
    }

    @DisplayName("청소년(13세)인 경우 요금을 조회한다.")
    @Test
    void teenagers_fare() {
        // given
        Path path = pathFinder.findShortestPath(교대역, 양재역);

        // when
        Fare fare = path.calculateFare(청소년회원);

        // then
        assertThat(fare.get()).isEqualTo(1600);
    }

    @DisplayName("어린이(6세)인 경우 요금을 조회한다.")
    @Test
    void kids_fare() {
        // given
        Path path = pathFinder.findShortestPath(교대역, 양재역);

        // when
        Fare fare = path.calculateFare(어린이회원);

        // then
        assertThat(fare.get()).isEqualTo(1000);
    }
}