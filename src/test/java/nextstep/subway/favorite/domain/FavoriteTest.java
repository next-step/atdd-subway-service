package nextstep.subway.favorite.domain;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

public class FavoriteTest {
    private Station 강남역;
    private Station 정자역;
    private Member 콜펌;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        정자역 = Station.of(2L, "정자역");

        콜펌 = new Member("test@gamil.com", "password", 30);
    }

    @Test
    @DisplayName("출발역과 도착역이 같으면 즐겨찾기 추가에 실패한다.")
    void validateStations() {
        assertThatThrownBy(() -> Favorite.of(콜펌, 정자역, 정자역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 역을 즐겨찾기에 추가할 수 없습니다.");
    }

    @Test
    @DisplayName("즐겨찾기를 등록했던 사용자가 아니면 등록에 실패한다.")
    void validateMember() {
        Favorite 즐겨찾기 = Favorite.of(콜펌, 강남역, 정자역);
        Member 필필 = new Member("test2@gamil.com", "password2", 30);

        assertThatThrownBy(() -> 즐겨찾기.validateMember(필필))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("즐겨찾기를 등록한 사용자가 아닙니다.");
    }

    @Test
    @DisplayName("즐겨찾기를 생성한다.")
    void createFavorite() {
        Favorite 즐겨찾기 = Favorite.of(콜펌, 강남역, 정자역);

        assertAll(
            () ->  assertThat(즐겨찾기.getSource().getName()).isEqualTo("강남역"),
            () ->  assertThat(즐겨찾기.getTarget().getName()).isEqualTo("정자역"),
            () ->  assertThat(즐겨찾기.getMember()).isEqualTo(콜펌)
        );
    }
}
