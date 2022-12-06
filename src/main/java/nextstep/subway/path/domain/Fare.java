package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

public class Fare {

    private static final int BASE_PRICE = 1250;
    private static final int DISCOUNT_BASE_PRICE = 350;
    private static final int BASE_POINT = 10;
    private static final int MIDDLE_POINT = 50;
    private static final int MIDDLE_CHARGE_DISTANCE = 5;
    private static final int LAST_CHARGE_DISTANCE = 8;
    private static final int ADDITIONAL_CHARGE = 100;


    private int value;

    protected Fare(){}

    private Fare(int distance, int surcharge, Discount discount){
        addByDistance(distance);
        this.value+=surcharge;
        getDiscount(discount);
    }
    private Fare(int distance, int surcharge){
        addByDistance(distance);
        this.value+=surcharge;
    }

    public int value() {
        return value;
    }

    public static Fare of(Distance distance, int surCharge){
        return new Fare(distance.value(), surCharge);
    }
    public static Fare ofDiscount(Distance distance, int surCharge, Discount discount){
        return new Fare(distance.value(), surCharge, discount);
    }
    private void addByDistance(int distance){
        if(distance<=BASE_POINT){
            this.value+=BASE_PRICE;
            return;
        }
        if(BASE_POINT<distance && distance<=MIDDLE_POINT){
            this.value+=BASE_PRICE+additionalChargeInMiddleSection(distance);
            return;
        }
        if(MIDDLE_POINT<distance){
            this.value+=BASE_PRICE+additionalChargeIFinalSection(distance);
        }
    }

    private int additionalChargeInMiddleSection(int distance){
        return (int)(distance-BASE_POINT)/MIDDLE_CHARGE_DISTANCE*ADDITIONAL_CHARGE;
    }
    private int additionalChargeIFinalSection(int distance){
        return (MIDDLE_POINT-BASE_POINT)/MIDDLE_CHARGE_DISTANCE*ADDITIONAL_CHARGE
                + (int)(distance-MIDDLE_POINT)/LAST_CHARGE_DISTANCE*ADDITIONAL_CHARGE;
    }
    private void getDiscount(Discount discount){
        if(!discount.equals(Discount.NO_DISCOUNT)){
            this.value-=DISCOUNT_BASE_PRICE;
            this.value*=(1-discount.getRate());
        }
    }
}
