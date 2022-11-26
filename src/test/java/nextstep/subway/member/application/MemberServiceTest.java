package nextstep.subway.member.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.common.constant.ErrorCode;
import nextstep.subway.member.domain.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("회원 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("존재하지 않는 회원을 조회할 수 없다.")
    @Test
    void findMemberThrowErrorWhenMemberIsNotExists() {
        // given
        when(memberRepository.findById(1L))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.findMember(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.존재하지_않는_회원.getErrorMessage());
    }
}
