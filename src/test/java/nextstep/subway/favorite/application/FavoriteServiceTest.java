package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
    StationService stationService;

    @Mock
    MemberService memberService;

    @Mock
    FavoriteRepository favoriteRepository;

    @InjectMocks
    FavoriteService favoriteService;

    Station 잠실역;
    Station 강남역;
    Member 회원;
    LoginMember 로그인_회원;
    Favorite 즐겨찾기;

    @BeforeEach
    void before() {
        //given: 지하철역이 등록되어 있다.
        잠실역 = new Station(2L, "잠실역");
        강남역 = new Station(1L, "강남역");
        //and : 회원이 등록 되어 있다.
        회원 = new Member("email@email.com", "password", 30);
        로그인_회원 = new LoginMember(1L, "email@email.com", 30);
        즐겨찾기 = new Favorite(1L, 회원, 강남역, 잠실역);
    }

    @Test
    @DisplayName("즐겨찾기를 등록 한다.")
    void save() {
        // 즐겨찾기를 db에 저장
        // FavoriteResponse를 응답한다.

        //given
        when(stationService.findStationById(1L)).thenReturn(강남역);
        when(stationService.findStationById(2L)).thenReturn(잠실역);
        when(memberService.findById(1L)).thenReturn(회원);

        FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 잠실역.getId());
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(즐겨찾기);

        //when: 즐겨 찾기를 저장 한다.
        FavoriteResponse favoriteResponse = favoriteService.createFavorite(로그인_회원, favoriteRequest);

        //then: 저장 확인
        assertThat(favoriteResponse).isNotNull();
        assertThat(favoriteResponse.getId()).isNotNull();
    }

    @Test
    @DisplayName("즐겨찾기 목록 조회한다.")
    void findFavorites() {
        //given
        when(memberService.findById(any())).thenReturn(회원);
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(Arrays.asList(즐겨찾기));

        //when
        List<FavoriteResponse> responses = favoriteService.findAll(로그인_회원);
        assertThat(responses).hasSize(1);
    }
}
