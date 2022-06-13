package nextstep.subway.favorite.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import nextstep.subway.DataJpaTestWithDatabaseCleanup;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class FavoriteRepositoryTest extends DataJpaTestWithDatabaseCleanup {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    private Station 신촌역;
    private Station 홍대입구역;
    private LoginMember 사용자;

    @BeforeEach
    void setUp() {
        신촌역 = 지하철역_저장되어_있음("신촌역");
        홍대입구역 = 지하철역_저장되어_있음("홍대입구역");
        사용자 = new LoginMember(1L, "email@email.com", 40);
    }

    @Test
    void 즐겨찾기_저장() {
        Favorite favorite = 즐겨찾기_저장되어_있음(사용자.getId(), 신촌역, 홍대입구역);
        Optional<Favorite> savedFavorite = favoriteRepository.findById(favorite.getId());

        assertThat(savedFavorite).isPresent().containsSame(favorite);
    }

    @Test
    void 즐겨찾기_목록조회() {
        Favorite favorite1 = 즐겨찾기_저장되어_있음(사용자.getId(), 신촌역, 홍대입구역);
        Favorite favorite2 = 즐겨찾기_저장되어_있음(사용자.getId(), 홍대입구역, 신촌역);
        List<Favorite> favoriteList = favoriteRepository.findByMemberId(사용자.getId());
        assertThat(favoriteList).hasSize(2).containsExactlyInAnyOrder(favorite1, favorite2);
    }

    @Test
    void 즐겨찾기_삭제() {
        Favorite favorite = 즐겨찾기_저장되어_있음(사용자.getId(), 신촌역, 홍대입구역);
        favoriteRepository.delete(favorite);
        Optional<Favorite> deletedFavorite = favoriteRepository.findById(favorite.getId());
        assertThat(deletedFavorite).isEmpty();
    }

    private Station 지하철역_저장되어_있음(String stationName) {
        return stationRepository.save(new Station(stationName));
    }

    private Favorite 즐겨찾기_저장되어_있음(Long memberId, Station source, Station target) {
        return favoriteRepository.save(new Favorite(memberId, source, target));
    }

}
