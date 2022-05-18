import os
from utils import PredictionsUtilsCommand as p_utils
from classifiers.RolesClassifierCommand import RolesClassifier
from classifiers import InstancesClassifierTesterCommand

ROLES_DATASET_PATH        = 'datasets/com_roles_dataset.csv'
ROLES_FEATURE_COLUMNS     = ['ClassType','ClassDeclarationKeyword',
                             'MethodsDeclarationKeyword','ExecutesCommand', 'InstantiatesCommand', 'HasSuperclass',
                             'ImplementsInterfaces','IsPartOfExecute','Role']
ROLES_LABELS              = ['CommandInterface', 'ConcreteCommand', 'Invoker', 'Receiver', 'Client','None']

ROLES_TRAIN_BATCH_SIZE    = 20
ROLES_EVALUATE_BATCH_SIZE = 5
ROLES_TRAINING_STEPS      = 5500

FOLDERS_NUMBER = 2

PREDICTIONS_ROOT_DIRECTORY       = 'OBSClassifier/tester/predictions'
SOFTWARES_ROOT_DIRECTORY         = 'softwares'
ROLES_PREDICTIONS_HEADER         = ['Class','Role','Probability']
ROLES_PREDICTIONS_FILE_PATH      = PREDICTIONS_ROOT_DIRECTORY + '/roles_predictions_command.csv'
SW_PATH                          = SOFTWARES_ROOT_DIRECTORY+'/TestSoftware2.csv'

def main():
    SW_ROLES_BATCH_SIZE=8

    rolesClassifier=RolesClassifier(ROLES_FEATURE_COLUMNS,ROLES_LABELS,FOLDERS_NUMBER)
    rolesClassifier.initFeatureColumns()
    rolesClassifier.initClassifier()
    rolesClassifier.loadDataset(ROLES_DATASET_PATH,0,';')
    rolesClassifier.suffleDataset()
    rolesClassifier.kFoldersTrainAndEvaluation(ROLES_TRAIN_BATCH_SIZE,ROLES_TRAINING_STEPS,ROLES_EVALUATE_BATCH_SIZE,True)

    sw_classes,roles_predictions = rolesClassifier.predict(SW_PATH, 0, ';', SW_ROLES_BATCH_SIZE)
    roles_predictions_list=p_utils.get_roles_predictions_list(sw_classes,roles_predictions,ROLES_LABELS)
    p_utils.log_predictions_on_file(PREDICTIONS_ROOT_DIRECTORY,ROLES_PREDICTIONS_FILE_PATH,ROLES_PREDICTIONS_HEADER,roles_predictions_list)

    classes_pairs_pred,classes_triplets_pred, classes_quadruplets_pred, classes_quintuplets_pred = p_utils.roles_combinations(roles_predictions_list)

    classes_pairs_act,classes_pairs_roles = p_utils.filter_pairs_list(roles_predictions_list, classes_pairs_pred)
    classes_triplets_act, classes_triplets_roles = p_utils.filter_triplets_list(roles_predictions_list, classes_triplets_pred)
    classes_quadruplets_act, classes_quadruplets_roles = p_utils.filter_quadruplets_list(roles_predictions_list,classes_quadruplets_pred)
    classes_quintuplets_act, classes_quintuplets_roles = p_utils.filter_quintuplets_list(roles_predictions_list,classes_quintuplets_pred)

    #combinations = [classes_pairs_act, classes_triplets_act]
    #roles = [classes_pairs_roles, classes_triplets_roles]

    p_utils.log_combinations_on_file(InstancesClassifierTesterCommand.INSTANCES_COMBINATIONS_FILE_PATH, InstancesClassifierTesterCommand.INSTANCES_COMBINATIONS_HEADER, classes_pairs_act, classes_triplets_act, classes_quadruplets_act, classes_quintuplets_act, classes_pairs_roles, classes_triplets_roles, classes_quadruplets_roles, classes_quintuplets_roles)

    print('The combinations to test as observer instances are in '+InstancesClassifierTesterCommand.INSTANCES_COMBINATIONS_FILE_PATH)
    print('Please, assign values to features columns before proceeding.')

if __name__ == '__main__':
    os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'
    main()
