# Testing API

**Backend:** https://backend-dot-e-laborate-project.et.r.appspot.com/

**ML Model:** https://elaborate-ml-fexdiufx7a-et.a.run.app/

*****

## User Authentication, Authorization, and Validation API

-   ### Sign Up (POST)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/signup`

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
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/signin`

    Request Body:

    ```
    {
        "email": "",
        "password": ""
    }
    ```

-   ### Sign Out (POST)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/signout`

    RequestBody:

    ```
    {
        "token": ""
    }
    ```

-   ### Forgot Password (POST)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/forgot-password`

    Request Body:

    ```
    {
        "email": ""
    }
    ```

-   ### Verify Code (POST)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/verify-code`

    Request Body:

    ```
    {
        "email": "",
        "verificationCode": ""
    }
    ```

-   ### Reset Password (POST)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/reset-password`

    Request Body:

    ```
    {
        "resetToken": "",
        "newPassword": ""
    }
    ```

## User Data API

-   ### Home (GET)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/{userId}`

-   ### Profile (GET)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/{userId}/profile`

-   ### Edit Profile (POST)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/{userId}/profile/edit`

    Request Body:

    ```
    {
        "username": "",
        "email": "",
        "phone": "",
        "address": ""
    }
    ```

## Diagnosis API

-   ### Diagnosis Form (POST)
    Endpoint: `https://elaborate-ml-fexdiufx7a-et.a.run.app/predict`

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
    Endpoint: `https://elaborate-ml-fexdiufx7a-et.a.run.app/diagnose/{diagnosis_id}`

## Doctor API

-   ### Adding Doctors (POST) --> Private API
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/private/{privateKey}/add-doctor`

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
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/{userId}/doctor-list`

## Workout API

-   ### Adding Workouts (POST)  -->  Private API
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/private/{privateKey}/add-workout`

    Request Body:

    ```
    {
        "title": "",
        "videoLink": ""
    }
    ```

-   ### Workout List (GET)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/{userId}/workout-list`

## Medicine API

-   ### Adding Medicines (POST) --> Private API
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/private/{privateKey}/add-medicine`

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
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/{userId}/medicine-list`

## Cart API

-   ### Cart (POST)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/{userId}/add-to-cart/{medicineId}`

-   ### Cart (GET)
    Endpoint: `https://backend-dot-e-laborate-project.et.r.appspot.com/{userId}/cart`