package nextstep.subway.favorite.application;

import java.util.List;
import java.util.Optional;
import nextstep.subway.DataJpaTestWithDatabaseCleanup;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;

class FavoriteServiceTest extends DataJpaTestWithDatabaseCleanup {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    private FavoriteService favoriteService;

    private Station 신촌역;
    private Station 홍대입구역;
    private LoginMember 사용자;

    @BeforeEach
    void setUp(){
        StationService stationService = new StationService(stationRepository);
        favoriteService = new FavoriteService(favoriteRepository,stationService);

        신촌역 = 지하철역_저장되어_있음("신촌역");
        홍대입구역 = 지하철역_저장되어_있음("홍대입구역");
        사용자 = new LoginMember(1L, "email@email.com",40);
    }

    @Test
    void 즐겨찾기_저장(){
        FavoriteCreateRequest request = new FavoriteCreateRequest(String.valueOf(신촌역.getId()),String.valueOf(홍대입구역.getId()));
        favoriteService.saveFavorite(사용자.getId(),request);

        List<Favorite> favoriteList = favoriteRepository.findByMemberId(사용자.getId());
        assertThat(favoriteList).hasSize(1);
        Favorite favorite = favoriteList.get(0);
        assertThat(favorite.getSource()).isSameAs(신촌역);
        assertThat(favorite.getTarget()).isSameAs(홍대입구역);
    }

    @Test
    void 즐겨찾기_목록조회(){
        즐겨찾기_저장되어_있음(사용자.getId(),신촌역,홍대입구역);
        즐겨찾기_저장되어_있음(사용자.getId(),홍대입구역,신촌역);
        List<FavoriteResponse> favoriteList = favoriteService.findFavorites(사용자.getId());
        assertThat(favoriteList).hasSize(2);
    }

    @Test
    void 즐겨찾기_삭제(){
        Favorite favorite = 즐겨찾기_저장되어_있음(사용자.getId(),신촌역,홍대입구역);
        favoriteService.deleteFavorite(favorite.getId());
        Optional<Favorite> deletedFavorite = favoriteRepository.findById(favorite.getId());
        assertThat(deletedFavorite.isPresent()).isFalse();
    }

    private Station 지하철역_저장되어_있음(String stationName){
        return stationRepository.save(new Station(stationName));
    }

    private Favorite 즐겨찾기_저장되어_있음(Long memberId, Station source, Station target){
        return favoriteRepository.save(new Favorite(memberId,source,target));
    }

}
