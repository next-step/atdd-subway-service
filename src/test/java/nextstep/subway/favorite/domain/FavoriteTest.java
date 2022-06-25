package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FavoriteTest {
    private Station source;
    private Station target;
    private Favorite favorite;
    private Member loginMember;
    private Member member;

    @BeforeEach
    void setUp() {
        source = new Station("강남역");
        target = new Station("양재역");

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
    
    @Test
    void 출발역과_도착역이_같으면_즐겨찾기에_등록할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                new Favorite(member, source, source)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }
}
