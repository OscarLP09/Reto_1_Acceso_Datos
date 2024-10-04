package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * La clase <code>generarHTML</code> es responsable de generar archivos HTML
 * a partir de un archivo CSV que contiene información sobre películas.
 * Utiliza una plantilla HTML para crear la estructura del archivo de salida
 * y escribe un archivo HTML para cada película.
 */
public class generarHTML {

    /**
     * Método principal que inicia la ejecución del programa.
     *
     * @param args Argumentos de línea de comandos (no se utilizan en este programa).
     */
    public static void main(String[] args) {
        // Ruta completa al archivo CSV y al archivo de plantilla HTML
        String peliculas_csv = "C:/Users/OscarLuquePorca/IdeaProjects/Reto_AD_1/peliculas.csv";
        String HTML = "C:/Users/OscarLuquePorca/IdeaProjects/Reto_AD_1/template.html.txt";
        String Salida = "C:/Users/OscarLuquePorca/IdeaProjects/Reto_AD_1/salida";

        try {
            File archivoCSVFile = new File(peliculas_csv);
            if (!archivoCSVFile.exists()) {
                System.err.println("El archivo CSV no existe: " + peliculas_csv);
                return; // Termina el programa si no se encuentra el archivo
            }

            // Leer las películas del archivo CSV
            List<String[]> peliculas = readCSV(peliculas_csv);

            // Verificar si el archivo de plantilla existe
            File plantillaFile = new File(HTML);
            if (!plantillaFile.exists()) {
                System.err.println("El archivo de plantilla HTML no existe: " + HTML);
                return; // Termina el programa si no se encuentra el archivo
            } else {
                System.out.println("Archivo de plantilla encontrado en: " + HTML);
            }

            // Leer la plantilla HTML
            String template = new String(Files.readAllBytes(Paths.get(HTML)));

            // Crear carpeta de salida si no existe
            File directorioSalida = new File(Salida);
            if (!directorioSalida.exists()) {
                directorioSalida.mkdir();
            } else {
                // Vaciar la carpeta de salida
                for (File archivo : directorioSalida.listFiles()) {
                    archivo.delete();
                }
            }

            // Generar un archivo HTML para cada película
            for (String[] pelicula : peliculas) {
                genera_Archivo_HTML(pelicula, template, Salida);
            }

            System.out.println("Archivos HTML generados correctamente en la carpeta 'salida'.");

        } catch (Exception e) {
            System.err.println("Ocurrió un error: " + e.getMessage());
            e.printStackTrace(); // Imprimir el stack trace para más detalles sobre el error
        }
    }

    /**
     * Método para leer el archivo CSV y almacenar la información de las películas.
     *
     * @param rutaArchivoCSV Ruta al archivo CSV que contiene los datos de las películas.
     * @return Una lista de arreglos de cadenas, donde cada arreglo representa
     *         una película y sus propiedades (ID, título, año, director, género).
     * @throws IOException Si ocurre un error al leer el archivo.
     */
    public static List<String[]> readCSV(String rutaArchivoCSV) throws IOException {
        List<String[]> peliculas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivoCSV))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                // Dividir la línea en columnas separadas por comas
                String[] datos = linea.split(",");
                // Validar que se tienen todos los campos requeridos
                if (datos.length != 5) {
                    System.err.println("Datos incompletos en la línea: " + linea);
                    continue; // Saltar a la siguiente línea si los datos son incorrectos
                }
                peliculas.add(datos);
            }
        }
        return peliculas;
    }

    /**
     * Método para generar un archivo HTML por cada película.
     *
     * @param pelicula Arreglo de cadenas que contiene los datos de la película
     *                 (ID, título, año, director, género).
     * @param template Plantilla HTML que se utilizará para crear el archivo.
     * @param carpetaSalida Ruta de la carpeta donde se guardarán los archivos HTML generados.
     * @throws IOException Si ocurre un error al crear el archivo HTML.
     */
    public static void genera_Archivo_HTML(String[] pelicula, String template, String carpetaSalida) throws IOException {
        // Reemplazar las variables de la plantilla con los datos de la película
        String htmlContenido = template
                .replace("%%1%%", pelicula[0])   // ID
                .replace("%%2%%", pelicula[1])   // Título
                .replace("%%3%%", pelicula[2])   // Año
                .replace("%%4%%", pelicula[3])   // Director
                .replace("%%5%%", pelicula[4]);  // Género

        // Crear el nombre del archivo HTML
        String nombreArchivo = pelicula[1].replace(" ", "_") + " - " + pelicula[0] + ".html";

        // Crear el archivo HTML en la carpeta de salida
        File archivoSalida = new File(carpetaSalida, nombreArchivo);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida))) {
            writer.write(htmlContenido);
        }
    }
}
