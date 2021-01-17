package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	public FavoriteResponse saveFavorite(Long userId, FavoriteRequest request) {
		Member member =  memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("개인정보에 오류가 있습니다."));
		List<Station> stations = stationRepository.findAllByIdIn(new ArrayList<>(Arrays.asList(request.getSource(), request.getTarget())));
		Favorite favorite = new Favorite(stations, member);
		favoriteRepository.save(favorite);
		return new FavoriteResponse(favorite);
	}

	public List<FavoriteResponse> findFavorites(Long userId) {
		return null;
	}

	public void deleteLineById(Long userId, Long favoriteId) {


	}
}
