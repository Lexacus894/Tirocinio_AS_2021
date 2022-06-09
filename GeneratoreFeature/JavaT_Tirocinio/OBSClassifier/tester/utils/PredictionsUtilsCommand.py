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

            if ((probability * 100)<50):
                for x in range(5):
                    if (pred['probabilities'][x] * 100)>19:
                        predictionResults.update({classNames[i] + " (" + labels[x] + ")": (labels[x], pred['probabilities'][x] * 100)})
            else:
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

def roles_combinations(predictions_list):
    pairs_list = list(itertools.combinations(predictions_list, 2))
    triplets_list = list(itertools.combinations(predictions_list, 3))
    quadruplets_list = list(itertools.combinations(predictions_list, 4))
    quintuplets_list = list(itertools.combinations(predictions_list, 5))
    #print(pairs_list)
    return (pairs_list,triplets_list, quadruplets_list, quintuplets_list)
    #return (triplets_list, quadruplets_list, quintuplets_list)

def filter_pairs_list(prediction_list, pairs_list):
    filtered_pairs_list = []
    pairs_roles = []
    roles = [1, 2]

    for item in pairs_list:
        roles[0] = prediction_list[item[0]][0]
        roles[1] = prediction_list[item[1]][0]

        if (roles[0] != roles[1]):
            filtered_pairs_list.append(item)
            for i in range(2):
                if (roles[i] == 'CommandInterface'):
                    pairs_roles.append('CI')
                elif (roles[i] == 'ConcreteCommand'):
                    pairs_roles.append('CC')
                elif (roles[i] == 'Invoker'):
                    pairs_roles.append('IN')
                elif (roles[i] == 'Receiver'):
                    pairs_roles.append('RE')
                elif (roles[i] == 'Client'):
                    pairs_roles.append('CL')

    return (filtered_pairs_list, pairs_roles)

def filter_triplets_list(prediction_list, triplets_list):
    filtered_triplets_list=[]
    triplets_roles = []
    roles = [1,2,3]

    for item in triplets_list:
        roles[0] = prediction_list[item[0]][0]
        roles[1] = prediction_list[item[1]][0]
        roles[2] = prediction_list[item[2]][0]

        if (roles[0] != roles[1] and roles[0] != roles[2] and roles[1] != roles[2]):
            filtered_triplets_list.append(item)
            for i in range(3):
                if (roles[i] == 'CommandInterface'):
                    triplets_roles.append('CI')
                elif (roles[i] == 'ConcreteCommand'):
                    triplets_roles.append('CC')
                elif (roles[i] == 'Invoker'):
                    triplets_roles.append('IN')
                elif (roles[i] == 'Receiver'):
                    triplets_roles.append('RE')
                elif (roles[i] == 'Client'):
                    triplets_roles.append('CL')

    return (filtered_triplets_list,triplets_roles)

def filter_quadruplets_list(predictions_list,quadruplets_list):
    filtered_quadruplets_list = []
    quadruplets_roles = []
    roles = [1,2,3,4]

    for item in quadruplets_list:
        roles[0] = predictions_list[item[0]][0]
        roles[1] = predictions_list[item[1]][0]
        roles[2] = predictions_list[item[2]][0]
        roles[3] = predictions_list[item[3]][0]

        if (roles[0]!=roles[1] and roles[0]!=roles[2] and roles[0]!=roles[3] and roles[1]!=roles[2] and roles[1]!=roles[3] and roles[2]!=roles[3]):
            filtered_quadruplets_list.append(item)
            for i in range(4):
                if (roles[i] == 'CommandInterface'):
                    quadruplets_roles.append('CI')
                elif (roles[i] == 'ConcreteCommand'):
                    quadruplets_roles.append('CC')
                elif (roles[i] == 'Invoker'):
                    quadruplets_roles.append('IN')
                elif (roles[i] == 'Receiver'):
                    quadruplets_roles.append('RE')
                elif (roles[i] == 'Client'):
                    quadruplets_roles.append('CL')

    return (filtered_quadruplets_list, quadruplets_roles)

