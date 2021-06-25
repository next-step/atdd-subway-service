package nextstep.subway.favorite.domain;

import static nextstep.subway.station.domain.StationFixtures.광화문역;
import static nextstep.subway.station.domain.StationFixtures.서대문역;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("즐겨찾기 테스트")
public class FavoriteTest {

    @DisplayName("자신의 즐겨찾기 여부 확인")
    @Test
    void delete() {
        Member member = new Member(1L, "jhh992000@gmail.com", "1234", 39);
        Favorite favorite = new Favorite(1L, member, 서대문역, 광화문역);

        assertThat(favorite.isOwner(2L)).isFalse();
    }

}
