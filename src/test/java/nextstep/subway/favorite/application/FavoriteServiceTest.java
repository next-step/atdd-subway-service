package nextstep.subway.favorite.application;

import static nextstep.subway.member.MemberTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
			.isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("즐겨찾기될 출발역, 도착역은 DB에 있어야 한다.")
	@Test
	void nonExistsSourceTest() {
		when(stationRepository.findById(anyLong())).thenReturn(Optional.empty());
		when(memberRepository.findById(anyLong())).thenReturn(Optional.of(자바지기));

		assertThatThrownBy(() -> favoriteService.createFavorite(1L , new FavoriteRequest(1L, 2L)))
			.isInstanceOf(IllegalArgumentException.class);
	}
}