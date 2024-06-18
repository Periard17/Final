import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.text.SimpleDateFormat;

class Doacao {
    private String tipo;
    private int quantidade;
    private Date data;

    public Doacao(String tipo, int quantidade, Date data) {
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Tipo: " + tipo + ", Quantidade: " + quantidade + ", Data: " + new SimpleDateFormat("dd/MM/yyyy").format(data);
    }
}

class SistemaDeGerenciamentoDeDoacoes {
    private ArrayList<Doacao> listaDeDoacoes = new ArrayList<>();
    private static final String ARQUIVO_DOACOES = "doacoes.txt";

    public void adicionarDoacao(Doacao doacao) {
        listaDeDoacoes.add(doacao);
        salvarDoacaoNoArquivo(doacao);
    }

    public int calcularTotalDoacoes() {
        int total = 0;
        for (Doacao doacao : listaDeDoacoes) {
            total += doacao.getQuantidade();
        }
        return total;
    }

    public void listarDoacoes() {
        for (Doacao doacao : listaDeDoacoes) {
            System.out.println(doacao);
        }
    }

    private void salvarDoacaoNoArquivo(Doacao doacao) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARQUIVO_DOACOES, true))) {
            writer.write(doacao.toString());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar doação no arquivo: " + e.getMessage());
        }
    }

    public void carregarDoacoesDoArquivo() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ARQUIVO_DOACOES))) {
            String linha;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            while ((linha = reader.readLine()) != null) {
                String[] partes = linha.split(", ");
                String tipo = partes[0].split(": ")[1];
                int quantidade = Integer.parseInt(partes[1].split(": ")[1]);
                Date data = dateFormat.parse(partes[2].split(": ")[1]);

                Doacao doacao = new Doacao(tipo, quantidade, data);
                listaDeDoacoes.add(doacao);
            }
        } catch (IOException | java.text.ParseException e) {
            System.out.println("Erro ao carregar doações do arquivo: " + e.getMessage());
        }
    }
}

public class Main {
    public static void main(String[] args) {
        SistemaDeGerenciamentoDeDoacoes sistema = new SistemaDeGerenciamentoDeDoacoes();
        sistema.carregarDoacoesDoArquivo();
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        while (true) {
            System.out.println("Digite o tipo de doação (ou 'sair' para terminar):");
            String tipo = scanner.nextLine();
            if (tipo.equalsIgnoreCase("sair")) {
                break;
            }

            System.out.println("Digite a quantidade:");
            int quantidade = Integer.parseInt(scanner.nextLine());

            System.out.println("Digite a data da doação (dd/MM/yyyy):");
            String dataStr = scanner.nextLine();
            Date data = null;
            try {
                data = dateFormat.parse(dataStr);
            } catch (Exception e) {
                System.out.println("Data inválida. Tente novamente.");
                continue;
            }

            Doacao doacao = new Doacao(tipo, quantidade, data);
            sistema.adicionarDoacao(doacao);
            System.out.println("Doação adicionada com sucesso!");

            System.out.println("Total de doações: " + sistema.calcularTotalDoacoes());
            System.out.println("Lista de doações:");
            sistema.listarDoacoes();
        }

        scanner.close();
    }
}
