package nextstep.subway.fare.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.Member;
import nextstep.subway.path.domain.Fare;
import nextstep.subway.path.domain.FareCalculator;
import nextstep.subway.path.domain.Path;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("요금 계산 기능")
public class FareCalculatorTest {

    private static Station 강남역;
    private static Station 양재역;
    private static Station 남부터미널역;
    private static Station 교대역;

    private static Line 신분당선 = new Line("신분당선", "bg-red-600", 강남역, 양재역, 13, 900);
    private static Line 이호선 = new Line("이호선", "bg-red-600", 교대역, 강남역, 15, 800);
    private static Line 삼호선 = new Line("삼호선", "bg-red-600", 교대역, 양재역, 5, 0);

    private static Member 일반_사용자 = new Member("email1", "pwd", 30);
    private static Member 청소년_사용자 = new Member("email2", "pwd", 15);
    private static Member 어린이_사용자 = new Member("email3", "pwd", 10);

    @BeforeEach
    public void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        남부터미널역 = new Station("남부터미널역");
        교대역 = new Station("교대역");
    }

    @DisplayName("경로의 요금을 구한다 - 거리 별 요금")
    @ParameterizedTest
    @CsvSource(value = {"5:1250", "10:1250", "15:1350", "49:2050", "50:2050", "55:2150", "58:2150"},
        delimiter = ':')
    void calculateFareByDistance(int distance, int expectedFare) {
        // when
        Fare actualFare = FareCalculator.calculateExtraFare(distance);

        // then
        assertThat(actualFare.getValue()).isEqualTo(expectedFare);
    }

    @DisplayName("경로의 추가 요금을 구한다 - 노선 별 요금")
    @ParameterizedTest
    @MethodSource("generateLineAndFareList")
    void calculateFareByLine(List<Line> lines, int expectedFare) {
        // when
        Fare actualFare = FareCalculator.calculateExtraFare(lines);

        // then
        assertThat(actualFare.getValue()).isEqualTo(expectedFare);
    }

    @DisplayName("경로의 추가 요금을 구한다 - 연령 별 요금")
    @ParameterizedTest
    @MethodSource("generateMemberAndFareList")
    void calculateFareByAge(Member member, int expectedFare) {
        // given
        Fare baseFare = new Fare(1250);

        // when
        Fare actualFare = FareCalculator.calculateDiscount(baseFare, member);

        // then
        assertThat(actualFare.getValue()).isEqualTo(expectedFare);
    }

    @DisplayName("경로의 요금을 구한다 - 통합")
    @Test
    void calculateFare() {
        // given
        int expectedFare = (int) ((1250 + 800) - ((1250 + 800) - 350) * 0.5);
        Path path = 경로를_구했음();

        // when
        Fare actualFare = FareCalculator.calculateFare(path, 어린이_사용자);

        // then
        assertThat(actualFare.getValue()).isEqualTo(expectedFare);
    }

    private Path 경로를_구했음() {
        Sections pathSections = new Sections();
        pathSections.addSection(new Section(이호선, 교대역, 남부터미널역, 3));
        pathSections.addSection(new Section(이호선, 남부터미널역, 양재역, 2));
        return new Path(Arrays.asList(교대역, 남부터미널역, 양재역), 5, pathSections);
    }

    private static Stream<Arguments> generateLineAndFareList() {
        List<Arguments> listOfLinesAndFare = new LinkedList<>();
        listOfLinesAndFare.add(Arguments.of(Arrays.asList(삼호선), 0));
        listOfLinesAndFare.add(Arguments.of(Arrays.asList(이호선), 800));
        listOfLinesAndFare.add(Arguments.of(Arrays.asList(삼호선, 이호선), 800));
        listOfLinesAndFare.add(Arguments.of(Arrays.asList(이호선, 삼호선, 신분당선), 900));
        return listOfLinesAndFare.stream();
    }

    private static Stream<Arguments> generateMemberAndFareList() {
        List<Arguments> listOfLinesAndFare = new LinkedList<>();
        listOfLinesAndFare.add(Arguments.of(일반_사용자, 1250));
        listOfLinesAndFare.add(Arguments.of(청소년_사용자, 1070));
        listOfLinesAndFare.add(Arguments.of(어린이_사용자, 800));
        return listOfLinesAndFare.stream();
    }

}
