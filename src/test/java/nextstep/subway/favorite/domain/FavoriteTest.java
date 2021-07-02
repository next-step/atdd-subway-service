package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteTest {
    @Test
    @DisplayName("즐겨찾기 소유유저인지 확인")
    void isOwner() {
        Member member = new Member(1L, "email", "password", 99);
        Station sourceStation = new Station("1번역");
        Station targetStation = new Station("2번역");
        Favorite favorite = new Favorite(member, sourceStation, targetStation);
        assertThat(favorite.isOwner(1L)).isTrue();
    }
}