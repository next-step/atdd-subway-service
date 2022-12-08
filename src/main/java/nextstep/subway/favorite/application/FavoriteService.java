package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.dto.SourceAndTargetStationDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final StationService stationService;
    private final MemberService memberService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteService(StationService stationService, MemberService memberService, FavoriteRepository favoriteRepository) {
        this.stationService = stationService;
        this.memberService = memberService;
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        SourceAndTargetStationDto stations = stationService.findStationById(request.getSourceId(), request.getTargetId());
        Member member = memberService.findById(loginMember.getId());
        Favorite result = favoriteRepository.save(
                Favorite.of(stations.getSourceStation(), stations.getTargetStation(), member)
        );

        return FavoriteResponse.from(result);
    }

    public List<FavoriteResponse> getFavorite(LoginMember loginMember) {
        MemberResponse member = memberService.findMember(loginMember.getId());
        List<Favorite> favorite = favoriteRepository.findAllByMemberId(member.getId());

        return favorite.stream()
                .map(FavoriteResponse::from)
                .collect(Collectors.toList());
    }
}
