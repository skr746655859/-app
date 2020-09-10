import csv
import random
tmp = []

f = open('bottle.csv', 'r')
for id in range(1, 7):
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
for row in tmp:
    for colID in range(1, len(row)):
        thisStr = str(row[colID])
        if '-' in thisStr:
            newList = thisStr.split('-')
            row[colID] = newList[0]
        row[colID] = float(row[colID])
    bar = random.random()
    if bar > 0.7:
        row.append(float(row[-1])*(1+random.random()))
    else:
        row.append(row[-1])

for row in tmp:
    print(len(row))
    print(row)

f = open('bottleFinal.csv', 'w')
print(len(tmp))
with f:

    writer = csv.writer(f)

    for row in tmp:
        writer.writerow(row)