package nextstep.subway.favorite.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
@Transactional(readOnly = true)
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final MemberRepository memberRepository;
	private final StationRepository stationRepository;

	public FavoriteService(
		FavoriteRepository favoriteRepository,
		MemberRepository memberRepository,
		StationRepository stationRepository
	) {
		this.favoriteRepository = favoriteRepository;
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	@Transactional
	public FavoriteResponse addFavorite(Long memberId, FavoriteRequest favoriteRequest) {
		Long sourceId = favoriteRequest.getSource();
		Long targetId = favoriteRequest.getTarget();

		Member member = memberRepository.findById(memberId).orElseThrow(NoSuchElementException::new);
		Station source = stationRepository.findById(sourceId).orElseThrow(NoSuchElementException::new);
		Station target = stationRepository.findById(targetId).orElseThrow(NoSuchElementException::new);

		Favorite favorite = favoriteRepository.save(Favorite.of(member, source, target));
		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> findAllFavorites(Long memberId) {
		return favoriteRepository.findByMemberId(memberId)
			.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteFavorite(Long favoriteId) {
		Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(NoSuchElementException::new);
		favoriteRepository.delete(favorite);
	}
}
