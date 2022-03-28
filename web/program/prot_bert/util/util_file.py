import pandas

def load_tsv_format_data(filename, skip_head=True):
    sequences = []

    with open(filename, 'r') as file:
        if skip_head:
            next(file)
        for line in file:
            if line[-1] == '\n':
                line = line[:-1]
            # l = line.split('\t')
            sequences.append(line)

    return sequences

def load_tsv_multi_data(filename, skip_head=True):
    sequences = []
    names = []
    with open(filename, 'r') as file:
        if skip_head:
            next(file)
        for line in file:
            if line[-1] == '\n':
                line = line[:-1]
            list = line.split('\t')
            sequences.append(list[2])
            names.append(list[1])

    return names, sequences