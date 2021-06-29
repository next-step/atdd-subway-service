package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class FavoriteServiceTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteService favoriteService;

    private static final String EMAIL = "bbbnam@naver.com";
    private static final String PASSWORD = "12345";
    private static final int AGE = 24;

    private Member 가입된_회원;
    private LoginMember 로그인된_회원;
    private Station 강남역;
    private Station 역삼역;
    private Station 교대역;

    @BeforeEach
    void setUp() {
        강남역 = stationRepository.save(new Station("강남역"));
        역삼역 = stationRepository.save(new Station("역삼역"));
        교대역 = stationRepository.save(new Station("교대역"));
        가입된_회원 = memberRepository.save(new Member(EMAIL, PASSWORD, AGE));
        로그인된_회원 = new LoginMember(가입된_회원.getId(), 가입된_회원.getEmail(), 가입된_회원.getAge());
    }

    @DisplayName("즐겨찾기를 저장한다.")
    @Test
    void saveFavorites() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 교대역.getId());

        FavoriteResponse favoriteResponse = favoriteService.saveFavorites(로그인된_회원, favoriteRequest);

        assertAll(
                () -> assertThat(favoriteResponse).isNotNull(),
                () -> assertThat(favoriteResponse.getSource()).isEqualTo(강남역));
    }

    @DisplayName("즐겨찾기 목록을 조회한다.")
    @Test
    void findFavorites() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 교대역.getId());
        favoriteService.saveFavorites(로그인된_회원, favoriteRequest);

        List<Favorite> favorites = favoriteService.findFavorites(로그인된_회원);

        assertThat(favorites).isNotEmpty();
        assertThat(favorites.get(0).getSource()).isEqualTo(강남역);
    }

    @DisplayName("즐겨찾기를 제거한다.")
    @Test
    void deleteFavorite() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 교대역.getId());
        FavoriteResponse favoriteResponse = favoriteService.saveFavorites(로그인된_회원, favoriteRequest);

        favoriteService.deleteFavorite(favoriteResponse.getId());

        assertThat(favoriteService.findFavorites(로그인된_회원)).isEmpty();
    }
}
