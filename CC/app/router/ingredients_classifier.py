import json
from fastapi import APIRouter, File, UploadFile, HTTPException, Depends
from tensorflow.keras.models import load_model
from tensorflow.keras.preprocessing.image import load_img, img_to_array
from tensorflow.keras.applications.mobilenet_v3 import preprocess_input
from app.database import get_db
import numpy as np
from io import BytesIO
from sqlalchemy.orm import Session
from sqlalchemy import text

# Initialize the router
router = APIRouter(
    prefix="/ingredients",
    tags=["Ingredient Classifier"]
)

# Load the trained model
try:
    model_path = "app/model_2/ingredients_classification_mobileNetV3.h5"
    model = load_model(model_path)
    print("Model loaded successfully.")
except Exception as e:
    raise RuntimeError(f"Error loading model: {e}")

# Load the class indices
class_indices_path = "app/model_2/class_indices.json"
try:
    with open(class_indices_path, "r") as json_file:
        class_indices = json.load(json_file)
    index_to_class = {v: k for k, v in class_indices.items()}  # Reverse mapping
    print("Class indices loaded successfully.")
except Exception as e:
    raise RuntimeError(f"Error loading class indices: {e}")

# Define the target size (should match the size used during training)
TARGET_SIZE = (224, 224)

@router.post("/predict")
async def predict(file: UploadFile = File(...), db: Session = Depends(get_db)):
    """
    Predict the class of the uploaded image and retrieve corresponding food details from the database.
    
    Args:
        file (UploadFile): The image file uploaded by the user.
        db: Database connection.

    Returns:
        JSON: Prediction results including ingredient details and nutritional information.
    """
    try:
        # Check if the file is provided
        if not file.filename:
            raise HTTPException(status_code=400, detail="No file selected")

        # Read and process the image
        img_bytes = BytesIO(await file.read())
        img = load_img(img_bytes, target_size=TARGET_SIZE)
        img_array = img_to_array(img)
        img_array = np.expand_dims(img_array, axis=0).astype(np.float32)
        img_array = preprocess_input(img_array)

        # Perform prediction
        predictions = model.predict(img_array)
        predicted_label_index = np.argmax(predictions)
        confidence = predictions[0][predicted_label_index]

        # Confidence threshold
        threshold = 0.2
        if confidence < threshold:
            raise HTTPException(status_code=400, detail="Ingredient Not Detected")

        # Map the predicted index to the class label
        class_label = index_to_class[predicted_label_index]

        # Query the database for ingredient details using text() for raw SQL
        result = db.execute(
            text("""
                SELECT 
                    m.id_makanan, 
                    m.nama_makanan, 
                    k.nama_kategori,
                    n.kalori, 
                    n.protein, 
                    n.lemak, 
                    n.karbohidrat, 
                    n.serat
                FROM 
                    makanan m
                JOIN 
                    kategori k ON m.kategori_id = k.id_kategori
                JOIN 
                    nutrisi n ON m.id_makanan = n.makanan_id
                WHERE 
                    LOWER(m.nama_makanan) = LOWER(:class_label)
            """), {'class_label': class_label}).fetchone()

        # If no matching ingredient found
        if not result:
            return {
                "class": class_label,
                "confidence": float(confidence),
                "message": "Ingredient found, but no detailed information available"
            }

        # Return the prediction result with ingredient details
        return {
            "class": class_label,
            "confidence": float(confidence),
            "details": {
                "id": result[0],  # Use index position for tuple-based results
                "name": result[1],
                "category": result[2],
                "nutrition": {
                    "calories": float(result[3]),
                    "protein": float(result[4]),
                    "fat": float(result[5]),
                    "carbohydrates": float(result[6]),
                    "fiber": float(result[7])
                }
            }
        }

    except Exception as e:
        # Handle errors gracefully
        raise HTTPException(status_code=500, detail=str(e))

@router.get("/test-db-connection")
async def test_database_connection(db: Session = Depends(get_db)):
    """
    Test database connection and retrieve a sample of ingredients.
    
    Returns:
        JSON: List of ingredients or connection error
    """
    try:
        # Fetch first 10 ingredients using text() for raw SQL
        result = db.execute(text("SELECT nama_makanan FROM makanan LIMIT 10")).fetchall()

        # Return the list of ingredients
        return {
            "status": "success",
            "ingredients": [row[0] for row in result]  # Access by index for tuple results
        }

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Database connection test failed: {e}")
