package nextstep.subway.favorite.application;

import static nextstep.subway.member.MemberAcceptanceTest.AGE;
import static nextstep.subway.member.MemberAcceptanceTest.EMAIL;
import static nextstep.subway.member.MemberAcceptanceTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    MemberService memberService;
    @Mock
    StationService stationService;
    @Mock
    FavoriteRepository favoriteRepository;
    @Mock
    Station 강남역 = new Station("강남역");
    @Mock
    Station 광교역 = new Station("광교역");
    @Mock
    Member 사용자 = new Member(EMAIL, PASSWORD, AGE);

    @DisplayName("즐겨찾기를 등록한다")
    @Test
    void createFavorite() {
        given(강남역.getId()).willReturn(1L);
        given(광교역.getId()).willReturn(2L);
        given(memberService.findMember(any())).willReturn(사용자);
        given(stationService.findStationById(any())).willReturn(강남역);
        given(stationService.findStationById(any())).willReturn(광교역);
        given(favoriteRepository.save(any())).willReturn(new Favorite(사용자, 강남역, 광교역));
        FavoriteService favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

        FavoriteResponse favoriteResponse = favoriteService
                .createFavorite(new LoginMember(1L, EMAIL, AGE), new FavoriteRequest(1L, 2L));

        assertAll(
                () -> assertThat(favoriteResponse.getId()).isNull(),
                () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(1L),
                () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(2L)
        );
    }

    @DisplayName("사용자가 등록한 즐겨찾기 목록을 조회한다")
    @Test
    void findAllFavorites() {
        given(memberService.findMember(any())).willReturn(사용자);
        given(사용자.getFavorites()).willReturn(Collections.singletonList(new Favorite(사용자, 강남역, 광교역)));
        FavoriteService favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

        List<FavoriteResponse> favorites = favoriteService.findAllFavorites(new LoginMember(1L, EMAIL, AGE));

        assertAll(
                () -> assertThat(favorites).hasSize(1),
                () -> assertThat(favorites.get(0).getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(favorites.get(0).getTarget().getName()).isEqualTo(광교역.getName())
        );
    }

    @DisplayName("사용자가 등록했던 즐겨찾기를 삭제한다")
    @Test
    void deleteFavorite() {
        FavoriteService favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

        ThrowingCallable 즐겨찾기_삭제 = () -> favoriteService.deleteFavorite(1L);

        assertThatNoException().isThrownBy(즐겨찾기_삭제);
    }
}
