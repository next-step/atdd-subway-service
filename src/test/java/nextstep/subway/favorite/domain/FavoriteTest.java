package nextstep.subway.favorite.domain;

import static nextstep.subway.station.StationAcceptanceTest.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;

import nextstep.subway.member.domain.Member;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FavoriteTest {
    private Member mond;
    private Station source, target;

    @BeforeEach
    void setUp() {
        mond = new Member("mond@mond.com", "mond", 10);
        source = 지하철역_생성("강남역");
        target = 지하철역_생성("교대역");
    }

    @Test
    @DisplayName("객체가 같은지 검증")
    void verifySameFavorite() {
        Favorite favorite = new Favorite(mond, source, target);

        assertThat(favorite).isEqualTo(new Favorite(mond, source, target));
    }
}
