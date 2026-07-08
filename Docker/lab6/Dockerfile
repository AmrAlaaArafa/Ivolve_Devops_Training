FROM python:3.11-slim
WORKDIR /app
RUN pip install flask
COPY . .
EXPOSE 8080
ENV APP_MODE=production
ENV APP_REGION=canada-west
CMD ["python", "app.py"]