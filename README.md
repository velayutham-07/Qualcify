🧠 Qualcify – AI-Powered Defect Prediction System
📋 Overview

Qualcify is an intelligent defect analysis and prediction platform built using Spring Boot and FastAPI (Python).
It helps manufacturing and quality teams analyze current defect data from production batches, visualize the trends, and predict future defects using AI forecasting.

🚀 Features

📂 Upload Excel files containing batch defect data.

📊 Visualize current defects with dynamic charts.

🤖 AI-powered prediction for future defect counts using FastAPI microservice.

🧾 Automatically generated AI summary report displayed on the website.

💾 Downloadable charts and processed files for reference.

🖥️ Modern Spring Boot + Thymeleaf UI for seamless user experience.

🏗️ Tech Stack
Component	Technology Used
Backend (Java)	Spring Boot (Web, Reactive WebClient, Thymeleaf)
AI Microservice	Python (FastAPI, scikit-learn, pandas, numpy)
Visualization	XChart (Java), Matplotlib (Python)
Frontend	Thymeleaf Templates (HTML, CSS, JS)
Data Source	Excel Uploads (Apache POI / Custom Reader)


📁 Project Structure

qualcify/
├── src/main/java/com/qualcify/
│   ├── controller/
│   │   └── DataController.java
│   ├── model/
│   │   └── BatchDefect.java
│   ├── service/
│   │   └── AiPredictionService.java
│   ├── utils/
│   │   ├── ExcelReader.java
│   │   └── WekaClassifier.java
│   └── QualcifyApplication.java
│
├── ai_service/
│   ├── app.py                 ← FastAPI backend
│   ├── model.pkl              ← Trained ML model
│   └── requirements.txt
│
├── src/main/resources/
│   ├── templates/
│   │   ├── upload.html
│   │   └── result.html
│   └── static/uploads/
│
└── README.md

⚙️ How It Works

Excel Upload
⚪The user uploads an Excel sheet containing defect data (Batch, Defect Count).

Defect Classification
⚪The system classifies each defect using WekaClassifier.

Chart Generation
⚪XChart generates visual charts for current defect data.

AI Forecasting
⚪The data is sent to the FastAPI microservice (localhost:8001/predict).
⚪The microservice predicts defect counts for the next 5 batches.
⚪Results Displayed
⚪Both charts (current + forecasted) and an AI-generated summary report appear on the result page.

🧩 How to Run Locally
---------------------------------------------------------------
| Step 1: Clone the Repository                                |
| git clone https://github.com/<your-username>/qualcify.git   |
| cd qualcify                                                 |
---------------------------------------------------------------

-------------------------------------------
| Step 2: Run the FastAPI AI Microservice | 
| cd ai_service                           |
| pip install -r requirements.txt         |
| python app.py                           |
-------------------------------------------

➡ The AI service will start on http://127.0.0.1:8001.

---------------------------------------------
| Step 3: Run the Spring Boot Application   |
| cd ..                                     |
| mvn spring-boot:run                       |
---------------------------------------------

➡ The app will start on http://localhost:8080.

------------------------------------------------
| Step 4: Upload Data and Get Predictions      |
|                                              |
| Open http://localhost:8080.                  |
|                                              |
| Upload your Excel file.                      |
------------------------------------------------

➡ View the defect visualization and AI forecast directly in your browser.

🧠 Example Input

  Batch	Defect Count
  Batch   1     10
  Batch   2	    15
  Batch   3	     9
  Batch   4	    20

📈 Example Output

Chart 1: Current Defects per Batch

Chart 2: AI Forecast for Next 5 Batches

AI Summary Report:

Identifies trends (increasing or decreasing defects).

Predicts approximate defect range for upcoming production.

Suggests preventive actions (if model includes recommendation logic).

🧰 Requirements

Java Backend

➡ Java 17+
➡ Maven 3.9+
➡ Spring Boot 3.x

Python Microservice

➡ Python 3.9+
➡ FastAPI
➡ Uvicorn
➡ Pandas
➡ NumPy
➡ scikit-learn

🤝 Contributing

Pull requests are welcome!
If you find a bug or have suggestions, open an issue or fork the repo to enhance the model/report generation.

📜 License

This project is licensed under the MIT License – you’re free to use, modify, and distribute it with attribution.

✨ Credits

Developed by Velayutham V
Guided by AI-assisted architecture & ML integration for intelligent defect prediction.
