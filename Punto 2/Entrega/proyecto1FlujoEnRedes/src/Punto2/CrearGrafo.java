package Punto2;

import java.util.ArrayList;

public class CrearGrafo {

	private static ArrayList<ArrayList<Nodo>> rutas;
	private static int[][] matriz;
	private static int[][] matrizLongitud;
	private static int[] demandaNodo;
	private static ArrayList<Nodo> nodos;
	
	private static int[][] matrizAdyacencia;
	private static int[][] matrizCostos;

	private static double costoLong;
	private static double costoTiemp;
	private static double costoDemora;
	private static int capacidadMax;

	public static void main(String[] args)
	{
		boolean continuar = true;
		int contador=1;
		while(continuar)
		{
			if(matriz[0][contador]>0){
				anadirRutas(contador);
			}
			contador++;
			if(contador > matriz.length)
			{
				continuar = false;
			}
		}
	}

	public static void anadirRutas(int contador)
	{
		ArrayList<Nodo> rutaActual = new ArrayList<Nodo>();
		rutas = new ArrayList<ArrayList<Nodo>>(); 
		//Agregar nodo inicial a la ruta
		String nombre = "Ru"+contador+"Cl"+contador+"ca"+demandaNodo[contador-1];
		Nodo nuevo = new Nodo(contador,nombre,demandaNodo[contador-1]);
		nodos.add(nuevo);
		ArrayList<Nodo> nodosNuevos = new ArrayList<Nodo>();
		nodosNuevos.add(nuevo);
		rutaActual.add(nuevo);
		for(int i=0; i<matriz.length;i++)
		{
			//Agregar siguiente nivel de profundidad a la ruta
			if(matriz[contador][i]>0)
			{
				if(i == 0){
					//anadir a la ruta el ultimo nodo
					rutas.add(rutaActual);
				}
				else
				{
					int demandaAcum = nodosNuevos.get(nodosNuevos.size()-1).capacidad+demandaNodo[i-1];
					nombre = "Ru"+contador+"Cl"+i+"ca"+demandaAcum;
					Nodo sigu = new Nodo(i,nombre,demandaAcum);
					nodosNuevos.add(sigu);
					nodos.add(sigu);
					anadirRutasProfundidad(nodosNuevos,contador);
				}

			}
		}
	}


	public static void anadirRutasProfundidad(ArrayList<Nodo> nodosNuevos, int numRuta)
	{
		Nodo nodoIn = nodosNuevos.get(nodosNuevos.size()-1);
		ArrayList<Nodo> rutaActual =nodosNuevos;
		for(int i=0; i<matriz.length;i++)
		{
			//Agregar siguiente nivel de profundidad a la ruta
			if(matriz[nodoIn.nodo][i]>0)
			{
				if(i == 0){
					//anadir a la ruta el ultimo nodo
					rutas.add(rutaActual);
				}
				else
				{
					if(!nodoEnRuta(i, nodosNuevos))
					{
						int demandaAcum = nodosNuevos.get(nodosNuevos.size()-1).capacidad+demandaNodo[i-1];
						if(demandaAcum <= capacidadMax)
						{
							String nombre = "Ru"+numRuta+"Cl"+i+"ca"+demandaAcum;
							Nodo sigu = new Nodo(i,nombre,demandaAcum);
							nodosNuevos.add(sigu);
							nodos.add(sigu);
							anadirRutasProfundidad(nodosNuevos,numRuta);	
						}
					}

				}

			}
		}
	}


	public static void construirMatrices()
	{
		matrizAdyacencia = new int[nodos.size()+1][nodos.size()+1];
		matrizCostos = new int[nodos.size()+1][nodos.size()+1];
		//Matriz adyacencia
		ArrayList ruta = new ArrayList<Nodo>();
		for(int i=0;i<rutas.size();i++)
		{
			ruta = rutas.get(i);
			for(int j=0;j< ruta.size();j++)
			{
				
			}
		}
		//Matriz costos
		
	}


	public static boolean nodoEnRuta(int nodo, ArrayList<Nodo> ruta){
		boolean enRuta = false;
		for(int i = 0; i<ruta.size();i++)
		{
			int num = ruta.get(i).nodo;
			if(num == nodo)
				enRuta=true;
		}
		return enRuta;
	}

	private static class Nodo{
		public int nodo;
		public String nombre;
		public int capacidad;

		public Nodo(int pNodo, String pNombre, int pCapacidad)
		{
			nodo = pNodo;
			nombre=pNombre;
			capacidad=pCapacidad;
		}
	}
}



