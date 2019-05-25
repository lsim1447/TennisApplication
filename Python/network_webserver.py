import time
import numpy
import sys
import os
import json
from http.server import BaseHTTPRequestHandler, HTTPServer
from NeuralNetwork import Network
HOST_NAME = 'localhost'
PORT_NUMBER = 9000

NR_OF_INPUTS = 8
NR_OF_OUTPUTS = 2
NR_OF_LAY = 20
NR_OF_EPOCH = 200


class MyHandler(BaseHTTPRequestHandler):
    def do_HEAD(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()

    def do_GET(self):
        paths = {
            '/training': {'status': 200},
            '/prediction': {'status': 200},
        }

        if self.path in paths:
            self.respond(paths[self.path])
        else:
            self.respond({'status': 500})

    def handle_http(self, status_code, path):
        print('path = |', path, '|')
        if path == '/training':
          print('training van vaze')
          data = []
          try:
              config = json.loads(open('./training-data-extended.txt').read())
              tr_data = config['data']
              i = 0
              for line in tr_data:
                  exist = 0
                  try:
                      my_input = line['inputs']
                      my_output = line['outputs']
                      l = ()
                      temp = []
                      for j in range(NR_OF_INPUTS):
                          x = float(my_input[j])
                          temp.append(x)
                      temp = numpy.asarray(temp)
                      l += (temp,)
                      res = 0
                      if my_output[0] == 0:
                          res = 0
                      else:
                          res = 1
                      res = numpy.asarray(res)
                      l += (res,)
                      data.append(l)
                      i += 1
                  except Exception as ex:
                      raise Exception("Corrupted file", ex)
          except Exception as ex:
              raise Exception("Error opening file", ex)

          data = [(numpy.reshape(x, (NR_OF_INPUTS, 1)), vectorized_result(y)) for x, y in data]
          network = Network([NR_OF_INPUTS, NR_OF_LAY, NR_OF_OUTPUTS])
          network.read_biases_from_file("biases.txt")
          network.read_weights_from_file("weights.txt")
    
          network.training(data, 1.0, 0.9)
    
          network.get_predicted_result(data[148][0])
        else:
          print('prediction van vaze')

    def respond(self, opts):
        response = self.handle_http(opts['status'], self.path)

def vectorized_result(j):
    x = numpy.zeros((NR_OF_OUTPUTS, 1))
    x[j] = 1.0
    return x

def sigmoid(x):
    return 1.0 / (1.0 + numpy.exp(-x))

def sigmoid_prime(x):
    return sigmoid(x) * (1 - sigmoid(x))

if __name__ == '__main__':
    server_class = HTTPServer
    httpd = server_class((HOST_NAME, PORT_NUMBER), MyHandler)
    print(time.asctime(), 'Server Starts - %s:%s' % (HOST_NAME, PORT_NUMBER))
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print(time.asctime(), 'Server Stops - %s:%s' % (HOST_NAME, PORT_NUMBER))