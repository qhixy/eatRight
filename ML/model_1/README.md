# Obesity Level Prediction Model

This repository contains a machine learning model that predicts obesity levels based on personal attributes, lifestyle, and dietary habits. The model is built using TensorFlow/Keras and is designed to be deployed as an API for real-time predictions.

---

## **Features**

The dataset includes the following features:
- **Gender**: Categorical (Male, Female)
- **Age**: Continuous
- **Height**: Continuous
- **Weight**: Continuous
- **Family History with Overweight**: Binary
- **High-Calorie Food Consumption**: Binary
- **Vegetable Consumption Frequency**: Integer
- **Number of Meals per Day**: Continuous
- **Food Between Meals**: Categorical
- **Smoking Habit**: Binary
- **Water Consumption (Liters/Day)**: Continuous
- **Calorie Monitoring**: Binary
- **Physical Activity Frequency**: Continuous
- **Technology Usage Time (Hours/Day)**: Integer
- **Alcohol Consumption Frequency**: Categorical
- **Transportation Mode**: Categorical

The target variable is the **Obesity Level**, categorized into various levels such as:
- Normal Weight
- Overweight Level I
- Overweight Level II
- Obesity Types I, II, III

---

## **Model Architecture**

- Input Layer: 16 features (preprocessed and scaled)
- Hidden Layers:
  - Dense (64 units, ReLU activation, Batch Normalization, Dropout)
  - Dense (128 units, ReLU activation, Batch Normalization, Dropout)
  - Dense (64 units, ReLU activation, Batch Normalization, Dropout)
- Output Layer:
  - Dense (Number of obesity classes, Softmax activation)
- Loss Function: Categorical Crossentropy
- Optimizer: Adam
- Metrics: Accuracy

---

## **Results**

- **Test Accuracy**: 95.89%
- **Test Loss**: 0.23

The model performs well with a high classification accuracy on the test dataset.

---

## **How to Run**

### 1. **Clone the Repository**
```bash
git clone https://github.com/Faisal12104/eatRight
cd eatRight/model_1
```

### 2. **Install Dependencies**
Ensure Python 3.9+ is installed, and then install the required Python packages:
```bash
pip install -r requirements.txt
```

### 3. **Run the Flask API**
Start the API server:
```bash
OC_deploys.py
```

### 4. **Test the API**
Use `curl` or a tool like Postman to send POST requests to the API:
```bash
curl -X POST -H "Content-Type: application/json" -d '{
    "Gender": "Male",
    "Age": 23,
    "Height": 1.8,
    "Weight": 77,
    "family_history_with_overweight": "yes",
    "FAVC": "no",
    "FCVC": 2,
    "NCP": 3,
    "CAEC": "Sometimes",
    "SMOKE": "no",
    "CH2O": 2,
    "SCC": "no",
    "FAF": 2,
    "TUE": 1,
    "CALC": "Sometimes",
    "MTRANS": "Public_Transportation"
}' http://127.0.0.1:5000/predict
```
Output Example: The API returns:
```bash
{
    "prediction": "Normal_Weight"
}

```

The response will include the predicted obesity level.
