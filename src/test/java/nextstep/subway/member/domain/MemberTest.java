package nextstep.subway.member.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void 동일한_아이디_여부_확인() {
        Member member = new Member(1L);
        Assertions.assertThat(member.hasId(2L)).isFalse();
    }
}