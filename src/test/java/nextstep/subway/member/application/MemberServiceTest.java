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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    private String 신규_회원_이메일;
    private String 신규_회원_패스워드;
    private int 신규_회원_나이;
    private String 신규_회원_변경_이메일;
    private String 신규_회원_변경_패스워드;
    private int 신규_회원_변경_나이;
    private Member 신규_회원;

    @BeforeEach
    void setUp() {
        신규_회원_이메일 = "test@test.com";
        신규_회원_패스워드 = "test";
        신규_회원_나이 = 31;
        신규_회원_변경_이메일 = "testNew@test.com";
        신규_회원_변경_패스워드 = "testNew";
        신규_회원_변경_나이 = 32;
        신규_회원 = new Member(신규_회원_이메일, 신규_회원_패스워드, 신규_회원_나이);
    }

    @DisplayName("신규 회원 등록 정보가 주어지면 신규 회원 등록 후 회원 정보를 반환한다")
    @Test
    void create_member() {
        // given
        given(memberRepository.save(any())).willReturn(신규_회원);
        MemberRequest memberRequest = new MemberRequest(신규_회원_이메일, 신규_회원_패스워드, 신규_회원_나이);

        // when
        MemberResponse member = memberService.createMember(memberRequest);

        // then
        회원_등록됨(member);
    }

    @DisplayName("회원 ID가 주어지면 해당 ID로 조회 후 회원 정보를 반환한다")
    @Test
    void find_member() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.of(신규_회원));

        // when
        MemberResponse member = memberService.findMember(1L);

        // then
        회원_조회됨(member);
    }

    @DisplayName("회원 ID와 수정 정보가 주어지면 회원 정보를 수정한다")
    @Test
    void update_member() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.of(신규_회원));
        MemberRequest memberRequest = new MemberRequest(신규_회원_변경_이메일, 신규_회원_변경_패스워드, 신규_회원_변경_나이);

        // when
        memberService.updateMember(1L, memberRequest);

        // then
        회원_수정됨();
    }

    @DisplayName("회원 ID가 주어지면 회원을 삭제한다")
    @Test
    void delete_member() {
        // given
        willDoNothing().given(memberRepository).deleteById(any());

        // when
        memberService.deleteMember(1L);

        // then
        회원_삭제됨();
    }

    private void 회원_등록됨(MemberResponse member) {
        then(memberRepository).should(times(1)).save(any());
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(신규_회원_이메일),
                () -> assertThat(member.getAge()).isEqualTo(신규_회원_나이)
        );
    }

    private void 회원_조회됨(MemberResponse member) {
        then(memberRepository).should(times(1)).findById(any());
        assertAll(
                () -> assertThat(member.getEmail()).isEqualTo(신규_회원_이메일),
                () -> assertThat(member.getAge()).isEqualTo(신규_회원_나이)
        );
    }

    private void 회원_수정됨() {
        then(memberRepository).should(times(1)).findById(any());
        assertAll(
                () -> assertThat(신규_회원.getEmail()).isEqualTo(신규_회원_변경_이메일),
                () -> assertThat(신규_회원.getAge()).isEqualTo(신규_회원_변경_나이)
        );
    }

    private void 회원_삭제됨() {
        then(memberRepository).should(times(1)).deleteById(any());
    }
}
