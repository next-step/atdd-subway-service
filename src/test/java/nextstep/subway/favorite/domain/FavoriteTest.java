package nextstep.subway.favorite.domain;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Section;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FavoriteTest {

    private Station 강남역;
    private Station 건대역;
    private Station 사당역;
    private Line 이호선;
    private Member 사용자;

    @BeforeEach
    void setUp() {
        강남역 = new Station(1L, "강남역");
        건대역 = new Station(2L, "건대역");
        사당역 = new Station(3L, "사당역");
        이호선 = new Line("2호선", "bg-red-600", 강남역, 건대역, 10);
        이호선.addSection(new Section(이호선, 강남역, 사당역, 3));
        사용자 = new Member(1L, "test@email.com", "password", 20);
    }

    @DisplayName("즐겨찾기 객체 생성 가즈아ㅏ")
    @Test
    void create() {
        Favorite favorite = new Favorite(사용자,강남역,건대역);

        assertThat(favorite).isNotNull();
    }
}
