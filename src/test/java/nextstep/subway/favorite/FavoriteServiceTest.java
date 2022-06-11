package nextstep.subway.favorite;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.CannotDeleteException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.favorite.application.FavoriteService;
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

@DisplayName("즐겨찾기 추가에 대한 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;
    @Mock
    private StationService stationService;
    @Mock
    private MemberService memberService;

    @InjectMocks
    private FavoriteService favoriteService;

    private Station 대림역;
    private Station 구로디지털단지역;
    private Station 신대방역;
    private Member 회원;
    private LoginMember 로그인_멤버;

    private Favorite 즐겨찾기_대림_구로디지털단지;
    private Favorite 즐겨찾기_대림_신대방;
    private List<Favorite> 즐겨찾기_목록;


    @BeforeEach
    void setUp() {
        대림역 = new Station(1L, "대림");
        구로디지털단지역 = new Station(2L, "구로디지털단지");
        신대방역 = new Station(3L, "신대방역");

        회원 = new Member("woobeen@naver.com", "password", 29);
        로그인_멤버 = new LoginMember(1L, "woobeen@naver.com", 29);

        즐겨찾기_대림_구로디지털단지 = Favorite.of(대림역, 구로디지털단지역, 회원);
        즐겨찾기_대림_신대방 = Favorite.of(대림역, 신대방역, 회원);
        즐겨찾기_목록 = Arrays.asList(즐겨찾기_대림_구로디지털단지, 즐겨찾기_대림_신대방);
    }

    @DisplayName("지하철역을 즐겨찾기로 등록하면 정상적으로 등록되어야 한다")
    @Test
    void register_favorite_test() {
        // given
        FavoriteRequest request = new FavoriteRequest(1L, 2L);

        when(stationService.findById(anyLong()))
            .thenReturn(대림역)
            .thenReturn(구로디지털단지역);

        when(memberService.findById(anyLong()))
            .thenReturn(회원);

        // when
        FavoriteResponse result = favoriteService.registerFavorite(로그인_멤버, request);

        // then
        assertThat(result.getSource().getId()).isEqualTo(대림역.getId());
        assertThat(result.getTarget().getId()).isEqualTo(구로디지털단지역.getId());
    }

    @DisplayName("즐겨찾기로 등록할 출발역이 null 이라면 예외가 발생한다")
    @Test
    void register_favorite_exception_test() {
        // given
        FavoriteRequest request = new FavoriteRequest(1L, 2L);

        when(stationService.findById(anyLong()))
            .thenReturn(대림역)
            .thenReturn(null);

        when(memberService.findById(anyLong()))
            .thenReturn(회원);

        // then
        assertThatThrownBy(() -> {
            favoriteService.registerFavorite(로그인_멤버, request);
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_FOUND_STATION.getMessage());
    }

    @DisplayName("즐겨찾기로 등록할 도착역이 null 이라면 예외가 발생한다")
    @Test
    void register_favorite_exception_test2() {
        // given
        FavoriteRequest request = new FavoriteRequest(1L, 2L);

        when(stationService.findById(anyLong()))
            .thenReturn(null)
            .thenReturn(구로디지털단지역);

        when(memberService.findById(anyLong()))
            .thenReturn(회원);

        // then
        assertThatThrownBy(() -> {
            favoriteService.registerFavorite(로그인_멤버, request);
        }).isInstanceOf(NotFoundException.class)
            .hasMessageContaining(ExceptionType.NOT_FOUND_STATION.getMessage());
    }

    @DisplayName("즐겨찾기로 등록할 역이 서로 같다면 예외가 발생한다")
    @Test
    void register_favorite_same_exception_test() {
        // given
        FavoriteRequest request = new FavoriteRequest(1L, 1L);

        when(stationService.findById(anyLong()))
            .thenReturn(구로디지털단지역)
            .thenReturn(구로디지털단지역);

        when(memberService.findById(anyLong()))
            .thenReturn(회원);

        // then
        assertThatThrownBy(() -> {
            favoriteService.registerFavorite(로그인_멤버, request);
        }).isInstanceOf(BadRequestException.class)
            .hasMessageContaining(ExceptionType.CAN_NOT_SAME_STATION.getMessage());
    }

    @DisplayName("즐겨찾기 목록을 조회하면 정상저으로 조회되어야 한다")
    @Test
    void find_favorite_test() {
        // given
        when(memberService.findById(anyLong()))
            .thenReturn(회원);
        when(favoriteRepository.findAllByMember(any()))
            .thenReturn(즐겨찾기_목록);

        // when
        List<FavoriteResponse> result = favoriteService.findAll(로그인_멤버);

        // then
        assertThat(result.size()).isEqualTo(즐겨찾기_목록.size());
    }

    @DisplayName("즐겨찾기 항목을 삭제하면 정상적으로 삭제되어야 한다")
    @Test
    void delete_favorite_test() {
        Favorite favorite = mock(Favorite.class);

        // given
        when(memberService.findById(anyLong()))
            .thenReturn(회원);
        when(favoriteRepository.findById(anyLong()))
            .thenReturn(Optional.of(favorite));

        // when
        favoriteService.removeFavorite(로그인_멤버, anyLong());

        // then
        verify(favoriteRepository, times(1))
            .delete(any());
    }

    @DisplayName("즐겨찾기 항목의 작성자가 회원과 다르면 예외가 발생한다")
    @Test
    void delete_favorite_exception_test() {
        // given
        Member 새로운_회원 = new Member("woobeen2@naver.com", "password", 29);
        when(memberService.findById(anyLong()))
            .thenReturn(새로운_회원);
        when(favoriteRepository.findById(anyLong()))
            .thenReturn(Optional.of(즐겨찾기_대림_신대방));

        // then
        assertThatThrownBy(() -> {
            favoriteService.removeFavorite(로그인_멤버, anyLong());
        }).isInstanceOf(CannotDeleteException.class)
            .hasMessageContaining(ExceptionType.NOT_THE_MEMBER_FAVORITE.getMessage());
    }
}
