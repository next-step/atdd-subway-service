package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.NoDataException;
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

	public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
		this.memberRepository = memberRepository;
	}

	public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest favoriteRequest) {
		Station source = stationRepository.findById(favoriteRequest.getSource())
			.orElseThrow(() -> new NoDataException());
		Station target = stationRepository.findById(favoriteRequest.getTarget())
			.orElseThrow(() -> new NoDataException());
		Member member = memberRepository.findById(loginMember.getId())
			.orElseThrow(() -> new NoDataException());

		Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> findAllFavorites(LoginMember loginMember) {
		Member member= memberRepository.findById(loginMember.getId())
			.orElseThrow(()-> new AuthorizationException("멤버를 찾을 수 없습니다."));
		List<Favorite> favorites = favoriteRepository.findAllByMember(member);

		return favorites.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	public void deleteFavoriteById(Long id) {
		favoriteRepository.deleteById(id);
	}
}
