package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Service
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

	public List<Favorite> findFavorites(Long loginId) {

		return null;
	}
}
