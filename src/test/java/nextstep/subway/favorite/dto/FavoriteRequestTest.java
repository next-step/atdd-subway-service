package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FavoriteRequestTest {

    @Test
    void 회원_source_지하철역_target_지하철역_을이용하여_favorite객체_생성() {
        Member member = new Member(1L, "mekwon@test.com", "password", 37);
        Station 강남역 = new Station(1L, "강남역");
        Station 역삼역 = new Station(3L, "역삼역");

        Favorite favorite = FavoriteRequest.toFavorite(member, 강남역, 역삼역);
        assertThat(favorite.getMember().getId()).isEqualTo(member.getId());
        assertThat(favorite.getSource().getId()).isEqualTo(강남역.getId());
        assertThat(favorite.getTarget().getId()).isEqualTo(역삼역.getId());
    }
}
