package nextstep.subway.path.unit.fare;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.path.domain.fare.FareStrategy;
import nextstep.subway.path.domain.path.SectionEdge;
import nextstep.subway.path.domain.path.SectionEdges;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareStrategyTest {

    private final Line 이호선 = Line.ofNameAndColorAndSurCharge("2호선", "초록색", 400);
    private final Line 신분당선 = Line.ofNameAndColorAndSurCharge("신분당선", "빨간색", 300);
    private final Line 삼호선 = Line.ofNameAndColorAndSurCharge("3호선", "주황색", 200);
    private final Station 강남역 = new Station("강남역");
    private final Station 양재역 = new Station("양재역");
    private final Station 교대역 = new Station("교대역");
    private final Station 남부터미널역 = new Station("남부터미널역");
    private final Section 강남역_양재역 = Section.of(신분당선, 강남역, 양재역, 10);
    private final Section 교대역_강남역 = Section.of(이호선, 교대역, 강남역, 10);
    private final Section 교대역_남부터미널역 = Section.of(삼호선, 교대역, 남부터미널역, 7);
    private final Section 남부터미널역_양재역 = Section.of(삼호선, 남부터미널역, 양재역, 5);

    private SectionEdges sectionEdges;

    @BeforeEach
    void setUp() {
        sectionEdges = SectionEdges.of(Arrays.asList(강남역_양재역, 교대역_강남역, 교대역_남부터미널역, 남부터미널역_양재역)
                .stream()
                .map(SectionEdge::new)
                .collect(Collectors.toList()));
    }


    @DisplayName("할인 요금을 조회할 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"5,10,650", "20,10,750", "60,10,1100", "5,15,1040,", "20,15,1200",
            "60,15,1760"}, delimiter = ',')
    void createFare_discount_test(int distanceInput, int ageInput, int expectedFare) {
        // given
        Distance distance = Distance.from(distanceInput);
        LoginMember loginMember = new LoginMember(1L, "email@email.com", ageInput);

        // when
        FareStrategy fareStrategy = FareStrategy.of(sectionEdges, distance, loginMember);

        // then
        assertEquals(expectedFare, fareStrategy.getFare());
    }


}
