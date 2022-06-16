package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

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
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    private Station 강남역;
    private Station 잠실역;
    private Member 사용자;
    private LoginMember 로그인_사용자;

    @InjectMocks
    FavoriteService favoriteService;

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    MemberService memberService;

    @Mock
    StationService stationService;

    @BeforeEach
    void setUp() {
        강남역 = new Station("강남역");
        역_ID_임의_지정(강남역, 1L);

        잠실역 = new Station("잠실역");
        역_ID_임의_지정(잠실역, 2L);

        사용자 = new Member("email@email.com", "email", 10);
        로그인_사용자 = new LoginMember(1L, "email@email.com", 20);
    }

    @Test
    @DisplayName("즐겨찾기를 저장한다")
    void save() {
        // given
        Favorite favorite = new Favorite(1L, 강남역, 잠실역);
        given(memberService.findByEmail(사용자.getEmail())).willReturn(사용자);
        given(stationService.findStationById(강남역.getId())).willReturn(강남역);
        given(stationService.findStationById(잠실역.getId())).willReturn(잠실역);
        given(favoriteRepository.save(favorite)).willReturn(favorite);

        // when
        FavoriteResponse save = favoriteService.save(로그인_사용자, new FavoriteRequest(강남역.getId(), 잠실역.getId()));

        // then
        assertThat(save).isNotNull();
    }

    void 역_ID_임의_지정(Station station, Long id) {
        ReflectionTestUtils.setField(station, "id", id);
    }
}
