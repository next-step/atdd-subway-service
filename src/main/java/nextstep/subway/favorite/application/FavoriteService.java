package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class FavoriteService {

	private final MemberService memberService;
	private final StationService stationService;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(MemberService memberService,
		  StationService stationService, FavoriteRepository favoriteRepository) {
		this.memberService = memberService;
		this.stationService = stationService;
		this.favoriteRepository = favoriteRepository;
	}

	public FavoriteResponse create(Long memberId, FavoriteRequest request) {
		validate(request);

		Member member = memberService.getMember(memberId);
		Station source = stationService.findById(request.getSource());
		Station target = stationService.findById(request.getTarget());

		Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
		return FavoriteResponse.of(persistFavorite);
	}

	public List<FavoriteResponse> findAllFavoritesOfMine(Long memberId) {
		Member member = memberService.getMember(memberId);
		List<Favorite> favorites = member.getFavorites();

		return favorites.stream()
			  .map(FavoriteResponse::of)
			  .collect(Collectors.toList());
	}

	public void deleteFavorite(Long memberId, FavoriteRequest request) {
		Member member = memberService.getMember(memberId);
		member.deleteFavorite(request.getSource(), request.getTarget());
	}

	private void validate(FavoriteRequest request) {
		if(request.isSameSourceTarget()) {
			throw new RuntimeException("출발역과 도착역이 같습니다.");
		}
	}
}
