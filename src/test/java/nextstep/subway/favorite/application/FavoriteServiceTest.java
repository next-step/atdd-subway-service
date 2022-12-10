package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    private FavoriteService favoriteService;

    @Mock
    private MemberService memberService;

    @Mock
    private StationService stationService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);
    }

    @DisplayName("즐겨찾기를 생성하는 경우")
    @Test
    void createFavorite() {
        // given
        when(memberService.findMemberById(1L)).thenReturn(new Member(EMAIL, PASSWORD, AGE));
        when(stationService.findStationById(1L)).thenReturn(new Station("강남역"));
        when(stationService.findStationById(2L)).thenReturn(new Station("양재역"));

        // when
        FavoriteResponse favorite = favoriteService.createFavorite(1L, FavoriteRequest.of(1L, 2L));

        // then
        assertThat(favorite.getSource().getName()).isEqualTo("강남역");
        assertThat(favorite.getTarget().getName()).isEqualTo("양재역");
    }

    @DisplayName("즐겨찾기를 조회하는 경우")
    @Test
    void findFavorites() {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Member 사용자 = new Member(EMAIL, PASSWORD, AGE);
        Favorite 즐겨찾기 = Favorite.builder()
                .member(사용자)
                .source(강남역)
                .target(양재역)
                .build();
        사용자.addFavorite(즐겨찾기);

        when(memberService.findMemberById(1L)).thenReturn(사용자);

        // when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(1L);

        // then
        assertThat(favorites).hasSize(1);
    }

    @DisplayName("즐겨찾기를 삭제하는 경우")
    @Test
    void deleteFavorite() {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Member 사용자 = new Member(EMAIL, PASSWORD, AGE);
        Favorite 즐겨찾기 = Favorite.builder()
                .member(사용자)
                .source(강남역)
                .target(양재역)
                .build();
        사용자.addFavorite(즐겨찾기);

        when(memberService.findMemberById(1L)).thenReturn(사용자);
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(즐겨찾기));

        // when
        favoriteService.deleteFavorite(1L, 1L);

        // then
        assertThat(사용자.getFavorites().values()).hasSize(0);
    }
}
