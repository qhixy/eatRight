from flask import Flask, request, jsonify
import numpy as np
import pandas as pd
from tensorflow.keras.models import load_model
from sklearn.preprocessing import LabelEncoder, StandardScaler

app = Flask(__name__)

model = load_model("obesity_prediction_model.h5")

# Load dataset to set up encoders and scaler (ensure the dataset is the same as used for training)
file_path = r"obesity classification/ObesityDataSet_raw_and_data_sinthetic.csv"
data = pd.read_csv(file_path)

label_encoders = {}
for col in data.select_dtypes(include="object").columns:
    le = LabelEncoder()
    data[col] = le.fit_transform(data[col])
    label_encoders[col] = le

scaler = StandardScaler()
scaled_features = scaler.fit_transform(data.drop(columns=["NObeyesdad"])) 

@app.route('/predict', methods=['POST'])
def predict():
    try:
        input_data = request.json
        
        features = pd.DataFrame([input_data])
        
        expected_columns = data.drop(columns=["NObeyesdad"]).columns
        features = features[expected_columns]
        
        for col, encoder in label_encoders.items():
            if col in features.columns:
                features[col] = encoder.transform(features[col])
        
        features_scaled = scaler.transform(features)
        
        predictions = model.predict(features_scaled)
        predicted_class = np.argmax(predictions, axis=1)
        
        decoded_prediction = label_encoders["NObeyesdad"].inverse_transform(predicted_class)
        
        return jsonify({"prediction": decoded_prediction[0]})
    except Exception as e:
        return jsonify({"error": str(e)})

if __name__ == '__main__':
    app.run(debug=True)
