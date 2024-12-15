import json

from flask import Flask, request, jsonify, render_template
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import load_img, img_to_array
from tensorflow.keras.applications.mobilenet_v3 import preprocess_input
import numpy as np
from io import BytesIO

app = Flask(__name__)

# Load the trained model
model = load_model('ingredients_classification_mobileNetV3.h5')

# Load the class indices
with open("class_indices.json", "r") as json_file:
    class_indices = json.load(json_file)

# Reverse the mapping to get index-to-class mapping
index_to_class = {v: k for k, v in class_indices.items()}

# Define the target size (should match the size used during training)
TARGET_SIZE = (224, 224)

@app.route('/', methods=['GET', 'POST'])
def index():
    if request.method == 'POST':
        return jsonify({'message': 'POST not supported on root endpoint'}), 405
    return render_template('index.html')

@app.route('/predict', methods=['POST'])
def predict():
    print("Predict endpoint hit!")  # Debugging line
    if 'file' not in request.files:
        return jsonify({'message': 'No file uploaded'}), 400

    file = request.files['file']
    if file.filename == '':
        return jsonify({'message': 'No file selected'}), 400

    try:
        img_bytes = BytesIO(file.read())
        img = load_img(img_bytes, target_size=TARGET_SIZE)
        img_array = img_to_array(img)
        img_array = np.expand_dims(img_array, axis=0).astype(np.float32)
        img_array = preprocess_input(img_array)

        predictions = model.predict(img_array)
        predicted_label_index = np.argmax(predictions)
        confidence = predictions[0][predicted_label_index]

        threshold = 0.2
        if confidence < threshold:
            return jsonify({'message': 'Ingredient Not Detected'}), 400

        class_label = index_to_class[predicted_label_index]

        return jsonify({
            'class': class_label,
            'confidence': float(confidence)
        })
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)