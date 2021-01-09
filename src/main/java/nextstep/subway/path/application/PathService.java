package nextstep.subway.path.application;

import lombok.RequiredArgsConstructor;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.common.Money;
import nextstep.subway.path.domain.*;
import nextstep.subway.path.dto.PathResponse;
import nextstep.subway.path.infra.JgraphtPathFinder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PathService {

    private final PathRepository pathRepository;

    private final PathFinder pathFinder = JgraphtPathFinder.getInstance();

    private final DistanceFee distanceFee;

    private final LineFee lineFee;

    private final MemberDiscount memberDiscount;

    public PathResponse findShortest(final long sourceId, final long targetId) {
        Path shortest = findShortestPath(sourceId, targetId);
        return PathResponse.of(shortest);
    }

    public PathResponse findShortestWithFee(final long sourceId, final long targetId, final LoginMember loginMember) {
        Path shortest = findShortestPath(sourceId, targetId);
        Money fee = settleFee(shortest, loginMember);
        return PathResponse.of(shortest, fee);
    }

    private Path findShortestPath(final long sourceId, final long targetId) {
        PathSections allSections = pathRepository.findAllSections();
        PathStation source = findById(sourceId);
        PathStation target = findById(targetId);
        return pathFinder.findShortest(allSections, source, target);
    }

    private Money settleFee(final Path path, final LoginMember loginMember) {
        Money fee = path.settle(distanceFee, lineFee);
        Money discount = memberDiscount.discount(loginMember, fee);
        return fee.subtract(discount);
    }

    private PathStation findById(final long targetId) {
        return pathRepository.findById(targetId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
