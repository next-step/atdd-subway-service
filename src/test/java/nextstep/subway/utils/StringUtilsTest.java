package nextstep.subway.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import nextstep.subway.common.constant.ErrorCode;
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

    @DisplayName("문자열이 정수가 아니면 Long 형태로 변환할 수 없다.")
    @Test
    void ifStringIsNotNumberThenThrowException() {
        // when & then
        assertThatThrownBy(() -> StringUtils.stringToLong("텍"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.숫자가_될_수_있는_문자열.getErrorMessage());
    }
}
