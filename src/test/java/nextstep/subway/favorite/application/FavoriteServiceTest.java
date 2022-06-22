package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static nextstep.subway.member.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스 레이어 테스트")
@ExtendWith(SpringExtension.class)
class FavoriteServiceTest {
    @MockBean
    private FavoriteRepository favoriteRepository;
    @MockBean
    private MemberService memberService;
    @MockBean
    private StationService stationService;

    private Station 강남역;
    private Station 양재역;

    @BeforeEach
    void setUp() {
        강남역 = Station.of(1L, "강남역");
        양재역 = Station.of(2L, "양재역");
    }

    @DisplayName("즐겨찾기 생성 기능 테스트")
    @Test
    void createFavorite() {
        // given
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Favorite favorite = Favorite.of(member, 강남역, 양재역);
        LoginMember loginMember = new LoginMember(0L, EMAIL, AGE);
        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());

        // stubbing
        when(memberService.findById(any())).thenReturn(member);
        when(stationService.findStationById(강남역.getId())).thenReturn(강남역);
        when(stationService.findStationById(양재역.getId())).thenReturn(양재역);
        when(favoriteRepository.save(any())).thenReturn(favorite);

        // when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember, favoriteRequest);

        // then
        assertAll(
                () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(강남역.getId()),
                () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(양재역.getId())
        );
    }

    @DisplayName("즐겨찾기 목록 조회 기능 테스트")
    @Test
    void findFavorites() {
        // given
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Favorite favorite = Favorite.of(member, 강남역, 양재역);
        LoginMember loginMember = new LoginMember(0L, EMAIL, AGE);

        // stubbing
        when(memberService.findById(any())).thenReturn(member);
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(Collections.singletonList(favorite));


        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavorites(loginMember);

        // then
        assertAll(
                () -> assertThat(favoriteResponses).hasSize(1),
                () -> assertThat(favoriteResponses.get(0).getSource().getId()).isEqualTo(강남역.getId()),
                () -> assertThat(favoriteResponses.get(0).getTarget().getId()).isEqualTo(양재역.getId())
        );
    }
}