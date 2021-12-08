import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TrabalhoPratico {

    final static int DIM_HORIZONTAL_INDEX = 0;
    final static int DIM_VERTICAL_INDEX = 1;
    final static int ALTERACAO_NIVEL_AGUA = -1;
    final static int QUANTIDADE_DIMENSOES = 2;

    final static String FILE_NAME = "./src/data.txt";
    final static String FILE_DELIMITER = " ";

    static Scanner sc_file;
    static int dimensaoVertical, dimensaoHorizontal;
    static char alinea = 'a';

    public static void main(String[] args) throws FileNotFoundException {

        sc_file = new Scanner(new File(FILE_NAME));

        // Descritivo do terreno
        String descritivoTerreno = sc_file.nextLine();

        // Dimensoes horizontal e vertical
        int[] arrayDimensoes = obterDimensoesTerreno();
        dimensaoHorizontal = arrayDimensoes[DIM_HORIZONTAL_INDEX];
        dimensaoVertical = arrayDimensoes[DIM_VERTICAL_INDEX];

        // Obter cotas do terreno
        int[][] terreno = obterCotasTerreno();

        // Imprime o mapa do terreno
        imprimeMapaTerreno(terreno);

        // Obter e mostrar novo mapa com alteracao do nivel da agua
        int[][] mapaAlterado = calculaNovoMapaTerreno(terreno);
        imprimeMapaTerreno(mapaAlterado);

        // Mostrar percentagem de area do terreno submersa
        mostraPercentagemTerrenoSubmerso(calculaPercentagemTerrenoSubmerso(mapaAlterado));

        // Mostra a variação da área inundada
        mostraVariacaoAreaInundada(terreno, mapaAlterado);


    }


    // Calcula novo mapa do terreno tendo em conta a alteração do nivel da agua
    private static int[][] calculaNovoMapaTerreno(int[][] mapaTerreno) {
        int[][] novoMapa = new int[dimensaoHorizontal][dimensaoVertical];

        for (int linha = 0; linha < dimensaoHorizontal; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical; coluna++) {
                novoMapa[linha][coluna] = mapaTerreno[linha][coluna] - ALTERACAO_NIVEL_AGUA;
            }

        }

        return novoMapa;
    }

    // Obtem as cotas de cada linha do terreno
    private static int[][] obterCotasTerreno() {
        int[][] cotasTerreno = new int[dimensaoHorizontal][dimensaoVertical];
        int numeroLinha = 0;
        String[] arrayAux;
        String cotasLinha;

        while (sc_file.hasNextLine()) {
            cotasLinha = sc_file.nextLine();
            arrayAux = cotasLinha.split(FILE_DELIMITER);

            for (int i = 0; i < arrayAux.length; i++) {
                cotasTerreno[numeroLinha][i] = Integer.parseInt(arrayAux[i]);
            }

            numeroLinha++;
        }

        return cotasTerreno;
    }

    // Obtem as dimensoes do terreno
    public static int[] obterDimensoesTerreno() {
        String[] arrString = sc_file.nextLine().split(FILE_DELIMITER);
        int[] arrInt = new int[QUANTIDADE_DIMENSOES];

        for (int i = 0; i < QUANTIDADE_DIMENSOES; i++) {
            arrInt[i] = Integer.parseInt(arrString[i]);
        }

        return arrInt;
    }


    // Imprime o mapa do terreno com com os valores alinhados à direita
    public static void imprimeMapaTerreno(int[][] mapaTerreno) {
        mostraProximaAlinea();

        for (int linha = 0; linha < dimensaoHorizontal; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical; coluna++) {
                System.out.printf("%5d", mapaTerreno[linha][coluna]);
            }
            System.out.println();
        }
    }

    // Calcula percentagem de terreno submersa
    public static float calculaPercentagemTerrenoSubmerso(int[][] terreno) {
        int quantidade = 0;
        for (int linha = 0; linha < dimensaoHorizontal; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical; coluna++) {
                if (terreno[linha][coluna] < 0) {
                    quantidade++;
                }
            }
        }
        return ((float) quantidade / (dimensaoHorizontal * dimensaoVertical)) * 100;
    }

    // Mostra área submersa
    public static void mostraPercentagemTerrenoSubmerso(float valor) {
        mostraProximaAlinea();
        System.out.printf("area submersa: %7.2f%%\n", valor);
    }

    public static void mostraProximaAlinea() {
        alinea++;
        System.out.printf("%c)\n", alinea);
    }

    private static void mostraVariacaoAreaInundada(int[][] mapa1, int[][] mapa2) {
        int variacao;
        mostraProximaAlinea();
        variacao = calculaAreaSubmersa(mapa2) - calculaAreaSubmersa(mapa1);
        System.out.printf("variacao da area inundada: %d m2\n", variacao);
    }

    public static int calculaAreaSubmersa(int[][] mapaTerreno) {
        int soma = 0;

        for (int linha = 0; linha < dimensaoHorizontal; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical; coluna++) {
                if (mapaTerreno[linha][coluna] < 0) {
                    soma += mapaTerreno[linha][coluna];
                }
            }
        }
        return soma;
    }
}
