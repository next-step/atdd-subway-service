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

	private FavoriteRepository favoriteRepository;
	private MemberRepository memberRepository;
	private StationRepository stationRepository;

	public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
		this.favoriteRepository = favoriteRepository;
		this.memberRepository = memberRepository;
		this.stationRepository = stationRepository;
	}

	public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest favoriteRequest) {
		Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
		Station source = stationRepository.findById(Long.parseLong(favoriteRequest.getSource())).orElseThrow(RuntimeException::new);
		Station target = stationRepository.findById(Long.parseLong(favoriteRequest.getTarget())).orElseThrow(RuntimeException::new);
		Favorite favorite = favoriteRepository.save(FavoriteRequest.toFavorite(member, source, target));
		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> findFavorites(Long memberId) {
		Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
		return FavoriteResponse.of(favoriteRepository.findByMember(member));
	}

	public void deleteFavoriteById(Long memberId, Long favoriteId) {
		Member member = memberRepository.findById(memberId).orElseThrow(RuntimeException::new);
		favoriteRepository.deleteByIdAndMember(favoriteId, member);
	}
}
