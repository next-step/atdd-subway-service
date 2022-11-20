package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("색상 관련 도메인 테스트")
public class ColorTest {

    @DisplayName("색상 생성 시 null이면 에러가 발생한다.")
    @Test
    void createColorThrowErrorWhenColorIsNull() {
        // when & throw
        assertThatThrownBy(() -> Color.from(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선색상은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("색상 생성 시 비어있으면 에러가 발생한다.")
    @Test
    void createColorThrowErrorWhenColorIsEmpty() {
        // when & throw
        assertThatThrownBy(() -> Color.from(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선색상은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("색상을 생성하면 색상을 조회할 수 있다.")
    @Test
    void createColor() {
        // given
        String actual = "bg-green";

        // when
        Color color = Color.from(actual);

        // then
        assertThat(color.value()).isEqualTo(actual);
    }

    @DisplayName("색상이 동일하면 동일한 객체이다.")
    @Test
    void equalColor() {
        // given
        String color = "bg-green";
        String duplicateColor = "bg-green";

        // when & then
        assertThat(Color.from(color)).isEqualTo(Color.from(duplicateColor));
    }
}
