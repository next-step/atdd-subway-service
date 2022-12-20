package nextstep.subway.favorite.application;

import static nextstep.subway.auth.application.AuthServiceTest.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
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
    
    private Station 강남역;
    private Station 양재역;


    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
    }


    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void 즐겨찾기를_생성한다() {
        // given
        when(memberService.findMemberById(1L)).thenReturn(new Member(EMAIL, PASSWORD, AGE));
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(양재역);

        // when
        FavoriteResponse favorite = favoriteService.createFavorite(1L, FavoriteRequest.of(1L, 2L));

        // then
        assertThat(favorite.getSource().getName()).isEqualTo("강남역");
        assertThat(favorite.getTarget().getName()).isEqualTo("양재역");
    }

    @DisplayName("즐겨찾기를 조회한다.")
    @Test
    void 즐겨찾기를_조회한다() {
        // given
        Member 사용자 = new Member(EMAIL, PASSWORD, AGE);
        사용자.addFavorite(강남역, 양재역);

        when(memberService.findMemberById(1L)).thenReturn(사용자);

        // when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(1L);

        // then
        assertThat(favorites).hasSize(1);
    }

}
