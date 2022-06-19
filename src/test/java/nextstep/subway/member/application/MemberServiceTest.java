package nextstep.subway.member.application;

import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.infrastructure.InMemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class MemberServiceTest {
    private MemberService memberService;
    private MemberRequest memberRequest;
    private MemberRequest updateRequest;

    @BeforeEach
    void setUp() {
        MemberRepository memberRepository = new InMemoryMemberRepository();
        memberService = new MemberService(memberRepository);

        memberRequest = new MemberRequest(EMAIL, PASSWORD, AGE);
        updateRequest = new MemberRequest(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
    }

    @Test
    void 멤버를_생성한다() {
        // when
        MemberResponse result = memberService.createMember(memberRequest);

        // then
        assertAll(
                () -> assertThat(result.getId()).isNotNull(),
                () -> assertThat(result.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(result.getAge()).isEqualTo(AGE)
        );
    }

    @Test
    void 멤버를_조회한다() {
        // when
        MemberResponse result = memberService.findMember(1L);

        // then
        assertThat(result).isNotNull();
    }

    @Test
    void 멤버를_수정한다() {
        // when
        memberService.updateMember(1L, updateRequest);
        MemberResponse result = memberService.findMember(1L);

        // then
        assertAll(
                () -> assertThat(result.getEmail()).isEqualTo(NEW_EMAIL),
                () -> assertThat(result.getAge()).isEqualTo(NEW_AGE)
        );
    }

    @Test
    void 멤버를_삭제한다() {
        // when
        memberService.deleteMember(1L);

        // then
        assertThatThrownBy(() ->
                memberService.findMember(1L)
        ).isInstanceOf(NoSuchElementException.class);
    }
}
