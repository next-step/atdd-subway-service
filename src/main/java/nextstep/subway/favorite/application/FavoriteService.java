package nextstep.subway.favorite.application;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberService memberService;
    private final StationService stationService;

    public FavoriteService(
        final FavoriteRepository favoriteRepository,
        final MemberService memberService,
        final StationService stationService
    ) {
        this.favoriteRepository = favoriteRepository;
        this.memberService = memberService;
        this.stationService = stationService;
    }

    @Transactional
    public FavoriteResponse save(final LoginMember loginMember, final FavoriteRequest request) {
        final Member member = memberService.findMemberById(loginMember.getId());
        final Station source = stationService.getStationById(request.getSourceId());
        final Station target = stationService.getStationById(request.getTargetId());
        final Favorite favorite = favoriteRepository.save(
            new Favorite(member, source, target)
        );
        return FavoriteResponse.of(favorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAll(final LoginMember loginMember) {
        final Member member = memberService.findMemberById(loginMember.getId());
        return favoriteRepository.findAllByMemberId(member.getId())
            .stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(final LoginMember loginMember, final Long id) {
        final Member member = memberService.findMemberById(loginMember.getId());
        final Favorite favorite = favoriteRepository.findByIdAndMember(id, member)
            .orElseThrow(NoSuchElementException::new);
        favoriteRepository.delete(favorite);
    }
}
