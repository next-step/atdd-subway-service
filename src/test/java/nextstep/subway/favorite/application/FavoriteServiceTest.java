package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

	@BeforeEach
	void setUp() {
		favoriteService = new FavoriteService(memberService, stationService, favoriteRepository);

		회원 = new Member(1L, "test@test.com", "1234", 20);

		시청역 = new Station(1L, "시청역");
		서울역 = new Station(2L, "서울역");
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
}
