package nextstep.subway.member.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    @DisplayName("id가 일치하는지 확인")
    @Test
    void matchId_true() {
        Member member = new Member(1L, "email", "password", 20);

        assertThat(member.matchId(1L)).isTrue();
    }

    @DisplayName("id가 불일치하는지 확인")
    @Test
    void matchId_false() {
        Member member = new Member(1L, "email", "password", 20);

        assertThat(member.matchId(2L)).isFalse();
    }
}