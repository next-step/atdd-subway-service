package nextstep.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class EmailTest {

    @DisplayName("이메일은 null이 될 수 없다.")
    @Test
    void 이메일은_null이_될_수_없다() {
        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> Email.from(null));
    }

    @DisplayName("이메일은 빈 값이 될 수 없다.")
    @Test
    void 이메일은_빈_값이_될_수_없다() {
        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> Email.from(""));
    }

    @DisplayName("이메일 생성 성공")
    @Test
    void 이메일_생성_성공() {
        // given
        Email email = Email.from("email@email.com");

        // when, then
        assertThat(email.value()).isEqualTo("email@email.com");
    }
}
