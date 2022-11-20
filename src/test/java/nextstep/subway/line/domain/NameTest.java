package nextstep.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.subway.common.constant.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("이름 관련 도메인 테스트")
public class NameTest {

    @DisplayName("이름 생성 시 null이면 에러가 발생한다.")
    @Test
    void createNameThrowErrorWhenNameIsNull() {
        // when & then
        assertThatThrownBy(() -> Name.from(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선_또는_지하철명은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("이름 생성 시 비어있이면 에러가 발생한다.")
    @Test
    void createNameThrowErrorWhenNameIsEmpty() {
        // when & then
        assertThatThrownBy(() -> Name.from(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.노선_또는_지하철명은_비어있을_수_없음.getErrorMessage());
    }

    @DisplayName("이름을 생성하면 이름을 조회할 수 있다.")
    @Test
    void createName() {
        // given
        String actual = "2호선";

        // when
        Name name = Name.from(actual);

        // then
        assertThat(name.value()).isEqualTo(actual);
    }

    @DisplayName("이름이 동일하면 동일한 객체이다.")
    @Test
    void equalName() {
        // given
        String name = "강남역";
        String duplicateName = "강남역";

        // when & then
        assertThat(Name.from(name)).isEqualTo(Name.from(duplicateName));
    }
}
