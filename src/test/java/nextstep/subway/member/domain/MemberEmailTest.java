package nextstep.subway.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 이메일 도메인 관련")
class MemberEmailTest {
    private MemberEmail memberEmail;

    @BeforeEach
    void setUp() {
        memberEmail = new MemberEmail("test@domain.com");
    }

    @DisplayName("회원 이메일을 생성할 수 있다")
    @Test
    void createMemberEmail() {
        assertThat(memberEmail.getEmail()).isEqualTo("test@domain.com");
    }
}