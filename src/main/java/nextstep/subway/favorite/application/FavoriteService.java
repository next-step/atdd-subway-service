package nextstep.subway.favorite.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;

@Transactional
@Service
public class FavoriteService {

	private final FavoriteRepository favoriteRepository;
	private final StationService stationService;
	private final MemberService memberService;

	public FavoriteService(final FavoriteRepository favoriteRepository,
		final StationService stationService, final MemberService memberService) {
		this.favoriteRepository = favoriteRepository;
		this.stationService = stationService;
		this.memberService = memberService;
	}

	public FavoriteResponse save(final Long id, final FavoriteRequest favoriteRequest) {
		Member member = findMemberById(id);
		Station source = stationService.findById(favoriteRequest.getSource());
		Station target = stationService.findById(favoriteRequest.getTarget());

		Favorite favorite = new Favorite(member, source, target);

		favoriteRepository.save(favorite);

		return FavoriteResponse.of(favorite);
	}

	@Transactional(readOnly = true)
	public List<FavoriteResponse> findAll(final Long id) {
		Member member = findMemberById(id);
		return FavoriteResponse.ofList(favoriteRepository.findAllByMember(member));
	}

	public void delete(final Long id, final Long favoriteId) {
		Member member = findMemberById(id);
	}

	@Transactional(readOnly = true)
	public Member findMemberById(final Long id) {
		return memberService.findById(id);
	}
}
