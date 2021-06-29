package nextstep.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.calculator.AgeCalculatorService;

@ExtendWith(SpringExtension.class)
public class AgeCalculatorServiceTest {

    @InjectMocks
    private AgeCalculatorService ageCalculatorService;

    @ParameterizedTest
    @DisplayName("20세 이상은 100%, 12세 ~19세는 80%, 5~11세는 50%, 5세 미만은 100% 할인을 받는다")
    @MethodSource("ageResultSet")
    void discountRateTest(LoginMember member, double result) {
        assertThat(ageCalculatorService.getDiscountRate(member)).isEqualTo(result);
    }

    private static Stream<Arguments> ageResultSet() {
        return Stream.of(
            Arguments.of(new LoginMember(1L, "test", 19), 1),
            Arguments.of(new LoginMember(1L, "test", 18), 0.8),
            Arguments.of(new LoginMember(1L, "test", 13), 0.8),
            Arguments.of(new LoginMember(1L, "test", 12), 0.5),
            Arguments.of(new LoginMember(1L, "test", 6), 0.5),
            Arguments.of(new LoginMember(1L, "test", 5), 0));
    }
}
