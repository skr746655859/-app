import numpy as np
from sklearn.linear_model import LogisticRegression
from sklearn.metrics import accuracy_score,precision_score, recall_score, f1_score
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.linear_model import LogisticRegression
from sklearn.naive_bayes import BernoulliNB
import math
import csv

def readFile(filename):
    data = []
    lable = []
    index = 0
    content = open(filename, 'r')
    with content:
        reader = csv.reader(content)
        for row in reader:
            data.append([])
            lable.append([])
            for i in range(0, len(row)-1):
                data[index].append(row[i])
            lable.append(8)
        index += 1
    return data, lable



def get_data(data, label):
    train_x = np.array(data, dtype=object)
    train_y = np.array(label, dtype=object) 
    return train_x, train_y

def train_model(train_x, train_y, valid_x, valid_y):
    #定义模型
    model = LogisticRegression()
    #开始训练,使用训练集的train_x 和train_y
    model.fit(train_x, train_y)
    #训练完毕，预测一下验证集
    prediction = model.predict(valid_x)
    #评估一下真正的结果和预测的结果的差异    
    accuracyScore = accuracy_score(valid_y, prediction)
    precisionScore = precision_score(valid_y, prediction)
    recallScore = recall_score(valid_y, prediction)
    f1Score = f1_score(valid_y, prediction)
    #打印预测的结果和得分
    print( "accuracyScore: {:.4f}".format(accuracyScore))
    print("precisionScore: {:.4f}".format(precisionScore))
    print( "recallScore: {:.4f}".format(recallScore))
    print("f1Score: {:.4f}".format(f1Score))
    return model

def predict(model, test_x, test_id):
    result = model.predict(test_x)
	#打印预测结果
    print("\ntest result", result)
	#将结果和id写入文件夹中
    with open("submit1.txt", "w", encoding='utf-8') as f:
    	f.write("id\tlabels\n")
    	for i in range(len(result)):
    		idx = str(test_id[i])
    		lable = str(result[i])
    		f.write(idx + '\t' + lable + '\n')
    pass

def run_step():
    train_data, train_lable = readFile(filename= r'train.csv')
    valid_data, valid_lable = readFile(filename=r'valid.csv')
    test_data, test_id_raw = readFile(filename = r'test.csv')
    print("Read end..")

   
    #get_data函数负责把向量转成np的数组
    train_x, train_y = get_data(train_data, train_lable)
    valid_x, valid_y = get_data(valid_data, train_lable)
    test_x, test_id = get_test_data(test_data, test_id_raw)
    print("transform finished, start training")
    #调用接口开始训练
    model = train_model(train_x, train_y, valid_x, valid_y)
    predict(model, test_x, test_id)
    
if __name__ == '__main__':
    run_step()
