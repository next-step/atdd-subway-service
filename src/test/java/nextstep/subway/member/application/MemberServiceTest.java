package nextstep.subway.member.application;


import static nextstep.subway.member.step.MemberFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.infrastructure.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("MemberServiceTest 단위 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        member = 회원생성();
    }

    @Test
    @DisplayName("회원 정보 업데이트 됨")
    void updateMember() {
        // when
        Member updateMember = new Member(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        member.update(updateMember);

        // then
        assertThat(member.getEmail()).isEqualTo(NEW_EMAIL);
        assertThat(member.getAge()).isEqualTo(NEW_AGE);
    }
    

}
