package com.marcelorodrigo.fit.domain;

import lombok.Builder;

import java.time.Instant;

@Builder
public record WeightMeasurement(float weight, Float percentFat, Float percentHydration, Float boneMass,
                                Float muscleMass, Integer physiqueRating, Integer metabolicAge,
                                Integer visceralFatRating,
                                Integer userProfileIndex, Float bmi, Instant timestamp) {
}