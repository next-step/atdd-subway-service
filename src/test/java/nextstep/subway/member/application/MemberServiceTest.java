package nextstep.subway.member.application;

import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@DisplayName("MemberService 테스트")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private static final long MEMBER_ID = 1L;
    private static final String EMAIL = "testuser@test.com";
    private static final String PASSWORD = "password157#";
    private static final int AGE = 20;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @Test
    void 사용자_생성() {
        Member member = new Member(EMAIL, PASSWORD, AGE);
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);

        when(memberRepository.save(memberRequest.toMember())).thenReturn(member);

        MemberResponse response = memberService.createMember(memberRequest);

        assertAll(
                () -> assertEquals(EMAIL, response.getEmail()),
                () -> assertEquals(AGE, response.getAge())
        );
    }

    @Test
    void 사용자_수정() {
        Member member = new Member(EMAIL, PASSWORD, AGE);
        MemberRequest memberRequest = new MemberRequest("testuser-new@test.com", PASSWORD, AGE);

        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(member));

        memberService.updateMember(MEMBER_ID, memberRequest);

        assertEquals("testuser-new@test.com", member.getEmail());
    }
}
