package nextstep.subway.favorite.application;

import static nextstep.subway.auth.application.AuthServiceTest.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

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


    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void 즐겨찾기를_생성한다() {
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

}
