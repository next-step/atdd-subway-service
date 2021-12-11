package nextstep.subway.favorite.application;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;
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
import java.util.stream.Collectors;

@Service
@Transactional
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StationRepository stationRepository;


    public FavoriteService(FavoriteRepository favoriteRepository, MemberRepository memberRepository, StationRepository stationRepository) {
        this.favoriteRepository = favoriteRepository;
        this.memberRepository = memberRepository;
        this.stationRepository = stationRepository;
    }

    public Long save(Long loginMemberId, FavoriteRequest favoriteRequest) {
        Member member = findMember(loginMemberId);

        Station source = stationRepository.findById(favoriteRequest.getSourceId())
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION));

        Station target = stationRepository.findById(favoriteRequest.getTargetId())
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_STATION));

        Favorite savedFavorite = favoriteRepository.save(Favorite.of(member, source, target));
        return savedFavorite.getId();
    }

    private Member findMember(Long loginMemberId) {
        return memberRepository.findById(loginMemberId)
                .orElseThrow(() -> new InputDataErrorException(InputDataErrorCode.THERE_IS_NOT_SEARCHED_MEMBER));
    }

    @Transactional(readOnly = true)
    public List<FavoriteResponse> findFavorites(Long loginMemberId) {
        Member member = findMember(loginMemberId);
        List<Favorite> allFavorites = favoriteRepository.findAll();
        return allFavorites.stream()
                .filter(it -> it.isSameMember(member))
                .map(it -> FavoriteResponse.of(it))
                .collect(Collectors.toList());
    }

    public void deleteFavorite(Long id) {
        favoriteRepository.deleteById(id);
    }
}
