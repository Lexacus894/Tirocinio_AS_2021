import os
from utils import PredictionsUtilsCommand as p_utils
from classifiers.RolesClassifierCommand import RolesClassifier
from tkinter import Tk  
from tkinter.filedialog import askopenfilename

ROLES_DATASET_PATH        = 'datasets/com_roles_dataset.csv'
ROLES_FEATURE_COLUMNS     = ['ClassType','ClassDeclarationKeyword',
                             'MethodsDeclarationKeyword','ImportDeclarationKeyword','ExecutesCommand', 'InstantiatesCommand', 'HasSuperclass',
                             'ImplementsInterfaces','IsPartOfExecute','Role']
ROLES_LABELS              = ['CommandInterface', 'ConcreteCommand', 'Invoker', 'Receiver', 'Client','None']

INSTANCES_FEATURE_COLUMNS = ['Classes']

ROLES_TRAIN_BATCH_SIZE    = 8
ROLES_EVALUATE_BATCH_SIZE = 8
ROLES_TRAINING_STEPS      = 250

FOLDERS_NUMBER = 10

PREDICTIONS_ROOT_DIRECTORY       = 'results/command/roles_predictions'
COMBINATIONS_ROOT_DIRECTORY = "results/combinations"
#SOFTWARES_ROOT_DIRECTORY         = 'softwares'
ROLES_PREDICTIONS_HEADER         = ['Class','Role','Probability']
ROLES_PREDICTIONS_FILE_PATH      = PREDICTIONS_ROOT_DIRECTORY + '/roles_predictions_command'
#SW_PATH                          = SOFTWARES_ROOT_DIRECTORY+'/TestSoftware2.csv'

INSTANCES_COMBINATIONS_HEADER    = INSTANCES_FEATURE_COLUMNS[:len(INSTANCES_FEATURE_COLUMNS)-1]
INSTANCES_COMBINATIONS_FILE_PATH = COMBINATIONS_ROOT_DIRECTORY + '/combinations_to_test_command.csv'

def main():
    SW_ROLES_BATCH_SIZE=8

    rolesClassifier=RolesClassifier(ROLES_FEATURE_COLUMNS,ROLES_LABELS,FOLDERS_NUMBER)
    rolesClassifier.initFeatureColumns()
    rolesClassifier.initClassifier()
    rolesClassifier.loadDataset(ROLES_DATASET_PATH,0,';')
    rolesClassifier.suffleDataset()
    rolesClassifier.kFoldersTrainAndEvaluation(ROLES_TRAIN_BATCH_SIZE,ROLES_TRAINING_STEPS,ROLES_EVALUATE_BATCH_SIZE,True)

    Tk().withdraw() 
    filename = askopenfilename(initialdir="./results/command/role_features") 
    fileBaseName = os.path.basename(filename)
    projectName = fileBaseName.split("_")[3]

    sw_classes,roles_predictions = rolesClassifier.predict(filename, 0, ';', SW_ROLES_BATCH_SIZE)
    roles_predictions_list=p_utils.get_roles_predictions_list(sw_classes,roles_predictions,ROLES_LABELS)
    p_utils.log_predictions_on_file(PREDICTIONS_ROOT_DIRECTORY,ROLES_PREDICTIONS_FILE_PATH  + "_" + projectName,ROLES_PREDICTIONS_HEADER,roles_predictions_list)

    classes_pairs_pred,classes_triplets_pred, classes_quadruplets_pred, classes_quintuplets_pred = p_utils.roles_combinations(roles_predictions_list)

    classes_pairs_act,classes_pairs_roles = p_utils.filter_pairs_list(roles_predictions_list, classes_pairs_pred)
    classes_triplets_act, classes_triplets_roles = p_utils.filter_triplets_list(roles_predictions_list, classes_triplets_pred)
    classes_quadruplets_act, classes_quadruplets_roles = p_utils.filter_quadruplets_list(roles_predictions_list,classes_quadruplets_pred)
    classes_quintuplets_act, classes_quintuplets_roles = p_utils.filter_quintuplets_list(roles_predictions_list,classes_quintuplets_pred)

    p_utils.log_combinations_on_file(INSTANCES_COMBINATIONS_FILE_PATH, INSTANCES_COMBINATIONS_HEADER, classes_pairs_act, classes_triplets_act, classes_quadruplets_act, classes_quintuplets_act, classes_pairs_roles, classes_triplets_roles, classes_quadruplets_roles, classes_quintuplets_roles)

    print("Role predictions can be found in " + ROLES_PREDICTIONS_FILE_PATH  + "_" + projectName)
    print('The combinations to test as Command instances are in '+ INSTANCES_COMBINATIONS_FILE_PATH)

if __name__ == '__main__':
    os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
    main()
