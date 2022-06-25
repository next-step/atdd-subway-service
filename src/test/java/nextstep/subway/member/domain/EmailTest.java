package nextstep.subway.member.domain;

import nextstep.subway.member.exception.MemberException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class EmailTest {
    @Test
    @DisplayName("이메일 생성")
    void createEmail() {
        // when
        final Email email = Email.of("lcjltj@gmail.com");
        // then
        assertThat(email).isEqualTo(Email.of("lcjltj@gmail.com"));
    }

    @Test
    @DisplayName("이메일 생성 정규식 오류")
    void regexEmailException() {
        //then
        assertThatThrownBy(() -> Email.of("lcjltj"))
                .isInstanceOf(MemberException.class);
    }
}
