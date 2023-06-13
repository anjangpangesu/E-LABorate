# Testing API

**Backend Base URL:** https://backend-dot-e-laborate-project.et.r.appspot.com/

**ML Model Base URL:** https://elaborate-ml-fexdiufx7a-et.a.run.app/

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

## Diagnosis API (Using ML Model Base URL)

-   ### Diagnosis Form (POST)
    Endpoint: `/predict`

    Request Body:

    > Do not change the "units" value.

    ```
    {
        "age": ,
        "sex": ,
        "rbc": ,
        "hgb": ,
        "hct": ,
        "mcv": ,
        "mch": ,
        "mchc": ,
        "rdw_cv": ,
        "wbc": ,
        "neu": ,
        "lym": ,
        "mo": ,
        "eos": ,
        "ba": 
    }
    ```

-   ### Diagnosis Results (GET)
    Endpoint: `/diagnose/{diagnosis_id}`

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
        "videoLink": ""
    }
    ```

-   ### Workout List (GET)
    Endpoint: `/{userId}/workout-list`

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

