# Testing API

**Local:** http://localhost:8080/

**Online:** https://backend-dot-e-laborate-project.et.r.appspot.com/

*****

## User Authentication, Authorization, and Validation API

-   ### Sign Up (POST)
    Endpoint: `/signup`

    Request Body:

      ```
      {
          "username": "",
          "email": "",
          "password": "",
          "confirmPassword": ""
      }
      ``` 

-   ### Sign In (POST)
    Endpoint: `/signin`

    Request Body:

    ```
    {
        "email": "",
        "password": ""
    }
    ```

-   ### Sign Out (POST)
    Endpoint: `/signout`

    RequestBody:

    ```
    {
        "token": ""
    }
    ```

-   ### Forgot Password (POST)
    Endpoint: `/forgot-password`

    Request Body:

    ```
    {
        "email": ""
    }
    ```

-   ### Verify Code (POST)
    Endpoint: `/verify-code`

    Request Body:

    ```
    {
        "email": "",
        "verificationCode": ""
    }
    ```

-   ### Reset Password (POST)
    Endpoint: `/reset-password`

    Request Body:

    ```
    {
        "resetToken": "",
        "newPassword": ""
    }
    ```

## User Data API

-   ### Home (GET)
    Endpoint: `/{userId}`

-   ### Profile (GET)
    Endpoint: `/{userId}/profile`

-   ### Edit Profile (POST)
    Endpoint: `/{userId}/profile/edit`

    Request Body:

    ```
    {
        "username": "",
        "email": "",
        "phone": "",
        "address": ""
    }
    ```

## Diagnostic API

-   ### Diagnostic Form (POST)  -->  On Progress
    Endpoint: `/{userId}/diagnose`

    Request Body:

    > Do not change the "units" value.

    ```
    {
        "age": ,
        "sex": ,
        "rbc": {
            "value": ,
            "units": "10¹² cells/L"
        },
        "hgb": {
            "value": ,
            "units": "g/dl"
        },
        "hct": {
            "value": ,
            "units": "%"
        },
        "mcv": {
            "value": ,
            "units": "fL"
        },
        "mch": {
            "value": ,
            "units": "pg"
        },
        "mchc": {
            "value": ,
            "units": "g/dL"
        },
        "rdw_cv": {
            "value": ,
            "units": "%"
        },
        "wbc": {
            "value": ,
            "units": "10³ cells/cmm"
        },
        "neu": {
            "value": ,
            "units": "%"
        },
        "lym": {
            "value": ,
            "units": "%"
        },
        "mo": {
            "value": ,
            "units": "%"
        },
        "eos": {
            "value": ,
            "units": "%"
        },
        "ba": {
            "value": ,
            "units": "%"
        }
    }
    ```

-   ### Diagnostic Results (GET)  -->  On Progress
    Endpoint: `/{userId}/diagnose?results={diagnosisId}`

## Medicine API

-   ### Adding Medicines (POST) --> Private API
    Endpoint: `/private/{privateKey}/add-medicine`

    Request Body:

    ```
    {
        "name": "",
        "description": ,
        "price": ,
        "stock":
    }
    ```

-   ### Medicine List (GET)
    Endpoint: `/{userId}/medicine-list`

## Cart API

-   ### Cart (POST)
    Endpoint: `/{userId}/add-to-cart/{medicineId}`

-   ### Cart (GET)
    Endpoint: `/{userId}/cart`

## Doctor API

-   ### Adding Doctors (POST) --> Private API
    Endpoint: `/private/{privateKey}/add-doctor`

    Request Body:

    ```
    {
        "name": "",
        "age": ,
        "gender": "",
        "specialty": "",
        "workplace": ["", ""],
        "experiences": 
    }
    ```

-   ### Doctor List (GET)
    Endpoint: `/{userId}/doctor-list`

## Workout API

-   ### Adding Workouts (POST)  -->  Private API
    Endpoint: `/private/{privateKey}/add-workout`

    Request Body:

    ```
    {
        "title": "",
        "video_link": ""
    }
    ```

-   ### Workout List (GET)
    Endpoint: `/{userId}/workout-list`
