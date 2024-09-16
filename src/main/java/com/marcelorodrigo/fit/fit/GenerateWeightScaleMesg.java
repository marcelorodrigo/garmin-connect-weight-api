package com.marcelorodrigo.fit.fit;

import com.garmin.fit.DateTime;
import com.garmin.fit.WeightScaleMesg;
import com.marcelorodrigo.fit.domain.WeightMeasurement;
import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GenerateWeightScaleMesg {
    public WeightScaleMesg execute(final DateTime timeCreated, final WeightMeasurement weight) {
        val weightScale = new WeightScaleMesg();
        weightScale.setTimestamp(timeCreated);
        weightScale.setWeight(weight.weight());
        if (Objects.nonNull(weight.percentFat())) {
            weightScale.setPercentFat(weight.percentFat());
        }
        if (Objects.nonNull(weight.percentHydration())) {
            weightScale.setPercentHydration(weight.percentHydration());
        }
        if (Objects.nonNull(weight.boneMass())) {
            weightScale.setBoneMass(weight.boneMass());
        }
        if (Objects.nonNull(weight.muscleMass())) {
            weightScale.setMuscleMass(weight.muscleMass());
        }
        if (Objects.nonNull(weight.physiqueRating())) {
            weightScale.setPhysiqueRating(weight.physiqueRating().shortValue());
        }
        if (Objects.nonNull(weight.metabolicAge())) {
            weightScale.setMetabolicAge(weight.metabolicAge().shortValue());
        }
        if (Objects.nonNull(weight.visceralFatRating())) {
            weightScale.setVisceralFatRating(weight.visceralFatRating().shortValue());
        }
        if (Objects.nonNull(weight.userProfileIndex())) {
            weightScale.setUserProfileIndex(weight.userProfileIndex());
        }
        if (Objects.nonNull(weight.bmi())) {
            weightScale.setBmi(weight.bmi());
        }
        return weightScale;
    }
}
