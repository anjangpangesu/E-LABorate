from fastapi import FastAPI
from pydantic import BaseModel
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import load_model
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import datetime
import locale

# Initialize Firebase credentials and Firestore
cred = credentials.Certificate("./serviceAccountKey.json")
firebase_admin.initialize_app(cred)
db = firestore.client()

# Define the input data schema
class InputData(BaseModel):
    age: int
    sex: int
    rbc: float
    hgb: float
    hct: float
    mcv: float
    mch: float
    mchc: float
    rdw_cv: float
    wbc: float
    neu: float
    lym: float
    mo: float
    eos: float
    ba: float

# Define the output data schema
class OutputData(BaseModel):
    userId: str
    diagnosisId: str
    test_type: str
    date: str
    prediction: int
    prediction_result: str

# Load the ML model
model = load_model('./elaborate_model.h5')

# Initialize the FastAPI app
app = FastAPI()

# Define the GET endpoint to retrieve data
@app.get("/{userId}/diagnosis/{diagnosisId}")
def get_data(diagnosisId: str, userId: str):
    user_ref = db.collection('users').document(userId)
    user = user_ref.get()
    if user.exists:
        userDiagnosisId = user.to_dict().get('diagnosisId')
        if diagnosisId == userDiagnosisId:
            diagnosis_ref = db.collection('diagnosis').document(diagnosisId)
            diagnosis = diagnosis_ref.get()
            if diagnosis.exists:
                output_data = diagnosis.to_dict().get('output_data')
                prediction = output_data.get('prediction')
                prediction_result = output_data.get('prediction_result')
                input_data_ordered = diagnosis.to_dict().get('input_data')
                input_data = {
                    'age': input_data_ordered.get('age'),
                    'sex': input_data_ordered.get('sex'),
                    'rbc': input_data_ordered.get('rbc'),
                    'hgb': input_data_ordered.get('hgb'),
                    'hct': input_data_ordered.get('hct'),
                    'mcv': input_data_ordered.get('mcv'),
                    'mch': input_data_ordered.get('mch'),
                    'mchc': input_data_ordered.get('mchc'),
                    'rdw_cv': input_data_ordered.get('rdw_cv'),
                    'wbc': input_data_ordered.get('wbc'),
                    'neu': input_data_ordered.get('neu'),
                    'lym': input_data_ordered.get('lym'),
                    'mo': input_data_ordered.get('mo'),
                    'eos': input_data_ordered.get('eos'),
                    'ba': input_data_ordered.get('ba')
                }
                diagnosis_data = {
                    'userData': {
                        'userId': user.to_dict().get('userId'),
                        'username': user.to_dict().get('username'),
                    },
                    'diagnosisData': {
                        'diagnosisId': diagnosis.to_dict().get('diagnosisId'),
                        'test_type': diagnosis.to_dict().get('test_type'),
                        'date': diagnosis.to_dict().get('date'),
                        'input_data': input_data,
                        'output_data': {
                            'prediction': prediction,
                            'prediction_result': prediction_result
                        }
                    }
                }
                return diagnosis_data
            else:
                return {"message": "Data not found"}
        else:
            return {"message": "Data not found"}
    else:
        return {"message": "User not found"}

# Define the GET endpoint for hello message
@app.get("/")
def hello():
    return {"message": "ML Model Success to Deploy"}

# Define the prediction endpoint
@app.post("/{userId}/predict", response_model=OutputData)
def predict(data: InputData, userId: str):
    # Convert input data to a numpy array
    input_array = np.array([[
        data.age, data.sex, data.rbc, data.hgb, data.hct, data.mcv, data.mch,
        data.mchc, data.rdw_cv, data.wbc, data.neu, data.lym, data.mo, data.eos, data.ba
    ]])

    # Perform the prediction
    prediction = model.predict(input_array)

    # Convert the prediction to the corresponding class label
    class_label = np.argmax(prediction, axis=1)[0]
    
    # Define prediction_result based on class_label
    if class_label == 0:
        prediction_result = "Normal health status: no immediate need for medical consultation"
    elif class_label == 1:
        prediction_result = "It is advisable to seek a medical consultation in order to facilitate a comprehensive assessment for further diagnostic purposes"
    elif class_label == 2:
        prediction_result = "Medical consultation recommended for moderate condition"
    elif class_label == 3:
        prediction_result = "Immediate medical consultation required"
    else:
        prediction_result = "Unknown prediction value"

    # Generate diagnosisId
    diagnosisId = db.collection('diagnosis').document().id
    
    test_type = "Blood Test"
    
    # Set locale to Indonesian
    locale.setlocale(locale.LC_TIME, 'id_ID')
    
    # Get today date
    current_date = datetime.date.today()

    # Create date format
    current_date_str = current_date.strftime("%A, %d %B %Y")

    # Create the output data
    output_data = OutputData(
        userId=userId,
        diagnosisId=diagnosisId,
        test_type=test_type,
        date=current_date_str,
        prediction=class_label,
        prediction_result=prediction_result
    )
    
    # Save the input and output data to Firestore
    data_dict = {
        'userId': userId,
        'diagnosisId': diagnosisId,
        'test_type' : test_type,
        'date': current_date_str,
        'input_data': data.dict(),
        'output_data': {
            'prediction': output_data.prediction,
            'prediction_result': output_data.prediction_result
        }
    }
    doc_ref = db.collection('diagnosis').document(diagnosisId)
    doc_ref.set(data_dict)
    
    # Update the user document with diagnosisId
    user_ref = db.collection('users').document(userId)
    user_ref.update({'diagnosisId': diagnosisId})

    return output_data
