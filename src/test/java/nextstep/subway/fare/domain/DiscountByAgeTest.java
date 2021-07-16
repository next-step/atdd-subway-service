package nextstep.subway.fare.domain;

import nextstep.subway.member.domain.Age;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("연령 별 할인율 테스트")
class DiscountByAgeTest {


    @CsvSource(delimiterString = ":",
            value = {
                    "3350:5:3350",
                    "3350:6:1850",
                    "3350:12:1850",
                    "3350:13:2750",
                    "3350:18:2750",
                    "3350:19:3350",
            })
    @ParameterizedTest
    void calculate_성공(int fareArgument, int ageArgument, int expectedResult) {
        // given
        Fare fare = new Fare(fareArgument);
        Age age = new Age(ageArgument);

        DiscountByAge.calculate(fare, age);
    }
}
