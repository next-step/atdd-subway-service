package nextstep.subway.favorite.domain;

import nextstep.subway.common.exception.InvalidDataException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static nextstep.subway.station.StationFixture.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FavoriteTest {

    @DisplayName("출발역과 도착역이 같은 즐겨찾기를 등록한다.")
    @Test
    void 출발역과_도착역이_같은_즐겨찾기_생성_테스트() {
        assertThatThrownBy(
                () -> new Favorite(서울역().getId(), 서울역().getId())
        ).isInstanceOf(InvalidDataException.class);
    }

    @DisplayName("기존에 등록된 즐겨찾기를 등록한다.")
    @Test
    void 기존에_등록된_즐겨찾기_등록_테스트() {
        //given
        List<Favorite> favorites = new ArrayList<>();
        favorites.add(new Favorite(서울역().getId(), 시청역().getId()));

        Favorite favorite = new Favorite(서울역().getId(), 시청역().getId());
        assertThatThrownBy(
                () -> favorite.validateDuplicate(favorites)
        ).isInstanceOf(InvalidDataException.class);
    }

}
