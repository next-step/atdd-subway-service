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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("즐겨 찾기 관리 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationRepository stationRepository;
    @Mock
    private FavoriteRepository favoriteRepository;

    private FavoriteService favoriteService;

    private FavoriteRequest favoriteRequest;

    private Optional<Member> member;
    private Optional<Station> 서초역;
    private Optional<Station> 역삼역;

    private LoginMember loginMember;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberRepository, stationRepository, favoriteRepository);
        favoriteRequest = new FavoriteRequest(1L, 3L);
        member = Optional.of(new Member(1L, "mwkwon@test.com", "password", 37));
        서초역 = Optional.of(new Station(1L, "서초역"));
        역삼역 = Optional.of(new Station(3L, "역삼역"));
        loginMember = new LoginMember(1L, "mwkwon@test.com", 37);
    }

    @Test
    @DisplayName("즐겨 찾기 저장 기능 테스트")
    void saveFavorites() {
        // given
        when(memberRepository.findById(member.get().getId())).thenReturn(member);
        when(stationRepository.findById(1L)).thenReturn(서초역);
        when(stationRepository.findById(3L)).thenReturn(역삼역);
        // when
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(member.get().getId(), favoriteRequest);

        // then
        assertThat(favoriteResponse.getId()).isNotNull();
        assertThat(favoriteResponse.getSource().getId()).isEqualTo(favoriteRequest.getSource());
        assertThat(favoriteResponse.getTarget().getId()).isEqualTo(favoriteRequest.getTarget());
    }

    @Test
    @DisplayName("즐겨 찾기 조회 테스트")
    void findFavorites() {
        // given
        List<Favorite> favorites = Arrays.asList(new Favorite(member.get(), 서초역.get(), 역삼역.get()));
        when(favoriteRepository.findByMemberId(1L)).thenReturn(favorites);

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(loginMember);

        // given
        assertThat(favorites.size()).isGreaterThan(0);
        assertThat(favoriteResponses.get(0).getSource().getId()).isEqualTo(favoriteRequest.getSource());
        assertThat(favoriteResponses.get(0).getTarget().getId()).isEqualTo(favoriteRequest.getTarget());
    }

    @Test
    void deleteFavorite() {
        when(memberRepository.findById(loginMember.getId())).thenReturn(member);
        // when
        favoriteService.deleteFavorite(loginMember, 2L);
    }
}
