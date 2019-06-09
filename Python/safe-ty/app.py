from flask import Flask
from flask import request
import numpy
import json
import requests
import copy
from datetime import datetime
from NeuralNetwork import Network

NR_OF_INPUTS = 8
NR_OF_OUTPUTS = 2
NR_OF_LAY = 20
GET_EXISTING_DATA = True

biases_filename = 'biases.txt'
weights_filename = 'weights.txt'

app = Flask(__name__)

class MyPythonWebApp(object):
    def __init__(self, network=None, fromFile=True):
        if network is not None:
            self.network = network
        else:
            self.network = Network([NR_OF_INPUTS, NR_OF_LAY, NR_OF_OUTPUTS])

        if fromFile is not True:
            self.network.biases = [numpy.random.randn(x, 1) for x in sizes[1:]]
            self.network.weights = [numpy.random.randn(y, x) for x, y in zip(sizes[:-1], sizes[1:])]
        else:
            self.network.read_biases_from_file(biases_filename)
            self.network.read_weights_from_file(weights_filename)

@app.route('/training', methods=['GET'])
def train():
    print('training... is in progress ...')
    my_app.network.training(1.0, 0.9)
    return app.response_class(
        response=json.dumps('resp'),
        status=200,
        mimetype='application/json'
    )


@app.route('/prediction', methods=['GET'])
def predict():
    print('prediction... is in progress ...')
    return app.response_class(
        response=json.dumps('resp'),
        status=200,
        mimetype='application/json'
    )

if __name__ == '__main__':
    my_app = MyPythonWebApp()
    app.run()