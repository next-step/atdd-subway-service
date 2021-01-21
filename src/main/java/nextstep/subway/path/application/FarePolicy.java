package nextstep.subway.path.application;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.Sections;
import nextstep.subway.station.domain.Station;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum FarePolicy {

    CargesByDistance((policyElements, fare) -> fare + getOverFareByDistance(policyElements.getDistance())
            , farePolicyElements -> farePolicyElements.getDistance() > 0
            , "거리별 추가 요금 정책")
    , CargesByLine((policyElements, fare) -> fare + getMaxLineFare(policyElements.getStations(), policyElements.getPersistLines())
            , farePolicyElements -> farePolicyElements.getLoginMember().getAge() >= 0
            , "노선별 추가 요금 정책")
    , DiscountByAge((policyElements, fare) -> saleForAge(fare, policyElements.getLoginMember().getAge())
            , farePolicyElements -> farePolicyElements.getStations().size() > 0
            , "연령별 요금 할인 정책")
    ;

    public static final int BASE_FARE = 1250;                       // 기본 요금
    public static final int SALE_RATE_FOR_TEENAGER = 20;            // 청소년 할인율
    public static final int SALE_RATE_FOR_CHILDREN = 50;            // 어린이 할인율
    public static final int ADULT_BOUND_AGE = 19;                   // 성인 나이 기준
    public static final int TEENAGER_BOUND_AGE = 13;                // 청소년 나이 기준
    public static final int CHILDREN_BOUND_AGE = 6;                 // 어린이 나이 기준
    public static final int ADDITIONAL_OVER_DISTANCE_BOUND = 50;    // 추가 운임 거리 기준 : 최대 구간
    public static final int OVER_DISTANCE_BOUND = 10;               // 추가 운임 거리 기준 : 중간 구간

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

    /**
     * 최단 경로의 거리 값으로 거리에 따른 추가 운임을 구합니다.
     * @param distance
     * @return
     */
    private static int getOverFareByDistance(int distance) {
        if(isOverFareDistance(distance)) {
            return calculateOverFareByDistance(distance);
        }

        if(isAdditionalOverFareDistance(distance)) {
            return calculateAdditionalOverFareDistance(distance);
        }

        return 0;
    }


    /**
     * 추가 운임 구간인 경우의 추가 운임을 계산합니다.
     * @param distance
     * @return
     */
    private static int calculateAdditionalOverFareDistance(int distance) {
        return calculateOverFareByDistance(FarePolicy.ADDITIONAL_OVER_DISTANCE_BOUND)
                + (int) ((Math.ceil(((distance - FarePolicy.OVER_DISTANCE_BOUND) - 1) / 8) + 1) * 100);
    }

    /**
     * 추가 운임 기본 구간인 경우 추가 운임을 계산합니다.
     * @param distance
     * @return
     */
    private static int calculateOverFareByDistance(int distance) {
        return (int) ((Math.ceil(((distance - FarePolicy.OVER_DISTANCE_BOUND) - 1) / 5) + 1) * 100);
    }

    /**
     * 추가 운임 구간인지 반환합니다.
     * @param distance
     * @return
     */
    private static boolean isOverFareDistance(int distance) {
        return distance > FarePolicy.OVER_DISTANCE_BOUND
                && distance <= FarePolicy.ADDITIONAL_OVER_DISTANCE_BOUND;
    }

    /**
     * 추가 운임 기본 구간인지 반환합니다
     * @param distance
     * @return
     */
    private static boolean isAdditionalOverFareDistance(int distance) {
        return distance > FarePolicy.ADDITIONAL_OVER_DISTANCE_BOUND;
    }


    /**
     * 사용자의 나이를 반환합니다.
     * 단, 사용자의 나이가 비어오는 경우(비로그인시와 같이) 기본 운임을 계산하도록 0을 반환합니다.
     * @param loginMember
     * @return
     */
    private static int getAge(LoginMember loginMember) {
        if(loginMember.getAge() != null) {
            return loginMember.getAge();
        }
        return 0;
    }

    /**
     * 사용자가 할인 받을 수 있는 나이면 주어진 운임에 할인 값을 적용 합니다.
     * @param fare
     * @param age
     * @return
     */
    private static int saleForAge(int fare, int age) {
        if(FarePolicy.isTeenager(age)) {
            return FarePolicy.calculateSaleFare(fare, FarePolicy.SALE_RATE_FOR_TEENAGER);
        }

        if(FarePolicy.isChildren(age)) {
            return FarePolicy.calculateSaleFare(fare, FarePolicy.SALE_RATE_FOR_CHILDREN);
        }

        return fare;
    }

    /**
     * 사용자가 어린이인지 확인 합니다.
     * @param age
     * @return
     */
    private static boolean isChildren(int age) {
        return age >= FarePolicy.CHILDREN_BOUND_AGE && age < FarePolicy.TEENAGER_BOUND_AGE;
    }

    /**
     * 사용자가 청소년인지 확인 합니다.
     * @param age
     * @return
     */
    private static boolean isTeenager(int age) {
        return age >= FarePolicy.TEENAGER_BOUND_AGE && age < FarePolicy.ADULT_BOUND_AGE;
    }

    /**
     * 비율에 따라 할인 값을 적용하여 계산합니다.
     * @param fare
     * @param saleRate
     * @return
     */
    private static int calculateSaleFare(int fare, int saleRate) {
        return (int) (((100 - saleRate) * 0.01) * fare);
    }

    /**
     * 노선의 추가 요금을 구합니다.
     * 단, 여러 노선을 거치는 경우 가장 큰 추가요금을 반환합니다.
     * @param vertexList
     * @return
     */
    private static int getMaxLineFare(List<Station> vertexList, List<Line> persistLines) {
        List<Integer> lineFares = getLineFares(vertaxListToFindSectionInfos(vertexList), persistLines);

        return lineFares.stream().max(Integer::compareTo).orElse(0);
    }

    /**
     * 최단경로에 포함 되어 있는 구간의 노선 추가 요금을 구합니다.
     * @param findSectionInfos
     * @param persistLines
     * @return
     */
    private static List<Integer> getLineFares(List<Map.Entry<Station, Station>> findSectionInfos, List<Line> persistLines) {
        List<Integer> lineFares = new ArrayList<>();
        for (Line line : persistLines) {
            FarePolicy.addLineFareFromSections(findSectionInfos, lineFares, line);
        }
        return lineFares;
    }

    /**
     * 최단경로를 찾는 구간의 상행역, 하행역 정보로 바꿉니다.
     * @param vertexList
     * @return
     */
    private static List<Map.Entry<Station, Station>> vertaxListToFindSectionInfos(List<Station> vertexList) {
        List<Map.Entry<Station, Station>> findSections = new ArrayList<>();

        vertexList.stream().reduce((upStation, downStation)
                -> { findSections.add(new AbstractMap.SimpleEntry<Station, Station>(upStation, downStation));
            return downStation;
        });

        return findSections;
    }

    /**
     * 노선의 구간에 주어진 구간이 있으면, 해당 노선의 추가 운임을 목록에 추가합니다.
     * @param findSectionInfos
     * @param lineFares
     * @param line
     */
    private static void addLineFareFromSections(List<Map.Entry<Station, Station>> findSectionInfos
            , List<Integer> lineFares, Line line) {
        Sections sections = new Sections(line.getSections());

        findSectionInfos.stream().filter(findSectionInfo
                -> sections.existSection(findSectionInfo.getKey(), findSectionInfo.getValue()))
                .findAny()
                .ifPresent(yes -> lineFares.add(line.getSurcharge()));
    }

}
