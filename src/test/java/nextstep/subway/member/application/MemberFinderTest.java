package nextstep.subway.member.application;

import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.infrastructure.InMemoryMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MemberFinderTest {
    private MemberFinder memberFinder;

    @BeforeEach
    void setUp() {
        MemberRepository memberRepository = new InMemoryMemberRepository();
        memberFinder = new MemberFinder(memberRepository);
    }

    @Test
    void 멤버를_조회한다() {
        // when
        MemberResponse result = memberFinder.findMember(1L);

        // then
        assertThat(result).isNotNull();
    }
}
