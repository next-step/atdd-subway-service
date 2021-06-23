package nextstep.subway.favorite.application;

import static nextstep.subway.member.MemberTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteException;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;

import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.StationRepository;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {
	@Mock
	private FavoriteRepository favoriteRepository;

	@Mock
	private StationRepository stationRepository;

	@Mock
	private MemberRepository memberRepository;

	private FavoriteService favoriteService;

	@BeforeEach
	void setup() {
		favoriteService = new FavoriteService(favoriteRepository, memberRepository, stationRepository);
	}

	@DisplayName("즐겨찾기할 회원은 DB에 있어야 한다.")
	@Test
	void nonExistsMemberTest() {
		when(memberRepository.findById(anyLong())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> favoriteService.createFavorite(1L , new FavoriteRequest()))
			.isInstanceOf(FavoriteException.class)
			.hasMessageContaining("로그인한 멤버가 없습니다.");
	}

	@DisplayName("즐겨찾기될 출발역, 도착역은 DB에 있어야 한다.")
	@Test
	void nonExistsSourceTest() {
		when(stationRepository.findById(anyLong())).thenReturn(Optional.empty());
		when(memberRepository.findById(anyLong())).thenReturn(Optional.of(자바지기));

		assertThatThrownBy(() -> favoriteService.createFavorite(1L , new FavoriteRequest(1L, 2L)))
			.isInstanceOf(FavoriteException.class)
			.hasMessageContaining("즐겨찾기할 지하철역이 없습니다.");
	}

	@DisplayName("즐겨찾기 목록을 조회할 때 로그인한 멤버가 존재하지 않으면 조회할 수 없다")
	@Test
	void findFavoritesNonExistsMemberTest() {
		when(memberRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> favoriteService.findFavorites(1L))
			.isInstanceOf(FavoriteException.class)
			.hasMessageContaining("로그인한 멤버가 없습니다.");
	}

	@DisplayName("로그인한 멤버의 즐겨찾기 목록을 가지고 온다.")
	@Test
	void findFavoritesTest() {
		when(memberRepository.findById(1L)).thenReturn(Optional.of(자바지기));
		favoriteService.findFavorites(1L);

		verify(favoriteRepository).findAllWithStationByCreator(자바지기);
	}

	@DisplayName("즐겨찾기가 존재하지 않으면 즐겨찾기를 지울 수 없다.")
	@Test
	void deleteNonExistsFavoriteTest() {
		when(favoriteRepository.findById(1L)).thenReturn(Optional.empty());
		when(memberRepository.findById(1L)).thenReturn(Optional.of(자바지기));

		assertThatThrownBy(() -> favoriteService.deleteFavorite(1L, 1L))
			.isInstanceOf(FavoriteException.class)
			.hasMessageContaining("즐겨찾기가 없습니다.");
	}

	@DisplayName("즐겨찾기를 한 사람이 아니면 지울 수 없다.")
	@Test
	void deleteFavoriteTest() {
		Favorite favorite = mock(Favorite.class);
		doThrow(FavoriteException.class).when(favorite).checkCreator(자바지기);
		when(favoriteRepository.findById(1L)).thenReturn(Optional.of(favorite));
		when(memberRepository.findById(1L)).thenReturn(Optional.of(자바지기));

		assertThatThrownBy(() -> favoriteService.deleteFavorite(1L, 1L))
			.isInstanceOf(FavoriteException.class);
	}
}