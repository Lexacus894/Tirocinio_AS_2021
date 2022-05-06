import itertools
import logging
import sys
import csv
import os

COMBINATIONS_ROLES_PREFIX=[['CI','CC','I','R','C']

                           ]

def get_roles_predictions_list(data, predictions, labels):
    i = 0
    classNames = []
    predictionResults = dict()

    for row, column in data.iterrows():
        classNames.append(row[2])

    for pred in predictions:
        class_id = pred['class_ids'][0]
        probability = pred['probabilities'][class_id]

        if (class_id != 5):
            predictionResults.update({classNames[i]: (labels[class_id], probability * 100)})
            #print(classNames[i] + " ha probabilità CI " + (str(pred['probabilities'][0] * 100)) + " e ha probabilità IN " + (str(pred['probabilities'][2] * 100)))
        i = i + 1

    return predictionResults

def get_instances_predictions_list(data, predictions, labels):
    i = 0
    classNames = []
    predictionResults = dict()

    for row, column in data.iterrows():
        classNames.append(row)

    for pred in predictions:
        class_id = pred['class_ids'][0]
        probability = pred['probabilities'][class_id]
        predictionResults.update({classNames[i]: (labels[class_id], probability * 100)})
        i = i + 1

    return predictionResults

def roles_permutation(predictions_list):
    pairs_list = list(itertools.permutations(predictions_list, 2))
    #print(pairs_list)
    return (pairs_list)

def get_quadruplets_list(predictions_list,pairs_list):
    abs_permutation_list = []
    con_permutation_list = []
    for item in pairs_list:
        roleOne = predictions_list[item[0]][0]
        roleTwo = predictions_list[item[1]][0]
        if (roleOne == 'CommandInterface' and roleTwo == 'ConcreteCommand') or (roleOne == 'ConcreteCommand' and roleTwo == 'CommandInterface') :
            abs_permutation_list.append(item)
        elif roleOne == 'Subject' and roleTwo == 'Subject':
            abs_permutation_list.append(item)
        elif roleOne == 'Observer' and roleTwo == 'Observer':
            abs_permutation_list.append(item)
        elif (roleOne == 'ConcreteSubject' and roleTwo == 'ConcreteObserver') or (roleOne == 'ConcreteObserver' and roleTwo == 'ConcreteSubject') :
            con_permutation_list.append(item)
        elif roleOne == 'ConcreteSubject' and roleTwo == 'ConcreteSubject':
            con_permutation_list.append(item)
        elif roleOne == 'ConcreteObserver' and roleTwo == 'ConcreteObserver':
            con_permutation_list.append(item)

    quadruplets_temp_list = list(itertools.product(abs_permutation_list, con_permutation_list))
    quadruplets_list=[]

    for quadruplets in quadruplets_temp_list:
        row=[]
        for pair in quadruplets:
            row.append(pair[0])
            row.append(pair[1])
        quadruplets_list.append(tuple(row))

    return quadruplets_list

def filter_pairs_list(prediction_list, pairs_list):
    pairs = []
    pairs_roles = []
    for item in pairs_list:
        roleOne = prediction_list[item[0]][0]
        roleTwo = prediction_list[item[1]][0]

        if (roleOne == 'CommandInterface' and roleTwo == 'ConcreteCommand'):
            pairs.append(item)
            pairs_roles.append('CI')
            pairs_roles.append('CC')
        #elif (roleOne == 'ConcreteCommand' and roleTwo == 'CommandInterface'):
        #    pairs.append(item)
        #    pairs_roles.append('CC')
        #    pairs_roles.append('CI')
        elif (roleOne == 'Receiver' and roleTwo == 'ConcreteCommand'):
            pairs.append(item)
            pairs_roles.append('RE')
            pairs_roles.append('CC')
        #elif (roleOne == 'ConcreteCommand' and roleTwo == 'Receiver'):
        #    pairs.append(item)
        #    pairs_roles.append('CC')
        #    pairs_roles.append('RE')
        elif (roleOne == 'Invoker' and roleTwo == 'ConcreteCommand'):
            pairs.append(item)
            pairs_roles.append('IN')
            pairs_roles.append('CC')
        #elif (roleOne == 'ConcreteCommand' and roleTwo == 'Invoker'):
        #    pairs.append(item)
        #    pairs_roles.append('CC')
        #    pairs_roles.append('IN')
        elif (roleOne == 'Invoker' and roleTwo == 'Client'):
            pairs.append(item)
            pairs_roles.append('IN')
            pairs_roles.append('CL')
        #elif (roleOne == 'Client' and roleTwo == 'Invoker'):
        #    pairs.append(item)
        #    pairs_roles.append('CL')
        #    pairs_roles.append('IN')

    return (pairs, pairs_roles)

def filter_triplets_list(prediction_list, triplets_list):
    filtered_triplets_list=[]

    for item in triplets_list:
        roleOne = prediction_list[item[0]][0]
        roleTwo = prediction_list[item[1]][0]
        roleThree = prediction_list[item[2]][0]

        if roleOne == 'ConcreteSubject' and roleTwo == 'Observer' and roleThree == 'ConcreteObserver':
            filtered_triplets_list.append(item)
        if roleOne == 'ConcreteSubject' and roleTwo == 'Observer' and roleThree == 'ConcreteSubject':
            filtered_triplets_list.append(item)
        if roleOne == 'ConcreteSubject' and roleTwo == 'Subject' and roleThree == 'ConcreteObserver':
            filtered_triplets_list.append(item)
        if roleOne == 'ConcreteSubject' and roleTwo == 'Subject' and roleThree == 'ConcreteSubject':
            filtered_triplets_list.append(item)
        if roleOne == 'ConcreteObserver' and roleTwo == 'Observer' and roleThree == 'ConcreteObserver':
            filtered_triplets_list.append(item)
        if roleOne == 'ConcreteObserver' and roleTwo == 'Observer' and roleThree == 'ConcreteSubject':
            filtered_triplets_list.append(item)
        if roleOne == 'ConcreteObserver' and roleTwo == 'Subject' and roleThree == 'ConcreteObserver':
            filtered_triplets_list.append(item)
        if roleOne == 'ConcreteObserver' and roleTwo == 'Subject' and roleThree == 'ConcreteSubject':
            filtered_triplets_list.append(item)

    return filtered_triplets_list


def get_logger(format,name):
    logging.basicConfig(stream=sys.stdout, level=logging.DEBUG, format=format)
    logger=logging.getLogger(name=name)
    return logger

def log_combinations_on_file(path,header,combinations,roles):
    with open(path, "w") as fp:
        writer = csv.writer(fp, delimiter=";", dialect="excel", lineterminator="\n")
        writer.writerow(header)
        for i,classes_set in enumerate(combinations):
            row = ''
            for j,combination in enumerate(classes_set):
                if ((2*i+1) < len(roles)):
                    if j==1:
                        row = row + combination + ' - ' + roles[2*i+1]
                    else:
                        row = row + combination + ' - ' + roles[2*i] + ', '
            writer.writerow([row])

def log_predictions_on_file(root_directory,path,header,predictions):
    if not os.path.exists(root_directory):
        os.makedirs(root_directory)
    with open(path,"w") as fp:
        writer=csv.writer(fp,delimiter=";", dialect="excel", lineterminator="\n")
        writer.writerow(header)
        for key in predictions:
            writer.writerow([key,predictions[key][0],"%.2f" % round(predictions[key][1],2)])