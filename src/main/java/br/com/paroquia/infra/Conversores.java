package br.com.paroquia.infra;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

public final class Conversores {
    private Conversores() { }
    public static String obrigatorio(String valor, String campo) {
        if (valor == null || valor.isBlank()) throw new IllegalArgumentException("Preencha o campo " + campo + ".");
        return valor.trim();
    }
    public static String cpf(String valor) {
        if (valor == null) throw new IllegalArgumentException("Informe o CPF.");
        String numeros = valor.replaceAll("\\D", "");
        if (numeros.length() != 11) throw new IllegalArgumentException("O CPF deve conter 11 dígitos.");
        return numeros;
    }
    public static String cpfOpcional(String valor) {
        return valor == null || valor.isBlank() ? null : cpf(valor);
    }
    public static int inteiro(String valor, String campo) {
        try { return Integer.parseInt(obrigatorio(valor, campo)); }
        catch (NumberFormatException e) { throw new IllegalArgumentException("Informe um número inteiro válido em " + campo + "."); }
    }
    public static LocalDate data(String valor) {
        String s = obrigatorio(valor,"data");
        try { return LocalDate.parse(s, DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(java.time.format.ResolverStyle.STRICT)); }
        catch (DateTimeParseException e) {
            try { return LocalDate.parse(s); }
            catch (DateTimeParseException e2) { throw new IllegalArgumentException("Data inválida. Use dd/MM/aaaa."); }
        }
    }
    public static LocalTime horario(String valor) {
        try { return LocalTime.parse(obrigatorio(valor,"horário"), DateTimeFormatter.ofPattern("H:mm")); }
        catch (DateTimeParseException e) { throw new IllegalArgumentException("Horário inválido. Use HH:mm."); }
    }
    public static BigDecimal valor(String valor) {
        try {
            String normalizado = obrigatorio(valor, "valor").replace("R$", "").trim();
            // Aceita 1234,56, 1.234,56 e 1234.56
            if (normalizado.contains(",")) normalizado=normalizado.replace(".", "").replace(",", ".");
            BigDecimal v = new BigDecimal(normalizado).setScale(2, java.math.RoundingMode.UNNECESSARY);
            if (v.signum() <= 0) throw new NumberFormatException();
            return v;
        } catch (NumberFormatException | ArithmeticException e) {
            throw new IllegalArgumentException("Valor inválido. Informe um número positivo com até 2 decimais.");
        }
    }
}
