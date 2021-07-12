package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.AlreadyExistFavoritException;
import nextstep.subway.exception.NoStationException;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private LoginMember loginMember;
    private Station sourceStation;
    private Station targetStation;
    private Member 사용자;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        sourceStation = new Station(1L, "강남역");
        targetStation = new Station(2L, "광교역");
        사용자 = new Member(1L, "test@email.com", "password", 30);
        loginMember = new LoginMember(사용자.getId(), 사용자.getEmail(), 사용자.getAge());
        favorite = new Favorite(사용자, sourceStation, targetStation);
    }

    @DisplayName("존재하지 않는 역이면 실패한다")
    @Test
    void createFailBecauseNonExistStationTest() {
        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(targetStation.getId(), sourceStation.getId());
        doThrow(new NoStationException("존재하지 않는 역입니다.")).when(stationService).findStationById(favoriteRequest.getSource());

        //when && then
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, favoriteRequest))
                .isInstanceOf(NoStationException.class)
                .hasMessageContaining("존재하지 않는 역입니다.");
    }

    @DisplayName("이미 등록된 즐겨찾기 추가시 실패한다")
    @Test
    void createFailBecauseExistFavoriteTest() {
        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(targetStation.getId(), sourceStation.getId());
        doReturn(sourceStation).when(stationService).findStationById(favoriteRequest.getSource());
        doReturn(targetStation).when(stationService).findStationById(favoriteRequest.getTarget());
        doReturn(사용자).when(memberService).findMemberById(loginMember.getId());
        given(favoriteRepository.existsByMemberAndSourceStationAndTargetStation(사용자, sourceStation, targetStation)).willReturn(true);

        //when && then
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, favoriteRequest))
                .isInstanceOf(AlreadyExistFavoritException.class)
                .hasMessageContaining("이미 등록된 즐겨찾기 입니다.");
    }

    @DisplayName("즐겨찾기를 등록할 수 있다")
    @Test
    void createTest() {
        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(sourceStation.getId(), targetStation.getId());
        doReturn(sourceStation).when(stationService).findStationById(favoriteRequest.getSource());
        doReturn(targetStation).when(stationService).findStationById(favoriteRequest.getTarget());
        doReturn(사용자).when(memberService).findMemberById(loginMember.getId());
        given(favoriteRepository.existsByMemberAndSourceStationAndTargetStation(사용자, sourceStation, targetStation)).willReturn(false);
        given(favoriteRepository.save(any())).willReturn(favorite);

        //when
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(loginMember, favoriteRequest);

        //when
        assertThat(favoriteResponse.getSource()).isEqualTo(favoriteRequest.getSource());
        assertThat(favoriteResponse.getTarget()).isEqualTo(favoriteRequest.getTarget());
        assertThat(favoriteResponse.getMemberId()).isEqualTo(loginMember.getId());
    }

}