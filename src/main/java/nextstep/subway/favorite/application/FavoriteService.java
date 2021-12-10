package nextstep.subway.favorite.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.line.domain.PathFinder;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.line.dto.PathRequest;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final StationRepository stationRepository;
    private final MemberRepository memberRepository;
    private final SectionRepository sectionRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, StationRepository stationRepository, MemberRepository memberRepository, SectionRepository sectionRepository) {
        this.favoriteRepository = favoriteRepository;
        this.stationRepository = stationRepository;
        this.memberRepository = memberRepository;
        this.sectionRepository = sectionRepository;
    }

    public FavoriteResponse save(LoginMember loginMember, PathRequest request) {
        Member member = memberRepository.findByIdElseThrow(loginMember.getId());
        Favorite persistFavorite = favoriteRepository.save(validPathFavorite(member, request));
        return FavoriteResponse.of(persistFavorite);
    }

    private Favorite validPathFavorite(Member member, PathRequest request) {
        Station source = stationRepository.findByIdElseThrow(request.getSource());
        Station target = stationRepository.findByIdElseThrow(request.getTarget());
        Sections sections = new Sections(sectionRepository.findAll());
        PathFinder graph = new PathFinder().enrollPaths(sections);
        graph.findPaths(source, target);
        return new Favorite(member, source, target);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findByMember(LoginMember loginMember) {
        return favoriteRepository.findByMemberId(loginMember.getId())
                .stream()
                .map(FavoriteResponse::of)
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
        favoriteRepository.deleteById(id);
    }
}
