import os
from classifiers.InstancesClassifierCommand import InstancesClassifier
from utils import PredictionsUtilsCommand as p_utils

INSTANCES_DATASET_PATH = "datasets/com_instances_dataset.csv"
INSTANCES_FEATURE_COLUMNS = ['InvokeMethod', 'CCRERelationship','HasClient','HasConcreteCommand','HasInvoker','HasReceiver','NumC','IsCommand']
INSTANCES_LABELS = ['NotCommand', 'Command']

FOLDERS_NUMBER = 5

INSTANCES_TRAIN_BATCH_SIZE = 20
INSTANCES_EVALUATE_BATCH_SIZE = 5
INSTANCES_TRAINING_STEPS = 5000

PREDICTIONS_ROOT_DIRECTORY = 'OBSClassifier/tester/predictions'
INSTANCES_MOKUP_PATH = PREDICTIONS_ROOT_DIRECTORY + '/combinations_mockup_command.csv'
INSTANCES_PREDICTIONS_FILE_PATH = PREDICTIONS_ROOT_DIRECTORY + '/com_instances_predictions.csv'
INSTANCES_PREDICTIONS_HEADER = ['Combinations', 'Result', 'Probability']

INSTANCES_COMBINATIONS_HEADER = INSTANCES_FEATURE_COLUMNS[:len(INSTANCES_FEATURE_COLUMNS) - 1]
INSTANCES_COMBINATIONS_FILE_PATH = PREDICTIONS_ROOT_DIRECTORY + '/combinations_to_test_command.csv'


# INSTANCES_MOKUP_PATH             = PREDICTIONS_ROOT_DIRECTORY +'/combinations_mokup.csv'
# INSTANCES_PREDICTIONS_FILE_PATH  = PREDICTIONS_ROOT_DIRECTORY +'/instances_predictions_Command.csv'
# INSTANCES_PREDICTIONS_HEADER     = ['Combinations','Result','Probability']

def main():
    SW_CLASSES_COMBINATIONS_BATCH_SIZE = 8

    instancesClassifier = InstancesClassifier(INSTANCES_FEATURE_COLUMNS, INSTANCES_LABELS, FOLDERS_NUMBER)
    instancesClassifier.initFeatureColumns()
    instancesClassifier.initClassifier()
    instancesClassifier.loadDataset(INSTANCES_DATASET_PATH, 0, ';')
    instancesClassifier.suffleDataset()
    instancesClassifier.kFoldersTrainAndEvaluation(INSTANCES_TRAIN_BATCH_SIZE, INSTANCES_TRAINING_STEPS,
                                                   INSTANCES_EVALUATE_BATCH_SIZE, True)

    instances, instances_predictions = instancesClassifier.predict(INSTANCES_MOKUP_PATH, 0, ';',SW_CLASSES_COMBINATIONS_BATCH_SIZE)
    instances_predictions_list = p_utils.get_instances_predictions_list(instances, instances_predictions,INSTANCES_LABELS)
    p_utils.log_predictions_on_file(PREDICTIONS_ROOT_DIRECTORY, INSTANCES_PREDICTIONS_FILE_PATH,INSTANCES_PREDICTIONS_HEADER, instances_predictions_list)
    print("Output has been produced in predictions folder")


if __name__ == '__main__':
    os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
    main()

