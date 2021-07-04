package nextstep.subway.favorite.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.favorite.FavoriteSection;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;

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

    public FavoriteResponse createFavoriteSection(final LoginMember loginMember,
        final FavoriteRequest favoriteRequest) {
        final Station source = findValidStation(favoriteRequest.getSourceId());
        final Station target = findValidStation(favoriteRequest.getTargetId());
        final Member member = findValidMember(loginMember);
        final FavoriteSection favoriteSection = new FavoriteSection(member, source, target);

        return FavoriteResponse.of(favoriteRepository.save(favoriteSection));
    }

    private Member findValidMember(final LoginMember loginMember) {
        return memberRepository.findById(loginMember.getId())
            .orElseThrow(IllegalArgumentException::new);
    }

    private Station findValidStation(final long sourceId) {
        return stationRepository.findById(sourceId)
            .orElseThrow(IllegalArgumentException::new);
    }

    public void deleteFavoriteSection(final LoginMember loginMember, final Long id) {
        final Member member = findValidMember(loginMember);
        final FavoriteSection favoriteSection = favoriteSection(id);

        if (favoriteSection.equalsMember(member)) {
            favoriteRepository.deleteById(id);
        }
    }

    private FavoriteSection favoriteSection(final Long id) {
        return favoriteRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public List<FavoriteResponse> findFavoriteSectionsByMember(final LoginMember loginMember) {
        final Member member = findValidMember(loginMember);

        return favoriteRepository.findAll(Example.of(new FavoriteSection(member))).stream()
            .map(FavoriteResponse::of)
            .collect(Collectors.toList());
    }
}
