import java.util.Scanner;

public class TrabalhoPratico {

    public static void main(String[] args) {
        final int DIM_VERTICAL_INDEX = 0;
        final int DIM_HORIZONTAL_INDEX = 1;
        final int ALTERACAO_NIVEL_AGUA = -1;

        Scanner sc = new Scanner(System.in);

        System.out.println("a)");

        // Descritivo do terreno
        String descritivoTerreno = lerString(sc);

        // Dimensoes horizontal e vertical
        String[] arrayDimensoes = obterDimensoesTerreno(sc);
        int dimensaoVertical = Integer.parseInt(arrayDimensoes[DIM_VERTICAL_INDEX]);
        int dimensaoHorizontal = Integer.parseInt(arrayDimensoes[DIM_HORIZONTAL_INDEX]);

        // Obter cotas do terreno
        int[][] terreno = obterCotasTerreno(sc, dimensaoVertical, dimensaoHorizontal);

        // Imprime o mapa do terreno
        System.out.println("b)");
        imprimeMapaTerreno(terreno, dimensaoVertical, dimensaoHorizontal);

        // Obter e mostrar novo mapa com alteracao do nivel da agua
        int[][] mapaAlterado = calculaNovoMapaTerreno(terreno, dimensaoVertical, dimensaoHorizontal, ALTERACAO_NIVEL_AGUA);
        System.out.println("c)");
        imprimeMapaTerreno(mapaAlterado, dimensaoVertical, dimensaoHorizontal);

        // Mostrar percentagem de area do terreno submersa
        System.out.println("d)");
        mostraPercentagemTerrenoSubmerso(calculaPercentagemTerrenoSubmerso(mapaAlterado, dimensaoVertical, dimensaoHorizontal));

    }

    // Calcula novo mapa do terreno tendo em conta a alteração do nivel da agua
    private static int[][] calculaNovoMapaTerreno(int[][] mapaTerreno, int dimensaoLinhas, int dimensaoColunas, int alteracao_nivel_agua) {
        int[][] novoMapa = new int[dimensaoLinhas][dimensaoColunas];

        for (int linha = 0; linha < dimensaoLinhas; linha++) {
            for (int coluna = 0; coluna < dimensaoColunas; coluna++) {
                novoMapa[linha][coluna] = mapaTerreno[linha][coluna] - alteracao_nivel_agua;
            }

        }

        return novoMapa;
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

    // Imprime o mapa do terreno com com os valores alinhados à direita
    public static void imprimeMapaTerreno(int[][] mapaTerreno, int dimensaoLinhas, int dimensaoColunas) {
        for (int linha = 0; linha < dimensaoLinhas; linha++) {
            for (int coluna = 0; coluna < dimensaoColunas; coluna++) {
                System.out.printf("%5d", mapaTerreno[linha][coluna]);
            }
            System.out.println();
        }
    }

    // Calcula percentagem de terreno submersa
    public static float calculaPercentagemTerrenoSubmerso(int[][] terreno, int dimensaoLinhas, int dimensaoColunas) {
        int quantidade = 0;
        for (int linha = 0; linha < dimensaoLinhas; linha++) {
            for (int coluna = 0; coluna < dimensaoColunas; coluna++) {
                if (terreno[linha][coluna] < 0) {
                    quantidade++;
                }
            }
        }
        return ((float) quantidade / (dimensaoLinhas * dimensaoColunas)) * 100;
    }

    // Mostra área submersa
    public static void mostraPercentagemTerrenoSubmerso(float valor) {
        System.out.printf("area submersa: %7.2f%%", valor);
    }

}
