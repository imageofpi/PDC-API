# PDC-API
This repo contains codes for Flask app.

Requirement : a keras model file named - best_model-weights.hdf5
Classes are mentioned in the app.py file. These classes belong to
to Pant Village dataset

My flask app for the same is hosted on Heroku .

API - https://plant-disease-classification.herokuapp.com/

* '/predict/' accepts image file and sends back a
json file with classes as key and resp. probability as value.
