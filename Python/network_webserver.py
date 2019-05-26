import time
from http.server import BaseHTTPRequestHandler, HTTPServer
from NeuralNetwork import Network

HOST_NAME = 'localhost'
PORT_NUMBER = 9000

NR_OF_INPUTS = 8
NR_OF_OUTPUTS = 2
NR_OF_LAY = 20
GET_EXISTING_DATA = True

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
            print('training ...')
            network = Network([NR_OF_INPUTS, NR_OF_LAY, NR_OF_OUTPUTS])
            network.set_options_and_start_training('training-data-extended.txt', 'weights.txt', 'biases.txt', GET_EXISTING_DATA)
        else:
            print('predicting ...')

    def respond(self, opts):
        response = self.handle_http(opts['status'], self.path)

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