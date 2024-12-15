from pydantic import BaseModel
from datetime import datetime
from typing import Optional


# ------------------------
# Kategori Table Schemas
# ------------------------
class KategoriBase(BaseModel):
    nama_kategori: str


class KategoriCreate(KategoriBase):
    pass


class KategoriResponse(KategoriBase):
    id_kategori: str

    class Config:
        orm_mode = True


# ------------------------
# Makanan Table Schemas
# ------------------------
class MakananBase(BaseModel):
    nama_makanan: str
    kategori_id: Optional[str]


class MakananCreate(MakananBase):
    pass


class MakananResponse(MakananBase):
    id_makanan: int

    class Config:
        orm_mode = True


# ------------------------
# Nutrisi Table Schemas
# ------------------------
class NutrisiBase(BaseModel):
    makanan_id: Optional[int]
    kategori_id: Optional[str]
    kalori: Optional[float]
    protein: Optional[float]
    lemak: Optional[float]
    karbohidrat: Optional[float]
    serat: Optional[float]


class NutrisiCreate(NutrisiBase):
    pass


class NutrisiResponse(NutrisiBase):
    id_nutrisi: str

    class Config:
        orm_mode = True


# ------------------------
# Lifestyle Table Schemas
# ------------------------
class LifestyleData(BaseModel):
    Age: float
    Gender: str
    Height: float
    Weight: float
    CALC: str
    FAVC: str
    FCVC: float
    NCP: float
    SCC: str
    SMOKE: str
    CH2O: float
    family_history_with_overweight: str
    FAF: float
    TUE: float
    CAEC: str
    MTRANS: str


class LifestyleResponse(BaseModel):
    user_id: int
    prediction: str
    message: str

    class Config:
        orm_mode = True


# ------------------------
# Users Table Schemas
# ------------------------
class UserCreate(BaseModel):
    username: str
    email: str
    password: str


class UserResponse(BaseModel):
    id: int
    username: str
    email: str

    class Config:
        orm_mode = True


# ------------------------
# Additional Feature Schemas
# ------------------------
class ReminderCreate(BaseModel):
    message: str
    time: datetime


class ObesityRecordCreate(BaseModel):
    weight: float
    height: float


class ObesityResponse(BaseModel):
    bmi: float

    class Config:
        orm_mode = True