def filter_quintuplets_list(predictions_list,quintuplets_list):
    filtered_quintuplets_list = []
    quintuplets_roles = []
    roles = [1,2,3,4,5]

    for item in quintuplets_list:
        roles[0] = predictions_list[item[0]][0]
        roles[1] = predictions_list[item[1]][0]
        roles[2] = predictions_list[item[2]][0]
        roles[3] = predictions_list[item[3]][0]
        roles[4] = predictions_list[item[4]][0]


        if (roles[0]!=roles[1] and roles[0]!=roles[2] and roles[0]!=roles[3] and roles[0] != roles[4] and roles[1]!=roles[2] and roles[1]!=roles[3] and roles[1] != roles[4] and roles[2]!=roles[3] and roles[2] != roles[4] and roles[3]!=roles[4]):
            filtered_quintuplets_list.append(item)
            for i in range(5):
                if (roles[i] == 'CommandInterface'):
                    quintuplets_roles.append('CI')
                elif (roles[i] == 'ConcreteCommand'):
                    quintuplets_roles.append('CC')
                elif (roles[i] == 'Invoker'):
                    quintuplets_roles.append('IN')
                elif (roles[i] == 'Receiver'):
                    quintuplets_roles.append('RE')
                elif (roles[i] == 'Client'):
                    quintuplets_roles.append('CL')

    return (filtered_quintuplets_list, quintuplets_roles)

def get_logger(format,name):
    logging.basicConfig(stream=sys.stdout, level=logging.DEBUG, format=format)
    logger=logging.getLogger(name=name)
    return logger

def log_combinations_on_file(path,header,pairs,triplets,quadruplets, quintuplets,pairs_roles,triplets_roles,quadruplets_roles, quintuplets_roles):
    with open(path, "w") as fp:
        writer = csv.writer(fp, delimiter=";", dialect="excel", lineterminator="\n")
        writer.writerow(header)
        #pairs combinations
        for i,classes_set in enumerate(pairs):
            row = ''
            for j,pairs in enumerate(classes_set):
                if ((2*i+1) < len(pairs_roles)):
                    if j==1:
                        row = row + pairs + ' - ' + pairs_roles[2*i+1]
                    else:
                        row = row + pairs + ' - ' + pairs_roles[2*i] + ', '
            writer.writerow([row])

        #triplets combinations
        for i,classes_set in enumerate(triplets):
            row = ''
            for j,triplets in enumerate(classes_set):
                if ((3*i+2) < len(triplets_roles)):
                    if j==2:
                        row = row + triplets + ' - ' + triplets_roles[3*i+2]
                    elif j==1:
                        row = row + triplets + ' - ' + triplets_roles[3 * i + 1] + ', '
                    else:
                        row = row + triplets + ' - ' + triplets_roles[3*i] + ', '
            writer.writerow([row])
        # quadruplets combinations
        for i, classes_set in enumerate(quadruplets):
            row = ''
            for j, quadruplets in enumerate(classes_set):
                if ((4 * i + 3) < len(quadruplets_roles)):
                    if j == 3:
                        row = row + quadruplets + ' - ' + quadruplets_roles[4 * i + 3]
                    elif j == 2:
                        row = row + quadruplets + ' - ' + quadruplets_roles[4 * i + 2] + ', '
                    elif j == 1:
                        row = row + quadruplets + ' - ' + quadruplets_roles[4 * i + 1] + ', '
                    else:
                        row = row + quadruplets + ' - ' + quadruplets_roles[4 * i] + ', '
            writer.writerow([row])
        # quintuplets combinations
        for i, classes_set in enumerate(quintuplets):
            row = ''
            for j, quintuplets in enumerate(classes_set):
                if ((5 * i + 4) < len(quintuplets_roles)):
                    if j == 4:
                        row = row + quintuplets + ' - ' + quintuplets_roles[5 * i + 4]
                    elif j == 3:
                        row = row + quintuplets + ' - ' + quintuplets_roles[5 * i + 3] + ', '
                    elif j == 2:
                        row = row + quintuplets + ' - ' + quintuplets_roles[5 * i + 2] + ', '
                    elif j == 1:
                        row = row + quintuplets + ' - ' + quintuplets_roles[5 * i + 1] + ', '
                    else:
                        row = row + quintuplets + ' - ' + quintuplets_roles[5 * i] + ', '
            writer.writerow([row])

def log_predictions_on_file(root_directory,path,header,predictions):
    if not os.path.exists(root_directory):
        os.makedirs(root_directory)
    with open(path,"w") as fp:
        writer=csv.writer(fp,delimiter=";", dialect="excel", lineterminator="\n")
        writer.writerow(header)
        for key in predictions:
            writer.writerow([key,predictions[key][0],"%.2f" % round(predictions[key][1],2)])