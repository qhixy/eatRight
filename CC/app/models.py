from sqlalchemy import Column, Integer, String, Float, ForeignKey, DECIMAL
from sqlalchemy.orm import relationship
from .database import Base


# Users Table
class User(Base):
    __tablename__ = "users"

    id = Column(Integer, primary_key=True, index=True)
    username = Column(String(255), unique=True, index=True)
    email = Column(String(255), unique=True, index=True)
    password = Column(String(255))

    lifestyle = relationship("Lifestyle", back_populates="user")


# Lifestyle Table
class Lifestyle(Base):
    __tablename__ = "lifestyle"

    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, ForeignKey("users.id"))
    Age = Column(Float)
    Gender = Column(String(255))
    Height = Column(Float)
    Weight = Column(Float)
    CALC = Column(String(255))
    FAVC = Column(String(255))
    FCVC = Column(Float)
    NCP = Column(Float)
    SCC = Column(String(255))
    SMOKE = Column(String(255))
    CH2O = Column(Float)
    family_history_with_overweight = Column(String(255))
    FAF = Column(Float)
    TUE = Column(Float)
    CAEC = Column(String(255))
    MTRANS = Column(String(255))
    prediction = Column(String(255))

    user = relationship("User", back_populates="lifestyle")


# Kategori Table
class Kategori(Base):
    __tablename__ = "kategori"

    id_kategori = Column(String(3), primary_key=True)
    nama_kategori = Column(String(50), nullable=False)

    makanan = relationship("Makanan", back_populates="kategori")


# Makanan Table
class Makanan(Base):
    __tablename__ = "makanan"

    id_makanan = Column(Integer, primary_key=True, index=True)
    nama_makanan = Column(String(50), nullable=False)
    kategori_id = Column(String(3), ForeignKey("kategori.id_kategori"))

    kategori = relationship("Kategori", back_populates="makanan")
    nutrisi = relationship("Nutrisi", back_populates="makanan")


# Nutrisi Table
class Nutrisi(Base):
    __tablename__ = "nutrisi"

    id_nutrisi = Column(String(5), primary_key=True)
    makanan_id = Column(Integer, ForeignKey("makanan.id_makanan"))
    kategori_id = Column(String(5), ForeignKey("kategori.id_kategori"))
    kalori = Column(DECIMAL(5, 2))
    protein = Column(DECIMAL(5, 2))
    lemak = Column(DECIMAL(5, 2))
    karbohidrat = Column(DECIMAL(5, 2))
    serat = Column(DECIMAL(5, 2))

    makanan = relationship("Makanan", back_populates="nutrisi")