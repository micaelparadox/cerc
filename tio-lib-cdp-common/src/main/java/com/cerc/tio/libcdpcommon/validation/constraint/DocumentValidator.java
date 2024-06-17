package com.cerc.tio.libcdpcommon.validation.constraint;

import com.cerc.tio.libcdpcommon.validation.annotation.ValidateDocument;
import com.cerc.tio.libcdpcommon.validation.enumeration.DocumentValidationType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Set;

public class DocumentValidator implements ConstraintValidator<ValidateDocument, String> {

    private Set<String> exclude;
    private Set<DocumentValidationType> type;

    @Override
    public void initialize(ValidateDocument constraintAnnotation) {
        this.exclude = new HashSet<>(Arrays.asList(constraintAnnotation.exclude()));
        this.type = new HashSet<>(Arrays.asList(constraintAnnotation.type()));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || value.isEmpty() || validateValue(value);
    }

    public boolean validateValue(String value) {
        boolean isValid = false;

        if (this.exclude.contains(value)) {
            return true;
        }

        if (this.type.contains(DocumentValidationType.CPF)) {
            isValid = isCpf(value);
        }

        if (this.type.contains(DocumentValidationType.CNPJ) && !isValid) {
            isValid = isCnpj(value);
        }

        return isValid;
    }

    /**
     * Realiza a validacao do CPF.
     *
     * @param cpf numero de CPF a ser validado pode ser passado no formado
     *            999.999.999-99 ou 99999999999
     * @return true se o CPF e valido e false se nao e valido
     */
    private boolean isCpf(String cpf) {
        if (!cpf.matches("\\d+")) {
            return false;
        }

        if (cpf.length() != 11) {
            return false;
        }

        if (cpf.equals("00000000000") || cpf.equals("11111111111") || cpf.equals("22222222222")
            || cpf.equals("33333333333") || cpf.equals("44444444444") || cpf.equals("55555555555")
            || cpf.equals("66666666666") || cpf.equals("77777777777") || cpf.equals("88888888888")
            || cpf.equals("99999999999"))
            return (false);

        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");

        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) {
            return false;
        }

        int d1;
        int d2;
        int digito1;
        int digito2;
        int resto;
        int digitoCPF;
        String nDigResult;

        d1 = d2 = 0;

        for (int nCount = 1; nCount < cpf.length() - 1; nCount++) {
            digitoCPF = Integer.parseInt(cpf.substring(nCount - 1, nCount));

            // multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4
            // e assim por diante.
            d1 = d1 + (11 - nCount) * digitoCPF;

            // para o segundo digito repita o procedimento incluindo o primeiro
            // digito calculado no passo anterior.
            d2 = d2 + (12 - nCount) * digitoCPF;
        }

        // Primeiro resto da divisao por 11.
        resto = (d1 % 11);

        // Se o resultado for 0 ou 1 o digito e 0 caso contrario o digito e 11
        // menos o resultado anterior.
        if (resto < 2)
            digito1 = 0;
        else
            digito1 = 11 - resto;

        d2 += 2 * digito1;

        // Segundo resto da divisao por 11.
        resto = (d2 % 11);

        // Se o resultado for 0 ou 1 o digito e 0 caso contrario o digito e 11
        // menos o resultado anterior.
        if (resto < 2)
            digito2 = 0;
        else
            digito2 = 11 - resto;

        // Digito verificador do CPF que esta sendo validado.
        String nDigVerific = cpf.substring(cpf.length() - 2, cpf.length());

        // Concatenando o primeiro resto com o segundo.
        nDigResult = String.valueOf(digito1) + String.valueOf(digito2);

        // comparar o digito verificador do cpf com o primeiro resto + o segundo
        // resto.
        return nDigVerific.equals(nDigResult);
    }

    /**
     * Realiza a validação de um cnpj
     *
     * @param cnpj String - o CNPJ pode ser passado no formato 99.999.999/9999-99 ou
     *             99999999999999
     * @return boolean
     */
    @SuppressWarnings("squid:S3776")
    private boolean isCnpj(String cnpj) {
        if (!cnpj.matches("\\d+")) {
            return false;
        }

        if (cnpj.length() < 14) {
            return false;
        }

        cnpj = cnpj.replace(".", "");
        cnpj = cnpj.replace("-", "");
        cnpj = cnpj.replace("/", "");

        try {
            Long.parseLong(cnpj);
        } catch (NumberFormatException e) {
            return false;
        }

        // considera-se erro CNPJs formados por uma sequencia de numeros iguais
        if (cnpj.equals("00000000000000") || cnpj.equals("11111111111111") || cnpj.equals("22222222222222")
            || cnpj.equals("33333333333333") || cnpj.equals("44444444444444") || cnpj.equals("55555555555555")
            || cnpj.equals("66666666666666") || cnpj.equals("77777777777777") || cnpj.equals("88888888888888")
            || cnpj.equals("99999999999999") || (cnpj.length() != 14))
            return (false);
        char dig13;
        char dig14;
        int sm;
        int i;
        int r;
        int num;
        int peso; // "try" - protege o codigo para eventuais
        // erros de conversao de tipo (int)
        try {
            // Calculo do 1o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 11; i >= 0; i--) {
                // converte o i-esimo caractere do CNPJ em um numero: // por
                // exemplo, transforma o caractere '0' no inteiro 0 // (48 eh a
                // posicao de '0' na tabela ASCII)
                num = (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }

            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig13 = '0';
            else
                dig13 = (char) ((11 - r) + 48);

            // Calculo do 2o. Digito Verificador
            sm = 0;
            peso = 2;
            for (i = 12; i >= 0; i--) {
                num = (cnpj.charAt(i) - 48);
                sm = sm + (num * peso);
                peso = peso + 1;
                if (peso == 10)
                    peso = 2;
            }
            r = sm % 11;
            if ((r == 0) || (r == 1))
                dig14 = '0';
            else
                dig14 = (char) ((11 - r) + 48);
            // Verifica se os dígitos calculados conferem com os dígitos
            // informados.
            if ((dig13 == cnpj.charAt(12)) && (dig14 == cnpj.charAt(13)))
                return (true);
            else
                return (false);
        } catch (InputMismatchException erro) {
            return (false);
        }
    }


}
