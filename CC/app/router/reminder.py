from fastapi import APIRouter, HTTPException
from apscheduler.schedulers.background import BackgroundScheduler
from datetime import datetime
import re

# Inisialisasi router
router = APIRouter(prefix="/reminder", tags=["Reminder"])

# Inisialisasi scheduler
scheduler = BackgroundScheduler()
scheduler.start()

# Fungsi untuk mengirim pengingat makan dan minum
def send_reminder(message: str):
    print(f"{datetime.now().strftime('%Y-%m-%d %H:%M:%S')} - Reminder: {message}")

# Fungsi untuk memvalidasi format waktu
def is_valid_time_format(time_of_day: str) -> bool:
    # Memeriksa apakah waktu sesuai dengan format HH:MM
    return bool(re.match(r'^[0-9]{2}:[0-9]{2}$', time_of_day))

# Fungsi untuk menambahkan reminder ke scheduler
@router.post("/set_reminder/")
async def set_reminder(time_of_day: str, message: str):
    """
    Endpoint untuk mengatur pengingat makan dan minum pada waktu tertentu.
    Waktu harus dalam format HH:MM (24-hour format)
    """
    try:
        if not is_valid_time_format(time_of_day):
            raise HTTPException(status_code=400, detail="Invalid time format. Use HH:MM (24-hour format).")
        
        hour, minute = map(int, time_of_day.split(":"))
        if hour < 0 or hour > 23 or minute < 0 or minute > 59:
            raise HTTPException(status_code=400, detail="Invalid time format. Use HH:MM (24-hour format).")
        
        # Menjadwalkan reminder
        reminder_time = f"{hour:02d}:{minute:02d}"
        scheduler.add_job(
            send_reminder, 
            'cron', 
            hour=hour, 
            minute=minute, 
            args=[message],
            id=reminder_time
        )
        return {"message": f"Reminder set for {message} at {time_of_day}"}
    
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Error setting reminder: {str(e)}")

# Endpoint untuk memeriksa status scheduler
@router.get("/reminders/")
async def get_reminders():
    """Endpoint untuk mendapatkan daftar pengingat yang terjadwal."""
    jobs = scheduler.get_jobs()
    reminders = []
    for job in jobs:
        reminders.append({
            "id": job.id,
            "next_run_time": job.next_run_time.strftime('%Y-%m-%d %H:%M:%S'),
            "message": job.args[0]  # Pesan pengingat
        })
    return {"reminders": reminders}

# Endpoint untuk menghentikan semua pengingat
@router.delete("/clear_reminders/")
async def clear_reminders():
    """Endpoint untuk menghentikan semua pengingat yang terjadwal."""
    scheduler.remove_all_jobs()
    return {"message": "All reminders have been cleared."}
