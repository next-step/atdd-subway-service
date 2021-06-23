package nextstep.subway.line.domain;

import nextstep.subway.line.domain.wrapped.LineName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    @DisplayName("같은 값의 이름은 hashcode와 equals가 같다")
    void 같은_값의_이름은_hashcode와_equals가_같다() {
        LineName name1 = new LineName("ABCD");
        LineName name2 = new LineName("ABCD");
        LineName name3 = new LineName("A");

        assertThat(name1.hashCode())
                .isEqualTo(name2.hashCode());
        assertThat(name1)
                .isEqualTo(name2);

        assertThat(name2.hashCode())
                .isNotEqualTo(name3.hashCode());
        assertThat(name2)
                .isNotEqualTo(name3);
    }
}