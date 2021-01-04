package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.exceptions.PathFindingException;
import nextstep.subway.path.domain.fee.distanceFee.DistanceFee;
import nextstep.subway.path.domain.fee.distanceFee.DistanceFeeSelector;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPaths;
import nextstep.subway.path.domain.fee.transferFee.TransferLines;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;

import java.math.BigDecimal;
import java.util.List;

public class ShortestPath {
    private final GraphPath shortestPath;

    ShortestPath(GraphPath shortestPath) {
        this.shortestPath = shortestPath;
    }

    static ShortestPath of(WeightedMultigraph path, Long sourceId, Long destinationId) {
        try {
            DijkstraShortestPath shortestPath = new DijkstraShortestPath<>(path);

            return new ShortestPath(shortestPath.getPath(sourceId, destinationId));
        } catch (IllegalArgumentException e) {
            throw new PathFindingException("경로에 없는 역을 탐색 대상으로 지정할 수 없습니다.");
        }
    }

    public List<Long> getPathStations() {
        try {
            return shortestPath.getVertexList();
        } catch (NullPointerException e) {
            throw new PathFindingException("경로가 존재하지 않습니다.");
        }
    }

    public double calculateTotalDistance() {
        try {
            return shortestPath.getWeight();
        } catch (NullPointerException e) {
            throw new PathFindingException("경로가 존재하지 않습니다.");
        }
    }

    public Fee calculateFee(LineOfStationInPaths lineOfStationInPaths, LoginMember loginMember) {
        BigDecimal distanceFee = this.calculateDistanceFee();
        BigDecimal transferFee = this.calculateTransferFee(lineOfStationInPaths);
        BigDecimal totalFee = distanceFee.add(transferFee);

        AgeDiscountPolicy discountPolicy = AgeDiscountPolicy.find(loginMember.getAge());

        return new Fee(totalFee, discountPolicy);
    }

    private BigDecimal calculateDistanceFee() {
        DistanceFee distanceFee = DistanceFeeSelector.select((int) this.calculateTotalDistance());

        return distanceFee.calculate();
    }

    private BigDecimal calculateTransferFee(LineOfStationInPaths lineOfStationInPaths) {
        TransferLines transferLines = lineOfStationInPaths.findTransferLines();

        return transferLines.calculateTransferFee();
    }
}
