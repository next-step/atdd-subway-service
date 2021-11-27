package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Name;
import nextstep.subway.common.exception.InvalidDataException;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("즐겨찾기 서비스")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private StationService stationService;
    @Mock
    private PathService pathService;
    @Mock
    private FavoriteRepository repository;

    @InjectMocks
    private FavoriteService service;

    private LoginMember 로그인_사용자;
    private Station 강남역;
    private Station 광교역;

    @BeforeEach
    void setUp() {
        로그인_사용자 = new LoginMember(1L, "email@email.com", 1);
        강남역 = Station.from(Name.from("강남역"));
        광교역 = Station.from(Name.from("광교역"));
    }

    @Test
    @DisplayName("저장")
    void saveFavorite() {
        // given
        검색된_지하철_역_제공(1L, 강남역);
        검색된_지하철_역_제공(2L, 광교역);
        저장된_즐겨찾기_반환(강남역, 광교역);

        // when
        service.saveFavorite(로그인_사용자, new FavoriteRequest(1L, 2L));

        // then
        즐겨찾기_저장_요청됨(로그인_사용자, 강남역, 광교역);
    }

    @Test
    @DisplayName("경로가 반드시 존재해야 즐겨찾기 저장 가능")
    void saveFavorite_notFoundPath_thrownInvalidDataException() {
        // given
        검색된_지하철_역_제공(1L, 강남역);
        검색된_지하철_역_제공(2L, 광교역);
        경로가_존재하지_않음(강남역, 광교역);

        // when
        ThrowingCallable saveFavoriteCallable =
            () -> service.saveFavorite(로그인_사용자, new FavoriteRequest(1L, 2L));

        // then
        즐겨찾기_저장_실패(saveFavoriteCallable);
    }

    @Test
    @DisplayName("삭제")
    void deleteFavorite() {
        // given
        Favorite mockFavorite = mock(Favorite.class);
        검색된_즐겨찾기_제공(mockFavorite);

        // when
        service.deleteFavorite(Long.MAX_VALUE, 로그인_사용자);

        // then
        즐겨찾기_삭제_요청됨(mockFavorite);
    }

    private void 즐겨찾기_저장_실패(ThrowingCallable saveFavoriteCallable) {
        assertAll(
            this::즐겨찾기_저장_요청되지_않음,
            () -> assertThatExceptionOfType(InvalidDataException.class)
                .isThrownBy(saveFavoriteCallable)
                .withMessageEndingWith("경로를 찾을 수 없어 즐겨찾기를 저장할 수 없습니다.")
        );
    }

    private void 즐겨찾기_저장_요청되지_않음() {
        verify(repository, never()).save(any());
    }

    private void 경로가_존재하지_않음(Station source, Station target) {
        when(pathService.shortestPath(eq(source), eq(target)))
            .thenThrow(new InvalidDataException("경로를 조회할 수 없습니다."));
    }

    private void 검색된_지하철_역_제공(Long id, Station station) {
        when(stationService.findById(id))
            .thenReturn(station);
    }

    private void 저장된_즐겨찾기_반환(Station source, Station target) {
        Favorite favorite = mock(Favorite.class);
        when(favorite.id()).thenReturn(1L);
        when(favorite.source()).thenReturn(source);
        when(favorite.target()).thenReturn(target);
        when(repository.save(any(Favorite.class)))
            .thenReturn(favorite);
    }

    private void 즐겨찾기_저장_요청됨(LoginMember member, Station expectedSource, Station expectedTarget) {
        ArgumentCaptor<Favorite> favoriteArgumentCaptor = ArgumentCaptor.forClass(Favorite.class);
        verify(repository, only()).save(favoriteArgumentCaptor.capture());
        Favorite favorite = favoriteArgumentCaptor.getValue();

        assertAll(
            () -> assertThat(favorite.memberId()).isEqualTo(member.getId()),
            () -> assertThat(favorite.source()).isEqualTo(expectedSource),
            () -> assertThat(favorite.target()).isEqualTo(expectedTarget)
        );
    }

    private void 검색된_즐겨찾기_제공(Favorite favorite) {
        when(repository.findByIdAndMemberId(anyLong(), eq(로그인_사용자.getId())))
            .thenReturn(Optional.of(favorite));
    }

    private void 즐겨찾기_삭제_요청됨(Favorite mockFavorite) {
        verify(repository, times(1))
            .delete(mockFavorite);
    }
}
