
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

f = open('MC.txt','r')
linea = f.readline()
linea = f.readline()

while not linea == "":
    linea = linea.split()
    for i in range(len(linea)-1):
        costosGrafo[contador][i] = linea[i+1]
    contador +=1
    linea = f.readline()

print (costosGrafo[9][34])
