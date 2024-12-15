from fastapi import FastAPI
import uvicorn
from .router import (
    ingredients_classifier,
    user,
    predict_obesity,
    user_full_data,
    reminder,
    barcode
)
import tensorflow as tf
import os
from google.cloud import storage

# Lokasi penyimpanan sementara untuk model yang diunduh
local_model_1_path = "app/model_1/obesity_prediction_model.h5"
local_model_2_path = "app/model_2/ingredients_classification_mobileNetV3.h5"

# URL dari model yang ada di Google Cloud Storage
model_1_url = "https://storage.googleapis.com/submissioncapstone/model_1/obesity_prediction_model.h5"
model_2_url = "https://storage.googleapis.com/submissioncapstone/model_2/ingredients_classification_mobileNetV3.h5"

# Fungsi untuk mendownload model dari Google Cloud Storage
def download_model_from_gcs(bucket_name, blob_name, local_path):
    """Fungsi untuk mengunduh model dari Google Cloud Storage jika belum ada di lokal"""
    if not os.path.exists(local_path):
        print(f"Model tidak ditemukan di lokal, mendownload dari GCS ke {local_path}...")
        # Inisialisasi client Google Cloud Storage
        client = storage.Client()
        bucket = client.get_bucket(bucket_name)
        blob = bucket.blob(blob_name)
        blob.download_to_filename(local_path)
        print(f"Model berhasil diunduh ke {local_path}")
    else:
        print(f"Model sudah ada di {local_path}, tidak perlu mengunduh ulang.")

# Pastikan model sudah ada di lokal, jika belum unduh dari Google Cloud Storage
download_model_from_gcs('submissioncapstone', 'model_1/obesity_prediction_model.h5', local_model_1_path)
download_model_from_gcs('submissioncapstone', 'model_2/ingredients_classification_mobileNetV3.h5', local_model_2_path)

# Load model menggunakan TensorFlow
model_1 = tf.keras.models.load_model(local_model_1_path)
model_2 = tf.keras.models.load_model(local_model_2_path)

# Inisialisasi aplikasi FastAPI
app = FastAPI()

# Menyertakan router dari modul lainnya
app.include_router(ingredients_classifier.router)
app.include_router(user.router)
app.include_router(predict_obesity.router)
app.include_router(user_full_data.router)
app.include_router(barcode.router)
app.include_router(reminder.router)

# Endpoint untuk tes server
@app.get("/")
def root():
    return {"message": "EatRight API is running successfully!"}

# Menentukan port dari variabel lingkungan PORT
port = int(os.getenv("PORT", 8080))

if __name__ == "__main__":
    uvicorn.run(app, host="0.0.0.0", port=8080)
