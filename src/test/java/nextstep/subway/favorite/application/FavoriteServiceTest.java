package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@DisplayName("즐겨찾기 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class FavoriteServiceTest {
    @Mock
    private MemberService memberService;
    @Mock
    private StationService stationService;
    @Mock
    private FavoriteRepository favoriteRepository;
    @InjectMocks
    private FavoriteService favoriteService;

    @DisplayName("즐겨찾기 추가할 사용자는 반드시 존재해야 한다.")
    @Test
    void 즐겨찾기_추가할_사용자는_반드시_존재해야_한다() {
        // given
        when(memberService.findMemberById(anyLong())).thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.createFavorite(1L, FavoriteRequest.of(1L, 2L)));
    }

    @DisplayName("즐겨찾기 추가할 지하철역은 반드시 존재해야 한다.")
    @Test
    void 즐겨찾기_추가할_지하철역은_반드시_존재해야_한다() {
        // given
        when(stationService.findStationById(anyLong())).thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.createFavorite(1L, FavoriteRequest.of(1L, 2L)));
    }

    @DisplayName("즐겨찾기를 생성한다.")
    @Test
    void 즐겨찾기를_생성한다() {
        // given
        when(memberService.findMemberById(1L)).thenReturn(new Member(EMAIL, PASSWORD, AGE));
        when(stationService.findStationById(1L)).thenReturn(new Station("강남역"));
        when(stationService.findStationById(2L)).thenReturn(new Station("양재역"));

        // when
        FavoriteResponse favorite = favoriteService.createFavorite(1L, FavoriteRequest.of(1L, 2L));

        // then
        assertThat(favorite.getSource().getName()).isEqualTo("강남역");
        assertThat(favorite.getTarget().getName()).isEqualTo("양재역");
    }

    @DisplayName("사용자가 존재하지 않으면 즐겨찾기 목록 조회에 실패한다.")
    @Test
    void 사용자가_존재하지_않으면_즐겨찾기_목록_조회에_실패한다() {
        // given
        when(memberService.findMemberById(anyLong())).thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.findFavorites(1L));
    }

    @DisplayName("사용자의 즐겨찾기 목록을 조회한다.")
    @Test
    void 사용자의_즐겨찾기_목록을_조회한다() {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Member 사용자 = new Member(EMAIL, PASSWORD, AGE);
        사용자.addFavorite(강남역, 양재역);

        when(memberService.findMemberById(1L)).thenReturn(사용자);

        // when
        List<FavoriteResponse> favorites = favoriteService.findFavorites(1L);

        // then
        assertThat(favorites).hasSize(1);
    }

    @DisplayName("사용자가 존재하지 않으면 즐겨찾기 삭제에 실패한다.")
    @Test
    void 사용자가_존재하지_않으면_즐겨찾기_삭제에_실패한다() {
        // given
        when(memberService.findMemberById(anyLong())).thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.deleteFavorite(1L, 1L));
    }

    @DisplayName("즐겨찾기가 존재하지 않으면 즐겨찾기 삭제에 실패한다.")
    @Test
    void 즐겨찾기가_존재하지_않으면_즐겨찾기_삭제에_실패한다() {
        // given
        when(favoriteRepository.findById(anyLong())).thenThrow(IllegalArgumentException.class);

        // when, then
        assertThatIllegalArgumentException().isThrownBy(() -> favoriteService.deleteFavorite(1L, 1L));
    }

    @DisplayName("즐겨찾기 삭제에 성공한다.")
    @Test
    void 즐겨찾기_삭제에_성공한다() {
        // given
        Station 강남역 = new Station("강남역");
        Station 양재역 = new Station("양재역");
        Member 사용자 = new Member(EMAIL, PASSWORD, AGE);
        Favorite 즐겨찾기 = 사용자.addFavorite(강남역, 양재역);

        when(memberService.findMemberById(1L)).thenReturn(사용자);
        when(favoriteRepository.findById(1L)).thenReturn(Optional.of(즐겨찾기));

        // when
        favoriteService.deleteFavorite(1L, 1L);

        // then
        assertThat(사용자.getFavorites().values()).hasSize(0);
    }
}
