package Punto2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Solucion2 {

	private static int[][] matriz;
	private static double[][] matrizLongitud;
	private static double[][] matrizTiempo;
	private static int[] demandaNodo;
	
	private static int[][] matrizAdyacencia;
	private static double[][] matrizCostos;

	private static double costoLong;
	private static double costoTiemp;
	private static double costoDemora;
	private static double costoFijo;
	private static int capacidadMax;
	private static int[] b;
	private static int V;
	private static boolean hayUnidadesdeSobra=false;
	private static int n;
	
	public static void main(String[] args) {
		//Cargar datos archivos .txt
		
		//parameters.txt
		String line = "";
		BufferedReader bf;
		try{
			bf= new BufferedReader(new FileReader("./data/parameters.txt"));
			while(line != null)
			{
				line = bf.readLine();
				if(line !=null)
				{
					String[] parametro =line.split(":");
					switch(parametro[0])
					{
					case "n" : n=Integer.parseInt(parametro[1]); break;
					case "V" : V=Integer.parseInt(parametro[1]); break;
					case "Mf" : costoFijo = Double.parseDouble(parametro[1]); break;
					case "Mv" : costoLong = Double.parseDouble(parametro[1]); break;
					case "Mt" : costoTiemp = Double.parseDouble(parametro[1]); break;
					case "Cm" : costoDemora = Double.parseDouble(parametro[1]); break;
					case "C" : capacidadMax = Integer.parseInt(parametro[1]); break;
					}
				}
				
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Cargar demanda
		demandaNodo = new int[n];
		try{
			bf= new BufferedReader(new FileReader("./data/d_j.txt"));
			line="";
			while(line != null)
			{
				line = bf.readLine();
				if(line !=null)
				{
					String[] parametro =line.split(",");
					int numCliente = Integer.parseInt(parametro[0]);
					int demandaN = Integer.parseInt(parametro[1]);
					demandaNodo[numCliente-1] = demandaN;
				}
				
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//Cargar tiempos y longitudes de arcos
		matriz = new int[n+1][n+1];
		matrizLongitud = new double[n+1][n+1];
		matrizTiempo = new double [n+1][n+1];
		BufferedReader bfL; 
		try{
			bf= new BufferedReader(new FileReader("./data/Tij.txt"));
			bfL= new BufferedReader(new FileReader("./data/Lij.txt"));
			line="";
			String lineL="";
			while(line != null && lineL!=null)
			{
				line = bf.readLine();
				lineL = bfL.readLine();
				if(line !=null && lineL != null)
				{
					String[] parametro =line.split("\t");
					String[] parametroL = lineL.split("\t");
					
					int numFil = Integer.parseInt(parametro[0]);
					int numCol = Integer.parseInt(parametro[1]);
					
					double tiempo = Double.parseDouble(parametro[2]);
					double longitud = Double.parseDouble(parametroL[2]);
					
					matriz[numFil][numCol]=1;
					matriz[numCol][numFil]=1;
					
					matrizLongitud[numFil][numCol]=longitud;
					matrizLongitud[numCol][numFil]=longitud;
					
					matrizTiempo[numFil][numCol]=tiempo;
					matrizTiempo[numCol][numFil]=tiempo;
					
				}
				
			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		generarBi();
		generarMatrices();
		imprimirEnArchivo();
	}
	
	public static void generarBi()
	{
		int demanda = 0;
		
		for(int i=0; i < demandaNodo.length;i++)
		{
			demanda += demandaNodo[i];
		}
		
		if(V*capacidadMax > demanda)
		{
			hayUnidadesdeSobra = true;
			b=new int[V+demandaNodo.length+1];
			b[b.length-1] = demanda - capacidadMax*V;	
		}
		else
		{
			b=new int[V+demandaNodo.length];
		}
		
		for(int i=0;i<V;i++)
		{
			b[i]=capacidadMax;
		}
		for(int i = V;i<V+demandaNodo.length;i++)
		{
			b[i]=demandaNodo[i-V]*(-1);
		}
		
	}
	
	public static void generarMatrices()
	{
		if(hayUnidadesdeSobra)
		{
			matrizAdyacencia = new int[V + n +1][V + n +1];
			matrizCostos = new double[V + n +1][V + n +1];
		}
		else
		{
			matrizAdyacencia = new int[V + n +1][V + n];
			matrizCostos = new double[V + n +1][V + n];
		}
		for(int i =0; i<matrizAdyacencia.length;i++)
		{
			for(int j=0;j<matrizAdyacencia.length;j++)
			{
				if(j<V && i<V)
				{
					matrizAdyacencia[i][j]= 0;
					matrizCostos[i][j]=0;
				}
				else if(i<V && j< matrizAdyacencia.length -1 && j>=V && hayUnidadesdeSobra){
					if(matriz[0][j-V+1]>0)
					{
						matrizAdyacencia[i][j]=1;
						matrizAdyacencia[j][i]=1;
						
						double costoTiempo=matrizTiempo[0][j-V+1]*costoTiemp;
						double costoLongitud=matrizLongitud[0][j-V+1]*costoLong;
						matrizCostos[i][j]=costoTiempo+costoLongitud+costoFijo;
						matrizCostos[j][i]=costoTiempo+costoLongitud;
					}
				}
				else if(i == matrizAdyacencia.length-1 && j != matrizAdyacencia.length-1 && hayUnidadesdeSobra)
				{
					matrizAdyacencia[j][i] = 1;
					matrizAdyacencia[i][j] = 0;
					matrizCostos[i][j] = 0;
					matrizCostos[j][i] = 0;
					
				}
				else if(j==i)
				{
					matrizAdyacencia[i][j]= 0;
					matrizCostos[i][j]=0;
				}
				else if(i>=V && i<matrizAdyacencia.length-1 && j< matrizAdyacencia.length -1 && j>=V && hayUnidadesdeSobra)
				{
					if(matriz[i-V+1][j-V+1]>0)
					{
						matrizAdyacencia[i][j]=1;
						
						double costoTiempo=matrizTiempo[i-V+1][j-V+1]*costoTiemp;
						double costoLongitud=matrizLongitud[i-V+1][j-V+1]*costoLong;
						matrizCostos[i][j] = costoTiempo+costoLongitud;
					}
				}
				
			}
		}
	}

	public static void imprimirEnArchivo()
	{
		String file ="./data/costos.txt";
		String fileAd = "./data/adyacencias.txt";
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			BufferedWriter bwA = new BufferedWriter(new FileWriter(fileAd));
			for(int i=0; i<matrizCostos.length+1; i++)
			{
				for(int j=0;j<matrizCostos.length+1;j++)
				{
					if(i==0)
					{
						if(j==0)
						{
							bw.write("\t");
							bwA.write("\t");
						}
						if(j<V)
						{
							bw.write("Vehiculo"+j+"\t");
							bwA.write("Vehiculo"+j+"\t");
						}
						else if(j>=V && j<matrizCostos.length-1 && hayUnidadesdeSobra)
						{
							bw.write("Cliente"+(j-V+1)+"\t");
							bwA.write("Cliente"+(j-V+1)+"\t");
						}
						else if(j==matrizCostos.length-1 && hayUnidadesdeSobra)
						{
							bw.write("Sobra");
							bwA.write("Sobra");
						}
					}
					else if(j==0 && i>0)
					{
						if(i<=V)
						{
							bw.write("Vehiculo"+(i-1)+"\t");
							bwA.write("Vehiculo"+(i-1)+"\t");
						}
						else if(i>V && i<matrizCostos.length && hayUnidadesdeSobra)
						{
							bw.write("Cliente"+(i-V)+"\t");
							bwA.write("Cliente"+(i-V)+"\t");
						}
						else if(i==matrizCostos.length && hayUnidadesdeSobra)
						{
							bw.write("Sobra"+"\t");
							bwA.write("Sobra"+"\t");
						}
					}
					else if(j>0 && i>0)
					{
						bw.write(matrizCostos[i-1][j-1]+"\t");
						bwA.write(matrizAdyacencia[i-1][j-1]+"\t");
					}
					else if(i==0 &&j==0)
					{
						bw.write("\t");
						bwA.write("\t");
					}
					
					if(j==matrizCostos.length && i<matrizCostos.length)
					{
						bw.newLine();
						bwA.newLine();
					}
				}
			}
			bw.close();
			bwA.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
