package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.adapters.SafeLineAdapter;
import nextstep.subway.path.domain.fee.distanceFee.*;
import nextstep.subway.path.domain.fee.transferFee.LineOfStationInPaths;
import nextstep.subway.path.domain.fee.transferFee.TransferLines;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class FeeCalculatorService {
    private final SafeLineAdapter safeLineAdapter;

    public FeeCalculatorService(SafeLineAdapter safeLineAdapter) {
        this.safeLineAdapter = safeLineAdapter;
    }

    public Fee calculateFee(ShortestPath shortestPath, LoginMember loginMember) {
        BigDecimal distanceFee = calculateDistanceFee(shortestPath);
        BigDecimal transferFee = calculateTransferFee(shortestPath);
        BigDecimal totalFee = distanceFee.add(transferFee);

        AgeDiscountPolicy discountPolicy = AgeDiscountPolicy.find(loginMember.getAge());

        return new Fee(totalFee, discountPolicy);
    }

    BigDecimal calculateDistanceFee(ShortestPath shortestPath) {
        int distance = (int) shortestPath.calculateTotalDistance();
        DistanceFee distanceFee = DistanceFeeSelector.select(distance);

        return distanceFee.calculate();
    }

    BigDecimal calculateTransferFee(ShortestPath shortestPath) {
        List<Long> pathStations = shortestPath.getPathStations();
        LineOfStationInPaths lineOfStationInPaths = safeLineAdapter.getLineOfStationInPaths(pathStations);
        TransferLines transferLines = lineOfStationInPaths.findTransferLines();

        return transferLines.calculateTransferFee();
    }
}
