import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Comercio {
    /** Para inclusão de novos produtos no vetor */
    static final int MAX_NOVOS_PRODUTOS = 10;

    /**
     * Nome do arquivo de dados. O arquivo deve estar localizado na raiz do projeto
     */
    static String nomeArquivoDados;

    /** Scanner para leitura do teclado */
    static Scanner teclado;

    /**
     * Vetor de produtos cadastrados. Sempre terá espaço para 10 novos produtos a
     * cada execução
     */
    static Produto[] produtosCadastrados;

    /** Quantidade produtos cadastrados atualmente no vetor */
    static int quantosProdutos;

    /** Gera um efeito de pausa na CLI. Espera por um enter para continuar */
    static void pausa() {
        System.out.println("Digite enter para continuar...");
        teclado.nextLine();
    }

    /** Cabeçalho principal da CLI do sistema */
    static void cabecalho() {
        System.out.println("AEDII COMÉRCIO DE COISINHAS");
        System.out.println("===========================");
    }

    /**
     * Imprime o menu principal, lê a opção do usuário e a retorna (int).
     * Perceba que poderia haver uma melhor modularização com a criação de uma
     * classe Menu.
     * 
     * @return Um inteiro com a opção do usuário.
     */
    static int menu() {
        cabecalho();
        System.out.println("1 - Listar todos os produtos");
        System.out.println("2 - Procurar e listar um produto");
        System.out.println("3 - Cadastrar novo produto");
        System.out.println("0 - Sair");
        System.out.print("Digite sua opção: ");
        return Integer.parseInt(teclado.nextLine());
    }

    /**
     * Lê os dados de um arquivo texto e retorna um vetor de produtos. Arquivo no
     * formato
     * N (quantiade de produtos) <br/>
     * tipo; descrição;preçoDeCusto;margemDeLucro;[dataDeValidade] <br/>
     * Deve haver uma linha para cada um dos produtos. Retorna um vetor vazio em
     * caso de problemas com o arquivo.
     * 
     * @param nomeArquivoDados Nome do arquivo de dados a ser aberto.
     * @return Um vetor com os produtos carregados, ou vazio em caso de problemas de
     *         leitura.
     */
    static Produto[] lerProdutos(String nomeArquivoDados) {
        List<Produto> listaProdutos = new ArrayList<>();

        try {
            List<String> linhas = Files.readAllLines(Paths.get(nomeArquivoDados), Charset.forName("UTF-8"));

            int quantidadeProdutos = Integer.parseInt(linhas.get(0).trim());
            for (int i = 1; i <= quantidadeProdutos; i++) {
                String linha = linhas.get(i);
                String[] dados = linha.split(";");

                if (dados.length < 4) {
                    continue;
                }

                try {
                    String tipo = dados[0].trim();
                    String descricao = dados[1].trim();
                    double precoCusto = Double.parseDouble(dados[2].trim());
                    double margemLucro = Double.parseDouble(dados[3].trim());

                    Produto produto = null;

                    if ("1".equals(tipo)) {
                        LocalDate validade = (dados.length > 4 && !dados[4].isEmpty())
                                ? LocalDate.parse(dados[4].trim(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                                : null;
                        produto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, validade);
                    } else if ("2".equals(tipo)) {
                        produto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
                    }

                    if (produto != null) {
                        listaProdutos.add(produto);
                    }

                } catch (Exception e) {
                    System.out.println("Erro ao processar a linha: " + linha);
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + nomeArquivoDados);
            return new Produto[0];
        }

        return listaProdutos.toArray(new Produto[0]);
    }

    static void cadastrarProduto() {
        System.out.println("Escolha o tipo de produto:");
        System.out.println("1 - Produto Perecível");
        System.out.println("2 - Produto Não Perecível");
        System.out.print("Digite a opção (1 ou 2): ");
        int tipo = Integer.parseInt(teclado.nextLine().trim());

        System.out.print("Digite a descrição do produto: ");
        String descricao = teclado.nextLine().trim();

        System.out.print("Digite o preço de custo do produto: ");
        double precoCusto = Double.parseDouble(teclado.nextLine().trim());

        System.out.print("Digite a margem de lucro do produto: ");
        double margemLucro = Double.parseDouble(teclado.nextLine().trim());

        Produto novoProduto = null;

        if (tipo == 1) { 
            System.out.print("Digite a data de validade do produto (dd/MM/yyyy): ");
            String dataValidadeStr = teclado.nextLine().trim();

            LocalDate validade = LocalDate.parse(dataValidadeStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            novoProduto = new ProdutoPerecivel(descricao, precoCusto, margemLucro, validade);
        } else if (tipo == 2) { 
            novoProduto = new ProdutoNaoPerecivel(descricao, precoCusto, margemLucro);
        } else {
            System.out.println("Opção inválida! O produto não foi cadastrado.");
            return;
        }

        if (quantosProdutos < produtosCadastrados.length) {
            produtosCadastrados[quantosProdutos] = novoProduto;
            quantosProdutos++;
            System.out.println("Produto cadastrado com sucesso!");
        } else {
            System.out.println("Não há espaço suficiente para cadastrar novos produtos.");
        }
    }

    /**
     * Localiza um produto no vetor de cadastrados, a partir do nome, e imprime seus
     * dados.
     * A busca não é sensível ao caso. Em caso de não encontrar o produto, imprime
     * mensagem padrão
     */
    static void localizarProdutos() {
        System.out.print("Digite o nome do produto a ser localizado: ");
        String nomeBusca = teclado.nextLine().trim().toLowerCase();
        boolean encontrado = false;

        for (int i = 0; i < produtosCadastrados.length; i++) {
            if (produtosCadastrados[i] != null && produtosCadastrados[i].toString().toLowerCase().equals(nomeBusca)) {
                System.out.println("Produto encontrado:");
                System.out.println(produtosCadastrados[i].gerarDadosTexto());
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            System.out.println("Produto não encontrado.");
        }
    }

    static void listarTodosOsProdutos() {
        if (produtosCadastrados == null || produtosCadastrados.length == 0) {
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println("Lista de Produtos Cadastrados:");

        for (int i = 0; i < produtosCadastrados.length; i++) {
            if (produtosCadastrados[i] != null) {
                System.out.println((i + 1) + ". " + produtosCadastrados[i].gerarDadosTexto());
            }
        }
    }

    /**
     * Salva os dados dos produtos cadastrados no arquivo csv informado. Sobrescreve
     * todo o conteúdo do arquivo.
     * 
     * @param nomeArquivo Nome do arquivo a ser gravado.
     */
    public static void salvarProdutos(String nomeArquivo) {
        try {
            FileWriter arquivoSaida = new FileWriter(nomeArquivo, Charset.forName("UTF-8"));
            arquivoSaida.append(quantosProdutos + "\n");
            for (int i = 0; i < produtosCadastrados.length; i++) {
                if (produtosCadastrados[i] != null)
                    arquivoSaida.append(produtosCadastrados[i].gerarDadosTexto() + "\n");
            }
            arquivoSaida.close();
            System.out.println("Arquivo " + nomeArquivo + " salvo.");
        } catch (IOException e) {
            System.out.println("Problemas no arquivo " + nomeArquivo + ". Tente novamente");
        }
    }

    public static void main(String[] args) throws Exception {
        teclado = new Scanner(System.in, Charset.forName("ISO-8859-2"));
        nomeArquivoDados = "dadosProdutos.csv";
        produtosCadastrados = lerProdutos(nomeArquivoDados);
        int opcao = -1;
        do {
            opcao = menu();
            switch (opcao) {
                case 1 -> listarTodosOsProdutos();
                case 2 -> localizarProdutos();
                case 3 -> cadastrarProduto();
            }
            pausa();
        } while (opcao != 0);

        salvarProdutos(nomeArquivoDados);
        teclado.close();
    }
}
