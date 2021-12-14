package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.dto.FavoriteResponses;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;

    private Station 강남역;
    private Station 양재역;
    private Station 교대역;
    private Station 남부터미널역;
    private Member member;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        교대역 = new Station("교대역");
        남부터미널역 = new Station("남부터미널역");
        member = new Member(1L, "email@email.com", "password", 20);
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void saveFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1, 2);
        Favorite favorite = new Favorite(강남역, 양재역, member);

        when(stationService.findStationById(1L))
            .thenReturn(강남역);
        when(stationService.findStationById(2L))
            .thenReturn(양재역);
        when(favoriteRepository.save(any()))
            .thenReturn(favorite);

        // when
        Favorite actual = favoriteService.saveFavorite(1L, favoriteRequest);

        // then
        assertThat(actual).isSameAs(favorite);
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void getFavorites() {
        // given
        Favorite favorite1 = new Favorite(강남역, 양재역, member);
        Favorite favorite2 = new Favorite(교대역, 남부터미널역, member);
        List<Favorite> favorites = Arrays.asList(favorite1, favorite2);
        List<FavoriteResponse> expected = favorites.stream()
            .map(FavoriteResponse::from)
            .collect(Collectors.toList());

        when(favoriteRepository.findAllByMemberId(anyLong()))
            .thenReturn(favorites);

        // when
        FavoriteResponses actual = favoriteService.getFavorites(1L);

        // then
        assertThat(actual.getResponses()).containsAll(expected);
    }

    @Test
    void deleteFavorite() {
        // given
        Favorite favorite = new Favorite(강남역, 양재역, member);
        when(favoriteRepository.findById(anyLong()))
            .thenReturn(Optional.of(favorite));

        doNothing()
            .when(favoriteRepository)
            .deleteById(anyLong());

        // when
        favoriteService.deleteFavorite(member.getId(), 1L);

        // then
        verify(favoriteRepository).deleteById(1L);
    }
}