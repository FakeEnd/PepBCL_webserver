# -*- coding: utf-8 -*-
# @Time    : 2021/5/7 16:50
# @Author  : WANG Ruheng
# @Email   : blwangheng@163.com
# @IDE     : PyCharm
# @FileName: data_loader_protBert.py

import pickle
import torch
import torch.utils.data as Data
import numpy as np

from configuration import config
from util import util_file


def toInt(l):
    l = [int(i) for i in l]
    return l

def make_data_with_unified_length(seq_list, labels, config):
    # pssm_pad = [toInt(20*'0 '.split())]
    data = []
    for i in range(len(labels)):
        labels[i] = [0] + labels[i] + [0]
        # pssm[i] = pssm_pad + pssm[i] +pssm_pad
        data.append([seq_list[i], labels[i]])

    # print('-' * 20, '[make_data_with_unified_length]: check token_list head', '-' * 20)
    # print('max_len + 1', max_len)
    # print('token_list + [pad]', seq_list[0:5])
    return data


# 构造迭代器
def construct_dataset(data, config):
    cuda = config.cuda
    batch_size = config.batch_size

    # print('-' * 20, '[construct_dataset]: check data dimension', '-' * 20)
    # print('len(data)', len(data))
    # print('len(data[0])', len(data[0]))
    # print('len(data[0][0])', len(data[0][0]))
    # print('data[0][1]', data[0][1])
    # print('len(data[1][0])', len(data[1][0]))
    # print('data[1][1]', data[1][1])

    input_ids, labels, pssm = zip(*data)

    if cuda:
        input_ids, labels, pssm = torch.cuda.LongTensor(input_ids), torch.cuda.LongTensor(labels), torch.cuda.LongTensor(pssm)
    else:
        input_ids, labels, pssm = torch.LongTensor(input_ids), torch.LongTensor(labels), torch.LongTensor(pssm)

    print('-' * 20, '[construct_dataset]: check data device', '-' * 20)
    print('input_ids.device:', input_ids.device)
    print('labels.device:', labels.device)

    print('-' * 20, '[construct_dataset]: check data shape', '-' * 20)
    print('input_ids:', input_ids.shape)  # [num_sequences, seq_len]
    print('labels:', labels.shape)  # [num_sequences, seq_len]

    data_loader = Data.DataLoader(MyDataSet(input_ids, labels, pssm),
                                  batch_size=batch_size,
                                  shuffle=True,
                                  drop_last=False)

    print('len(data_loader)', len(data_loader))
    return data_loader


class MyDataSet(Data.Dataset):
    def __init__(self, input_ids, labels, pssm):
        self.input_ids = input_ids
        self.labels = labels
        self.pssm = pssm

    def __len__(self):
        return len(self.input_ids)

    def __getitem__(self, idx):
        return self.input_ids[idx], self.labels[idx], self.pssm[idx]


def load_data(predict_path):
    path_data_test = predict_path
    # sequences_test = util_file.load_tsv_format_data(path_data_test)
    # data_test = make_data_with_unified_length(sequences_test, config)

    # python 读取csv文件
    names_test, sequences_test = util_file.load_tsv_multi_data(path_data_test)

    return names_test, sequences_test
