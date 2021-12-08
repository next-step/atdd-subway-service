package nextstep.subway.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.member.domain.*;
import nextstep.subway.member.dto.FavoriteRequest;
import nextstep.subway.member.dto.FavoriteResponse;
import nextstep.subway.member.dto.MemberRequest;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.member.exception.FavoriteNotFoundException;
import nextstep.subway.member.exception.MemberNotFoundException;
import nextstep.subway.path.application.PathService;
import nextstep.subway.path.domain.Path;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {
    private final PathService pathService;
    private final MemberRepository memberRepository;
    private final FavoriteRepository favoriteRepository;

    @Transactional
    public FavoriteResponse addFavorite(Long id, FavoriteRequest request) {
        Member member = findMemberById(id);
        Favorite favorite = getFavorite(request).by(member);

        //FIXME 1) Favorite 기준으로 저장하기.
        favoriteRepository.save(favorite);

        //FIXME 2) Member 기준으로 저장하기. 영속성 전이 때문에 Favorite의 Key를 Presentation 계층에 전달하기 위해서 flush()가 필요함.
        //member.addFavorite(favorite);
        //memberRepository.flush();
        return FavoriteResponse.of(favorite);
    }

    @Transactional
    public void deleteFavorite(Long memberId, Long favoriteId) {
        favoriteRepository.delete(
                favoriteRepository.findById(favoriteId)
                        .orElseThrow(FavoriteNotFoundException::new)
        );

        //FIXME member 기준으로 삭제하기
        //Member member = findMemberById(memberId);
        //member.removeFavorite(favoriteId);
    }

    private Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new);
    }

    private Favorite getFavorite(FavoriteRequest request) {
        Path path = pathService.getShortestPath(request);
        return Favorite.of(path);
    }

    public List<FavoriteResponse> findFavorites(Long id) {
        return FavoriteResponse.ofList(favoriteRepository.findByMemberId(id));
    }

    //FIXME member 기준으로 즐겨찾기를 조회하기
//    public List<FavoriteResponse> findFavorites(Long id) {
//        Member member = findMemberById(id);
//        return FavoriteResponse.ofList(member.getFavorites());
//    }
}
