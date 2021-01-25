package nextstep.subway.favorite.application;

import org.springframework.stereotype.Service;

import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.StationResponse;

@Service
public class FavoriteService {

	private final StationService stationService;
	private final MemberService memberService;

	public FavoriteService(final StationService stationService, final MemberService memberService) {
		this.stationService = stationService;
		this.memberService = memberService;
	}

	public FavoriteResponse save(final Long id, final FavoriteRequest favoriteRequest) {
		Member member = memberService.findById(id);
		return new FavoriteResponse(
			1L,
			StationResponse.of(stationService.findById(favoriteRequest.getSource())),
			StationResponse.of(stationService.findById(favoriteRequest.getTarget()))
		);
	}
}
