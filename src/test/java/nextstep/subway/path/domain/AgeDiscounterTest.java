package nextstep.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class AgeDiscounterTest {
    @DisplayName("나이에 적합한 할인 정책을 찾을 수 있다.")
    @ParameterizedTest
    @MethodSource("findTestResource")
    void findTest(Integer age, AgeDiscounter expected) {
        AgeDiscounter ageDiscounter = AgeDiscounter.find(age);

        assertThat(ageDiscounter).isEqualTo(expected);
    }
    public static Stream<Arguments> findTestResource() {
        return Stream.of(
                Arguments.of(5, AgeDiscounter.NONE),
                Arguments.of(6, AgeDiscounter.KID),
                Arguments.of(12, AgeDiscounter.KID),
                Arguments.of(13, AgeDiscounter.TEEN),
                Arguments.of(18, AgeDiscounter.TEEN),
                Arguments.of(19, AgeDiscounter.NONE)
        );
    }
}