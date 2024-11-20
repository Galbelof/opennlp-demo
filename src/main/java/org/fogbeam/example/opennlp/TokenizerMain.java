package org.fogbeam.example.opennlp;

import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

import java.io.*;

/**
 * Clase principal que utiliza OpenNLP para procesar archivos de texto y dividirlos en tokens.
 * El programa lee archivos de una carpeta, tokeniza el contenido, y genera un archivo único con los resultados.
 */
public class TokenizerMain {

    /**
     * Método principal que inicia el procesamiento de tokenización.
     * 
     * @param args Argumentos de la línea de comandos (no utilizados en este programa).
     * @throws Exception Si ocurre un error al leer archivos o cargar el modelo de OpenNLP.
     */
    public static void main(String[] args) throws Exception {
        // Ruta al modelo de OpenNLP para tokenización
        String modelPath = "src/main/resources/models/tokenizer-model.bin";

        // Directorio de entrada que contiene los archivos a procesar
        String inputFolderPath = "src/main/resources/entrada";

        // Archivo de salida donde se escribirán los tokens generados
        String outputFilePath = "salida_tokens.txt";

        // Inicializar tokenizador y procesar los archivos
        processFiles(modelPath, inputFolderPath, outputFilePath);
    }

    /**
     * Procesa los archivos de texto en la carpeta especificada, tokeniza el contenido
     * y escribe los tokens en un archivo de salida.
     * 
     * @param modelPath Ruta al modelo de OpenNLP para tokenización.
     * @param inputFolderPath Ruta de la carpeta que contiene los archivos de texto a procesar.
     * @param outputFilePath Ruta del archivo donde se escribirán los tokens generados.
     * @throws IOException Si ocurre un error al leer o escribir archivos.
     */
    private static void processFiles(String modelPath, String inputFolderPath, String outputFilePath) throws IOException {
        // Cargar el modelo de tokenización
        InputStream modelIn = new FileInputStream(modelPath);
        TokenizerModel model = new TokenizerModel(modelIn);
        TokenizerME tokenizer = new TokenizerME(model);

        // Leer los archivos del directorio de entrada
        File folder = new File(inputFolderPath);
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("No se encontraron archivos en la carpeta de entrada.");
            return;
        }

        // Crear el archivo de salida
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

        for (File file : files) {
            System.out.println("Procesando archivo: " + file.getName());
            processFile(file, tokenizer, writer);
        }

        writer.close();
        System.out.println("Procesamiento completado. Tokens escritos en: " + outputFilePath);
    }

    /**
     * Procesa un archivo de texto, tokeniza cada línea y escribe los tokens en el archivo de salida.
     * 
     * @param file Archivo de texto a procesar.
     * @param tokenizer Instancia de TokenizerME para realizar la tokenización.
     * @param writer BufferedWriter para escribir los tokens generados en el archivo de salida.
     * @throws IOException Si ocurre un error al leer el archivo de entrada o escribir el archivo de salida.
     */
    private static void processFile(File file, TokenizerME tokenizer, BufferedWriter writer) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            // Tokenizar la línea
            String[] tokens = tokenizer.tokenize(line);

            // Escribir los tokens en el archivo de salida
            writer.write(String.join(" ", tokens));
            writer.newLine();
        }

        reader.close();
    }
}
