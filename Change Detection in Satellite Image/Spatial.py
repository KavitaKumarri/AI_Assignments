import cv2
import numpy as np
from numpy import asarray
from numpy.linalg import slogdet, pinv
from math import log
from scipy.stats import multivariate_normal
from sklearn.mixture import GaussianMixture
import matplotlib
matplotlib.use('Agg')

import matplotlib.pyplot as plt
import datetime


def KL_divergance(a, b):
    a = np.asarray(a, dtype=np.float)
    b = np.asarray(b, dtype=np.float)

    try:
        sum = 0
        for i in range(len(a)):
            if a[i]!=0 and b[i]!=0:
                sum += a[i] * np.log(a[i] / b[i])
        # sum = np.sum(np.where(b != 0 and a!=0, a * np.log(a / b), 0))
        return sum
    except:
        return 0


def gaussian(input, u, sigma):
    n = len(input)
    sdet = slogdet(sigma)
    diff = input - u
    p = (np.matmul(np.matmul(diff[None, :], pinv(sigma)), diff[None, :].T))
    log_likelihood = (-n / 2 * log(2 * np.pi)) + (-0.5 * sdet[1]) + (0.5 * p)
    return log_likelihood


# Reading the 2 images
img1 = cv2.imread('k02-05m.tif',0)
img2 = cv2.imread('k12-05m.tif',0)

data1 = asarray(img1)
data2 = asarray(img2)

# The dimensions of both the images are the same
im_height, im_width = img1.shape

print(img1.shape)
print(img2.shape)

# Setting the window size
window_height, window_width = 51,51
output_height = im_height - window_height
output_width = im_width - window_width

distance_list = []


now = datetime.datetime.now()
print(now.strftime("%Y-%m-%d %H:%M:%S"))

file = open("timeValues.txt","w+") 
file.write(now.strftime("%Y-%m-%d %H:%M:%S"))
file.write('\n\n\n')


#Sliding window to get the
for i in range(2000,2020):
    for j in range(2000,2020):
        #if ((i+1)*(j+1))%100 == 0:
         #   print (i*j)
        try:
            mean1,std1 = cv2.meanStdDev(data1[i:i+window_height,j:j+window_width])
            mean1 = float(mean1)
            cov1 = np.cov(data1[i:i+window_height,j:j+window_width], rowvar=False)
            y1 = multivariate_normal.pdf(data1[i:i+window_height,j:j+window_width].flatten(),mean1,cov = std1)

            mean2,std2 = cv2.meanStdDev(data2[i:i+window_height,j:j+window_width])
            mean2 = float(mean2)
            cov2 = np.cov(data2[i:i+window_height,j:j+window_width])
            y2 = multivariate_normal.pdf(data2[i:i+window_height,j:j+window_width].flatten(),mean2,cov = std2)

            distance1 = KL_divergance(y1,y2)
            distance2 = KL_divergance(y2,y1)
            distance_list.append([(distance1 + distance2)/2])

        except:
            distance_list.append([0])

#print(len(distance_list))
distance_list = np.array(distance_list)
# picture intensity

now = datetime.datetime.now()
file.write(now.strftime("%Y-%m-%d %H:%M:%S"))
file.close();
print(now.strftime("%Y-%m-%d %H:%M:%S"))

# 1) GMM Wm cluster
# 2) Plot the color for the 2 outputs
# 3) Implement Grid Based approach
# 4) Plot the color for the 2 outputs, with pixel centering
# 5) Parallelize Code
# 6) Plot charts

plt.figure(1)
# Model-based Clustering (GMM)
gmm = GaussianMixture(n_components=4).fit(distance_list)
labels = gmm.predict(distance_list)
predict_gmm = labels.reshape(20,20)
#plt.imshow(predict_gmm)
plt.imsave('output.png', predict_gmm, format='png')