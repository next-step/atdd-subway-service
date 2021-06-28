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

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Service
public class FavoriteService {
    private MemberRepository memberRepository;
    private StationRepository stationRepository;
    private FavoriteRepository favoriteRepository;

    public FavoriteService(MemberRepository memberRepository, StationRepository stationRepository,
                           FavoriteRepository favoriteRepository) {
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Transactional
    public FavoriteResponse saveFavorite(Long memberId, FavoriteRequest param) {
        Member member = findMemberById(memberId);
        List<Station> stations = stationRepository.findBySourceAndTarget(Arrays.asList(param.getSource(), param.getTarget()));
        Station sourceStation = stations.stream()
                .filter(station -> station.getId().equals(param.getSource()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        Station targetStation = stations.stream()
                .filter(station -> station.getId().equals(param.getTarget()))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);
        Favorite persistFavorite = favoriteRepository.save(Favorite.of(member, sourceStation, targetStation));
        return FavoriteResponse.from(persistFavorite);
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findAllFavorites(Long memberId) {
        Member member = findMemberById(memberId);
        List<Favorite> favorites = favoriteRepository.findAllWithStationByMember(member);
        return FavoriteResponse.fromToCollection(favorites);
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        Member member = findMemberById(memberId);
        Favorite favorite = findFavoriteById(favoriteId);
        favorite.checkSameMember(member);
        favoriteRepository.delete(favorite);
    }

    private Favorite findFavoriteById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId).orElseThrow(EntityNotFoundException::new);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
    }
}
