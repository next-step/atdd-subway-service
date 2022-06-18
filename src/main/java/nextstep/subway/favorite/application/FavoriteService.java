package nextstep.subway.favorite.application;

import nextstep.subway.favorite.domain.Favorite;
import nextstep.subway.favorite.domain.FavoriteRepository;
import nextstep.subway.favorite.dto.FavoriteRequest;
import nextstep.subway.favorite.dto.FavoriteResponse;
import nextstep.subway.member.domain.Member;
import nextstep.subway.member.domain.MemberRepository;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;

    public FavoriteService(
            FavoriteRepository favoriteRepository, MemberRepository memberRepository,
            StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public FavoriteResponse create(long loginMemberId, FavoriteRequest request) {
        Member member = findMemberById(loginMemberId);
        Station source = findStationById(request.getSourceStationId());
        Station target = findStationById(request.getTargetStationId());
        Favorite persistFavorite = favoriteRepository.save(new Favorite(member, source, target));
        return FavoriteResponse.of(persistFavorite);
    }

    private Station findStationById(long stationId) {
        return stationRepository.findById(stationId).orElseThrow(NoSuchElementException::new);
    }

    private Member findMemberById(long loginMemberId) {
        return memberRepository.findById(loginMemberId).orElseThrow(NoSuchElementException::new);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> list(long loginMemberId) {
        Member member = findMemberById(loginMemberId);
        List<Favorite> list = favoriteRepository.findAllByMember(member);
        return list.stream().map(FavoriteResponse::of).collect(Collectors.toList());
    }

    public void delete(long loginMemberId, long id) {
        Member member = findMemberById(loginMemberId);
        favoriteRepository.deleteByMemberAndId(member, id);
    }
}
