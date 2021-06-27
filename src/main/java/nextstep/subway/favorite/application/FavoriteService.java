package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
@Transactional
public class FavoriteService {

	private final StationService stationService;
	private final MemberRepository memberRepository;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(StationService stationService, MemberRepository memberRepository,
		FavoriteRepository favoriteRepository) {
		this.stationService = stationService;
		this.memberRepository = memberRepository;
		this.favoriteRepository = favoriteRepository;
	}

	public Favorite createFavorite(Long loginId, FavoriteRequest request) {
		Member member = this.memberRepository.findById(loginId).orElseThrow(RuntimeException::new);
		Station sourceStation = this.stationService.findById(request.getSource());
		Station targetStation = this.stationService.findById(request.getTarget());
		return this.favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
	}

	public List<FavoriteResponse> findFavorites(Long loginId) {
		Member member = this.memberRepository.findById(loginId).orElseThrow(RuntimeException::new);

		return member.getFavorites().getFavorites().stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	public void removeFavorite(Long loginId, long id) {
		Member member = this.memberRepository.findById(loginId).orElseThrow(RuntimeException::new);
		Favorite favorite = this.favoriteRepository.findById(id).orElseThrow(RuntimeException::new);
		member.removeFavorite(favorite);
	}
}
