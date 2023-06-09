# Bangkit 2023 Capstone Project [C23-PS270] : e-LABorate

<img width="695" alt="logo" src="https://github.com/anjangpangesu/E-LABorate/assets/63623255/6d4275de-8265-41ef-938e-9b7447fbae3f">

Hello everyone!. Here is our repository for Bangkit 2023 Capstone project. Our team consist of 2 Machine Learning, 2 Android, and 2 Cloud Computing.

### Team
Here is the list of members of the C23-PS270 team.
|            Name           | Bangkit ID  |   Learning Path    |                        LInkedIn                        |
|---------------------------|-------------|--------------------|--------------------------------------------------------|
| Andrea Natania            | M321DSY0091 | Machine Learning   | https://www.linkedin.com/in/andrea-natania/            |
| Nathania Gunawan          | M321DSY1559 | Machine Learning   | https://www.linkedin.com/in/nathania-gunawan/          |
| Alif Maulidanar           | C168DSX4895 | Cloud Computing    | https://www.linkedin.com/in/alifmaulidanar/            | 
| Anjang Pangestu Selokaton | C168DKX3963 | Cloud Computing    | https://www.linkedin.com/in/anjang-pangestu-selokaton/ |
| Efri Vahmi                | A011DSX2294 | Mobile Development | https://www.linkedin.com/in/efrivahmi/                 |
| Vinsensius Adrian Wijaya  | A168DKX4387 | Mobile Development | https://www.linkedin.com/in/xm21/                      |

### Abstract
Before the pandemic, people would go to the laboratory to carry out various tests ranging from blood, cholesterol, antibody tests and numerous other tests. Usually, patients would wait for hours or even several days for their test results and they have to get their test results at the lab. After receiving their test results, they will visit health institutions such as hospitals or clinics to for diagnosis from the laboratory test result. 

However, during COVID-19 pandemic, the amount of laboratory test demand increased and caused people to line up for hours to get their tests done. This might have caused discomfort for some people because of fear and concern of getting infected at the laboratory. To solve this problem, we came up with an idea to create an e-LABorate application. So this app is created to help people lessen their time at the lab, decreasing COVID-19 infection rate. Moreover, people can book lab sessions and their test results instantly on their phones. This app also provides nearest doctor recommendations on the app which could give patients their diagnosis based on the lab results. Another feature from this app is that patients can buy medicine based on the given prescription.

### Project Schedule
The following is a project schedule for making the e-LABorate application.

<img width="695" alt="project timeline" src="https://github.com/anjangpangesu/E-LABorate/assets/63623255/fe2fda23-7b67-4ff3-81f4-1de1a37307db">

### Cloud Architecture Design
We designed a cloud architecture that will be used as a reference for developing the backend side of the application.

<img width="695" alt="cloud architecture design" src="https://github.com/anjangpangesu/E-LABorate/assets/71235904/27c9ee6a-fcf4-4828-8a13-e4626f551b8a">


### Tools
We use multiple tools in the development of the e-LABorate application.
|            Name           |                               Implementation of                             |                   Link                 |
|---------------------------|-----------------------------------------------------------------------------|----------------------------------------|
| Figma                     | Create low-fidelity and high-fidelity designs of the e-LABorate application | https://www.figma.com/                 |
| Visual Studio Code        | Text Editor                                                                 | https://code.visualstudio.com/download |
| Android Studio            | Integrated Development Environment (IDE) for Android app development        | https://developer.android.com/studio   |
| Google Cloud Platform     | Deploy Backend Application                                                  | https://cloud.google.com/?hl=id        |
| Postman                   | Testing Backend API                                                         | https://www.postman.com/downloads/     |
| GitHub                    | To store and manage code, as well as document and control its changes       | https://github.com/                    |
| Notion                    | To activity management of each member                                       | https://www.notion.so/                 |
| Jupyter Notebook          | To model the data                                                           | https://jupyter.org/                   |

### Languages
We use two programming languages to develop the backend and one for the mobile development of the e-LABorate application.
|            Name           |        Implementation of      |                      Documentation                       |
|---------------------------|------------------------------ |----------------------------------------------------------|
| JavaScript (Node.js)      | Create All API, ex: User API< | https://developer.mozilla.org/en-US/docs/Web/JavaScript  |
|                                                          || https://nodejs.org/en/docs                               |
| Python                    | Create Diagnosis API          | https://docs.python.org/3.9/                             |
| Kotlin                    | Mobile app development        | https://kotlinlang.org/docs/home.html                    |

