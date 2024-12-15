from fastapi import APIRouter, UploadFile, File, HTTPException
from PIL import Image
from pyzbar.pyzbar import decode
import requests  # Import library untuk mengakses API eksternal

router = APIRouter(prefix="/barcode", tags=["Barcode Scanner"])

@router.post("/")
async def scan_barcode(file: UploadFile = File(...)):
    try:
        # Membuka file gambar
        image = Image.open(file.file)
        # Decode barcode dalam gambar
        barcodes = decode(image)

        # Jika tidak ditemukan barcode
        if not barcodes:
            raise HTTPException(status_code=404, detail="No barcode found in the image")

        results = []
        for barcode in barcodes:
            barcode_data = barcode.data.decode("utf-8")  # Data dari barcode

            # Mengakses API Open Food Facts untuk mendapatkan data produk
            url = f"https://world.openfoodfacts.org/api/v0/product/{barcode_data}.json"
            response = requests.get(url)

            # Mengecek jika data produk ditemukan
            if response.status_code == 200:
                product_data = response.json()

                if product_data.get("status", 0) == 1:  # Pastikan status sukses
                    product_info = product_data.get("product", {})

                    # Ambil informasi nutrisi
                    nutrition_data = product_info.get("nutriments", {})

                    results.append({
                        "type": barcode.type,  # Jenis barcode
                        "data": barcode_data,  # Data dari barcode
                        "product_name": product_info.get("product_name", "Nama produk tidak tersedia"),
                        "brand": product_info.get("brands", "Merek produk tidak tersedia"),
                        "nutritional_info": {
                            "calories": nutrition_data.get("energy_kcal", "Data kalori tidak tersedia"),
                            "fat": nutrition_data.get("fat", "Data lemak tidak tersedia"),
                            "carbohydrates": nutrition_data.get("carbohydrates", "Data karbohidrat tidak tersedia"),
                            "protein": nutrition_data.get("proteins", "Data protein tidak tersedia"),
                            "sugar": nutrition_data.get("sugars", "Data gula tidak tersedia"),
                        },
                    })
                else:
                    results.append({
                        "type": barcode.type,
                        "data": barcode_data,
                        "message": "Produk tidak ditemukan dalam Open Food Facts."
                    })
            else:
                results.append({
                    "type": barcode.type,
                    "data": barcode_data,
                    "message": "Gagal mengambil data produk dari API."
                })

        return {"barcodes": results}

    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error processing the file: {str(e)}")
