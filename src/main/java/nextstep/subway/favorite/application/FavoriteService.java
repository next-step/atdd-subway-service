package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.exception.Exceptions;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteCreateRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

@Service
@Transactional(readOnly = true)
public class FavoriteService {
	private final FavoriteRepository favoriteRepository;
	private final StationRepository stationRepository;
	private final MemberRepository memberRepository;

	public FavoriteService(FavoriteRepository favoriteRepository,
		StationRepository stationRepository, MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
		this.memberRepository = memberRepository;
	}

	@Transactional
	public FavoriteResponse createFavorite(LoginMember loginMember, FavoriteCreateRequest favoriteCreateRequest) {
		Station source = findStationById(favoriteCreateRequest.getSource());
		Station target = findStationById(favoriteCreateRequest.getTarget());
		Member member = findMemberById(loginMember.getId());
		Favorite favorite = favoriteRepository.save(new Favorite(source, target, member));
		return FavoriteResponse.of(favorite);
	}

	public List<FavoriteResponse> readFavorites(LoginMember loginMember) {
		List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
		return favorites.stream()
			.map(FavoriteResponse::of)
			.collect(Collectors.toList());
	}

	@Transactional
	public void deleteFavorite(LoginMember loginMember, Long favoriteId) {
		Favorite favorite = favoriteRepository.findByIdAndMember_Id(favoriteId, loginMember.getId())
			.orElseThrow(Exceptions.FAVORITE_NOT_FOUND::getException);
		favoriteRepository.delete(favorite);
	}

	private Station findStationById(Long stationId) {
		return stationRepository.findById(stationId).orElseThrow(Exceptions.STATION_NOT_EXIST::getException);
	}

	private Member findMemberById(Long memberId) {
		return memberRepository.findById(memberId).orElseThrow(Exceptions.MEMBER_NOT_FOUND::getException);
	}
}
