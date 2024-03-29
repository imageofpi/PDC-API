from flask import Flask, request, jsonify
import numpy as np
import tensorflow as tf
from PIL import Image

app = Flask(__name__)

#class names
classes = [ 'Apple scab',
            'Apple Black_rot',
            'Apple Cedar_apple_rust',
            'Apple healthy',
            'Blueberry healthy',
            'Cherry(including_sour) Powdery_mildew',
            'Cherry_(including_sour) healthy',
            'Corn(maize) Cercospora or Gray leaf spot',
            'Corn(maize) Common rust',
            'Corn(maize) Northern Leaf Blight',
            'Corn(maize) healthy',
            'Grape Black rot',
            'Grape Esca (Black Measles)',
            'Grape Leaf blight(Isariopsis Leaf Spot)',
            'Grape healthy',
            'Orange Haunglongbing (Citrus greening)',
            'Peach Bacterial spot',
            'Peach healthy',
            'Pepper,bell Bacterial spot',
            'Pepper,bell healthy',
            'Potato Early blight',
            'Potato Late blight',
            'Potato healthy',
            'Raspberry healthy',
            'Soybean healthy',
            'Squash Powdery mildew',
            'Strawberry Leaf scorch',
            'Strawberry healthy',
            'Tomato Bacterial spot',
            'Tomato Early blight',
            'Tomato Late blight',
            'Tomato leaf Mold',
            'Tomato Septoria leaf spot',
            'Tomato spider mites Two-spotted spider mite',
            'Tomato Target Spot',
            'Tomato Yellow Leaf Curl Virus',
            'Tomato Tomato mosaic virus',
            'Tomato healthy']

model_path = 'model.tflite'

@app.route('/predict', methods=['GET','POST'])
def post_prediction():
    #image fetching and preparation for prediction
    if request.files['image'].filename == '':
        return {'Error':'No selected file'}
    img = Image.open(request.files['image']).convert('RGB')
    img = img.resize((224,224),Image.ANTIALIAS)
    img = np.array(img)
    img = np.expand_dims(img, axis=0).astype(np.float32)
    img = img/255
    

    interpreter = tf.lite.Interpreter(model_path=model_path)
    interpreter.allocate_tensors()
    input_tensor_index = interpreter.get_input_details()[0]["index"]
    output = interpreter.tensor(interpreter.get_output_details()[0]["index"])

    interpreter.set_tensor(input_tensor_index, img)
    interpreter.invoke()
    predictions = output()[0]
    indx = predictions.argmax(axis=0)
    return jsonify({'class':classes[indx]})

# A welcome message to test our server
@app.route('/' , methods=['POST','GET'])
def index():
    return "<div> <h1>Welcome to our server !!!</h1><h2> Go to /predict for disease predictions</h2></div>"

if __name__ == '__main__':
    # Threaded option to enable multiple instances for multiple user access support
    app.run(threaded=True, port=5000)


# app hosted on - https://pdc-api-lite.herokuapp.com/
