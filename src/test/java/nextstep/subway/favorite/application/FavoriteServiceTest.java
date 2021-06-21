package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.utils.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FavoriteServiceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    FavoriteService favoriteService;

    @Autowired
    StationRepository stationRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member 사용자;

    private Station 용산역;
    private Station 도심역;

    @BeforeEach
    void setUp() {
        databaseCleanup.execute();

        Member member = new Member("test@email.com", "password", 20);
        사용자 = memberRepository.save(member);

        용산역 = new Station("용산역");
        도심역 = new Station("도심역");
        stationRepository.save(용산역);
        stationRepository.save(도심역);
    }


    @DisplayName("즐겨찾기를 등록하기")
    @Test
    void createFavorite() {
        LoginMember 로그인_사용자 = new LoginMember(사용자.getId(), 사용자.getEmail(), 사용자.getAge());
        FavoriteRequest 즐겨찾기 = new FavoriteRequest(용산역.getId(), 도심역.getId());

        Favorite favorite = favoriteService.createFavorite(로그인_사용자, 즐겨찾기);

        assertThat(favorite.getId()).isNotNull();
        assertThat(favorite.getMember().getId()).isEqualTo(사용자.getId());
    }

    @DisplayName("등록된 즐겨찾기 목록을 확인해보기")
    @Test
    void findFavorite() {
        LoginMember 로그인_사용자 = new LoginMember(사용자.getId(), 사용자.getEmail(), 사용자.getAge());
        FavoriteRequest 즐겨찾기 = new FavoriteRequest(용산역.getId(), 도심역.getId());
        Favorite favorite = favoriteService.createFavorite(로그인_사용자, 즐겨찾기);

        List<FavoriteResponse> favorites = favoriteService.findFavorites(로그인_사용자);

        List<Long> favoriteIds = favorites.stream()
                .map(favoriteResponse -> favoriteResponse.getId())
                .collect(Collectors.toList());

        assertThat(favoriteIds).contains(favorite.getId());
    }

    @DisplayName("등록된 즐겨찾기를 삭제하기")
    @Test
    void deleteFavorite() {
        LoginMember 로그인_사용자 = new LoginMember(사용자.getId(), 사용자.getEmail(), 사용자.getAge());
        FavoriteRequest 즐겨찾기 = new FavoriteRequest(용산역.getId(), 도심역.getId());
        Favorite favorite = favoriteService.createFavorite(로그인_사용자, 즐겨찾기);

        favoriteService.deleteById(로그인_사용자, favorite.getId());

        List<FavoriteResponse> favorites = favoriteService.findFavorites(로그인_사용자);
        assertThat(favorites).isEmpty();
    }

}