package nextstep.subway.favorite.dto;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.Test;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static nextstep.subway.station.domain.StationTest.역삼역;
import static nextstep.subway.station.domain.StationTest.양재역;
import static org.assertj.core.api.Assertions.assertThat;

class FavoriteResponseTest {

    @Test
    void favoriteToFavoriteResponse() {
        // given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Favorite favorite = new Favorite(member, 역삼역, 양재역);

        // when
        FavoriteResponse actual = FavoriteResponse.of(favorite);

        // then
        assertThat(actual.getSource()).isEqualTo(StationResponse.of(역삼역));
        assertThat(actual.getTarget()).isEqualTo(StationResponse.of(양재역));
    }
}