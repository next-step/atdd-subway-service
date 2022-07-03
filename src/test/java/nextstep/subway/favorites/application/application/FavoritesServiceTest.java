package nextstep.subway.favorites.application.application;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Optional;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorites.application.FavoritesService;
import nextstep.subway.favorites.domain.Favorites;
import nextstep.subway.favorites.domain.FavoritesRepository;
import nextstep.subway.favorites.dto.FavoritesRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Favorites")
class FavoritesServiceTest {

    @Mock
    private FavoritesRepository favoritesRepository;

    @Mock
    private StationService stationService;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private FavoritesService favoritesService;

    private LoginMember 로그인_회원;
    private Member 회원;
    private Long 회원_번호;
    private Station 선정릉역, 선릉역;
    private Long 선정릉역_번호, 선릉역_번호;
    private Favorites 즐겨찾기;
    private Long 즐겨찾기_번호;

    @BeforeEach
    void setUp() {
        회원_관련_테스트_객체_생성();
        로그인_관련_테스트_객체_생성();
        역_관련_테스트_객체_생성();
        즐겨찾기_관련_테스트_객체_생성();
    }

    @Test
    @DisplayName("즐겨찾기를 저장한다.")
    public void saveFavorites() {
        // Given
        given(memberRepository.findById(회원_번호)).willReturn(Optional.of(회원));
        given(stationService.findStationById(선정릉역_번호)).willReturn(선정릉역);
        given(stationService.findStationById(선릉역_번호)).willReturn(선릉역);
        given(favoritesRepository.save(any(Favorites.class))).will(AdditionalAnswers.returnsFirstArg());
        given(favoritesRepository.existsBySourceIdAndTargetId(선정릉역_번호, 선릉역_번호)).willReturn(false);

        FavoritesRequest favoritesRequest = new FavoritesRequest(선정릉역_번호, 선릉역_번호);

        // When
        favoritesService.saveFavorites(로그인_회원, favoritesRequest);

        // Then
        verify(memberRepository).findById(회원_번호);
        verify(stationService, times(2)).findStationById(anyLong());
        verify(favoritesRepository).existsBySourceIdAndTargetId(선정릉역_번호, 선릉역_번호);
        verify(favoritesRepository).save(any(Favorites.class));
    }

    @Test
    @DisplayName("즐겨찾기 목록을 조회한다.")
    public void findAllFavorites() {
        // Given
        Favorites favorites = new Favorites(회원, 선정릉역, 선릉역);
        given(favoritesRepository.findAllByMemberId(로그인_회원.getId())).willReturn(Collections.singletonList(favorites));

        // When
        favoritesService.findAllFavorites(로그인_회원);

        // Then
        verify(favoritesRepository).findAllByMemberId(로그인_회원.getId());
    }

    @Test
    @DisplayName("즐겨찾기가 이미 등록된 경우 예외 발생 검증")
    public void throwException_WhenFavoritesIsAlreadyExist() {
        given(memberRepository.findById(회원_번호)).willReturn(Optional.of(회원));
        given(stationService.findStationById(선정릉역_번호)).willReturn(선정릉역);
        given(stationService.findStationById(선릉역_번호)).willReturn(선릉역);
        given(favoritesRepository.existsBySourceIdAndTargetId(선정릉역_번호, 선릉역_번호)).willReturn(true);

        FavoritesRequest favoritesRequest = new FavoritesRequest(선정릉역_번호, 선릉역_번호);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> favoritesService.saveFavorites(로그인_회원, favoritesRequest));

        // Then
        verify(memberRepository).findById(회원_번호);
        verify(stationService, times(2)).findStationById(anyLong());
        verify(favoritesRepository).existsBySourceIdAndTargetId(선정릉역_번호, 선릉역_번호);
    }

    @Test
    @DisplayName("즐겨찾기 목록을 삭제한다.")
    public void deleteFavoritesById() {
        // Given
        given(favoritesRepository.findByIdAndMemberId(로그인_회원.getId(), 즐겨찾기_번호)).willReturn(Optional.of(즐겨찾기));

        // When
        favoritesService.deleteFavoritesById(로그인_회원, 즐겨찾기_번호);

        // Then
        verify(favoritesRepository).findByIdAndMemberId(로그인_회원.getId(), 즐겨찾기_번호);
    }

    @Test
    @DisplayName("삭제 대상 즐겨찾기가 없는 경우 삭제 시 예외")
    public void throwException_WhenDeleteTargetFavoritesIsNotExist() {
        // Given
        given(favoritesRepository.findByIdAndMemberId(로그인_회원.getId(), 즐겨찾기_번호))
            .willThrow(IllegalArgumentException.class);

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> favoritesService.deleteFavoritesById(로그인_회원, 즐겨찾기_번호));

        // Then
        verify(favoritesRepository).findByIdAndMemberId(로그인_회원.getId(), 즐겨찾기_번호);
    }


    private void 회원_관련_테스트_객체_생성() {
        회원_번호 = 1L;
        회원 = new Member("honggildong@gmail.com", "password", 99);
    }

    private void 로그인_관련_테스트_객체_생성() {
        로그인_회원 = new LoginMember(회원_번호, 회원.getEmail(), 회원.getAge());
    }

    private void 역_관련_테스트_객체_생성() {
        선정릉역_번호 = 1L;
        선정릉역 = new Station("선정릉역");
        선릉역_번호 = 2L;
        선릉역 = new Station("선릉역");
    }

    private void 즐겨찾기_관련_테스트_객체_생성() {
        즐겨찾기_번호 = 1L;
        즐겨찾기 = new Favorites(회원, 선정릉역, 선릉역);
    }
}
