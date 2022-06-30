package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.member.application.MemberService;
import nextstep.subway.member.domain.Member;
import nextstep.subway.station.application.StationService;
import nextstep.subway.station.domain.Station;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final SectionRepository sectionRepository;
    private StationService stationService;
    private MemberService memberService;

    public FavoriteService(FavoriteRepository favoriteRepository, SectionRepository sectionRepository, StationService stationService, MemberService memberService) {
        this.favoriteRepository = favoriteRepository;
        this.sectionRepository = sectionRepository;
        this.stationService = stationService;
        this.memberService = memberService;
    }

    public FavoriteResponse saveFavorite(LoginMember loginMember, FavoriteRequest request) {
        Station source = stationService.findStationById(request.getSource());
        Station target = stationService.findStationById(request.getTarget());
        Section section = sectionRepository.findByUpStationAndDownStation(source, target)
                .orElseThrow(() -> new IllegalArgumentException("해당 노선을 찾을 수 없습니다."));
        Member member = memberService.findMember(loginMember.getId());

        Favorite favorite = favoriteRepository.save(Favorite.of(member, section));
        return FavoriteResponse.of(favorite);
    }

    public List<FavoriteResponse> findFavorites(LoginMember loginMember) {
        Member member = memberService.findMember(loginMember.getId());
        List<Favorite> favorites = favoriteRepository.findAllByMember(member);
        return favorites.stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }
}
