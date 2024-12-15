from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from fastapi import HTTPException
import os

# Ambil informasi koneksi dari environment variable atau secara manual
cloud_sql_connection_name = "eatright-444008:asia-southeast2:eatright"  # Gantilah dengan ID Cloud SQL instance Anda
DATABASE_URL = f"mysql+mysqlconnector://db-connection:db_connect123@/eatright?unix_socket=/cloudsql/{cloud_sql_connection_name}"





# SQLAlchemy engine
engine = create_engine(DATABASE_URL)

# SQLAlchemy session maker
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

# SQLAlchemy Base for ORM models
Base = declarative_base()

# Dependency to get the database session
def get_db():
    db = SessionLocal()
    try:
        yield db
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Database session error: {e}")
    finally:
        db.close()
