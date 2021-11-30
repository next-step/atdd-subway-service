package nextstep.subway.favorite.domain;

import nextstep.subway.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


public class FavoriteTest {

    private final Station 강남역 = new Station("강남역");
    private final Station 역삼역 = new Station("역삼역");
    private final Member 최웅석 = new Member("email", "1234", 10);

    @Test
    public void 즐겨찾기_생성() {
        Favorite actual = new Favorite(최웅석, 강남역, 역삼역);

        assertThat(actual).isNotNull();
    }

    @Test
    public void 즐겨찾기_출발역_없음_오류() {
        assertThatThrownBy(() -> {
            Favorite actual = new Favorite(최웅석, null, 역삼역);
        }).isInstanceOf(FavoriteException.class);
    }

    @Test
    public void 즐겨찾기_도착역_없음_오류() {
        assertThatThrownBy(() -> {
            Favorite actual = new Favorite(최웅석, 강남역, null);
        }).isInstanceOf(FavoriteException.class);
    }

    @Test
    public void 즐겨찾기_유저_없음_오류() {
        assertThatThrownBy(() -> {
            Favorite actual = new Favorite(null, 강남역, 역삼역);
        }).isInstanceOf(FavoriteException.class);
    }

}