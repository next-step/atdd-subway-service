package nextstep.subway.favorite.application;

import static nextstep.subway.generator.StationGenerator.*;
import static nextstep.subway.member.acceptance.MemberAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.Favorite.application.FavoriteService;
import nextstep.subway.Favorite.domain.Favorite;
import nextstep.subway.Favorite.domain.FavoriteRepository;
import nextstep.subway.Favorite.dto.FavoriteRequest;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.domain.Name;
import nextstep.subway.path.application.PathService;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@DisplayName("즐겨찾기 service")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

	@Mock
	private StationService stationService;

	@Mock
	private PathService pathService;

	@Mock
	private FavoriteRepository favoriteRepository;

	@InjectMocks
	private FavoriteService favoriteService;

	@DisplayName("즐겨찾기 저장")
	@Test
	void saveFavorite() {
		// given
		LoginMember 로그인_사용자 = 로그인_사용자();
		Station 강남역 = station(Name.from("강남역"));
		Station 역삼역 = station(Name.from("역삼역"));
		검색된_지하철_역(1L, 강남역);
		검색된_지하철_역(2L, 역삼역);
		저장된_즐겨찾기_반환(강남역, 역삼역);

		// when
		favoriteService.saveFavorite(로그인_사용자, FavoriteRequest.of(강남역.getId(), 역삼역.getId()));

		// then
		즐겨찾기_저장_요청됨(로그인_사용자, 강남역, 역삼역);
	}

	@DisplayName("즐겨찾기 저장 시 경로가 존재하지 않는 경우 예외 발생")
	@Test
	void saveFavoriteWhenPathNotExist() {
		// given
		LoginMember 로그인_사용자 = 로그인_사용자();
		Station 강남역 = station(Name.from("강남역"));
		Station 역삼역 = station(Name.from("역삼역"));
		검색된_지하철_역(1L, 강남역);
		검색된_지하철_역(2L, 역삼역);
		연결_경로_없음(강남역, 역삼역);

		// when, then
		FavoriteRequest request = FavoriteRequest.of(강남역.getId(), 역삼역.getId());
		assertThatThrownBy(() -> favoriteService.saveFavorite(로그인_사용자, request))
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("즐겨찾기 목록 조회")
	@Test
	void findAllFavorites() {
		// Given
		LoginMember 로그인_사용자 = 로그인_사용자();
		Station 강남역 = station(Name.from("강남역"));
		Station 역삼역 = station(Name.from("역삼역"));
		Favorite favorite = Favorite.of(강남역, 역삼역);
		given(favoriteRepository.findAll()).willReturn(Collections.unmodifiableList(Arrays.asList(favorite)));

		// When
		favoriteService.findAllFavorites(로그인_사용자);

		// Then
		verify(favoriteRepository).findAll();
	}

	@Test
	@DisplayName("즐겨찾기 삭제")
	void deleteFavorite() {
		// given
		Favorite mockFavorite = mock(Favorite.class);
		검색된_즐겨찾기(mockFavorite);

		// when
		favoriteService.deleteFavorite(로그인_사용자(), Long.MAX_VALUE);

		// then
		즐겨찾기_삭제_요청됨(mockFavorite);
	}

	private void 검색된_즐겨찾기(Favorite favorite) {
		when(favoriteRepository.findByIdAndMemberId(anyLong(), eq(로그인_사용자().getId())))
			.thenReturn(Optional.of(favorite));
	}

	private void 즐겨찾기_삭제_요청됨(Favorite mockFavorite) {
		verify(favoriteRepository, times(1)).delete(mockFavorite);
	}

	private LoginMember 로그인_사용자() {
		return new LoginMember(1L, EMAIL, AGE);
	}

	private void 연결_경로_없음(Station 강남역, Station 역삼역) {
		when(pathService.isInvalidPath(강남역, 역삼역)).thenReturn(false);
	}

	private void 검색된_지하철_역(Long id, Station station) {
		when(stationService.findById(id))
			.thenReturn(station);
	}

	private void 저장된_즐겨찾기_반환(Station source, Station target) {
		Favorite favorite = mock(Favorite.class);
		when(favorite.id()).thenReturn(1L);
		when(favorite.source()).thenReturn(source);
		when(favorite.target()).thenReturn(target);
		when(favoriteRepository.save(any(Favorite.class)))
			.thenReturn(favorite);
	}

	private void 즐겨찾기_저장_요청됨(LoginMember member, Station expectedSource, Station expectedTarget) {
		ArgumentCaptor<Favorite> favoriteArgumentCaptor = ArgumentCaptor.forClass(Favorite.class);
		verify(favoriteRepository, only()).save(favoriteArgumentCaptor.capture());
		Favorite favorite = favoriteArgumentCaptor.getValue();

		assertAll(
			() -> assertThat(favorite.memberId()).isEqualTo(member.getId()),
			() -> assertThat(favorite.source()).isEqualTo(expectedSource),
			() -> assertThat(favorite.target()).isEqualTo(expectedTarget)
		);
	}

}