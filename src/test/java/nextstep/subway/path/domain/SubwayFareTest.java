package nextstep.subway.path.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import nextstep.subway.member.domain.MemberAgeType;
import nextstep.subway.path.dto.SubwayFareRequest;

class SubwayFareTest {

    @DisplayName("어린이 회원은 350원이 공제된 금액에 50% 할인이 적용된 요금이 청구된다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "9,500", "11,500", "49,850", "57,900", "58,950", "120,1300"})
    void getSubwayUsageFareByKid(int distance, int expected) {
        int subwayUsageFare = SubwayFare.getSubwayUsageFare(new SubwayFareRequest(distance, 100,
            MemberAgeType.KID));

        assertEquals(expected, subwayUsageFare);
    }

    @DisplayName("청소년 회원은 350원이 공제된 금액에 20% 할인이 적용된 요금이 청구된다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "9,800", "11,800", "49,1360", "57,1440", "58,1520", "120,2080"})
    void getSubwayUsageFareByAdolescent(int distance, int expected) {
        int subwayUsageFare = SubwayFare.getSubwayUsageFare(new SubwayFareRequest(distance, 100,
            MemberAgeType.ADOLESCENT));

        assertEquals(expected, subwayUsageFare);
    }

    @DisplayName("어린이, 청소년이 아닌 회원은 할인이 적용되지 않은 요금이 청구된다.")
    @ParameterizedTest
    @CsvSource(value = {"0,0", "9,1350", "11,1350", "49,2050", "57,2150", "58,2250", "120,2950"})
    void getSubwayUsageFareByNone(int distance, int expected) {
        int subwayUsageFare = SubwayFare.getSubwayUsageFare(new SubwayFareRequest(distance, 100,
            MemberAgeType.NONE));

        assertEquals(expected, subwayUsageFare);
    }
}
