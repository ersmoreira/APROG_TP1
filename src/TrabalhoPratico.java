import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TrabalhoPratico {

    final static int DIM_LINHAS_TERRENO_INDEX = 0;
    final static int DIM_COLUNAS_TERRENO_INDEX = 1;
    final static int ALTERACAO_NIVEL_AGUA = -1;
    final static int QUANTIDADE_DIMENSOES = 2;

    final static String FILE_NAME = "./dataFiles/data.txt";
    final static String FILE_DELIMITER = " ";

    static Scanner sc_file;
    static char alinea = 'a';

    public static void main(String[] args) throws FileNotFoundException {

        sc_file = new Scanner(new File(FILE_NAME));

        // Descritivo do terreno
        String descritivoTerreno = sc_file.nextLine();

        // Dimensoes horizontal e vertical
        int[] arrayDimensoes = obterDimensoesTerreno();
        int dimensaoTerrenoLinhas = arrayDimensoes[DIM_LINHAS_TERRENO_INDEX];
        int dimensaoTerrenoColunas = arrayDimensoes[DIM_COLUNAS_TERRENO_INDEX];

        // Obter cotas do terreno
        int[][] terrenoOriginal = obterCotasTerreno(dimensaoTerrenoLinhas, dimensaoTerrenoColunas);

        // Imprime o mapa do terreno
        imprimeMapaTerreno(terrenoOriginal);

        // Obter e mostrar novo mapa com alteracao do nivel da agua
        int[][] terrenoNovo = calculaNovoMapaTerreno(terrenoOriginal, ALTERACAO_NIVEL_AGUA);
        imprimeMapaTerreno(terrenoNovo);

        // Mostrar percentagem de area do terreno submersa
        mostraPercentagemTerrenoSubmerso(calculaPercentagemTerrenoSubmerso(terrenoNovo));

        // Mostra a variação da área inundada
        mostraVariacaoAreaInundada(terrenoOriginal, terrenoNovo);

        // Mostra volume de água existente no terreno
        mostraVolumeAguaTerreno(terrenoNovo);

        // Inundação total
        mostraAlturaParaInundacaoTotal(terrenoNovo);

        // Mostrar incrementos de area inundada
        mostraIncrementoAreaInundadaAteInundacao(terrenoNovo);

        // Mostrar as coordenadas do terreno e a quantidade de terra a mobilizar
        mostraCoordenadasETerraAMobilizarDoTerrenoParaColocarCubo(terrenoNovo);

        //Caminho seco na vertical
        mostraCaminhoSecoNaVertical(terrenoNovo);

    }

    private static void mostraCoordenadasETerraAMobilizarDoTerrenoParaColocarCubo(int[][] terreno) {
        int terraMobilizar, minTerraImobilizar = Integer.MAX_VALUE, coordLinha = 0, coordColuna = 0;

        mostraProximaAlinea();

        for (int linha = 0; linha < terreno.length - 2; linha++) {
            for (int coluna = 0; coluna < terreno[linha].length - 2; coluna++) {
                terraMobilizar = 0;
                for (int linhaCubo = linha; linhaCubo <= linha + 2; linhaCubo++) {
                    for (int colunaCubo = coluna; colunaCubo <= coluna + 2; colunaCubo++) {
                        terraMobilizar += Math.abs(terreno[linhaCubo][colunaCubo] + 3);
                    }
                }
                if (terraMobilizar < minTerraImobilizar) {
                    minTerraImobilizar = terraMobilizar;
                    coordLinha = linha;
                    coordColuna = coluna;
                }
            }
        }

        System.out.printf("coordenadas do cubo: (%d,%d), terra a mobilizar: %d m2%n", coordLinha, coordColuna,
                minTerraImobilizar);
    }

    private static void mostraCaminhoSecoNaVertical(int[][] terreno) {
        int coluna = calculaCaminhoSecoNaVertical(terreno);

        mostraProximaAlinea();

        if (coluna != 0) {
            System.out.printf("caminho seco na vertical na coluna (%d)%n", coluna);
        } else {
            System.out.println("não há caminho seco na vertical");
        }
    }

    private static void mostraIncrementoAreaInundadaAteInundacao(int[][] terreno) {
        int[][] aux;
        int areaSubmersa = calculaAreaSubmersa(terreno);

        mostraProximaAlinea();
        System.out.printf("subida da agua (m) | area inundada (m2)%n");
        System.out.printf("------------------ | ------------------%n");

        for (int i = 1; i <= calculaAlturaMaximaAcimaAgua(terreno) + 1; i++) {
            aux = calculaNovoMapaTerreno(terreno, i);
            System.out.printf("%18d | %18d%n", i, calculaAreaSubmersa(aux) - areaSubmersa);
            areaSubmersa = calculaAreaSubmersa(aux);
        }
    }

    private static void mostraVolumeAguaTerreno(int[][] terreno) {
        mostraProximaAlinea();

        System.out.printf("volume de agua: %d m3%n", calculaVolumeAguaTerreno(terreno));
    }

    private static int calculaVolumeAguaTerreno(int[][] terreno) {
        int volume = 0;

        for (int linha = 0; linha < terreno.length; linha++) {
            for (int coluna = 0; coluna < terreno[linha].length; coluna++) {
                if (terreno[linha][coluna] < 0) {
                    volume += Math.abs(terreno[linha][coluna]);
                }
            }
        }
        return volume;
    }

    private static void mostraAlturaParaInundacaoTotal(int[][] terreno) {
        mostraProximaAlinea();
        System.out.printf("para inundacao total, subir :%d m%n", calculaAlturaMaximaAcimaAgua(terreno) + 1);
    }

    // Calcula novo mapa do terreno tendo em conta a alteração do nivel da agua
    private static int[][] calculaNovoMapaTerreno(int[][] terreno, int alteraAltura) {
        int[][] novoMapa = new int[terreno.length][terreno[0].length];

        for (int linha = 0; linha < terreno.length; linha++) {
            for (int coluna = 0; coluna < terreno[linha].length; coluna++) {
                novoMapa[linha][coluna] = terreno[linha][coluna] - alteraAltura;
            }

        }

        return novoMapa;
    }

    // Obtem as cotas de cada linha do terreno
    private static int[][] obterCotasTerreno(int dimensaoTerrenoLinhas, int dimensaoTerrenoColunas) {
        int[][] terreno = new int[dimensaoTerrenoLinhas][dimensaoTerrenoColunas];
        int numeroLinha = 0;
        String[] arrayAux;
        String cotasLinha;

        while (sc_file.hasNextLine()) {
            cotasLinha = sc_file.nextLine();
            arrayAux = cotasLinha.split(FILE_DELIMITER);

            for (int i = 0; i < arrayAux.length; i++) {
                terreno[numeroLinha][i] = Integer.parseInt(arrayAux[i]);
            }

            numeroLinha++;
        }

        return terreno;
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
    public static void imprimeMapaTerreno(int[][] terreno) {
        mostraProximaAlinea();

        for (int linha = 0; linha < terreno.length; linha++) {
            for (int coluna = 0; coluna < terreno[linha].length; coluna++) {
                System.out.printf("%5d", terreno[linha][coluna]);
            }
            System.out.println();
        }
    }

    // Calcula percentagem de terreno submersa
    public static float calculaPercentagemTerrenoSubmerso(int[][] terreno) {
        int quantidade = 0;
        for (int linha = 0; linha < terreno.length; linha++) {
            for (int coluna = 0; coluna < terreno[linha].length; coluna++) {
                if (terreno[linha][coluna] < 0) {
                    quantidade++;
                }
            }
        }
        return ((float) quantidade / (terreno.length * terreno[0].length)) * 100;
    }

    // Mostra área submersa
    public static void mostraPercentagemTerrenoSubmerso(float valor) {
        mostraProximaAlinea();
        System.out.printf("area submersa: %7.2f%%%n", valor);
    }

    public static void mostraProximaAlinea() {
        alinea++;
        System.out.printf("%c)%n", alinea);
    }

    private static void mostraVariacaoAreaInundada(int[][] terrenoAntigo, int[][] terrenoNovo) {
        int variacao;
        mostraProximaAlinea();
        variacao = calculaAreaSubmersa(terrenoNovo) - calculaAreaSubmersa(terrenoAntigo);
        System.out.printf("variacao da area inundada: %d m2%n", variacao);
    }

    public static int calculaAreaSubmersa(int[][] terreno) {
        int area = 0;

        for (int linha = 0; linha < terreno.length; linha++) {
            for (int coluna = 0; coluna < terreno[linha].length; coluna++) {
                if (terreno[linha][coluna] < 0) {
                    area++;
                }
            }
        }
        return area;
    }

    public static int calculaAlturaMaximaAcimaAgua(int[][] terreno) {
        int maximo = 0;
        for (int linha = 0; linha < terreno.length; linha++) {
            for (int coluna = 0; coluna < terreno[linha].length; coluna++) {
                if (terreno[linha][coluna] > maximo) {
                    maximo = terreno[linha][coluna];
                }
            }
        }
        return maximo;
    }

    public static int calculaCaminhoSecoNaVertical(int[][] terreno) {
        int count = 0, caminhoSeco = 0;

        for (int coluna = terreno[0].length - 1; coluna >= 0; coluna--) {
            for (int linha = 0; linha < terreno.length; linha++) {
                if (terreno[linha][coluna] >= 0) {
                    count++;
                }
            }
            if (count >= terreno.length && coluna > caminhoSeco) {
                caminhoSeco = coluna;
            }
            count = 0;
        }

        return caminhoSeco;
    }
}
