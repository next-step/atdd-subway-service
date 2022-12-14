package nextstep.subway.favorite;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.NotFoundDataException;
import nextstep.subway.favorite.application.FavoriteService;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.dto.SourceAndTargetStationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static nextstep.subway.exception.type.NotFoundDataExceptionType.NOT_FOUND_FAVORITE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private StationService stationService;

    @Mock
    private MemberService memberService;

    @Mock
    private FavoriteRepository favoriteRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    private Station 신도림역 = Station.from("신도림역");
    private Station 가디역 = Station.from("가디역");
    private Line 일호선 = Line.of("1호선", "blue", 가디역, 신도림역, Distance.from(30), 0);
    private Member 유저 = new Member("test@test.com", "test", 20);
    private Favorite 즐겨찾기 = Favorite.of(신도림역, 가디역, 유저);

    private LoginMember loginMember = LoginMember.ofLogin(1L, "test@test.com", Age.from(20));

    private FavoriteRequest favoriteRequest = new FavoriteRequest(1L, 2L);

    @Test
    @DisplayName("성공적으로 즐겨찾기를 저장한다")
    void saveFavorite() {
        when(stationService.findStationById(any(), any())).thenReturn(SourceAndTargetStationDto.of(신도림역, 가디역));
        when(memberService.findMemberEntity(any())).thenReturn(유저);
        when(favoriteRepository.save(any())).thenReturn(즐겨찾기);

        FavoriteResponse result = favoriteService.saveFavorite(loginMember, favoriteRequest);

        assertThat(result.getSource().getName()).isEqualTo(신도림역.getName());
        assertThat(result.getTarget().getName()).isEqualTo(가디역.getName());
    }

    @Test
    @DisplayName("성공적으로 즐겨찾기를 조회한다")
    void getFavorite() {
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(Arrays.asList(즐겨찾기));

        List<FavoriteResponse> result = favoriteService.getFavorite(loginMember);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSource().getName()).isEqualTo(신도림역.getName());
        assertThat(result.get(0).getTarget().getName()).isEqualTo(가디역.getName());
    }

    @Test
    @DisplayName("성공적으로 즐겨찾기를 삭제한다")
    void deleteFavorite() {
        when(favoriteRepository.findByIdAndMemberId(any(), any())).thenReturn(Optional.ofNullable(즐겨찾기));
        doNothing().when(favoriteRepository).delete(any());

        favoriteService.deleteFavorite(loginMember, 1L);

        verify(favoriteRepository, times(1)).findByIdAndMemberId(any(), any());
    }

    @Test
    @DisplayName("즐겨찾기를 조회헀지만 데이터가 존재하지 않는다")
    void getFavoriteEmpty() {
        when(favoriteRepository.findAllByMemberId(any())).thenReturn(Arrays.asList());

        List<FavoriteResponse> result = favoriteService.getFavorite(loginMember);

        assertThat(result).hasSize(0);
    }

    @Test
    @DisplayName("즐겨찾기가 존재하지않으면 삭제가 불가능하다")
    void deleteFavoriteException() {
        when(favoriteRepository.findByIdAndMemberId(any(), any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> favoriteService.deleteFavorite(loginMember, 1L))
                .isInstanceOf(NotFoundDataException.class)
                .hasMessageStartingWith(NOT_FOUND_FAVORITE.getMessage());
    }
}
