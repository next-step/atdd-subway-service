package nextstep.subway.member.domain;

import nextstep.subway.member.exception.MemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MemberTest {
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.of("lcjltj@gmail.com", "900821", 33);
    }

    @Test
    @DisplayName("멤버 생성 테스트")
    public void createMamber() {
        // then
        assertThat(member).isInstanceOf(Member.class);

    }

    @Test
    @DisplayName("멤버 정보 업데이트")
    void update() {
        // given
        Member updateMember = Member.of("lcjltj@gmail.com", "900821", 30);
        // when
        member.update(updateMember);
        // then
        assertThat(member.getAge()).isEqualTo(30);
    }

    @Test
    @DisplayName("비밀번호 오류")
    void checkPassword() {
        // then
        assertThatThrownBy(()-> member.checkPassword("21"))
                .isInstanceOf(MemberException.class);
    }
}
