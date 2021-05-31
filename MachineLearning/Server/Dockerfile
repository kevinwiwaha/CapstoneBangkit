FROM python:3.7.10-slim-buster
WORKDIR /app
RUN pip install --upgrade setuptools

COPY requirements.txt requirements.txt
RUN pip install -r requirements.txt

COPY . .
ENV FLASK_APP=main.py
ENV FLASK_ENV=development
CMD ["flask","run","--host=0.0.0.0"]