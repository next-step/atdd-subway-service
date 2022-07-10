package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @InjectMocks
    private FavoriteService favoriteService;
    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberRepository memberRepository;

    private Station 강남역;
    private Station 판교역;


    @BeforeEach
    private void setUp() {
        강남역 = new Station("강남역");
        판교역 = new Station("판교역");
    }


    @DisplayName("즐겨찾기 저장")
    @Test
    void saveFavorite() {
        //given
        when(stationService.findById(1L)).thenReturn(강남역);
        when(stationService.findById(2L)).thenReturn(판교역);
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(new ArrayList<>());
        when(favoriteRepository.save(any())).thenReturn(new Favorite(강남역, 판교역, 1L));

        //when
        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        LoginMember loginMember = new LoginMember(1L, "abbc@gmail.com", 2);
        FavoriteResponse response = favoriteService.saveFavorite(request, loginMember);

        //then
        assertAll(
                () -> assertThat(response.getSource().getName().equals(강남역.getName())),
                () -> assertThat(response.getTarget().getName().equals(판교역.getName()))
        );
    }

    @DisplayName("즐겨찾기 저장시 도착역 값이 null 인 경우")
    @Test
    void saveFavoriteWithNull() {
        //given
        when(stationService.findById(1L)).thenReturn(강남역);

        //when
        FavoriteRequest request = new FavoriteRequest(1L, null);
        LoginMember loginMember = new LoginMember(1L, "abbc@gmail.com", 2);

        //then
        assertThatThrownBy(() -> favoriteService.saveFavorite(request, loginMember)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("즐겨찾기 저장시 출발역과 도착역이 같은 경우")
    @Test
    void saveFavoriteWithSameStation() {
        //given
        when(stationService.findById(1L)).thenReturn(강남역);

        //when
        FavoriteRequest request = new FavoriteRequest(1L, 1L);
        LoginMember loginMember = new LoginMember(1L, "abbc@gmail.com", 2);

        //then
        assertThatThrownBy(() -> favoriteService.saveFavorite(request, loginMember)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("즐겨찾기 저장시 이미 존재하는 즐겨찾기 구간일 경우")
    @Test
    void saveFavoriteWithDuplicatedFavorite() {
        //given
        when(stationService.findById(1L)).thenReturn(강남역);
        when(stationService.findById(2L)).thenReturn(판교역);
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(Arrays.asList(new Favorite(강남역, 판교역, 1L)));

        //when
        FavoriteRequest request = new FavoriteRequest(1L, 2L);
        LoginMember loginMember = new LoginMember(1L, "abbc@gmail.com", 2);

        //then
        assertThatThrownBy(() -> favoriteService.saveFavorite(request, loginMember)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findFavorites() {
        //given
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(Arrays.asList(new Favorite(강남역, 판교역, 1L)));

        //when
        LoginMember loginMember = new LoginMember(1L, "abbc@gmail.com", 2);
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);

        //then
        assertAll(
                () -> assertThat(favorites.get(0).getSource().getName().equals(강남역.getName())),
                () -> assertThat(favorites.get(0).getTarget().getName().equals(판교역.getName()))
        );
    }

    @DisplayName("즐겨찾기 삭제시 작성자와 삭제 요청자가 다를 경우")
    @Test
    void deleteFavoriteWithOtherUser() {
        //given
        Member 다른유저 = new Member("abcc@naver.com", "1122", 3);
        when(favoriteRepository.findById(any())).thenReturn(Optional.of(new Favorite(강남역, 판교역, 1L)));

        //when
        LoginMember loginMember = new LoginMember(2L, "abbc@gmail.com", 2);

        //then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(1L, loginMember)).isInstanceOf(
                AuthorizationException.class);
    }
}
