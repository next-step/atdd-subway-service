package nextstep.subway.favorite.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StringUtilsTest {
    @DisplayName("스트링 타입 long 타입으로 변환 테스트")
    @Test
    void stringToLong() {
        assertThat(StringUtils.stringToLong("5").get()).isEqualTo(5);
    }

    @DisplayName("스트링 타입 long 타입으로 변환 실패시 빈 값 반환 테스트")
    @Test
    void stringToLongByNotNumber() {
        assertThat(StringUtils.stringToLong("글자")).isEmpty();
    }
}
