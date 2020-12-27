# get_ipython().run_line_magic('pip', 'install scikit-learn')
#
import matplotlib.pyplot as plt
import numpy as np

help("modules")
from sklearn import datasets

digits = datasets.load_digits()
digits.images[0]
plt.imshow(digits.images[0], cmap=plt.cm.gray_r)
plt.imshow(digits.images[0], cmap=plt.cm.gray_r)
for i in range(0, 8):
    for j in range(0, 8):
        plt.gca().text(i - 0.15, j, int(digits.images[0][i][j]))
np.matrix.flatten(digits.images[0])
np.matrix.flatten(digits.images[0]) / 15


def random_classifier(input_vector):
    return np.random.rand(10)


v = np.matrix.flatten(digits.images[0]) / 15.
result = random_classifier(v)
result

list(result).index(max(result))
digits.target[0]


def test_digit_classify(classifier, test_count=1000):
    correct = 0  # <1>
    for img, target in zip(digits.images[:test_count], digits.target[:test_count]):  # <2>
        v = np.matrix.flatten(img) / 15.  # <3>
        output = classifier(v)  # <4>
        answer = list(output).index(max(output))  # <5>
        if answer == target:
            correct += 1  # <6>
    return (correct / test_count)  # <7>


test_digit_classify(random_classifier)
np.array([5.00512567e-06, 3.94168539e-05, 5.57124430e-09, 9.31981207e-09,
          9.98060276e-01, 9.10328786e-07, 1.56262695e-03, 1.82976466e-04,
          1.48519455e-04, 2.54354113e-07])


def average_img(i):
    imgs = [img for img, target in zip(digits.images[1000:], digits.target[1000:]) if target == i]
    return sum(imgs) / len(imgs)


plt.imshow(average_img(9), cmap=plt.cm.gray_r)

avg_digits = [np.matrix.flatten(average_img(i)) for i in range(10)]


def compare_to_avg(v):
    return [np.dot(v, avg_digits[i]) for i in range(10)]


test_digit_classify(compare_to_avg)


class MLP():
    def __init__(self, layer_sizes):  # <1>
        self.layer_sizes = layer_sizes
        self.weights = [
            np.random.rand(n, m)  # <2>
            for m, n in zip(layer_sizes[:-1], layer_sizes[1:])  # <3>
        ]
        self.biases = [np.random.rand(n) for n in layer_sizes[1:]]  # <4>


nn = MLP([2, 3])
nn.weights
nn.biases

from math import exp


def sigmoid(x):
    return 1 / (1 + exp(-x))


class MLP():
    def __init__(self, layer_sizes):  # <1>
        self.layer_sizes = layer_sizes
        self.weights = [
            np.random.rand(n, m)  # <2>
            for m, n in zip(layer_sizes[:-1], layer_sizes[1:])  # <3>
        ]
        self.biases = [np.random.rand(n) for n in layer_sizes[1:]]  # <4>

    def feedforward(self, v):
        activations = []  # <1>
        a = v
        activations.append(a)  # <2>
        for w, b in zip(self.weights, self.biases):  # <3>
            z = w @ a + b  # <4>
            a = [sigmoid(x) for x in z]  # <5>
            activations.append(a)  # <6>
        return activations

    def evaluate(self, v):
        return np.array(self.feedforward(v)[-1])


nn = MLP([64, 16, 10])
v = np.matrix.flatten(digits.images[0]) / 15.
nn.evaluate(v)

test_digit_classify(nn.evaluate)

x = np.array([np.matrix.flatten(img) for img in digits.images[:1000]]) / 15.0
y = digits.target[:1000]

from sklearn.neural_network import MLPClassifier

mlp = MLPClassifier(hidden_layer_sizes=(16,),  # <1>
                    activation='logistic',  # <2>
                    max_iter=100,  # <3>
                    verbose=10,  # <4>
                    random_state=1,  # <5>
                    learning_rate_init=.1)  # <6>

mlp.fit(x, y)
# mlp._predict(x)[0]
mlp.predict_proba(x)[0]


def sklearn_trained_classify(v):
    # return mlp._predict([v])[0]
    return mlp.predict_proba([v])[0]


test_digit_classify(sklearn_trained_classify)


def test_digit_classify(classifier, start=0, test_count=1000):
    correct = 0
    end = start + test_count  # <1>
    for img, target in zip(digits.images[start:end], digits.target[start:end]):  # <2>
        v = np.matrix.flatten(img) / 15.
        output = classifier(v)
        answer = list(output).index(max(output))
        if answer == target:
            correct += 1
    return (correct / test_count)


test_digit_classify(sklearn_trained_classify, start=1000, test_count=500)


def y_vec(digit):
    return np.array([1 if i == digit else 0 for i in range(0, 10)])


def cost_one(classifier, x, i):
    return sum([(classifier(x)[j] - y_vec(i)[j]) ** 2 for j in range(10)])


def total_cost(classifier):
    return sum([cost_one(classifier, x[j], y[j]) for j in range(1000)]) / 1000.


total_cost(nn.evaluate)
total_cost(sklearn_trained_classify)
nn = MLP([64, 16, 10])
nn.weights = [w.T for w in mlp.coefs_]
nn.biases = mlp.intercepts_

test_digit_classify(nn.evaluate, start=1000, test_count=500)

from sympy import *
X = symbols('x')
diff(1 / (1+exp(-X)),X)
