package com.ntec.MiCajeroJava.servicios;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

import com.ntec.MiCajeroJava.identidad.Cliente;
import com.ntec.MiCajeroJava.identidad.Cuenta;
import com.ntec.MiCajeroJava.identidad.TipoCuenta;
import com.ntec.MiCajeroJava.repositorio.RepositorioCuenta;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioCuenta {

    private final RepositorioCuenta repositorioCuenta;

    // 💳 Crea una cuenta bancaria mágica para el compa (con saldo inicial incluido)
    public Cuenta crearCuenta(Cliente cliente, String numero, TipoCuenta tipo, double saldoInicial) {
        Cuenta cuenta = Cuenta.builder()
            .cliente(cliente)
            .numero(numero)
            .tipo(tipo)
            .saldo(saldoInicial)
            .build();

        return repositorioCuenta.save(cuenta); // Guardando cuenta chetada 🤑
    }

    // 🔍 Busca una cuenta por su número (tipo FBI)
    public Optional<Cuenta> buscarPorNumero(String numero) {
        return repositorioCuenta.findByNumero(numero);
    }

    // 📊 Consulta cuánto billete tiene la cuenta
    public double consultarSaldo(Cuenta cuenta) {
        return cuenta.getSaldo();
    }

    // 📦 Devuelve todas las cuentas activas del cliente (modo multiwallet)
    public List<Cuenta> obtenerCuentasCliente(Cliente cliente) {
        return cliente.getCuentas();
    }
    public List<Cuenta> buscarPorCliente(Cliente cliente) {
    return repositorioCuenta.findByCliente(cliente);
}

}
