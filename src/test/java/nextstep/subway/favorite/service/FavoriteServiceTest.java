package nextstep.subway.favorite.service;

import static nextstep.subway.favorite.domain.FavoriteTestFixture.favorite;
import static nextstep.subway.member.domain.MemberTestFixture.member;
import static nextstep.subway.station.domain.StationTestFixture.station;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.InvalidParameterException;
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

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;
    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Member member;
    private LoginMember loginMember;
    private Station departureStation;
    private Station arrivalStation;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        member = member("email@email.com", "password", 20);
        loginMember = new LoginMember(1L, member.emailValue(), member.ageValue());
        departureStation = station("강남역");
        arrivalStation = station("잠실역");
        favorite = favorite(member, departureStation, arrivalStation);
    }

    @Test
    @DisplayName("존재하지 않는 역을 출발역으로 즐겨찾기를 생성할 수 없다.")
    void createFavoriteByEmptyDepartureStation() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        given(memberService.findMemberById(1L)).willReturn(member);
        given(stationService.findStationById(1L)).willReturn(null);
        given(stationService.findStationById(2L)).willReturn(arrivalStation);

        // when
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, favoriteRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("출발역을 확인해주세요.");
    }

    @Test
    @DisplayName("존재하지 않는 역을 도착역으로 즐겨찾기를 생성할 수 없다.")
    void createFavoriteByEmptyArrivalStation() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        given(memberService.findMemberById(1L)).willReturn(member);
        given(stationService.findStationById(1L)).willReturn(departureStation);
        given(stationService.findStationById(2L)).willReturn(null);

        // when
        assertThatThrownBy(() -> favoriteService.createFavorite(loginMember, favoriteRequest))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("도착역을 확인해주세요.");
    }

    @Test
    @DisplayName("즐겨찾기를 생성한다")
    void createFavorite() {
        // given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        given(memberService.findMemberById(1L)).willReturn(member);
        given(stationService.findStationById(1L)).willReturn(departureStation);
        given(stationService.findStationById(2L)).willReturn(arrivalStation);
        given(favoriteRepository.save(any())).willReturn(favorite);

        // when
        Favorite actual = favoriteService.createFavorite(loginMember, favoriteRequest);

        // then
        assertAll(
                () -> assertNotNull(actual),
                () -> assertThat(actual).isInstanceOf(Favorite.class)
        );
    }
    
    @Test
    @DisplayName("즐겨찾기 목록을 조회한다")
    void findFavorites() {
        // given
        Favorite favorite2 = favorite(member, station("잠실역"), station("몽촌토성역"));
        member.addFavorite(favorite);
        member.addFavorite(favorite2);
        given(memberService.findMemberById(1L)).willReturn(member);

        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findFavoritesByMemberId(loginMember);

        assertThat(favoriteResponses).hasSize(2);
    }
    
    @Test
    @DisplayName("본인이 아닌 다른 회원의 즐겨찾기는 삭제 할 수 없다.")
    void deleteFavoriteByAnotherMember() {
        // given
        Member newMember = member("email2@email.com", "password", 21);
        given(memberService.findMemberById(any())).willReturn(newMember);
        given(favoriteRepository.findById(1L)).willReturn(Optional.of(favorite));

        // when & then
        assertThatThrownBy(() -> favoriteService.deleteFavorite(loginMember, 1L))
                .isInstanceOf(InvalidParameterException.class)
                .hasMessage("즐겨찾기를 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("즐겨찾기를 삭제한다")
    void deleteFavorite() {
        // given
        member.addFavorite(favorite);
        given(memberService.findMemberById(1L)).willReturn(member);
        given(favoriteRepository.findById(1L)).willReturn(Optional.of(favorite));

        // when
        favoriteService.deleteFavorite(loginMember, 1L);

        assertThat(member.favorites()).doesNotContain(favorite);
    }
}
