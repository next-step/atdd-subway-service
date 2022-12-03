package nextstep.subway.path.domain;

import nextstep.subway.path.fare.AboveFiftyKiloExtraFarePolicy;
import nextstep.subway.path.fare.BasicFarePolicy;
import nextstep.subway.path.fare.FarePolicies;
import nextstep.subway.path.fare.UntilFiftyKiloExtraFarePolicy;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class FarePolicyTest {

    FarePolicies farePolicies;

    @BeforeEach
    void setup() {
        farePolicies = new FarePolicies(
                Lists.newArrayList(
                        new BasicFarePolicy(),
                        new UntilFiftyKiloExtraFarePolicy(),
                        new AboveFiftyKiloExtraFarePolicy()
                        ));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,9,10})
    void testCalculateBasicFare(int distance) {
        Fare fare = farePolicies.calculate(distance);

        assertThat(fare).isEqualTo(BasicFarePolicy.BASIC_FARE);
    }

    @ParameterizedTest
    @CsvSource({"10,1250","14,1250","15,1350","49,1950","50,2050"})
    void testUntilFiftyKiloFarePolicy(int distance, int fareAmount) {
        Fare fare = farePolicies.calculate(distance);

        assertThat(fare).isEqualTo(Fare.valueOf(fareAmount));
    }

    @ParameterizedTest
    @CsvSource({"57,2050", "58,2150", "66,2250", "106,2750"})
    void testAboveFiftyKiloFarePolicy(int distance, int fareAmount) {
        Fare fare = farePolicies.calculate(distance);

        assertThat(fare).isEqualTo(Fare.valueOf(fareAmount));
    }

}
