package nextstep.subway.favorite.application;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import nextstep.subway.utils.DatabaseCleanup;

@SpringBootTest
public class FavoriteServiceTest {
	@Autowired
	private FavoriteService favoriteService;
	@Autowired
	private MemberService memberService;
	@Autowired
	private StationService stationService;
	@Autowired
	private DatabaseCleanup databaseCleanup;

	private MemberResponse 사용자;
	private StationResponse 강남역;
	private StationResponse 양재역;
	private StationResponse 교대역;

	@BeforeEach
	void setUp() {
		databaseCleanup.execute();
		사용자 = memberService.createMember(new MemberRequest("test@gmail.com", "test123", 20));
		강남역 = stationService.saveStation(new StationRequest("강남역"));
		양재역 = stationService.saveStation(new StationRequest("양재역"));
		교대역 = stationService.saveStation(new StationRequest("교대역"));
		favoriteService.saveFavorite(사용자.getId(), new FavoriteRequest(강남역.getId(), 교대역.getId()));
	}

	@DisplayName("즐겨찾기 등록")
	@Test
	void saveFavorite() {
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());

		FavoriteResponse favoriteResponse = favoriteService.saveFavorite(사용자.getId(), favoriteRequest);

		assertThat(favoriteResponse.getId()).isEqualTo(2L);
	}

	@DisplayName("출발역과 도착역이 같은 경우 IllegalArgumentException 발생")
	@Test
	void saveFavoriteWhenSameSourceAndTarget() {
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 강남역.getId());

		assertThatIllegalArgumentException()
			.isThrownBy(() -> favoriteService.saveFavorite(사용자.getId(), favoriteRequest))
			.withMessage(Favorite.SAME_SOURCE_AND_TARGET);
	}

	@DisplayName("이미 등록된 즐겨찾기의 경우 DataIntegrityViolationException 발생")
	@Test
	void saveFavoriteWhenExistFavorite() {
		FavoriteRequest favoriteRequest = new FavoriteRequest(강남역.getId(), 양재역.getId());

		favoriteService.saveFavorite(사용자.getId(), favoriteRequest);

		assertThatExceptionOfType(DataIntegrityViolationException.class)
			.isThrownBy(() -> favoriteService.saveFavorite(사용자.getId(), favoriteRequest));
	}

	@DisplayName("즐겨찾기 목록 조회")
	@Test
	void findAllFavoritesByMember() {
		List<FavoriteResponse> favoriteResponses = favoriteService.findAllFavoritesByMember(사용자.getId());

		assertThat(favoriteResponses).hasSize(1);
		FavoriteResponse favoriteResponse = favoriteResponses.get(0);
		assertThat(favoriteResponse.getSource()).isEqualTo(강남역);
		assertThat(favoriteResponse.getTarget()).isEqualTo(교대역);
	}
}
