package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
public class FavoriteService {
	private FavoriteRepository favoriteRepository;
	private StationRepository stationRepository;
	private MemberRepository memberRepository;

	public FavoriteService(FavoriteRepository favoriteRepository,
		StationRepository stationRepository, MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
		this.memberRepository = memberRepository;
	}

	public FavoriteResponse saveFavorite(FavoriteRequest favoriteRequest, Long memberId) {
		Station sourceStation = stationRepository.findById(favoriteRequest.getSourceId())
			.orElseThrow(() -> new IllegalArgumentException("즐겨찾기를 등록할 수 없는 지하철 역 입니다."));
		Station targetStation = stationRepository.findById(favoriteRequest.getTargetId())
			.orElseThrow(() -> new IllegalArgumentException("즐겨찾기를 등록할 수 없는 지하철 역 입니다."));
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원 입니다."));

		Favorite favorite = favoriteRepository.save(new Favorite(member, sourceStation, targetStation));
		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> findAllFavoritesByMember(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new IllegalArgumentException("등록되지 않은 회원 입니다."));

		return favoriteRepository.findByMember(member).stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}
}
