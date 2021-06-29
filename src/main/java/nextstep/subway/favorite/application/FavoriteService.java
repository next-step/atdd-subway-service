package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {

	private final StationService stationService;
	private final MemberService memberService;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(StationService stationService, MemberService memberService,
		FavoriteRepository favoriteRepository) {
		this.stationService = stationService;
		this.memberService = memberService;
		this.favoriteRepository = favoriteRepository;
	}

	public Favorite createFavorite(Long loginId, FavoriteRequest request) {
		Member member = memberService.findById(loginId);
		Station sourceStation = stationService.findById(request.getSource());
		Station targetStation = stationService.findById(request.getTarget());
		return favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
	}

	public List<FavoriteResponse> findFavorites(Long loginId) {
		Member member = memberService.findById(loginId);

		return member.getFavorites().getFavorites().stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	public void removeFavorite(Long loginId, long id) {
		Member member = memberService.findById(loginId);

		Favorite favorite = favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
		member.removeFavorite(favorite);
	}
}
