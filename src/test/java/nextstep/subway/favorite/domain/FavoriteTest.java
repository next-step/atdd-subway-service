package nextstep.subway.favorite.domain;

import static nextstep.subway.favorite.domain.FavoriteTestFixture.favorite;
import static nextstep.subway.station.domain.StationTestFixture.station;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nextstep.subway.common.exception.InvalidParameterException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {
    @Test
    @DisplayName("즐겨찾기 객체 생성")
    void createFavorite() {
        // when
        Favorite actual = favorite(1L, station("강남역"), station("잠실역"));

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).isInstanceOf(Favorite.class)
        );
    }

    @Test
    @DisplayName("즐겨찾기 생성시 회원정보 필수값 확인")
    void createFavoriteByNullMember() {
        // when & then
        assertThatThrownBy(() -> favorite(0, station("강남역"), station("잠실역")))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("회원정보를 확인해주세요.");
    }

    @Test
    @DisplayName("즐겨찾기 생성시 출발역 필수값 확인")
    void createFavoriteByNullDepartureStation() {
        // when & then
        assertThatThrownBy(() -> favorite(1L, null, station("잠실역")))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역을 확인해주세요.");
    }

    @Test
    @DisplayName("즐겨찾기 생성시 도착역 필수값 확인")
    void createFavoriteByNullArrivalStation() {
        // when & then
        assertThatThrownBy(() -> favorite(1L, station("강남역"), null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("도착역을 확인해주세요.");
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 즐겨찾기를 생성할 수 없다.")
    void createFavoriteBySameStations() {
        // when & then
        assertThatThrownBy(() -> favorite(1L, station("강남역"), station("강남역")))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }
}
