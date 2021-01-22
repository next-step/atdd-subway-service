package nextstep.subway.path.application.farepolicy;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FarePolicy {

    ChargesByDistance((policyElements, fare)
                -> fare + ChargesByDistanceCalculator.getOverFareByDistance(policyElements.getDistance())
            , farePolicyElements -> farePolicyElements.getDistance() > 0
            , "거리별 추가 요금 정책")
    , ChargesByLine((policyElements, fare) -> fare
                + ChargesByLineCalculator.getMaxLineFare(policyElements.getStations(), policyElements.getPersistLines())
            , farePolicyElements -> farePolicyElements.getLoginMember().getAge() >= 0
            , "노선별 추가 요금 정책")
    , DiscountByAge((policyElements, fare)
                -> DiscountByAgeCalculator.saleForAge(fare, policyElements.getLoginMember().getAge())
            , farePolicyElements -> farePolicyElements.getStations().size() > 0
            , "연령별 요금 할인 정책")
    ;

    public static final int BASE_FARE = 1250;                       // 기본 요금

    private BiFunction<FarePolicyElements, Integer, Integer> calculateFunction;
    private Function<FarePolicyElements, Boolean> validateFunction;
    private String description;

    FarePolicy(BiFunction<FarePolicyElements, Integer, Integer> calculateFunction
            , Function<FarePolicyElements, Boolean> validateFunction, String description) {
        this.calculateFunction = calculateFunction;
        this.validateFunction = validateFunction;
        this.description = description;
    }

    private int calculate(FarePolicyElements farePolicyElements, int fare) {
        return this.calculateFunction.apply(farePolicyElements, fare);
    }

    public boolean validatePolicy(FarePolicyElements farePolicyElements) {
        return this.validateFunction.apply(farePolicyElements);
    }

    /**
     * 주어진 정책 요소에 해당하는 정책으로 요금을 계산합니다.
     * @param farePolicyElements 
     * @return 최종 요금
     */
    public static int calculateFareByPolicies(FarePolicyElements farePolicyElements) {
        AtomicInteger fare = new AtomicInteger(FarePolicy.BASE_FARE);
        FarePolicy.getApplicableFarePolicies(farePolicyElements).stream()
                .forEach(farePolicy -> fare.set(farePolicy.calculate(farePolicyElements, fare.get())));
        return fare.get();
    }

    /**
     * 주어진 요소에 적용 되는 요금 정책을 가지고 옵니다.
     * @param farePolicyElements
     * @return 정책 목록
     */
    private static List<FarePolicy> getApplicableFarePolicies(FarePolicyElements farePolicyElements) {
        return Arrays.stream(FarePolicy.values())
                .filter(farePolicy -> farePolicy.validatePolicy(farePolicyElements))
                .collect(Collectors.toList());
    }

}
