package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class FavoriteServiceTest {
    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private StationRepository stationRepository;

    private MemberService memberService;
    private StationService stationService;
    private FavoriteService favoriteService;

    private Member 사용자;
    private LoginMember 로그인_사용자;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;

    private Favorite 강남역_양재역_즐겨찾기;
    private Favorite 교대역_남부터미널역_즐겨찾기;

    @BeforeEach
    void setUp() {
        memberService = new MemberService(memberRepository);
        stationService = new StationService(stationRepository);
        favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);

        사용자 = memberRepository.save(new Member("test@test.com", "password", 99));
        로그인_사용자 = new LoginMember(사용자.getId(), 사용자.getEmail(), 사용자.getAge());

        강남역 = stationRepository.save(new Station("강남역"));
        양재역 = stationRepository.save(new Station("양재역"));
        교대역 = stationRepository.save(new Station("교대역"));
        남부터미널역 = stationRepository.save(new Station("남부터미널역"));

        강남역_양재역_즐겨찾기 = favoriteRepository.save(new Favorite(사용자, 강남역, 양재역));
        교대역_남부터미널역_즐겨찾기 = favoriteRepository.save(new Favorite(사용자, 교대역, 남부터미널역));
    }

    @Test
    void 즐겨찾기를_등록할_수_있다() {
        // given
        final FavoriteRequest request = new FavoriteRequest(강남역.getId(), 교대역.getId());

        // when
        final FavoriteResponse response = favoriteService.createFavorite(로그인_사용자, request);

        // then
        assertThat(response.getMember()).usingRecursiveComparison().isEqualTo(로그인_사용자);
        assertThat(response.getSourceStation()).usingRecursiveComparison().isEqualTo(StationResponse.of(강남역));
        assertThat(response.getTargetStation()).usingRecursiveComparison().isEqualTo(StationResponse.of(교대역));
    }

    @Test
    void 즐겨찾기_목록을_조회할_수_있다() {
        // when
        final List<FavoriteResponse> favorites = favoriteService.findAllFavoriteResponses(로그인_사용자);

        // then
        assertThat(favorites.stream().map(FavoriteResponse::getId).collect(Collectors.toList()))
                .containsExactly(강남역_양재역_즐겨찾기.getId(), 교대역_남부터미널역_즐겨찾기.getId());
    }

    @Test
    void 즐겨찾기를_삭제할_수_있다() {
        // when
        favoriteService.deleteFavorite(강남역_양재역_즐겨찾기.getId());

        // then
        final List<FavoriteResponse> favorites = favoriteService.findAllFavoriteResponses(로그인_사용자);
        assertThat(favorites.stream().map(FavoriteResponse::getId).collect(Collectors.toList()))
                .containsExactly(교대역_남부터미널역_즐겨찾기.getId());
    }
}
