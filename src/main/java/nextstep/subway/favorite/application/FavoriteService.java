package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional
public class FavoriteService {
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;
	private final FavoriteRepository favoriteRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository,
		StationRepository stationRepository) {
		this.favoriteRepository = favoriteRepository;
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	public Long createFavorite(Long loginMemberId, FavoriteRequest favoriteRequest) {
		Member member = memberRepository.findById(loginMemberId).orElseThrow(IllegalArgumentException::new);
		Station source = stationRepository.findById(favoriteRequest.getSource()).orElseThrow(IllegalArgumentException::new);
		Station target = stationRepository.findById(favoriteRequest.getTarget()).orElseThrow(IllegalArgumentException::new);
		Favorite persistFavorite = favoriteRepository.save(Favorite.of(member, source, target));
		return persistFavorite.getId();
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> findFavorites(Long loginMemberId) {
		Member creator = memberRepository.findById(loginMemberId).orElseThrow(IllegalArgumentException::new);
		List<Favorite> favorites = favoriteRepository.findAllWithStationByCreator(creator);
		return FavoriteResponse.listOf(favorites);
	}

	public void deleteFavorite(Long loginMemberId, Long favoriteId) {
		Member member = memberRepository.findById(loginMemberId).orElseThrow(IllegalArgumentException::new);
		Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(IllegalArgumentException::new);
		favorite.checkCreator(member);
		favoriteRepository.delete(favorite);
	}
}
