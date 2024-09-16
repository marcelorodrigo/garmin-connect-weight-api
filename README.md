[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=marcelorodrigo_garmin-connect-weight-api&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=marcelorodrigo_garmin-connect-weight-api)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=marcelorodrigo_garmin-connect-weight-api&metric=coverage)](https://sonarcloud.io/summary/new_code?id=marcelorodrigo_garmin-connect-weight-api)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=marcelorodrigo_garmin-connect-weight-api&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=marcelorodrigo_garmin-connect-weight-api)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=marcelorodrigo_garmin-connect-weight-api&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=marcelorodrigo_garmin-connect-weight-api)
[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=marcelorodrigo_garmin-connect-weight-api&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=marcelorodrigo_garmin-connect-weight-api)

# Garmin Connect Weight API

Garmin Connect Weight API is a Java API that allows you to input your body measurements to generate a Garmin `.FIT` file
that can be uploaded to [Garmin Connect](https://connect.garmin.com).

# How to use

The service is available at the following URL:

```
https://garmin-connect-weight-api.onrender.com
```

# API

```
POST /weight
```

This endpoint receives a JSON payload with the following fields:

```json
{
  "weight": 70.0,
  "percentFat": 20.1,
  "percentHydration": 60.3,
  "boneMass": 3.9,
  "muscleMass": 44.7,
  "physiqueRating": 5,
  "metabolicAge": 30,
  "visceralFatRating": 5,
  "userProfileIndex": 0,
  "bmi": 22.0
}
```

- `weight` (required): The weight in kilograms.
- `percentFat` (optional): The fat percentage.
- `percentHydration` (optional): The hydration percentage.
- `boneMass` (optional): The bone mass in kilograms.
- `muscleMass` (optional): The muscle mass in kilograms.
- `physiqueRating` (optional): The physique rating.
- `metabolicAge` (optional): The metabolic age.
- `visceralFatRating` (optional): The visceral fat rating.
- `userProfileIndex` (optional): The user profile index.
- `bmi` (optional): The body mass index.


The response will be a Garmin `.FIT` file that can be uploaded to Garmin Connect.