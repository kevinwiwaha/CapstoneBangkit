from flask import Flask, request
import tensorflow as tf
import os
import json
import numpy as np
import base64
import matplotlib.pyplot as plt


app = Flask(__name__)
model = tf.keras.models.load_model('./model/model-test.h5')


@app.route("/")
def hello_world():
    return "bangkit ayos"


@app.route("/predict", methods=['POST'])
def predict():
    # file = '../Model/images/laptop.jpg'

    image = generate_image_from_base64(
        request.form["title"], request.form["body"])
    img = tf.keras.preprocessing.image.load_img(image, target_size=(224, 224))
    x = tf.keras.preprocessing.image.img_to_array(img)
    x = np.expand_dims(x, axis=0)
    images = np.vstack([x])
    history = model.predict(images)
    print(history[0][0])
    os.remove(get_filename(request.form["title"]))
    return {
        "userId": 1,
        "id": 1,
        "title": request.form["title"],
        "body": str(history[0][0])
    }


def generate_image_from_base64(filename, string):
    file = open('./{filename}.jpg'.format(filename=filename), 'wb')
    file.write(base64.b64decode((string)))
    file.close()

    return './{filename}.jpg'.format(filename=filename)


def get_filename(filename):
    return './{filename}.jpg'.format(filename=filename)


if __name__ == "__main__":
    app.run(host='127.0.0.1', debug=True, port=5000)
