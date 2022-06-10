package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("경로 관련 기능 테스트 - Mock")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    private Station 강남 = new Station(1L, "강남");
    private Station 교대 = new Station(2L, "교대");
    private Station 신림 = new Station(3L, "신림");
    private Station 건대입구 = new Station(4L, "건대입구");
    private Member 사용자 = new Member("test@email.com", "1234", 15);
    private LoginMember 로그인사용자 = new LoginMember(1L, "test@email.com", 15);

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberService memberService;

    @DisplayName("사용자의 즐겨찾기를 조회한다.")
    @Test
    void findFavorites() {

        //given
        Favorite favorite1 = new Favorite(1L, 강남, 교대, 사용자);
        Favorite favorite2 = new Favorite(2L, 신림, 건대입구, 사용자);
        when(memberService.findMemberById(any())).thenReturn(사용자);
        when(favoriteRepository.findAllByMember(any()))
                .thenReturn(Arrays.asList(favorite1, favorite2));

        //when
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);
        List<FavoriteResponse> favorites = favoriteService.findFavorites(로그인사용자);

        //then
        assertAll(
                () -> assertThat(favorites.get(0).getSource().getId()).isEqualTo(1L),
                () -> assertThat(favorites.get(0).getSource().getName()).isEqualTo("강남"),
                () -> assertThat(favorites.get(0).getTarget().getId()).isEqualTo(2L),
                () -> assertThat(favorites.get(0).getTarget().getName()).isEqualTo("교대"),

                () -> assertThat(favorites.get(1).getSource().getId()).isEqualTo(3L),
                () -> assertThat(favorites.get(1).getSource().getName()).isEqualTo("신림"),
                () -> assertThat(favorites.get(1).getTarget().getId()).isEqualTo(4L),
                () -> assertThat(favorites.get(1).getTarget().getName()).isEqualTo("건대입구")
        );
    }

    @DisplayName("사용자의 즐겨찾기를 생성한다.")
    @Test
    void createFavorite() {

        //given
        when(memberService.findMemberById(any())).thenReturn(사용자);
        when(stationService.findStationById(any())).thenReturn(강남).thenReturn(교대);
        when(favoriteRepository.save(any())).thenReturn(new Favorite(1L, 강남, 교대, 사용자));

        //when
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(로그인사용자, 1L, 4L);

        //then
        assertAll(
                () -> assertThat(favoriteResponse.getId()).isEqualTo(1L),
                () -> assertThat(favoriteResponse.getSource().getId()).isEqualTo(1L),
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo("강남"),
                () -> assertThat(favoriteResponse.getTarget().getId()).isEqualTo(2L),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo("교대")
        );
    }

    @DisplayName("동일역으로 즐겨찾기를 생성하면 예외가 발생한다.")
    @Test
    void createFavorite_same() {

        //given
        when(memberService.findMemberById(any())).thenReturn(사용자);
        when(stationService.findStationById(any())).thenReturn(강남).thenReturn(강남);

        //when then
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);
        assertThatThrownBy(()-> favoriteService.createFavorite(로그인사용자, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class);

    }

    @DisplayName("사용자의 즐겨찾기를 삭제한다.")
    @Test
    void deleteFavorite() {

        //when
        FavoriteService favoriteService = new FavoriteService(favoriteRepository, memberService, stationService);
        favoriteService.deleteFavorite(1L);

        // then
        verify(favoriteRepository).deleteById(any());
    }


}
