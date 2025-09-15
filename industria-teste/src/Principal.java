import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Principal {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private static final java.util.Locale BRAZIL = java.util.Locale.forLanguageTag("pt-BR");
    private static final DecimalFormatSymbols SYMBOLS = new DecimalFormatSymbols(BRAZIL);
    private static final DecimalFormat MONEY_FORMAT = new DecimalFormat("#,##0.00", SYMBOLS);
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1212.00");

    public static void main(String[] args) {
        List<Funcionario> funcionarios = new ArrayList<>();

        funcionarios.add(new Funcionario("Maria", LocalDate.parse("18/10/2000", DATE_FORMAT), new BigDecimal("2009.44"), "Operador"));
        funcionarios.add(new Funcionario("João", LocalDate.parse("12/05/1990", DATE_FORMAT), new BigDecimal("2284.38"), "Operador"));
        funcionarios.add(new Funcionario("Caio", LocalDate.parse("02/05/1961", DATE_FORMAT), new BigDecimal("9836.14"), "Coordenador"));
        funcionarios.add(new Funcionario("Miguel", LocalDate.parse("14/10/1988", DATE_FORMAT), new BigDecimal("19119.88"), "Diretor"));
        funcionarios.add(new Funcionario("Alice", LocalDate.parse("05/01/1995", DATE_FORMAT), new BigDecimal("2234.68"), "Recepcionista"));
        funcionarios.add(new Funcionario("Heitor", LocalDate.parse("19/11/1999", DATE_FORMAT), new BigDecimal("1582.72"), "Operador"));
        funcionarios.add(new Funcionario("Arthur", LocalDate.parse("31/03/1993", DATE_FORMAT), new BigDecimal("4071.84"), "Contador"));
        funcionarios.add(new Funcionario("Laura", LocalDate.parse("08/07/1994", DATE_FORMAT), new BigDecimal("3017.45"), "Gerente"));
        funcionarios.add(new Funcionario("Heloísa", LocalDate.parse("24/05/2003", DATE_FORMAT), new BigDecimal("1606.85"), "Eletricista"));
        funcionarios.add(new Funcionario("Helena", LocalDate.parse("02/09/1996", DATE_FORMAT), new BigDecimal("2799.93"), "Gerente"));

        
        funcionarios.removeIf(f -> f.getNome().equalsIgnoreCase("João"));

        System.out.println("---- Lista inicial (após remoção de João) ----");
        funcionarios.forEach(Principal::printFuncionario);

        funcionarios.forEach(f -> {
            BigDecimal novo = f.getSalario()
                               .multiply(new BigDecimal("1.10"))
                               .setScale(2, RoundingMode.HALF_EVEN);
            f.setSalario(novo);
        });
        System.out.println("\n---- Após aumento de 10% ----");
        funcionarios.forEach(Principal::printFuncionario);


        Map<String, List<Funcionario>> agrupadoPorFuncao = funcionarios.stream()
                .collect(Collectors.groupingBy(Funcionario::getFuncao));

       
        System.out.println("\n---- Agrupados por função ----");
        agrupadoPorFuncao.forEach((funcao, lista) -> {
            System.out.println("Função: " + funcao);
            lista.forEach(Principal::printFuncionario);
            System.out.println();
        });

       
        System.out.println("---- Aniversariantes (meses 10 e 12) ----");
        funcionarios.stream()
                .filter(f -> {
                    int m = f.getDataNascimento().getMonthValue();
                    return m == 10 || m == 12;
                })
                .forEach(Principal::printFuncionario);

     
        Optional<Funcionario> maisVelho = funcionarios.stream()
                .min(Comparator.comparing(Funcionario::getDataNascimento)); 
        if (maisVelho.isPresent()) {
            Funcionario f = maisVelho.get();
            int idade = Period.between(f.getDataNascimento(), LocalDate.now()).getYears();
            System.out.println("\n---- Funcionário mais velho ----");
            System.out.println("Nome: " + f.getNome() + " | Idade: " + idade + " anos");
        }

       
        System.out.println("\n---- Ordem alfabética ----");
        funcionarios.stream()
                .sorted(Comparator.comparing(Funcionario::getNome, String.CASE_INSENSITIVE_ORDER))
                .forEach(Principal::printFuncionario);

        
        BigDecimal totalSalarios = funcionarios.stream()
                .map(Funcionario::getSalario)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("\n---- Total dos salários ----");
        System.out.println("Total: " + MONEY_FORMAT.format(totalSalarios));

        
        System.out.println("\n---- Quantos salários mínimos cada funcionário ganha ----");
        funcionarios.forEach(f -> {
            BigDecimal quantidade = f.getSalario().divide(SALARIO_MINIMO, 2, RoundingMode.HALF_UP);
            System.out.println(f.getNome() + " => " + MONEY_FORMAT.format(quantidade) + " salários mínimos");
        });
    }

    private static void printFuncionario(Funcionario f) {
        String data = f.getDataNascimento().format(DATE_FORMAT);
        String salario = MONEY_FORMAT.format(f.getSalario());
        System.out.println("Nome: " + f.getNome()
                + " | Nasc: " + data
                + " | Salário: " + salario
                + " | Função: " + f.getFuncao());
    }

    
    static class Funcionario {
        private String nome;
        private LocalDate dataNascimento;
        private BigDecimal salario;
        private String funcao;

        public Funcionario(String nome, LocalDate dataNascimento, BigDecimal salario, String funcao) {
            this.nome = nome;
            this.dataNascimento = dataNascimento;
            this.salario = salario;
            this.funcao = funcao;
        }

        public String getNome() { return nome; }
        public LocalDate getDataNascimento() { return dataNascimento; }
        public BigDecimal getSalario() { return salario; }
        public String getFuncao() { return funcao; }
        public void setSalario(BigDecimal salario) { this.salario = salario; }
    }
}


//Teste com "java Principal" no terminal