package nextstep.subway.line.domain;

import static java.util.Arrays.asList;
import static nextstep.subway.line.domain.NavigationTest.createSubwayMap;
import static nextstep.subway.station.domain.StationFixture.강남역;
import static nextstep.subway.station.domain.StationFixture.신촌역;
import static nextstep.subway.station.domain.StationFixture.안국역;
import static nextstep.subway.station.domain.StationFixture.을지로3가역;
import static nextstep.subway.station.domain.StationFixture.종로3가역;
import static nextstep.subway.station.domain.StationFixture.홍대입구역;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

public class FareCalculateTest {

    /**
     * 1호선 추가요금 1000;
     * 2호선 추가요금 2000
     * 3호선 추가요금 0
     * 4호선 추가요금 500
     *
     * 요금  = 라인 추가요금 + 기본요금(1250) + 나이에따른 할인율 + 거리에따른 추가요금
     *
     */
    @ParameterizedTest
    @MethodSource("providerJoinMember")
    void 가입한_총_요금(int age, List<Station> fastPath, Distance distance, Fare fare) {
        // given
        LoginMember loginMember = new LoginMember(1L, "email", age);
        FareCalculate fareCalculate = FareCalculate.of(distance);

        // when
        Fare calculate = fareCalculate.calculate(fastPath, createSubwayMap(), loginMember);

        // then
        Assertions.assertThat(calculate).isEqualTo(fare);
    }

    public static Stream<Arguments> providerJoinMember() {
        return Stream.of(
            Arguments.of(10, asList(강남역(), 안국역(), 을지로3가역()), new Distance(50L), new Fare(2200L)),
            Arguments.of(10, asList(안국역(), 종로3가역(), 을지로3가역()), new Distance(10L), new Fare(400L))
        );
    }

    @ParameterizedTest
    @MethodSource("providerGuest")
    void 가입_안한_총_요금(List<Station> fastPath, Distance distance, Fare fare) {
        // given
        FareCalculate fareCalculate = FareCalculate.of(distance);

        // when
        Fare calculate = fareCalculate.calculate(fastPath, createSubwayMap(), new LoginMember());

        // then
        Assertions.assertThat(calculate).isEqualTo(fare);
    }

    public static Stream<Arguments> providerGuest() {
        return Stream.of(
            Arguments.of(asList(강남역(), 안국역(), 을지로3가역()), new Distance(50L), new Fare(3050L)),
            Arguments.of(asList(안국역(), 종로3가역(), 을지로3가역()), new Distance(10L), new Fare(1250L))
        );
    }


    @ParameterizedTest
    @MethodSource("provideStationsAndFare")
    void 라인중_가장_큰_추가_요금을_가져온다(Fare subwayFare, List<Station> stations) {
        // given
        Fare addFare = FareCalculate.of(new Distance(0L))
            .getSurcharge(stations, createSubwayMap());
        Assertions.assertThat(addFare).isEqualTo(subwayFare);
    }

    public static Stream<Arguments> provideStationsAndFare() {
        return Stream.of(
            Arguments.of(new Fare(2000L), asList(안국역(), 강남역(), 신촌역(), 홍대입구역())),
            Arguments.of(new Fare(0L), asList(안국역(), 을지로3가역()))
        );
    }

}
