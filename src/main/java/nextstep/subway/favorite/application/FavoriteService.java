package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.ErrorCode;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.favorite.exception.FavoriteException;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.exception.StationException;

@Transactional(readOnly = true)
@Service
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final StationRepository stationRepository;
	private final MemberRepository memberRepository;

	public FavoriteService(final FavoriteRepository favoriteRepository,
		final StationRepository stationRepository, final MemberRepository memberRepository) {
		this.favoriteRepository = favoriteRepository;
		this.stationRepository = stationRepository;
		this.memberRepository = memberRepository;
	}

	@Transactional
	public Favorite createFavorite(final LoginMember loginMember, final FavoriteRequest favoriteRequest) {
		Station source = stationFindById(favoriteRequest.getSource());
		Station target = stationFindById(favoriteRequest.getTarget());
		Member member = memberFindByLoginMember(loginMember);
		return favoriteRepository.save(Favorite.of(source, target, member));
	}

	private Station stationFindById(final Long stationId) {
		return stationRepository.findById(stationId)
			.orElseThrow(() -> {
				throw new StationException(ErrorCode.NO_SUCH_STATION_ERROR);
			});
	}

	public Member memberFindByLoginMember(final LoginMember loginMember) {
		return memberRepository.getOne(loginMember.getId());
	}

	public List<FavoriteResponse> findAll(final LoginMember loginMember) {
		List<Favorite> favorites = favoriteRepository.findAllByMemberId(loginMember.getId());
		return FavoriteResponse.ofList(favorites);
	}

	public Favorite favoriteFindById(Long favoriteId) {
		return favoriteRepository.findById(favoriteId)
			.orElseThrow(() -> {
				throw new FavoriteException(ErrorCode.NO_SUCH_FAVORITE_ERROR);
			});
	}

	@Transactional
	public void delete(LoginMember loginMember, Long id) {
		Favorite favorite = favoriteFindById(id);
		Member member = memberFindByLoginMember(loginMember);
		favorite.validOwner(member);
		favoriteRepository.delete(favorite);
	}
}
