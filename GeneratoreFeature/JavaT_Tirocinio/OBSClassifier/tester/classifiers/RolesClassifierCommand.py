from utils import DatasetUtils
import pandas as pd
import tensorflow as tf
from classifiers.AbstractClassifier import AbstractClassifier

class RolesClassifier(AbstractClassifier):

    def __init__(self,columnsName,rolesName,foldersNumber):
        super().__init__(columnsName,rolesName,foldersNumber)
        self.evaluationResult=None
        self.trainsNumber=0

    def getEvaluationResult(self):
        return self.evaluationResult

    def initFeatureColumns(self):
        self.featureColumns=[]
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[0], num_buckets=4))
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[1], num_buckets=4))
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[2], num_buckets=3))
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[3], num_buckets=3))
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[4], num_buckets=5))
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[5], num_buckets=5))
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[6], num_buckets=4))
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[7], num_buckets=4))
        self.featureColumns.append(tf.feature_column.categorical_column_with_identity(key=self.columnsName[8], num_buckets=3))

    def initClassifier(self):
        self.classifier=tf.estimator.LinearClassifier(feature_columns=self.featureColumns,
                                                      optimizer=tf.train.FtrlOptimizer(learning_rate=0.01),
                                                      loss_reduction=tf.losses.Reduction.SUM_BY_NONZERO_WEIGHTS,
                                                      n_classes=len(self.rolesName))

    def train(self,label_col_name,batch_size,training_steps):
        train_x, train_y=self.trainingSet, self.trainingSet.copy().pop(label_col_name)
        self.classifier.train(input_fn=lambda: DatasetUtils.train_input_fn(train_x, train_y, batch_size), steps=training_steps)
        self.trainsNumber+=1

    def evaluate(self,label_col_name,batch_size):
        test_x, test_y=self.testSet, self.testSet.copy().pop(label_col_name)
        self.evaluationResult=self.classifier.evaluate(input_fn=lambda: DatasetUtils.eval_input_fn(test_x, test_y, batch_size))
        self.guessedInstances+=round(self.evaluationResult['accuracy']*test_x[self.columnsName[0]].count())
        return self.evaluationResult

    def predict(self,data_path,header,delimiter,batch_size):
        sw_classes=pd.read_csv(data_path,names=self.columnsName[:len(self.columnsName)-1],header=header,delimiter=delimiter)
        predictions=self.classifier.predict(input_fn=lambda: DatasetUtils.eval_input_fn(sw_classes, labels=None, batch_size=batch_size))
        return (sw_classes,predictions)