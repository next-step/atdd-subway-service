package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
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
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
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

        given(memberRepository.findById(사용자_ID)).willReturn(Optional.of(사용자));
        given(stationService.findStationById(강남역_ID)).willReturn(강남역);
        given(stationService.findStationById(광교역_ID)).willReturn(광교역);
        given(favoriteRepository.save(any())).willReturn(즐겨찾기);
    }

    @DisplayName("강남역-광교역 즐겨찾기 등록을 요청하면, 사용자의 즐겨찾기로 등록된다.")
    @Test
    void saveFavorite() {
        //when
        FavoriteResponse favoriteResponse = 즐겨찾기_등록_요청(사용자_ID, new FavoriteRequest(강남역_ID, 광교역_ID));

        //then
        즐겨찾기_등록됨(favoriteResponse);
    }

    @DisplayName("사용자의 즐겨찾기 조회를 요청하면, 사용자의 모든 즐겨찾기가 조회된다.")
    @Test
    void findAllFavorites() {
        //given
        즐겨찾기_등록_요청(사용자_ID, new FavoriteRequest(강남역_ID, 광교역_ID));
        given(favoriteRepository.findAllByMemberId(any())).willReturn(Arrays.asList(즐겨찾기));

        //when
        List<FavoriteResponse> favoriteResponses = 즐겨찾기_전체조회_요청(사용자_ID);

        //then
        즐겨찾기_전체조회됨(favoriteResponses);
    }

    @DisplayName("강남역-광교역 즐겨찾기 삭제를 요청하면, 사용자의 즐겨찾기 중 강남역-광교역 즐겨찾기가 삭제된다.")
    @Test
    void deleteFavoriteById() {
        //given
        FavoriteResponse favoriteResponse = 즐겨찾기_등록_요청(사용자_ID, new FavoriteRequest(강남역_ID, 광교역_ID));
        given(favoriteRepository.findById(any())).willReturn(Optional.of(즐겨찾기));

        //when
        즐겨찾기_삭제_요청(사용자_ID, favoriteResponse.getId());

        //then
        즐겨찾기_삭제됨(사용자_ID);
    }

    private FavoriteResponse 즐겨찾기_등록_요청(Long memberId, FavoriteRequest favoriteRequest) {
        return favoriteService.saveFavorite(memberId, favoriteRequest);
    }

    private List<FavoriteResponse> 즐겨찾기_전체조회_요청(Long memberId) {
        return favoriteService.findAllFavorites(memberId);
    }

    private void 즐겨찾기_삭제_요청(Long memberId, Long favoriteId) {
        favoriteService.deleteFavoriteById(memberId, favoriteId);
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
