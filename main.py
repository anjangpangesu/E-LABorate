from fastapi import FastAPI
from pydantic import BaseModel
import numpy as np
import tensorflow as tf
from tensorflow.keras.models import load_model
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

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
    diagnosisId: str
    prediction: int

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
                diagnosis_data = {
                    'diagnosisId': diagnosis.to_dict().get('diagnosisId'),
                    'input_data': diagnosis.to_dict().get('input_data'),
                    'prediction': prediction
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

    # Generate diagnosisId
    diagnosisId = db.collection('diagnosis').document().id
    
    # Create the output data
    output_data = OutputData(diagnosisId=diagnosisId, prediction=class_label)

    # Save the input and output data to Firestore
    data_dict = {
        'diagnosisId': diagnosisId,
        'input_data': data.dict(),
        'output_data': output_data.dict()
    }
    doc_ref = db.collection('diagnosis').document(diagnosisId)
    doc_ref.set(data_dict)
    
    # Update the user document with diagnosisId
    user_ref = db.collection('users').document(userId)
    user_ref.update({'diagnosisId': diagnosisId})

    return output_data
