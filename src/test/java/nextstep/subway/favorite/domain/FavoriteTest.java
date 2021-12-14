package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;

class FavoriteTest {
    private Station 강남역;
    private Station 양재역;
    private Member member;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        member = new Member(1L, "email@email.com", "password", 20);
    }

    @DisplayName("id가 동일하지 않으면 에러 발생")
    @Test
    void checkSameMember_error() {
        Favorite favorite = new Favorite(강남역, 양재역, member);

        assertThatExceptionOfType(AuthorizationException.class)
            .isThrownBy(() -> favorite.checkSameMember(2L));
    }
}