package nextstep.subway.member.domain;

import nextstep.subway.auth.application.AuthorizationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberTest {
    private Member member;
    private Member updatedMember;

    @BeforeEach
    void setUp() {
        member = new Member(EMAIL, PASSWORD, AGE);
        updatedMember = new Member(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
    }

    @Test
    void 회원_정보를_변경한다() {
        // when
        member.update(updatedMember);

        // then
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(NEW_EMAIL),
                () -> assertThat(member.getPassword()).isEqualTo(NEW_PASSWORD),
                () -> assertThat(member.getAge()).isEqualTo(NEW_AGE)
        );
    }

    @Test
    void 패스워드가_일치하지_않으면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() ->
                member.checkPassword(NEW_PASSWORD)
        ).isInstanceOf(AuthorizationException.class);
    }
}
