package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StringUtils 테스트")
public class StringUtilsTest {

    @DisplayName("문자열이 null이거나 비어있으면 참이다.")
    @Test
    void ifStringIsNullOrEmptyThenTrue() {
        // when & then
        assertAll(
                () -> assertThat(StringUtils.isNullOrEmpty(null)).isTrue(),
                () -> assertThat(StringUtils.isNullOrEmpty("")).isTrue(),
                () -> assertThat(StringUtils.isNullOrEmpty("abc")).isFalse()
        );
    }
}
