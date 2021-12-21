package nextstep.subway.auth.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class AgeTest {

    @DisplayName("청소년 나이 true 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {13, 18})
    void isYouth_true(int age) {
        // when & then
        assertThat(Age.of(age).isYouth()).isTrue();
    }

    @DisplayName("청소년 나이 false 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {12, 19})
    void isYouth_false(int age) {
        // when & then
        assertThat(Age.of(age).isYouth()).isFalse();
    }

    @DisplayName("어린이 나이 true 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {6, 12})
    void isChild_true(int age) {
        // when & then
        assertThat(Age.of(age).isChild()).isTrue();
    }

    @DisplayName("어린이 나이 false 테스트")
    @ParameterizedTest(name = "{displayName}{index} -> age: {0}")
    @ValueSource(ints = {5, 13})
    void isChild_false(int age) {
        // when & then
        assertThat(Age.of(age).isChild()).isFalse();
    }
}
