package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteException;
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
		Member member = findLoginMember(loginMemberId);
		Station source = findStation(favoriteRequest.getSource());
		Station target = findStation(favoriteRequest.getTarget());
		Favorite persistFavorite = favoriteRepository.save(Favorite.of(member, source, target));
		return persistFavorite.getId();
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> findFavorites(Long loginMemberId) {
		Member creator = findLoginMember(loginMemberId);
		List<Favorite> favorites = favoriteRepository.findAllWithStationByCreator(creator);
		return FavoriteResponse.listOf(favorites);
	}

	public void deleteFavorite(Long loginMemberId, Long favoriteId) {
		Member member = findLoginMember(loginMemberId);
		Favorite favorite = findFavorite(favoriteId);
		favorite.checkCreator(member);
		favoriteRepository.delete(favorite);
	}

	private Favorite findFavorite(Long favoriteId) {
		return favoriteRepository.findById(favoriteId)
			.orElseThrow(() -> new FavoriteException("즐겨찾기가 없습니다."));
	}

	private Member findLoginMember(Long loginMemberId) {
		return memberRepository.findById(loginMemberId)
			.orElseThrow(() -> new FavoriteException("로그인한 멤버가 없습니다."));
	}

	private Station findStation(Long source) {
		return stationRepository.findById(source)
			.orElseThrow(() -> new FavoriteException("즐겨찾기할 지하철역이 없습니다."));
	}
}
