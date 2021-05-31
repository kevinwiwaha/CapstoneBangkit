from flask import Flask, request
import tensorflow as tf
import os
import json
import numpy as np
import matplotlib.pyplot as plt


app = Flask(__name__)
model = tf.keras.models.load_model('./model/model-test.h5')


@app.route("/")
def hello_world():
    return "bangkit ayos"


@app.route("/predict", methods=['POST'])
def predict():
    file = '../Model/images/laptop.jpg'
    img = tf.keras.preprocessing.image.load_img(file, target_size=(224, 224))
    x = tf.keras.preprocessing.image.img_to_array(img)
    x = np.expand_dims(x, axis=0)
    images = np.vstack([x])
    history = model.predict(images)
    print(history[0][0])
    return {
        "prediction": str(history[0][0])
    }


if __name__ == "__main__":
    app.run(host='127.0.0.1', debug=True, port=5000)
