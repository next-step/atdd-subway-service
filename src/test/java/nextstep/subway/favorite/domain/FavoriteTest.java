package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import nextstep.subway.common.exception.InvalidParameterException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {
    private Member member;
    private Station departureStation;
    private Station arrivalStation;

    @BeforeEach
    void setUp() {
        member = Member.of("email@email.com", "password", 20);
        departureStation = Station.from("강남역");
        arrivalStation = Station.from("잠실역");
    }

    @Test
    @DisplayName("즐겨찾기 객체 생성")
    void createFavorite() {
        // when
        Favorite actual = Favorite.of(member, departureStation, arrivalStation);

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
        assertThatThrownBy(() -> Favorite.of(null, departureStation, arrivalStation))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("회원정보를 확인해주세요.");
    }

    @Test
    @DisplayName("즐겨찾기 생성시 출발역 필수값 확인")
    void createFavoriteByNullDepartureStation() {
        // when & then
        assertThatThrownBy(() -> Favorite.of(member, null, arrivalStation))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역을 확인해주세요.");
    }

    @Test
    @DisplayName("즐겨찾기 생성시 도착역 필수값 확인")
    void createFavoriteByNullArrivalStation() {
        // when & then
        assertThatThrownBy(() -> Favorite.of(member, departureStation, null))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("도착역을 확인해주세요.");
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 즐겨찾기를 생성할 수 없다.")
    void createFavoriteBySameStations() {
        // when & then
        assertThatThrownBy(() -> Favorite.of(member, departureStation, departureStation))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역과 도착역이 같을 수 없습니다.");
    }
}
