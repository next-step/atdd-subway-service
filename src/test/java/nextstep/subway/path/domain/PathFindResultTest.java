package nextstep.subway.path.domain;

import static java.util.Collections.emptyList;
import static nextstep.subway.path.domain.PathFindServiceTest.노선생성;
import static org.assertj.core.api.Assertions.assertThat;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PathFindResultTest {
    Station 강남역 = new Station("강남역");
    Station 역삼역 = new Station("역삼역");
    Line 이호선 = 노선생성("이호선", "green", 강남역, 역삼역, 5, 500);
    Line 삼호선 = 노선생성("삼호선", "orange", 강남역, 역삼역, 5, 800);
    Line 추가요금없는노선 = 노선생성("삼호선", "orange", 강남역, 역삼역, 5, 0);
    LoginMember 비회원 = new LoginMember();

    @ParameterizedTest
    @CsvSource(delimiterString = ":", value = {"5:0", "10:0", "11:100", "20:200", "50:800", "51:900", "66:1000",
            "67:1100"})
    void 거리별_추가요금_계산_테스트(String distance, String expectedOverFare) {
        PathFindResult pathFindResult = new PathFindResult(emptyList(), Sets.newLinkedHashSet(추가요금없는노선), Integer.parseInt(distance));
        pathFindResult.applyFarePolicy(비회원);
        assertThat(pathFindResult.getFare()).isEqualTo(SubwayFare.DEFAULT_FARE.plus(Integer.parseInt(expectedOverFare)));
    }

    @Test
    void 노선별_추가요금_계산_테스트() {
        PathFindResult pathFindResult = new PathFindResult(emptyList(), Sets.newLinkedHashSet(이호선, 삼호선), 10);
        pathFindResult.applyFarePolicy(비회원);
        assertThat(pathFindResult.getFare()).isEqualTo(SubwayFare.DEFAULT_FARE.plus(삼호선.getExtraCharge()));
    }
}
