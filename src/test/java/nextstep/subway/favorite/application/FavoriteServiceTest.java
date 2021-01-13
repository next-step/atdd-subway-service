package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@DisplayName("즐겨찾기 서비스 stubbing 테스트")
@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

	@Mock
	private MemberService memberService;

	@Mock
	private StationService stationService;

	@Mock
	private FavoriteRepository favoriteRepository;

	private FavoriteService favoriteService;
	private Member 회원;
	private Station 시청역;
	private Station 서울역;
	private Station 주안역;
	private Station 강남역;
	private Station 양재역;

	@BeforeEach
	void setUp() {
		favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

		회원 = new Member(1L, "test@test.com", "1234", 20);

		시청역 = new Station(1L, "시청역");
		서울역 = new Station(2L, "서울역");
		주안역 = new Station(3L, "주안역");
		강남역 = new Station(4L, "강남역");
		양재역 = new Station(5L, "양재역");
	}

	@DisplayName("즐겨찾기 생성 테스트")
	@Test
	void createFavoriteTest() {
		// given
		given(memberService.findMemberById(1L)).willReturn(회원);
		given(stationService.findStationById(1L)).willReturn(시청역);
		given(stationService.findStationById(2L)).willReturn(서울역);
		given(favoriteRepository.save(any())).willReturn(new Favorite(1L, 회원, 시청역, 서울역));
		FavoriteRequest request = new FavoriteRequest(1L, 2L);

		// when
		FavoriteResponse response = favoriteService.createFavorite(1L, request);

		//then
		assertAll(
			() -> assertThat(response).isNotNull(),
			() -> assertThat(response.getId()).isEqualTo(1L),
			() -> assertThat(response.getSource().getName()).isEqualTo("시청역"),
			() -> assertThat(response.getTarget().getName()).isEqualTo("서울역")
		);
	}

	@DisplayName("즐겨찾기 조회 테스트")
	@Test
	void showFavoriteTest() {
		// given
		given(memberService.findMemberById(1L)).willReturn(회원);
		given(favoriteRepository.findAllByMember(회원)).willReturn(Arrays.asList(
			new Favorite(1L, 회원, 시청역, 서울역),
			new Favorite(2L, 회원, 시청역, 주안역),
			new Favorite(3L, 회원, 시청역, 강남역),
			new Favorite(4L, 회원, 시청역, 양재역)
		));

		// when
		List<FavoriteResponse> favorites = favoriteService.findFavorites(회원.getId());

		// then
		assertAll(
			() -> assertThat(favorites).isNotNull(),
			() -> assertThat(favorites).hasSize(4)
		);
	}
}
