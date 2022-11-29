package nextstep.subway.favorite.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.favorite.exception.FavoriteExceptionCode;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.exception.MemberExceptionCode;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("즐겨찾기 클래스 테스트")
class FavoriteTest {

    private Member member;
    private Station 강남역;
    private Station 잠실역;

    @BeforeEach
    void setUp() {
        member = new Member("testuser@test.com", "password157#", 20);
        강남역 = new Station("강남역");
        잠실역 = new Station("잠실역");
    }

    @Test
    void 동등성_테스트() {
        assertEquals(new Favorite(member, 강남역, 잠실역), new Favorite(member, 강남역, 잠실역));
    }

    @Test
    void Favorite_객체_생성시_member가_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            new Favorite(null, 강남역, 잠실역);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FavoriteExceptionCode.REQUIRED_MEMBER.getMessage());
    }

    @Test
    void Favorite_객체_생성시_source가_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            new Favorite(member, null, 잠실역);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FavoriteExceptionCode.REQUIRED_SOURCE.getMessage());
    }

    @Test
    void Favorite_객체_생성시_target이_null이면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            new Favorite(member, 강남역, null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FavoriteExceptionCode.REQUIRED_TARGET.getMessage());
    }

    @Test
    void Favorite_객체_생성시_source와_target이_같으면_IllegalArgumentException_발생() {
        assertThatThrownBy(() -> {
            new Favorite(member, 강남역, 강남역);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(FavoriteExceptionCode.CANNOT_EQUALS_SOURCE_TARGET.getMessage());
    }

    @Test
    void 로그인한_member와_Favorite을_등록한_member가_다르면_AuthorizedException_발생() {
        Favorite favorite = new Favorite(member, 강남역, 잠실역);

        assertThatThrownBy(() -> {
            favorite.checkLoginMember("loginuser@test.com");
        }).isInstanceOf(AuthorizationException.class)
                .hasMessage(MemberExceptionCode.EMAIL_NOT_MATCH.getMessage());
    }
}
