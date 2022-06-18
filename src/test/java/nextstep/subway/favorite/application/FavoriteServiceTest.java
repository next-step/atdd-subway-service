package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
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
    public static final Long 사용자_ID = 1L;
    public static final Long 강남역_ID = 1L;
    public static final Long 광교역_ID = 2L;

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private StationService stationService;
    @InjectMocks
    private FavoriteService favoriteService;

    private Member 사용자;
    private Station 강남역;
    private Station 광교역;
    private Favorite 즐겨찾기;

    @BeforeEach
    public void setUp() {
        //given
        사용자 = new Member("a@test.com", "pw", 20);
        강남역 = new Station("강남역");
        광교역 = new Station("광교역");
        즐겨찾기 = new Favorite(사용자, 강남역, 광교역);
    }

    @DisplayName("강남역-광교역 즐겨찾기 등록을 요청하면, 사용자의 즐겨찾기로 등록된다.")
    @Test
    void saveFavorite() {
        //given
        given(memberRepository.findById(사용자_ID)).willReturn(Optional.of(사용자));
        given(stationService.findStationById(강남역_ID)).willReturn(강남역);
        given(stationService.findStationById(광교역_ID)).willReturn(광교역);
        given(favoriteRepository.save(any())).willReturn(즐겨찾기);

        //when
        FavoriteResponse favoriteResponse = 즐겨찾기_등록_요청(사용자_ID, new FavoriteRequest(강남역_ID, 광교역_ID));

        //then
        즐겨찾기_등록됨(favoriteResponse);
    }

    @DisplayName("강남역-강남역 즐겨찾기 등록을 요청하면, IllegalArgumentException 이 발생한다.")
    @Test
    void invalid_saveFavorite() {
        //given
        given(memberRepository.findById(사용자_ID)).willReturn(Optional.of(사용자));
        given(stationService.findStationById(강남역_ID)).willReturn(강남역);

        //when & then
        assertThatThrownBy(() -> {
            즐겨찾기_등록_요청(사용자_ID, new FavoriteRequest(강남역_ID, 강남역_ID));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발지와 도착지는 다른 역으로 등록해주세요.");
    }

    @DisplayName("사용자의 즐겨찾기 조회를 요청하면, 사용자의 모든 즐겨찾기가 조회된다.")
    @Test
    void findAllFavorites() {
        //given
        given(memberRepository.findById(사용자_ID)).willReturn(Optional.of(사용자));
        given(stationService.findStationById(강남역_ID)).willReturn(강남역);
        given(stationService.findStationById(광교역_ID)).willReturn(광교역);
        given(favoriteRepository.save(any())).willReturn(즐겨찾기);
        given(favoriteRepository.findAllByMemberId(any())).willReturn(Arrays.asList(즐겨찾기));
        즐겨찾기_등록_요청(사용자_ID, new FavoriteRequest(강남역_ID, 광교역_ID));

        //when
        List<FavoriteResponse> favoriteResponses = 즐겨찾기_전체조회_요청(사용자_ID);

        //then
        즐겨찾기_전체조회됨(favoriteResponses);
    }

    @DisplayName("존재하지 않는 사용자의 즐겨찾기 조회를 요청하면, IllegalArgumentException 이 발생한다.")
    @Test
    void invalid_findAllFavorites() {
        //given
        Long 존재하지_않는_사용자_아이디 = 99L;
        given(memberRepository.findById(존재하지_않는_사용자_아이디)).willReturn(Optional.empty());

        //when & & then
        assertThatThrownBy(() -> {
            즐겨찾기_전체조회_요청(존재하지_않는_사용자_아이디);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 회원입니다.");
    }

    @DisplayName("강남역-광교역 즐겨찾기 삭제를 요청하면, 사용자의 즐겨찾기 중 강남역-광교역 즐겨찾기가 삭제된다.")
    @Test
    void deleteFavoriteById() {
        //given
        given(memberRepository.findById(사용자_ID)).willReturn(Optional.of(사용자));
        given(stationService.findStationById(강남역_ID)).willReturn(강남역);
        given(stationService.findStationById(광교역_ID)).willReturn(광교역);
        given(favoriteRepository.save(any())).willReturn(즐겨찾기);
        given(favoriteRepository.findById(any())).willReturn(Optional.of(즐겨찾기));
        FavoriteResponse favoriteResponse = 즐겨찾기_등록_요청(사용자_ID, new FavoriteRequest(강남역_ID, 광교역_ID));

        //when
        즐겨찾기_삭제_요청(favoriteResponse.getId());

        //then
        즐겨찾기_삭제됨(사용자_ID);
    }

    @DisplayName("존재하지 않는 즐겨찾기 삭제를 요청하면, IllegalArgumentException 이 발생한다.")
    @Test
    void invalid_deleteFavoriteById() {
        //given
        Long 존재하지_않는_즐겨찾기_아이디 = 99L;

        //when & & then
        assertThatThrownBy(() -> {
            즐겨찾기_삭제_요청(존재하지_않는_즐겨찾기_아이디);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("존재하지 않는 즐겨찾기입니다.");
    }

    private FavoriteResponse 즐겨찾기_등록_요청(Long memberId, FavoriteRequest favoriteRequest) {
        return favoriteService.saveFavorite(memberId, favoriteRequest);
    }

    private List<FavoriteResponse> 즐겨찾기_전체조회_요청(Long memberId) {
        return favoriteService.findAllFavorites(memberId);
    }

    private void 즐겨찾기_삭제_요청(Long favoriteId) {
        favoriteService.deleteFavoriteById(favoriteId);
    }

    private void 즐겨찾기_등록됨(FavoriteResponse favoriteResponse) {
        assertThat(favoriteResponse.getMember().getEmail()).isEqualTo(사용자.getEmail());
        assertThat(favoriteResponse.getSource().getName()).isEqualTo(강남역.getName());
        assertThat(favoriteResponse.getTarget().getName()).isEqualTo(광교역.getName());
    }

    private void 즐겨찾기_전체조회됨(List<FavoriteResponse> favoriteResponses) {
        assertThat(favoriteResponses).hasSize(1);
        즐겨찾기_등록됨(favoriteResponses.get(0));
    }

    private void 즐겨찾기_삭제됨(Long memberId) {
        List<FavoriteResponse> favoriteResponses = 즐겨찾기_전체조회_요청(memberId);
        assertThat(favoriteResponses).hasSize(0);
    }
}
