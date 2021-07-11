package nextstep.subway.favorite.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.junit.jupiter.api.Assertions.*;

class FavoriteTest {

    @DisplayName("즐겨찾기에 저장된 사용자와 요청한 사용자가 다른 경우 예외처리한다.")
    @Test
    void validateMember_test() {
        //given
        Favorite 즐겨찾기 = new Favorite(new Station("강남역"), new Station("서초역"), new Member(EMAIL, PASSWORD, 28));
        Member 다른_사용자 = new Member("another@github.com", PASSWORD, 28);

        //when
        assertThrows(AuthorizationException.class,
                () -> 즐겨찾기.validateMember(다른_사용자)
        );
    }
}
