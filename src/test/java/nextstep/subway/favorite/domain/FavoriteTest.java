package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {
    private Favorite favorite;
    private Member loginMember;
    private Member member;

    @BeforeEach
    void setUp() {
        Station source = new Station("강남역");
        Station target = new Station("강남역");

        loginMember = new Member(NEW_EMAIL, NEW_PASSWORD, NEW_AGE);
        ReflectionTestUtils.setField(loginMember, "id", 1L);

        member = new Member(EMAIL, PASSWORD, AGE);
        ReflectionTestUtils.setField(member, "id", 2L);

        favorite = new Favorite(loginMember, source, target);
    }

    @Test
    void 내가_등록한_즐겨찾기가_아니면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() ->
                favorite.validateOwner(member)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("내 즐겨찾기가 아닙니다.");
    }
}
