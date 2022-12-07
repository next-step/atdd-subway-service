package nextstep.subway.path.domain;

import com.google.common.collect.Lists;
import nextstep.subway.JpaEntityTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.path.domain.policy.*;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("요금 계산기 도메인 테스트")
@DataJpaTest
public class FareCalculatorTest extends JpaEntityTest {
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private StationRepository stationRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Station 사당역;
    private Station 교대역;
    private Station 강남역;
    private Station 역삼역;

    private Station 신사역;
    private Station 양재역;
    private Station 남부터미널역;

    private Line 신분당선;
    private Line 이호선;
    private Line 삼호선;

    private Member 성인;
    private Member 청소년;
    private Member 어린이;
    private Member 미취학아동;
    private Member 노인;

    private DiscountPolicy discountPolicy;
    private AdditionalFarePolicy additionalFarePolicy;
    private ChargeFarePolicy chargeFarePolicy;
    private FareCalculator fareCalculator;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        // 비이상 지하철역 생성
        사당역 = new Station("사당역");
        stationRepository.save(사당역);

        // 신분당
        신사역 = new Station("신사역");
        양재역 = new Station("양재역");

        // 2호선
        교대역 = new Station("교대역");
        강남역 = new Station("강남역");
        역삼역 = new Station("역삼역");

        // 3호선
        남부터미널역 = new Station("남부터미널역");

        신분당선 = new Line("신분당선", "red lighten-1", 신사역, 강남역, 8, 900);
        이호선 = new Line("2호선", "green lighten-1", 교대역, 역삼역, 60, 300);
        삼호선 = new Line("3호선", "orange darken-1", 교대역, 양재역, 34, 0);

        신분당선.addSection(강남역, 양재역, 15);
        이호선.addSection(교대역, 강남역, 20);
        삼호선.addSection(교대역, 남부터미널역, 27);

        lineRepository.saveAll(Lists.newArrayList(신분당선, 이호선, 삼호선));

        // 멤버 생성
        성인 = new Member("EMAIL", "PASSWORD", 19);
        청소년 = new Member("EMAIL", "PASSWORD", 18);
        어린이 = new Member("EMAIL", "PASSWORD", 6);
        미취학아동 = new Member("EMAIL", "PASSWORD", 5);
        노인 = new Member("EMAIL", "PASSWORD", 65);
        memberRepository.saveAll(Lists.newArrayList(성인, 청소년, 어린이, 미취학아동, 노인));
        flushAndClear();

        chargeFarePolicy = new ChargeFareByDistancePolicy();
        additionalFarePolicy = new AdditionalFareByLinePolicy();
        discountPolicy = new DiscountByAgePolicy();
        fareCalculator = new FareCalculator(chargeFarePolicy, additionalFarePolicy, discountPolicy);
    }

    @DisplayName("10km 이내 거리별 요금 정책 테스트")
    @Test
    void defaultFareByDistanceTest() {
        // given
        Sections sections = new Sections(sectionRepository.findAll());
        Path path = new PathFinder().find(sections, 신사역, 강남역); // 8

        // when
        int fare = chargeFarePolicy.charge(path.getDistance());

        // then
        assertThat(fare).isEqualTo(1_250);
    }

    @DisplayName("10km 초과 ~ 50km 이내 거리별 요금 정책 테스트")
    @Test
    void midExcessFareByDistanceTest() {
        // given
        Sections sections = new Sections(sectionRepository.findAll());
        Path path = new PathFinder().find(sections, 교대역, 남부터미널역); // 27

        // when
        int fare = chargeFarePolicy.charge(path.getDistance());

        // then
        assertThat(fare).isEqualTo(1_650);
    }

    @DisplayName("50km 초과 거리별 요금 정책 테스트")
    @Test
    void longExcessFareByDistanceTest() {
        // given
        Sections sections = new Sections(sectionRepository.findAll());
        Path path = new PathFinder().find(sections, 교대역, 역삼역); // 60

        // when
        int fare = chargeFarePolicy.charge(path.getDistance());

        // then
        assertThat(fare).isEqualTo(2_250);
    }

    @DisplayName("노선 추가요금 부과 테스트")
    @Test
    void lineAdditionalFareTest() {
        // given
        Sections sections = new Sections(sectionRepository.findAll());
        Path path = new PathFinder().find(sections, 남부터미널역, 역삼역); // 62

        // when
        int additionalFare = additionalFarePolicy.addFare(sections, path.getStationPaths());

        // then
        assertThat(additionalFare).isEqualTo(900);
    }

    @DisplayName("성인 - 나이별 할인 정책 테스트")
    @Test
    void discountFareByAge_성인() {
        // when
        int fare = discountPolicy.discount(1_250, 성인.getAge());

        // then
        assertThat(fare).isEqualTo(1_250);
    }

    @DisplayName("청소년 - 나이별 할인 정책 테스트")
    @Test
    void discountFareByAge_청소년() {
        // when
        int fare = discountPolicy.discount(1_250, 청소년.getAge());

        // then
        assertThat(fare).isEqualTo(720);
    }

    @DisplayName("어린이 - 나이별 할인 정책 테스트")
    @Test
    void discountFareByAge_어린이() {
        // when
        int fare = discountPolicy.discount(1_250, 어린이.getAge());

        // then
        assertThat(fare).isEqualTo(450);
    }

    @DisplayName("미취학아동 - 나이별 할인 정책 테스트")
    @Test
    void discountFareByAge_미취학아동() {
        // when
        int fare = discountPolicy.discount(1_250, 미취학아동.getAge());

        // then
        assertThat(fare).isEqualTo(0);
    }

    @DisplayName("노인 - 나이별 할인 정책 테스트")
    @Test
    void discountFareByAge_노인() {
        // when
        int fare = discountPolicy.discount(1_250, 노인.getAge());

        // then
        assertThat(fare).isEqualTo(0);
    }
}
