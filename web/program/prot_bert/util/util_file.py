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
