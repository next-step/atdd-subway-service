package nextstep.subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineNameTest {
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("공백이거나 null이면 IllegalArgumentException이 발생한다")
    void 공백이거나_null이면_IllegalArgumentException이_발생한다(String s) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new LineName(s));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", "abc", "abcd"})
    @DisplayName("이름이 공백이 아니면 정상이다")
    void 이름이_공백이_아니면_정상이다(String s) {
        assertDoesNotThrow(() -> new LineName(s));
    }
}