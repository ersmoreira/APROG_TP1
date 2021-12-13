
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
        int[][] mapaAlterado = calculaNovoMapaTerreno(terreno, ALTERACAO_NIVEL_AGUA);
        imprimeMapaTerreno(mapaAlterado);

        // Mostrar percentagem de area do terreno submersa
        mostraPercentagemTerrenoSubmerso(calculaPercentagemTerrenoSubmerso(mapaAlterado));

        // Mostra a variação da área inundada
        mostraVariacaoAreaInundada(terreno, mapaAlterado);

        // Mostra volume de água existente no terreno
        mostraVolumeAguaTerreno(mapaAlterado);

        // Inundação total
        mostraAlturaParaInundacaoTotal(mapaAlterado);

        // Mostrar incrementos de area inundada
        mostraIncrementoAreaInundadaAteInundacao(mapaAlterado);

        // Mostrar as coordenadas do terreno e a quantidade de terra a mobilizar
        mostraCoordenadasETerraAMobilizarDoTerrenoParaColocarCubo(mapaAlterado);

        //Caminho seco na vertical
        mostraCaminhoSecoNaVertical(mapaAlterado);

    }

    private static void mostraCoordenadasETerraAMobilizarDoTerrenoParaColocarCubo(int[][] terreno) {
        int terraMobilizar = 0, minTerraImobilizar = Integer.MAX_VALUE, coordLinha, coordCol;

        mostraProximaAlinea();

        for (int linha = 0; linha < dimensaoHorizontal-2; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical-2; coluna++) {
                terraMobilizar = 0;
                for (int linhaCubo = linha; linhaCubo <= linha+2; linhaCubo++) {
                    for (int colunaCubo = coluna; colunaCubo <= coluna+2; colunaCubo++) {
                        terraMobilizar += Math.abs(terreno[linhaCubo][colunaCubo]+3);
                        System.out.println("nova coluna");
                    }
                    System.out.println("nova linha");
                }
                System.out.println(terraMobilizar);
                if(terraMobilizar<minTerraImobilizar){
                    minTerraImobilizar = terraMobilizar;
                    coordLinha = linha;
                    coordCol = coluna;
                }
            }
        }
    }

    private static void mostraCaminhoSecoNaVertical(int[][] terreno) {
        int coluna = calculaCaminhoSecoNaVertical(terreno);

        mostraProximaAlinea();

        if (coluna != 0) {
            System.out.printf("caminho seco na vertical na coluna (%d)\n", coluna);
        } else {
            System.out.println("não há caminho seco na vertical");
        }
    }

    private static void mostraIncrementoAreaInundadaAteInundacao(int[][] terreno) {
        int[][] aux = new int[dimensaoHorizontal][dimensaoVertical];
        int areaSubmersa = calculaAreaSubmersa(terreno);

        mostraProximaAlinea();
        System.out.printf("subida da agua (m) | area inundada (m2)\n");
        System.out.printf("------------------ | ------------------\n");

        for (int i = 1; i <= calculaAlturaMaximaAcimaAgua(terreno) + 1; i++) {
            aux = calculaNovoMapaTerreno(terreno, i);
            System.out.printf("%18d | %18d\n", i, calculaAreaSubmersa(aux) - areaSubmersa);
            areaSubmersa = calculaAreaSubmersa(aux);
        }
    }

    private static void mostraVolumeAguaTerreno(int[][] terreno) {
        mostraProximaAlinea();

        System.out.printf("volume de agua: %d m3\n", calculaVolumeAguaTerreno(terreno));
    }

    private static int calculaVolumeAguaTerreno(int[][] terreno) {
        int volume = 0;

        for (int linha = 0; linha < dimensaoHorizontal; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical; coluna++) {
                if (terreno[linha][coluna] < 0) {
                    volume += Math.abs(terreno[linha][coluna]);
                }
            }
        }
        return volume;
    }

    private static void mostraAlturaParaInundacaoTotal(int[][] terreno) {
        mostraProximaAlinea();
        System.out.printf("para inundacao total, subir :%d m\n", calculaAlturaMaximaAcimaAgua(terreno) + 1);
    }


    // Calcula novo mapa do terreno tendo em conta a alteração do nivel da agua
    private static int[][] calculaNovoMapaTerreno(int[][] mapaTerreno, int alteraAltura) {
        int[][] novoMapa = new int[dimensaoHorizontal][dimensaoVertical];

        for (int linha = 0; linha < dimensaoHorizontal; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical; coluna++) {
                novoMapa[linha][coluna] = mapaTerreno[linha][coluna] - alteraAltura;
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
        int area = 0;

        for (int linha = 0; linha < dimensaoHorizontal; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical; coluna++) {
                if (mapaTerreno[linha][coluna] < 0) {
                    area++;
                }
            }
        }
        return area;
    }

    public static int calculaAlturaMaximaAcimaAgua(int[][] mapa) {
        int maximo = 0;
        for (int linha = 0; linha < dimensaoHorizontal; linha++) {
            for (int coluna = 0; coluna < dimensaoVertical; coluna++) {
                if (mapa[linha][coluna] > maximo) {
                    maximo = mapa[linha][coluna];
                }
            }
        }
        return maximo;
    }

    public static int calculaCaminhoSecoNaVertical(int[][] terreno) {
        int count = 0, caminhoSeco = 0;

        for (int coluna = dimensaoVertical - 1; coluna >= 0; coluna--) {
            for (int linha = 0; linha < dimensaoHorizontal; linha++) {
                if (terreno[linha][coluna] >= 0) {
                    count++;
                }
            }
            if (count >= dimensaoHorizontal && coluna > caminhoSeco) {
                caminhoSeco = coluna;
            }
            count = 0;
        }

        return caminhoSeco;
    }
}
