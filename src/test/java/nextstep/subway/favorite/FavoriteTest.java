package nextstep.subway.favorite;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
}
