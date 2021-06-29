package nextstep.subway.line.domain.wrapped;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class LineColorTest {
    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("공백이거나 null이면 IllegalArgumentException이 발생한다")
    void 공백이거나_null이면_IllegalArgumentException이_발생한다(String s) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new LineColor(s));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a", "ab", "abc", "abcd"})
    @DisplayName("색깔이 공백이 아니면 정상이다")
    void 색깔이_공백이_아니면_정상이다(String s) {
        assertDoesNotThrow(() -> new LineColor(s));
    }

    @Test
    @DisplayName("같은 값의 색깔은 hashcode와 equals가 같다")
    void 같은_값의_색깔은_hashcode와_equals가_같다() {
        LineColor color1 = new LineColor("ABCD");
        LineColor color2 = new LineColor("ABCD");
        LineColor color3 = new LineColor("A");

        assertThat(color1.hashCode())
                .isEqualTo(color2.hashCode());
        assertThat(color1)
                .isEqualTo(color2);

        assertThat(color2.hashCode())
                .isNotEqualTo(color3.hashCode());
        assertThat(color2)
                .isNotEqualTo(color3);
    }
}