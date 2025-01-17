import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONObject;

public class Principal {


    private static final String[] MOEDAS = {"USD", "EUR", "BRL", "GBP"};


    public static double obterTaxaCambio(String moedaOrigem, String moedaDestino) {
        String apiKey = "18de13ccc65e8f56e429e580";
        String urlStr = String.format("https://api.exchangerate-api.com/v4/latest/%s", moedaOrigem);

        try {
            URL url = new URL(urlStr);
            HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
            conexao.setRequestMethod("GET");

            if (conexao.getResponseCode() == 200) {
                Scanner scanner = new Scanner(url.openStream());
                StringBuilder resposta = new StringBuilder();

                while (scanner.hasNext()) {
                    resposta.append(scanner.nextLine());
                }
                scanner.close();

                JSONObject jsonObj = new JSONObject(resposta.toString());
                JSONObject taxas = jsonObj.getJSONObject("rates");

                return taxas.getDouble(moedaDestino);
            } else {
                System.out.println("Erro ao obter taxas de câmbio.");
            }
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
        return -1;
    }

    public static void exibirMenuMoedas() {
        System.out.println("=== Moedas Disponíveis ===");
        for (int i = 0; i < MOEDAS.length; i++) {
            System.out.printf("%d. %s%n", i + 1, MOEDAS[i]);
        }
        System.out.printf("%d. SAIR %n", MOEDAS.length + 1);
    }

    public static int selecionarOpcao(Scanner scanner, String tipo) {
        int escolha;
        do {
            System.out.printf("Selecione o número correspondente à moeda de %s (ou %d para sair): ",
                    tipo, MOEDAS.length + 1);
            escolha = scanner.nextInt();
            if (escolha < 1 || escolha > MOEDAS.length + 1) {
                System.out.println("Escolha inválida. Tente novamente.");
            }
        } while (escolha < 1 || escolha > MOEDAS.length + 1);
        return escolha;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean continuar = true;

        while (continuar) {
            System.out.println("\n=== Conversor de Moedas ===");

            exibirMenuMoedas();

            int escolhaOrigem = selecionarOpcao(scanner, "origem");
            if (escolhaOrigem == MOEDAS.length + 1) {
                continuar = false;
                break;
            }
            String moedaOrigem = MOEDAS[escolhaOrigem - 1];

            int escolhaDestino = selecionarOpcao(scanner, "destino");
            if (escolhaDestino == MOEDAS.length + 1) {
                continuar = false;
                break;
            }
            String moedaDestino = MOEDAS[escolhaDestino - 1];

            System.out.print("Digite o valor a ser convertido: ");
            double valor = scanner.nextDouble();

            double taxaCambio = obterTaxaCambio(moedaOrigem, moedaDestino);

            if (taxaCambio != -1) {
                double valorConvertido = valor * taxaCambio;
                System.out.printf("Taxa de câmbio: 1 %s = %.2f %s%n", moedaOrigem, taxaCambio, moedaDestino);
                System.out.printf("Valor convertido: %.2f %s%n", valorConvertido, moedaDestino);
            } else {
                System.out.println("Não foi possível obter a taxa de câmbio. Tente novamente mais tarde.");
            }
        }

        System.out.println("Programa finalizado. Até logo!");
        scanner.close();
    }
}
