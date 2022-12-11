package nextstep.subway.favorite.domain;

import org.springframework.test.util.ReflectionTestUtils;

import static nextstep.subway.station.StationFixture.*;

public class FavoriteFixture {

    public static Favorite 즐겨찾기1() {
        Favorite favorite = new Favorite(서울역().getId(), 시청역().getId());
        ReflectionTestUtils.setField(favorite, "id", 1L);
        return favorite;
    }

    public static Favorite 즐겨찾기2() {
        Favorite favorite = new Favorite(서울역().getId(), 남영역().getId());
        ReflectionTestUtils.setField(favorite, "id", 2L);
        return favorite;
    }
}
