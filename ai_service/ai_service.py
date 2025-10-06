from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse
import uvicorn
import numpy as np

app = FastAPI(title="AI Prediction Service", version="1.0")

def predict_future_defects(defect_counts, future_count=5):
    if len(defect_counts) < 2:
        return [float(np.mean(defect_counts))] * future_count
    x = np.arange(len(defect_counts))
    y = np.array(defect_counts)
    a, b = np.polyfit(x, y, 1)
    future_x = np.arange(len(defect_counts), len(defect_counts) + future_count)
    predictions = a * future_x + b
    return [max(0.0, round(float(p), 2)) for p in predictions]

def generate_report(defect_counts, predictions):
    """
    Returns a list of easy-to-read points
    """
    report = []
    total_defects = sum(defect_counts)
    avg_defects = np.mean(defect_counts)
    report.append(f"Total defects so far: {total_defects}")
    report.append(f"Average defects per batch: {avg_defects:.2f}")

    trend = "increasing" if predictions[-1] > predictions[0] else "decreasing"
    report.append(f"Predicted trend for next {len(predictions)} batches: {trend}")

    report.append(f"Predicted defects for next batches: {predictions}")

    return report

@app.post("/predict")
async def predict(request: Request):
    try:
        data = await request.json()
        defects_data = data.get("defects", [])
        future_count = data.get("future_count", 5)

        if not defects_data:
            return JSONResponse(status_code=400, content={"error": "Missing 'defects' data"})

        defect_counts = [d["defect"] for d in defects_data]

        predictions = predict_future_defects(defect_counts, future_count)
        report = generate_report(defect_counts, predictions)

        return JSONResponse(content={
            "predictions": predictions,
            "report": report
        })
    except Exception as e:
        return JSONResponse(status_code=500, content={"error": str(e)})

if __name__ == "__main__":
    uvicorn.run("ai_service:app", host="0.0.0.0", port=8001, reload=True)