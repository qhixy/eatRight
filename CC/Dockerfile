# Gunakan image Python 3.9
FROM python:3.9-slim

# Setel direktori kerja
WORKDIR /app

# Install dependensi sistem yang diperlukan (gcc, libmysqlclient, dan libzbar untuk pyzbar)
RUN apt-get update && apt-get install -y \
    gcc \
    default-libmysqlclient-dev \
    libzbar0 \
 && apt-get clean && rm -rf /var/lib/apt/lists/*

# Salin file requirements.txt dan install dependensi Python
COPY requirements.txt .
RUN pip install --no-cache-dir --upgrade pip && pip install --no-cache-dir -r requirements.txt

# Salin seluruh aplikasi
COPY . .

# Expose port 8080 (Cloud Run menggunakan port ini secara default)
EXPOSE 8080

# Jalankan aplikasi dengan Uvicorn
CMD ["uvicorn", "app.main:app", "--host", "0.0.0.0", "--port", "8080"]
