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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    private Station 강남역;
    private Station 양재역;
    private LoginMember 로그인_사용자;
    private Member 사용자;
    private Favorite 즐겨찾기;

    @InjectMocks
    private FavoriteService favoriteService;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");

        양재역 = new Station("양재역");

        로그인_사용자 = new LoginMember(1L , "email@email.com", 20);

        사용자 = new Member(로그인_사용자.getEmail(), "password", 로그인_사용자.getAge());

        즐겨찾기 = new Favorite(사용자, 강남역, 양재역);
    }

    @DisplayName("즐겨찾기 등록")
    @Test
    void create() {
        // given
        given(stationService.findStationById(1L)).willReturn(강남역);
        given(stationService.findStationById(1L)).willReturn(양재역);
        given(memberService.findMemberById(1L)).willReturn(사용자);
        given(favoriteRepository.save(any())).willReturn(즐겨찾기);

        // when
        FavoriteResponse favorite = favoriteService.create(로그인_사용자, new FavoriteRequest(1L, 2L));

        // then
        assertAll(
                () -> assertThat(favorite).isNotNull(),
                () -> assertThat(favorite.getSource().getName()).isEqualTo("강남역"),
                () -> assertThat(favorite.getTarget().getName()).isEqualTo("양재역")
        );
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void findAll() {
        // given
        given(favoriteRepository.findByMember(any())).willReturn(Collections.singletonList(즐겨찾기));

        // when
        List<FavoriteResponse> favorites = favoriteService.findAll(로그인_사용자);

        // then
        assertThat(favorites).isNotEmpty();
    }
}