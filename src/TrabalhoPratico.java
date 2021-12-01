import java.util.Scanner;

public class TrabalhoPratico {

    public static void main(String[] args) {
        final int DIM_VERTICAL_INDEX = 0;
        final int DIM_HORIZONTAL_INDEX = 1;

        Scanner sc = new Scanner(System.in);


        // Descritivo do terreno
        String descritivoTerreno = lerString(sc);

        // Dimensoes horizontal e vertical
        String[] arrayDimensoes = obterDimensoesTerreno(sc);
        int dimensaoVertical = Integer.parseInt(arrayDimensoes[DIM_VERTICAL_INDEX]);
        int dimensaoHorizontal = Integer.parseInt(arrayDimensoes[DIM_HORIZONTAL_INDEX]);

        // Obter cotas do terreno
        int[][] terreno = obterCotasTerreno(sc, dimensaoVertical, dimensaoHorizontal);


    }

    // Obtem as cotas de cada linha do terreno
    private static int[][] obterCotasTerreno(Scanner sc, int dimensaoLinhas, int dimensaoColunas) {
        int[][] cotasTerreno = new int[dimensaoLinhas][dimensaoColunas];
        String cotasLinha;

        for (int linha = 0; linha < dimensaoLinhas; linha++) {
            cotasLinha = lerString(sc);
            cotasTerreno[linha] = stringToArrayIntBySpace(cotasLinha);
        }

        return cotasTerreno;
    }

    // Obtem as dimensoes do terreno
    public static String[] obterDimensoesTerreno(Scanner sc) {
        String dimensoes = lerString(sc);
        String[] arrayDimensoes = stringToArrayBySpace(dimensoes);
        return arrayDimensoes;
    }

    // Método que lê e retorna uma string
    public static String lerString(Scanner sc) {
        String texto;
        texto = sc.nextLine();
        return texto.trim();
    }

    // Metodo que converte e retorna uma String num Array de Strings dividindo pelo espaço
    public static String[] stringToArrayBySpace(String texto) {
        String[] array = texto.split(" ");
        return array;
    }

    // Metodo que converte e retorna uma String num Array de inteiros dividindo pelo espaço
    public static int[] stringToArrayIntBySpace(String texto) {
        String[] arrayString = stringToArrayBySpace(texto);
        int[] arrayInt = new int[arrayString.length];

        for (int i = 0; i < arrayString.length; i++) {
            arrayInt[i] = Integer.parseInt(arrayString[i]);
        }
        return arrayInt;
    }


}
