import csv
import random
tmp = []

for id in range(1, 7):
    f = open('cat' + str(id) +'.csv', 'r')
    index = 0
    with f:
        reader = csv.DictReader(f)
        for row in reader:
            if len(row) != 18 or 'None' in str(row['市场价格']):
                continue;
            if id == 1:
                tmp.append([])
                tmp[index].append(int(row['商品ID']))
            tmp[index].append(str(row['市场价格']))
            index += 1
it = 0
for row in tmp:
    for colID in range(1, len(row)):
        thisStr = str(row[colID])
        if '-' in thisStr:
            newList = thisStr.split('-')
            row[colID] = newList[0]
        row[colID] = float(row[colID])
    row.append(it)
    it += 1
    # bar = random.random()
    # if bar > 0.7:
    #     row.append(float(row[-1])*(1+random.random()))
    #     row.append(int(1))
    # else:
    #     row.append(row[-1])
    #     row.append(int(0))

for row in tmp:
    print(len(row))
    print(row)

f = open('test.csv', 'w')
print(len(tmp))
with f:

    writer = csv.writer(f)

    for row in tmp:
        writer.writerow(row)