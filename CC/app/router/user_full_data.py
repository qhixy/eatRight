from fastapi import APIRouter, HTTPException, Depends
from sqlalchemy.orm import Session
from app.database import get_db
from app.models import User, Lifestyle

router = APIRouter(prefix="/user_database", tags=["User Database"])

@router.get("/{username}")
async def get_user_data(username: str, db: Session = Depends(get_db)):
    # Fetch the user by username
    user = db.query(User).filter(User.username == username).first()
    if not user:
        raise HTTPException(status_code=404, detail="User not found")

    # Fetch lifestyle data related to the user
    lifestyle_data = db.query(Lifestyle).filter(Lifestyle.user_id == user.id).all()

    # Prepare the response data combining both user and lifestyle data
    user_lifestyle = {
        "user": {
            "id": user.id,
            "username": user.username,
            "email": user.email,
        },
        "lifestyle": [
            {
                "Age": lifestyle.Age,
                "Gender": lifestyle.Gender,
                "Height": lifestyle.Height,
                "Weight": lifestyle.Weight,
                "CALC": lifestyle.CALC,
                "FAVC": lifestyle.FAVC,
                "FCVC": lifestyle.FCVC,
                "NCP": lifestyle.NCP,
                "SCC": lifestyle.SCC,
                "SMOKE": lifestyle.SMOKE,
                "CH2O": lifestyle.CH2O,
                "family_history_with_overweight": lifestyle.family_history_with_overweight,
                "FAF": lifestyle.FAF,
                "TUE": lifestyle.TUE,
                "CAEC": lifestyle.CAEC,
                "MTRANS": lifestyle.MTRANS,
                "prediction": lifestyle.prediction,
            }
            for lifestyle in lifestyle_data
        ] if lifestyle_data else []  # Return an empty list if no lifestyle data is found
    }

    return user_lifestyle