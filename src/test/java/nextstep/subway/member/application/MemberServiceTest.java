package nextstep.subway.member.application;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

@DisplayName("사용자 노선 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private MemberService memberService;

    private Member 사용자;

    @BeforeEach
    void setUp() {
        사용자 = new Member("email@email.com", "password", 20);
    }

    @DisplayName("회원 저장에 성공한다.")
    @Test
    void 회원_저장에_성공한다() {
        // given
        when(memberRepository.save(사용자)).thenReturn(사용자);

        // when
        MemberResponse 조회된_사용자 = memberService.createMember(new MemberRequest("email@email.com", "password", 20));

        // then
        assertThat(조회된_사용자.getEmail()).isEqualTo("email@email.com");
        assertThat(조회된_사용자.getAge()).isEqualTo(20);
    }

    @DisplayName("존재하지 않는 회원을 조회할 수 없다.")
    @Test
    void 존재하지_않는_회원을_조회할_수_없다() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> memberService.findMember(1L));
    }

    @DisplayName("회원 조회에 성공한다.")
    @Test
    void 회원_조회에_성공한다() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(사용자));

        // when
        MemberResponse 조회된_사용자 = memberService.findMember(1L);

        // then
        assertThat(조회된_사용자.getEmail()).isEqualTo("email@email.com");
        assertThat(조회된_사용자.getAge()).isEqualTo(20);
    }

    @DisplayName("회원 정보 수정에 성공한다.")
    @Test
    void 회원_정보_수정에_성공한다() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(사용자));

        // when
        memberService.updateMember(1L, new MemberRequest("update@email.com", "newPassword", 25));

        // then
        assertThat(사용자.getEmail()).isEqualTo("update@email.com");
        assertThat(사용자.getAge()).isEqualTo(25);
    }

    @DisplayName("회원 정보 조회에 성공한다.")
    @Test
    void 회원_정보_조회에_성공한다() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(사용자));

        // when
        Member 조회된_사용자 = memberService.findMemberById(1L);

        // then
        assertThat(조회된_사용자.getEmail()).isEqualTo("email@email.com");
        assertThat(조회된_사용자.getAge()).isEqualTo(20);
    }
}
