from gurobipy import *

nodos = 1707
grafo = [[0 for x in range(nodos)] for y in range(nodos)]
costosGrafo = [[0 for x in range(nodos)] for y in range(nodos)]

contador = 0

f = open('MA.txt','r')
linea = f.readline()
linea = f.readline()

while not linea == "":
    linea = linea.split()
    for i in range(len(linea)-1):
        grafo[contador][i] = linea[i+1]
    contador +=1
    linea = f.readline()

contador = 0

f.close()

f = open('MC.txt','r')
linea = f.readline()
linea = f.readline()

while not linea == "":
    linea = linea.split()
    for i in range(len(linea)-1):
        costosGrafo[contador][i] = linea[i+1]
    contador +=1
    linea = f.readline()

f.close()

numArcos = 0

for i in range(nodos):
    for j in range(nodos):
        if (grafo[i][j] > 0):
            numArcos += 1

A = [[0.0 for x in range(numArcos)] for y in range(nodos)]
c = [0.0 for x in range(numArcos)]



numArcos = 0

for i in range(nodos):
    for j in range(nodos):
        if (grafo[i][j] > 0):
            A[i][numArcos]=1.0
            A[j][numArcos]=-1.0
            c[numArcos]= costosGrafo[i][j]
            numArcos += 1

b = [0.0 for x in range(nodos)]
b[0] = 1.0
b[1] = -1.0

sol = sc.linprog(c, A_ub=None, b_ub=None, A_eq= A, b_eq=b)
