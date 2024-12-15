from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from app.database import get_db
from app.models import User, Lifestyle
from app.schemas import LifestyleData
from tensorflow.keras.models import load_model
import pickle
import pandas as pd
import numpy as np

router = APIRouter(prefix="/predict_obesity", tags=["Predict Obesity"])

# Path to model and preprocessing artifacts
model_path = "app/model_1/obesity_prediction_model.h5"
label_encoders_path = "app/model_1/label_encoders.pkl"
scalar_path = "app/model_1/scaler.pkl"

# Load the model, label encoders, and scaler
model = load_model(model_path)

with open(label_encoders_path, "rb") as f:
    label_encoders = pickle.load(f)

with open(scalar_path, "rb") as f:
    scaler = pickle.load(f)

# Preprocessing function
def preprocess_data(data: LifestyleData):
    df = pd.DataFrame([data.dict()])
    categorical_columns = [
        "Gender", "CALC", "FAVC", "SCC", "SMOKE",
        "family_history_with_overweight", "CAEC", "MTRANS"
    ]
    for col in categorical_columns:
        if col in label_encoders:  # Ensure the column is in label_encoders
            df[col] = label_encoders[col].transform(df[col])
    continuous_columns = ["Age", "Height", "Weight", "FCVC", "NCP", "CH2O", "FAF", "TUE"]
    df[continuous_columns] = scaler.transform(df[continuous_columns])
    return df

# Prediction function
def make_prediction(data: pd.DataFrame):
    prediction = model.predict(data)
    return int(np.argmax(prediction, axis=1)[0])

# Endpoint for making prediction and saving the result
@router.post("/{user_id}")
async def predict_lifestyle(user_id: int, data: LifestyleData, db: Session = Depends(get_db)):
    # Check if user exists
    user = db.query(User).filter(User.id == user_id).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    # Preprocess the input data
    processed_data = preprocess_data(data)

    # Make the prediction
    predicted_class_index = make_prediction(processed_data)
    label_mapping = {
        0: "Insufficient_Weight",
        1: "Normal_Weight",
        2: "Overweight_Level_I",
        3: "Overweight_Level_II",
        4: "Obesity_Type_I",
        5: "Obesity_Type_II",
        6: "Obesity_Type_III"
    }
    predicted_class = label_mapping[predicted_class_index]

    # Save the lifestyle data and prediction in the database
    lifestyle_entry = Lifestyle(
        user_id=user_id,
        Age=data.Age,
        Gender=data.Gender,
        Height=data.Height,
        Weight=data.Weight,
        CALC=data.CALC,
        FAVC=data.FAVC,
        FCVC=data.FCVC,
        NCP=data.NCP,
        SCC=data.SCC,
        SMOKE=data.SMOKE,
        CH2O=data.CH2O,
        family_history_with_overweight=data.family_history_with_overweight,
        FAF=data.FAF,
        TUE=data.TUE,
        CAEC=data.CAEC,
        MTRANS=data.MTRANS,
        prediction=predicted_class
    )
    db.add(lifestyle_entry)
    db.commit()
    db.refresh(lifestyle_entry)

    return {"user_id": user_id, "prediction": predicted_class, "message": "Prediction saved successfully!"}