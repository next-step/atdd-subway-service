package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.PathFindServiceTest.노선생성;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.line.domain.Line;
import nextstep.subway.station.domain.Station;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SubwayFarePolicyTest {

    private SubwayFarePolicy policy = new DefaultSubwayFarePolicy();

    Station 강남역 = new Station("강남역");
    Station 역삼역 = new Station("역삼역");
    Line 이호선 = 노선생성("이호선", "green", 강남역, 역삼역, 5, 500);
    Line 삼호선 = 노선생성("삼호선", "orange", 강남역, 역삼역, 5, 800);

    @ParameterizedTest
    @CsvSource(delimiterString = ":", value = {"5:0", "10:0", "11:100", "20:200", "50:800", "51:900", "66:1000",
            "67:1100"})
    void 거리별_추가요금_계산_테스트(String distance, String expectedOverFare) {
        int actualOverFare = policy.calculateOverFareByDistance(Integer.parseInt(distance));
        assertThat(actualOverFare).isEqualTo(Integer.parseInt(expectedOverFare));
    }

    @Test
    void 노선별_추가요금_계산_테스트() {
        int actualOverFare = policy.calculateOverFareByLine(Sets.newLinkedHashSet(이호선, 삼호선));
        assertThat(actualOverFare).isEqualTo(삼호선.getExtraCharge());
    }
}
