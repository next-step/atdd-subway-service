package nextstep.subway.favorite.application;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import nextstep.subway.AcceptanceTest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.StationResponse;

@DisplayName("즐겨찾기 서비스 관련 기능")
public class FavoriteServiceTest extends AcceptanceTest  {
    @Mock
    StationService stationService; 
    
    @Mock
    MemberService memberService;

    @Mock
    FavoriteRepository favoriteRepository;
    
    @InjectMocks
    private FavoriteService favoriteService;

    private LoginMember loginMember;
    private Member expectedRegisteMember;

    private FavoriteRequest favoriteRequest;
    private FavoriteResponse expectedFavoriteResponse;

    private Station 양재역;
    private Station 교대역;
    
    @BeforeEach
    public void setUp(){
        super.setUp();
        
        양재역 = new Station(1L, "양재역");
        교대역 = new Station(2L, "교대역");

        loginMember = new LoginMember(1L, "test@test.com", 20);        
        expectedRegisteMember = new Member(loginMember.getEmail(), "password", loginMember.getAge());

        favoriteRequest = FavoriteRequest.of(양재역.getId(), 교대역.getId());
        expectedFavoriteResponse = FavoriteResponse.of(null, StationResponse.of(양재역), StationResponse.of(교대역));
    }

    @DisplayName("로그인한 맴버의 즐겨찾기가 추가된다.")
    @Test
    void create_favorite() {
        // given
        역_등록됨(양재역);
        역_등록됨(교대역);
        맴버_등록됨(loginMember);

        즐겨찾기_추가됨(양재역, 교대역, expectedRegisteMember);

        // when
        FavoriteResponse favoriteResponse = favoriteService.create(loginMember, favoriteRequest);

        // then
        Assertions.assertThat(favoriteResponse).isEqualTo(expectedFavoriteResponse);
    }

    @DisplayName("로그인한 맴버가 등록되지 않을때 즐겨찾기 추가시 예외가 발생된다.")
    @Test
    void exception_createFavorite_notRegistedMember() {
        // when
        when(memberService.findMemberEntity(loginMember.getId())).thenThrow(NoSuchElementException.class);

        // then
        Assertions.assertThatExceptionOfType(NoSuchElementException.class) 
                    .isThrownBy(() -> favoriteService.create(loginMember, favoriteRequest));
    }

    @DisplayName("즐겨찾기를 삭제한다.")
    @Test
    void delete_favorite() {
        // when
        // then
        Assertions.assertThatNoException()
                    .isThrownBy(() -> favoriteService.deleteById(loginMember.getId()));
    }

    @DisplayName("특정 맴버가 생성한 즐겨찾기를 조회한다.")
    @Test
    void search_favoriteByMember() {
        // given
        맴버_등록됨(loginMember);
        맴버의_즐겨찾기_조회됨(expectedRegisteMember);
        
        // when
        List<FavoriteResponse> favoriteResponses = favoriteService.findByMember(loginMember);

        // then
        Assertions.assertThat(favoriteResponses).isEqualTo(List.of(expectedFavoriteResponse));
    }

    @DisplayName("미등록된 맴버로 즐겨찾기를 조회시 에러가 발생된다.")
    @Test
    void exception_searchFavoriteByMember_notRegistedMember() {
        // given
        when(memberService.findMemberEntity(loginMember.getId())).thenThrow(NoSuchElementException.class);

        // when
        Assertions.assertThatExceptionOfType(NoSuchElementException.class) 
                    .isThrownBy(() -> favoriteService.findByMember(loginMember));
    }

    private void 맴버의_즐겨찾기_조회됨(Member member) {
        Favorite searchingFavorite = Favorite.of(양재역, 교대역, expectedRegisteMember);

        when(favoriteRepository.findByMember(member)).thenReturn(List.of(searchingFavorite));
    }

    private void 즐겨찾기_추가됨(Station sourceStation, Station targetStation, Member member) {
        when(favoriteRepository.save(Favorite.of(sourceStation, targetStation, member))).thenReturn(Favorite.of(sourceStation, targetStation, member));
    }

    private void 역_등록됨(Station station) {
        when(stationService.findById(station.getId())).thenReturn(station);
    }

    private void 맴버_등록됨(LoginMember loginMember) {
        when(memberService.findMemberEntity(loginMember.getId())).thenReturn(expectedRegisteMember);
    }
}
