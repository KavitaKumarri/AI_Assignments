#!/usr/bin/env python
# coding: utf-8

import csv
import pandas as pd
import sklearn
import numpy as np 
import _pickle as cPickle
from textblob import TextBlob
import sklearn.model_selection
from nltk.tokenize import word_tokenize
from nltk.corpus import stopwords
from nltk.stem import PorterStemmer
from sklearn.feature_extraction.text import CountVectorizer, TfidfTransformer
from sklearn.naive_bayes import MultinomialNB
from sklearn.svm import SVC, LinearSVC
from sklearn.metrics import classification_report, f1_score, accuracy_score, confusion_matrix
from sklearn.pipeline import Pipeline
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import StratifiedKFold, cross_val_score, train_test_split 


# read the data from the folder
messageList = pd.read_csv('./smsspamcollection/SMSSpamCollection', sep='\t',names = ["label","message"])
#messageList.head()


# summarizing the no. of messages for each label
messageList.groupby('label').describe()



#splitting the message into tokens
def split_message(message):
    message = str(message.encode('utf-8'))
    return TextBlob(message).words


#function to return word lemmas
def normalizeWordlemma(message):
    message = str(message.encode('utf-8'))
    msg_words = TextBlob(message).words

    return [msg_word.lemma for msg_word in msg_words]


def normalizeWord(message):
    message = str(message.encode('utf-8'))
    message = message.lower()
    #tokenization
    words = word_tokenize(message)
    #remove stop words 
    stopWordList = stopwords.words('english')
    words = [word for word in words if word not in stopWordList]
    #stemming
    stemmer = PorterStemmer()
    words = [stemmer.stem(word) for word in words]  
    return words
    

#split the data into train and test with 80% as train and 20% as test

trainData, testData, trainLabel, testLabel =     train_test_split(messageList['message'], messageList['label'], test_size=0.2)

print(len(trainData), len(testData), len(trainLabel) + len(testLabel))


#create pipeline for Naive Bayer classifier using count vectorizer and TFIDF transformers
bayes_pipeline = Pipeline([
    ('bow', CountVectorizer(analyzer=normalizeWordlemma)),  
    ('tfidf', TfidfTransformer()), 
    ('classifier', MultinomialNB()),  
])


params = {
    'tfidf__use_idf': (True, False),
    'bow__analyzer': (normalizeWordlemma, split_message),
}

gridSearch = GridSearchCV(
    bayes_pipeline, 
    params,  
    refit=True, 
    n_jobs=-1, 
    scoring='accuracy', 
)

#train the model

model = gridSearch.fit(trainData, trainLabel)


#test model on test data and check the accuracy
predictedLabel = model.predict(testData)
print(confusion_matrix(testLabel, predictedLabel))
print(classification_report(testLabel, predictedLabel))



#create pipeline for SVM classifier 
 
pipeline_svm = Pipeline([
    ('bow', CountVectorizer(analyzer=normalizeWordlemma)),
    ('tfidf', TfidfTransformer()),
    ('classifier', SVC()),  
])



param_svm = [
  {'classifier__C': [1, 10, 100, 1000], 'classifier__kernel': ['linear']},
  {'classifier__C': [1, 10, 100, 1000], 'classifier__gamma': [0.001, 0.0001], 'classifier__kernel': ['rbf']},
]

grid_svm = GridSearchCV(
    pipeline_svm,  
    param_grid=param_svm,  
    refit=True,  
    n_jobs=-1, 
    scoring='accuracy', 
    cv=StratifiedKFold(),
)


#train SVM model
svm_model = grid_svm.fit(trainData, trainLabel)

#predict the label using test data and check accuracy using confusion matrix

print(confusion_matrix(testLabel, svm_model.predict(testData)))
print(classification_report(testLabel, svm_model.predict(testData)))