### Frameworks
We also use a couple of frameworks that help in the development stage.
|            Name           |                       Implementation of                     |              Documentation             |
|---------------------------|-------------------------------------------------------------|----------------------------------------|
| Express.js                | We use Express.js for backend and API development           | https://expressjs.com/en/5x/api.html   |
| FastAPI                   | we use FastAPI for the machine learning model development   | https://fastapi.tiangolo.com/tutorial/ |

### Library
Here are a brief overview of some libraries in the development of the e-LABorate application.

A. Cloud Cumputing

    Node.js:
    -   bcrypt: Encrypt and compare user passwords.
    -   crypto: To perform hash, encryption, and decryption.
    -   dotenv: This package is useful for storing other configurations of the application.
    -   firebase-admin: Provides access to the Firebase services from the Node.js server side.
    -   joi: Helps in validating user input data.
    -   jsonwebtoken: It is utilized to secure the API and store user information in a verifiable token.
    -   nodemailer: To send an email from Node.js.
    -   winston: It helps to log application activities and assists in monitoring and diagnosing the application.
    
    FastAPI:
    -  numpy: To perform numerical computation.
    -  pydantic: Defines the data model with automatic validation, parsing, and data transformation.
    -  firebase-admin: Provides access to the Firebase services from the Python server side.

B. Machine Learning

    TensorFlow:
    -  Sequential: specify a neural network, precisely, sequential: from input to output, passing through a series of neural layers, one after the other
    -  Dense: a dense layer
    -  activation: sets the element-wise activation function to be used in the dense layer. 
    -  input_shape: determine the input shape to be used in the sequence
    -  kernel_regularizer: apply penalties on layer parameters or layer activity during optimization.
    -  Dropout: randomly sets input units to 0 with a frequency of rate at each step during training time
    -  compile: configures the model for training
    -  fit: fit the model
    -  evaluate: evaluate the model
    -  TFLiteConverter: convert the model to TFLite format
    
    Pandas:
    -  read_csv: To read excel csv format dataset   
    -  drop: To drop not needed columns
    -  unique: To see unique values in a column
    
    scikit-learn:
    -  StandardScaler: removes the mean and scales each feature/variable to unit variance
    -  fit_transform: fit the data into a model and transform it into a form that is more suitable for the model in a single step
    -  to_categorical: converts a class vector (integers) to binary class matrix
    -  train_test_split: split data into a training set and a testing set

C. Mobile Development

    Data Store:
    -   Save Session User: Used to save user data and share it across different activities or components in the application. It helps in maintaining user session information and providing easy access to user-related data. 
    
    Live Data & View Model :
    -   LiveData: Used to hold and observe data that can be observed for changes. It's typically used in combination with ViewModel to provide reactive and lifecycle-aware data updates to the UI components.
    -   ViewModel: Used to manage and store UI-related data. It holds the data required by the UI components and survives configuration changes, such as screen rotations. ViewModels provide a separation between the UI and data handling logic.
    
    Retrofit2 & Okhttp3 :
    -   Retrofit2: A popular library used for making network requests and handling API interactions. It simplifies the process of sending HTTP requests, serializing/deserializing data, and processing API responses.
    -   OkHttp3: An HTTP client library used as the underlying network layer for Retrofit. OkHttp3 provides features such as  request/response interception, caching, and efficient connection pooling.
    
    View Binding & Glide :
    -   View Binding: An Android feature that allows for more efficient and type-safe access to views in XML layouts. It generates a binding class for each XML layout, which eliminates the need for using findViewById and provides direct access to views. 
    -   Glide: An image loading and caching library for Android. It simplifies the process of loading images from various sources (local, remote, or content providers) and handles caching, resizing, and displaying images efficiently.
    
    MVVM & Clean Code: 
    -   MVVM (Model-View-ViewModel): A software architecture pattern that separates the UI (View) from the underlying data (Model) by  introducing a ViewModel layer. It promotes separation of concerns and provides a more maintainable and testable codebase.
    -   Clean Code: A set of principles and practices for writing code that is easy to understand, maintain, and enhance. It focuses on code readability, simplicity, and adhering to software design principles, such as SOLID and DRY. Clean code helps improve code quality, collaboration, and overall software development process.

### Services
The services that we utilize to develop and run this application are listed below.
|            Name           |                  Implementation of                     |
|---------------------------|--------------------------------------------------------|
| Google App Engine         | To deploy Node.js based API and backend services       |
| Google Cloud Run          | To deploy FastAPI based API and machine learning model |
| Google Cloud Storage      | Store application images                               |
| Cloud Firestore           | To store, manage, and access application data.         |

### Application
https://github.com/anjangpangesu/E-LABorate/releases/download/v1.0.0/e_laborate.apk
