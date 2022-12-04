package nextstep.subway.favorite.application;

import static nextstep.subway.auth.application.AuthServiceTest.AGE;
import static nextstep.subway.auth.application.AuthServiceTest.EMAIL;
import static nextstep.subway.auth.application.AuthServiceTest.PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
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

    @DisplayName("즐겨찾기를 등록할 수 있다.")
    @Test
    void save() {
        //given
        FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);
        LoginMember loginMember = new LoginMember(1L, EMAIL, AGE);
        given(stationRepository.findById(1L)).willReturn(Optional.of(new Station("강남역")));
        given(stationRepository.findById(2L)).willReturn(Optional.of(new Station("양재역")));
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.of(new Member(EMAIL, PASSWORD, AGE)));
        //when
        favoriteService.saveFavorite(loginMember, favoriteRequest);
        List<Favorite> favorites = favoriteRequest.findFavorite(loginMember);
        //then
        assertThat(favorites).isNotNull();

    }

    @DisplayName("즐겨찿기 목록을 조회할 수 있다.")
    @Test
    void find() {
        //given
        LoginMember loginMember = new LoginMember(1L, EMAIL, AGE);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Favorite favorite = new Favorite(member, 강남역, 양재역);
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.of(member));
        given(stationRepository.findById(1L)).willReturn(Optional.of(강남역));
        given(stationRepository.findById(2L)).willReturn(Optional.of(양재역));
        given(favoriteRepository.findFavoritesByMember(member)).willReturn(
                Collections.singletonList(favorite));

        //when
        List<Favorite> favorites = favoriteService.findFavorites(loginMember);

        //then
        assertThat(favorites).containsExactly(favorite);
    }

    @DisplayName("즐겨찾기를 삭제할 수 있다.")
    @Test
    void delete() {
        //given
        LoginMember loginMember = new LoginMember(1L, EMAIL, AGE);
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Favorite favorite = new Favorite(member, 강남역, 양재역);
        given(memberRepository.findByEmail(EMAIL)).willReturn(Optional.of(member));
        given(stationRepository.findById(1L)).willReturn(Optional.of(강남역));
        given(stationRepository.findById(2L)).willReturn(Optional.of(양재역));
        given(favoriteRepository.findFavoritesByMember(member)).willReturn(
                Collections.singletonList(favorite));

        //when
        favoriteService.deleteFavorite(loginMember, id);
        List<Favorite> favorites = favoriteService.findFavorites(loginMember);


        //then
        assertThat(favorites).isEmpty();
    }
}
