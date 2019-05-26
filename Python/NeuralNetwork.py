import numpy
import sys
import os
import json

NR_OF_INPUTS = 8
NR_OF_OUTPUTS = 2
NR_OF_LAY = 20
NR_OF_EPOCH = 200

class Network(object):
    def __init__(self, sizes):
        self.layers = len(sizes)
        self.sizes = sizes
        self.biases = [numpy.random.randn(x, 1) for x in sizes[1:]]
        self.weights = [numpy.random.randn(y, x) for x, y in zip(sizes[:-1], sizes[1:])]

    def feedforward(self, a):
        for bias, weight in zip(self.biases, self.weights):
            a = self.sigmoid(numpy.dot(weight, a) + bias)
        return a

    def training(self, data, eta, epsilon):
        j = 0
        while j < NR_OF_EPOCH:
            j += 1
            self.update(data, eta)
            right = self.evaluate(data)
            whole = len(data)
            cur = right / whole
            print("Epoch", j, ":", cur)
        self.write_weights_to_file(self.weights, "weights.txt")    
        self.write_biases_to_file(self.biases, "biases.txt")

    def update(self, data, eta):
        dif_bias = [numpy.zeros(b.shape) for b in self.biases]
        dif_weight = [numpy.zeros(w.shape) for w in self.weights]
        for x, y in data:
            delta_dif_bias, delta_dif_weight = self.backpropagation(x, y)
            dif_bias = [newbias + difnewbias for newbias, difnewbias in zip(dif_bias, delta_dif_bias)]
            dif_weight = [newweight + difnewweight for newweight, difnewweight in zip(dif_weight, delta_dif_weight)]
        self.weights = [weight - (eta / len(data)) * newweight for weight, newweight in zip(self.weights, dif_weight)]
        self.biases = [bias - (eta / len(data)) * newbias for bias, newbias in zip(self.biases, dif_bias)]

    def backpropagation(self, x, y):
        dif_bias = [numpy.zeros(bias.shape) for bias in self.biases]
        dif_weight = [numpy.zeros(weight.shape) for weight in self.weights]
        activation = x
        activations = [x]
        results = []
        for bias, weight in zip(self.biases, self.weights):
            result = numpy.dot(weight, activation) + bias
            results.append(result)
            activation = self.sigmoid(result)
            activations.append(activation)
        delta = self.cost_derivative(activations[-1], y) * self.sigmoid_prime(results[-1])
        dif_bias[-1] = delta
        dif_weight[-1] = numpy.dot(delta, activations[-2].transpose())
        for l in range(2, self.layers):
            result = results[-l]
            sp = self.sigmoid_prime(result)
            delta = numpy.dot(self.weights[-l + 1].transpose(), delta) * sp
            dif_bias[-l] = delta
            dif_weight[-l] = numpy.dot(delta, activations[-l - 1].transpose())
        return (dif_bias, dif_weight)

    def evaluate(self, data):
        test_results = [(numpy.argmax(self.feedforward(x)), y) for (x, y) in data]
        return sum(int(x == numpy.argmax(y)) for (x, y) in test_results)

    def cost_derivative(self, output_activations, y):
        return (output_activations - y)
		
    def write_biases_to_file(self, biases, filename):
        f = open(filename, "w")
        first_part = biases[0]
        second_part = biases[1]

        for i in first_part:
            line = str(i[0]) + "\n"
            f.writelines(line)
        for i in second_part:
            line = str(i[0]) + "\n"
            f.writelines(line)

        f.close()

    def write_weights_to_file(self, weights, filename):
        f = open(filename, "w")
        first_part = weights[0]
        second_part = weights[1]

        for i in first_part:
            line = ""
            for j in i:
                line = line + str(j) + " "
            f.writelines(line + "\n");

        second_part_one = second_part[0]
        second_part_two = second_part[1]

        line = ""
        for k in second_part_one:
            line = line + str(k) + " "
        f.writelines(line + "\n")

        line = ""
        for k in second_part_two:
            line = line + str(k) + " "
        f.writelines(line + "\n")
        f.close()
    def read_weights_from_file(self, filename):
        my_data = []
        first_array = []
        second_array = []
        my_data = []
        try:
            with open(filename) as f:
                counter = 0
                for line in f:
                    counter = counter + 1
                    if counter <= NR_OF_LAY:
                        try:
                            prop = line.split(" ")
                            prop = prop[:(len(prop)-1)]
                            temp = []
                            for j in prop:
                                x = float(j)
                                temp.append(x)
                            temp = numpy.asarray(temp)
                            first_array.append(temp);
                        except Exception as ex:
                            raise Exception("Corrupted file", ex)
                    else:
                        try:
                            prop = line.split(" ")
                            prop = prop[:(len(prop)-1)]
                            temp = []
                            for j in prop:
                                x = float(j)
                                temp.append(x)
                            temp = numpy.asarray(temp)
                            second_array.append(temp);
                        except Exception as ex:
                            raise Exception("Corrupted file", ex)
            my_data.append(numpy.asarray(first_array))
            my_data.append(numpy.asarray(second_array))
            self.weights = my_data
        except Exception as ex:
            raise Exception("Error opening file", ex)

    def read_biases_from_file(self, filename):
        my_data = []
        first_array = []
        second_array = []
        my_data = []
        try:
            with open(filename) as f:
                counter = 0
                for line in f:
                    counter = counter + 1
                    if counter <= NR_OF_LAY:
                        try:
                            temp = []
                            temp.append(float(line))
                            temp = numpy.asarray(temp)
                            first_array.append(temp);
                        except Exception as ex:
                            raise Exception("Corrupted file", ex)
                    else:
                        try:
                            temp = []
                            temp.append(float(line))
                            temp = numpy.asarray(temp)
                            second_array.append(temp);
                        except Exception as ex:
                            raise Exception("Corrupted file", ex)
            my_data.append(numpy.asarray(first_array))
            my_data.append(numpy.asarray(second_array))
            self.biases = my_data
        except Exception as ex:
            raise Exception("Error opening file", ex)

    def get_predicted_result(self, input):
        feed = self.feedforward(input)
        print("input = ", input)
        print("possibilities = ", feed[0][0], ", ", feed[1][0])
        return(feed[0][0], feed[1][0])

    def sigmoid(self, x):
        return 1.0 / (1.0 + numpy.exp(-x))

    def sigmoid_prime(self, x):
        return self.sigmoid(x) * (1 - self.sigmoid(x))

    def vectorized_result(self, j):
        x = numpy.zeros((NR_OF_OUTPUTS, 1))
        x[j] = 1.0
        return x
	
    def set_options_and_start_training(self, training_data_filename, weights_filename, biases_filename, is_already_calculated):
        data = []
        try:
            config = json.loads(open('./' + training_data_filename).read())
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

        data = [(numpy.reshape(x, (NR_OF_INPUTS, 1)), self.vectorized_result(y)) for x, y in data]
		
        if is_already_calculated:
            self.read_biases_from_file(biases_filename)
            self.read_weights_from_file(weights_filename)
        self.training(data, 1.0, 0.9)