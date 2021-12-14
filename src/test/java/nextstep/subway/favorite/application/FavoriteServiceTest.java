package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
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
    private Member member;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);

        강남역 = new Station("강남역");
        양재역 = new Station("양재역");

        member = new Member();
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void saveFavorite() {
        FavoriteRequest favoriteRequest = new FavoriteRequest(1, 2);
        Favorite favorite = new Favorite(강남역, 양재역, member);

        Mockito.when(stationService.findStationById(1L))
            .thenReturn(강남역);
        Mockito.when(stationService.findStationById(2L))
            .thenReturn(양재역);
        Mockito.when(memberService.findMemberById(Mockito.any()))
            .thenReturn(member);
        Mockito.when(favoriteRepository.save(Mockito.any()))
            .thenReturn(favorite);

        Favorite actual = favoriteService.saveFavorite(1L, favoriteRequest);

        assertThat(actual).isSameAs(favorite);
    }
}