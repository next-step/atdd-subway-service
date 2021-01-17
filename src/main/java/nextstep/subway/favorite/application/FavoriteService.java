package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteService {

	private FavoriteRepository favoriteRepository;
	private StationRepository stationRepository;
	private MemberRepository memberRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
		this.memberRepository = memberRepository;
	}

	@Transactional
	public FavoriteResponse saveFavorite(Long userId, FavoriteRequest request) {
		Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 정보에 오류가 있습니다."));
		List<Station> stations = stationRepository.findAllByIdIn(new ArrayList<>(Arrays.asList(request.getSource(), request.getTarget())));
		Favorite favorite = new Favorite(stations, member);
		favoriteRepository.save(favorite);
		return new FavoriteResponse(favorite);
	}

	public List<FavoriteResponse> findFavorites(Long userId) {
		Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 정보에 오류가 있습니다."));
		List<Favorite> favorites = favoriteRepository.findByMemberId(member.getId());
		return favorites.stream().map(FavoriteResponse::new).collect(Collectors.toList());
	}

	@Transactional
	public void deleteLineById(Long userId, Long favoriteId) {
		Member member = memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자 정보에 오류가 있습니다."));
		Favorite favorite = favoriteRepository.findById(favoriteId).orElseThrow(() -> new IllegalArgumentException("즐겨찾기 조회 결과가 없습니다."));
		favoriteRepository.delete(favorite);
	}
}
