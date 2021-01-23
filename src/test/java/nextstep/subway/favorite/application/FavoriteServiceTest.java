package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

	@Mock
	FavoriteRepository favoriteRepository;
	@Mock
	StationService stationService;
	@Mock
	MemberService memberService;

	@InjectMocks
	FavoriteService favoriteService;

	private LoginMember 로그인_사용자;
	private Member 사용자;
	private Station 강남역;
	private Station 정자역;
	private Station 서울역;
	private Favorite 생성된_즐겨찾기;

	@BeforeEach
	void setUp() {
		로그인_사용자 = new LoginMember(1L, "email@email.com", 20);
		사용자 = new Member("email@email.com", "password", 20);

		강남역 = new Station(1L, "강남역");
		정자역 = new Station(2L, "정자역");
		서울역 = new Station(3L, "서울역");

		생성된_즐겨찾기 = new Favorite(1L, 사용자, 강남역, 정자역);

	}

	@Test
	void createFavorite() {
		// given
		when(favoriteRepository.save(any(Favorite.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(memberService.findById(로그인_사용자.getId())).thenReturn(사용자);
		when(stationService.findById(강남역.getId())).thenReturn(강남역);
		when(stationService.findById(서울역.getId())).thenReturn(서울역);

		// when
		FavoriteResponse created = favoriteService.createFavorite(로그인_사용자,
			new FavoriteRequest(강남역.getId(), 서울역.getId()));

		// then
		assertThat(created.getSource().getId()).isEqualTo(강남역.getId());
		assertThat(created.getSource().getName()).isEqualTo(강남역.getName());
		assertThat(created.getTarget().getId()).isEqualTo(서울역.getId());
		assertThat(created.getTarget().getName()).isEqualTo(서울역.getName());
	}

	@Test
	void findFavorites() {
		// given
		when(favoriteRepository.findAllByMemberId(로그인_사용자.getId())).thenReturn(Arrays.asList(생성된_즐겨찾기));

		// when
		List<FavoriteResponse> findList = favoriteService.findFavorites(로그인_사용자);

		// then
		assertThat(findList).anySatisfy(favoriteResponse -> {
			assertThat(favoriteResponse.getSource().getId()).isEqualTo(생성된_즐겨찾기.getSource().getId());
			assertThat(favoriteResponse.getSource().getName()).isEqualTo(생성된_즐겨찾기.getSource().getName());
			assertThat(favoriteResponse.getTarget().getId()).isEqualTo(생성된_즐겨찾기.getTarget().getId());
			assertThat(favoriteResponse.getTarget().getName()).isEqualTo(생성된_즐겨찾기.getTarget().getName());
		});
	}
}
