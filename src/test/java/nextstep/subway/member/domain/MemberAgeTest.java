package nextstep.subway.member.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("회원 나이 도메인 관련")
class MemberAgeTest {
    private MemberAge memberAge;

    @BeforeEach
    void setUp() {
        memberAge = new MemberAge(4);
    }

    @DisplayName("멤버 나이를 생성할 수 있다")
    @Test
    void createMemberAge() {
        assertThat(memberAge.getAge()).isEqualTo(4);
    }
}