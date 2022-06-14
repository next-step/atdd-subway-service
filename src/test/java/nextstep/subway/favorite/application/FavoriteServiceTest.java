package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    private FavoriteService favoriteService;

    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(stationService, memberService, favoriteRepository);
    }

    @Test
    @DisplayName("즐겨찾기를 생성한다")
    void saveFavorite() {
        // given
        final FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        final Member mond = new Member(1L, "mond@mond.com", "mond", 10);
        final Station gangNam = new Station(1L, "강남역");
        final Station gyoDae = new Station(2L, "교대역");
        when(memberService.findMemberById(1L)).thenReturn(mond);
        when(stationService.findStationById(1L)).thenReturn(gangNam);
        when(stationService.findStationById(2L)).thenReturn(gyoDae);
        when(favoriteRepository.save(any())).thenReturn(new Favorite(mond, gangNam, gyoDae));

        // when
        FavoriteResponse favoriteResponse = favoriteService.saveFavoriteOfMine(1L, favoriteRequest);

        // then
        assertAll(
                () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(1L),
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo("강남역"),
                () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(2L),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo("교대역")
        );
    }
}
