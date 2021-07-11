package nextstep.subway.favorite.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;

@Service
@Transactional
public class FavoriteService {
	private FavoriteRepository favoriteRepository;
	private MemberRepository memberRepository;
	private StationService stationService;

	public FavoriteService(FavoriteRepository favoriteRepository, StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
	}

	public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationService stationService) {
		this.favoriteRepository = favoriteRepository;
		this.memberRepository = memberRepository;
		this.stationService = stationService;
	}

	@Transactional(readOnly = true)
	public FavoriteResponse save(FavoriteRequest favoriteRequest) {
		Favorite favorite = favoriteRepository.save(new Favorite(stationService.findStationById(favoriteRequest.getSourceId()), stationService.findStationById(favoriteRequest.getTargetId())));

		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> findFavorites(long id) {
		Member member = memberRepository.findById(id).orElseThrow(RuntimeException::new);

		return getFavoriteResponses(member);
	}

	private List<FavoriteResponse> getFavoriteResponses(Member member) {
		List<FavoriteResponse> favoriteResponses = new ArrayList<>();
		for (Favorite favorite : member.getFavorites()) {
			favoriteResponses.add(FavoriteResponse.of(favorite));
		}
		return favoriteResponses;
	}
}
