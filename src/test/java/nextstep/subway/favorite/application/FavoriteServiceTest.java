package nextstep.subway.favorite.application;

import static nextstep.subway.auth.application.AuthServiceTest.AGE;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.assertj.core.api.Assertions;
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
    private FavoriteRepository favoriteRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private MemberRepository memberRepository;

    private LoginMember loginMember;
    private Member member;
    private Station 강남역;
    private Station 양재역;
    private Favorite favorite;

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService(favoriteRepository, stationRepository, memberRepository);
        loginMember = new LoginMember(1L, EMAIL, AGE);
        member = new Member(EMAIL, PASSWORD, AGE);
        강남역 = new Station("강남역");
        양재역 = new Station("양재역");
        favorite = Favorite.of(member, 강남역, 양재역);
    }

    @DisplayName("즐겨찾기를 등록할 수 있다.")
    @Test
    void save() {
        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        given(stationRepository.findById(1L)).willReturn(Optional.of(강남역));
        given(stationRepository.findById(2L)).willReturn(Optional.of(양재역));
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.of(member));

        //when
        FavoriteResponse favoriteResponse = favoriteService.saveFavorite(loginMember, favoriteRequest);

        //then
        assertAll(
                () -> assertThat(favoriteResponse.getSource().getName()).isEqualTo(강남역.getName()),
                () -> assertThat(favoriteResponse.getTarget().getName()).isEqualTo(양재역.getName())
        );
    }

    @DisplayName("즐겨찿기 목록을 조회할 수 있다.")
    @Test
    void find() {
        //given
        member.addFavorite(favorite);
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.of(member));

        //when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(loginMember);

        //then
        assertAll(
                () -> assertThat(favorites).isNotEmpty(),
                () -> assertThat(favorites.size()).isEqualTo(1)
        );
    }

    @DisplayName("즐겨찾기를 삭제할 수 있다.")
    @Test
    void delete() {
        //given
        member.addFavorite(favorite);
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.of(member));
        given(favoriteRepository.findById(1L)).willReturn(Optional.of(favorite));

        //when
        favoriteService.deleteFavorite(loginMember, 1L);

        //then
        assertThat(member.getFavorites()).isEmpty();
    }

    @DisplayName("본인의 즐겨찾기만 삭제할 수 있다.")
    @Test
    void delete_exception() {
        //given
        int newAge = 20;
        String newEmail = "NEW_EMAIL@EMAIL.COM";
        LoginMember newLoginMember = new LoginMember(2L, newEmail, newAge);
        Member newMember = new Member(newEmail, "newPassword", newAge);
        given(memberRepository.findByEmail(newEmail)).willReturn(Optional.of(newMember));
        given(favoriteRepository.findById(1L)).willReturn(Optional.of(favorite));

        //when then
        Assertions.assertThatThrownBy(() -> favoriteService.deleteFavorite(newLoginMember, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
