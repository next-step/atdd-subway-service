package nextstep.subway.favorite.service;

import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private FavoriteRepository favoriteRepository;


    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 10;

    private static final Long MEMBER_ID = 1L;
    private static final Long SOURCE_STATION_ID = 1L;
    private static final Long TARGET_STATION_ID = 2L;

    private FavoriteService favoriteService;
    private Member 사용자 = new Member(EMAIL, PASSWORD, AGE);
    private Station 강남역 = new Station("강남역");
    private Station 광교역 = new Station("광교역");
    private Station 회현역 = new Station("회현역");
    private Station 명동역 = new Station("명동역");

    @BeforeEach
    void setUp() {
        favoriteService = new FavoriteService();
        when(memberRepository.findById(MEMBER_ID)).thenReturn(Optional.of(사용자));
        when(stationRepository.findById(SOURCE_STATION_ID)).thenReturn(Optional.of(강남역));
        when(stationRepository.findById(TARGET_STATION_ID)).thenReturn(Optional.of(광교역));
    }

    @DisplayName("즐겨찾기 생성")
    @Test
    void 즐겨찾기_생성_성공() {

        //when
        FavoriteRequest 강남_광교_요청 = new FavoriteRequest(SOURCE_STATION_ID, TARGET_STATION_ID);
        FavoriteResponse 강남_광교_응답 = favoriteService.createFavorite(MEMBER_ID, 강남_광교_요청);

        //then
        assertThat(강남_광교_응답.getSource()).isEqualTo(StationResponse.of(강남역));
        assertThat(강남_광교_응답.getTarget()).isEqualTo(StationResponse.of(광교역));
    }

    @DisplayName("즐겨찾기 목록 조회")
    @Test
    void 즐겨찾기_목록_조회_성공() {
        //given
        Favorite 강남_광교 = new Favorite(사용자, 강남역, 광교역);
        Favorite 회현_명동 = new Favorite(사용자, 회현역, 명동역);

        List<Favorite> 예상_즐겨찾기목록 = new ArrayList<>(Arrays.asList(강남_광교, 회현_명동));
        when(favoriteRepository.findByMemberId(MEMBER_ID)).thenReturn(예상_즐겨찾기목록);

        //when
        List<FavoriteResponse> 실제_즐겨찾기목록 = favoriteService.findFavorites(MEMBER_ID);

        //then
        assertThat(실제_즐겨찾기목록).hasSize(2);
        assertThat(FavoriteResponse.toList()).extracting("source").containsExactly("강남역", "회현역");
        assertThat(FavoriteResponse.toList()).extracting("target").containsExactly("회현역", "명동역");
    }

    @DisplayName("즐겨찾기 삭제")
    @Test
    void 즐겨찾기_목록_삭제_성공() {
        //given
        Favorite 강남_광교 = new Favorite(사용자, 강남역, 광교역);
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(강남_광교));

        //when
        favoriteService.deleteFavorite(MEMBER_ID, 1L);
        List<FavoriteResponse> 즐겨찾기목록 = favoriteService.findFavorites(MEMBER_ID);

        //then
        verify(favoriteRepository).delete(강남_광교);
        assertThat(즐겨찾기목록).isEmpty();
    }
}
