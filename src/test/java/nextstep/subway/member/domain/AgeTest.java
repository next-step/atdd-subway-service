package nextstep.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class AgeTest {

    @DisplayName("나이는 null이 될 수 없다.")
    @Test
    void 나이는_null이_될_수_없다() {
        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> Age.from(null));
    }

    @DisplayName("나이가 0보다 작거나 같으면 에러가 발생한다")
    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    void 나이가_0보다_작거나_같으면_에러가_발생한다(int age) {
        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> Age.from(age));
    }

    @DisplayName("나이 생성 성공")
    @Test
    void 나이_생성_성공() {
        // given
        Age age = Age.from(30);

        // when, then
        assertThat(age.value()).isEqualTo(30);
    }
}
