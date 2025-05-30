package com.ntec.MiCajeroJava.servicios;

import java.util.Optional;
import org.springframework.stereotype.Service;
import com.ntec.MiCajeroJava.identidad.Cliente;
import com.ntec.MiCajeroJava.repositorio.RepositorioCliente;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioCliente {

    private final RepositorioCliente repositorioCliente;

    // 🧠 Crea un nuevo cliente sin bloqueos ni strikes
    public Cliente crearCliente(Cliente cliente) {
        cliente.setBloqueado(false);
        cliente.setIntentosFallidos(0);
        return repositorioCliente.save(cliente);
    }

    // 🔍 Busca a un compita por su cédula
    public Optional<Cliente> buscarPorIdentificacion(String identificacion) {
        return repositorioCliente.findByIdentificacion(identificacion);
    }

    // 🔐 Valida el PIN: si mete mal 3 veces, se va baneado
    public boolean validarPin(Cliente cliente, String pin) {
        if (cliente.isBloqueado()) return false;

        if (cliente.getPin().equals(pin)) {
            cliente.setIntentosFallidos(0); // ¡Bien hecho, compita!
            repositorioCliente.save(cliente);
            return true;
        } else {
            int intentos = cliente.getIntentosFallidos() + 1;
            cliente.setIntentosFallidos(intentos);

            if (intentos >= 3) {
                cliente.setBloqueado(true); // RIP cuenta... bloqueada por tronco
            }

            repositorioCliente.save(cliente);
            return false;
        }
    }

    // 🚪 Desbloquea cuentas bloqueadas por manquear el PIN
    public void desbloquearCliente(String identificacion, String nuevoPin) {
        Optional<Cliente> optionalCliente = repositorioCliente.findByIdentificacion(identificacion);

        if (optionalCliente.isPresent()) {
            Cliente cliente = optionalCliente.get();
            cliente.setBloqueado(false);
            cliente.setIntentosFallidos(0);
            cliente.setPin(nuevoPin); // Reset al compita
            repositorioCliente.save(cliente);
        }
    }

    // 🔄 Cambia el PIN del cliente (modo elegante)
    public void cambiarPin(Cliente cliente, String nuevoPin) {
        cliente.setPin(nuevoPin);
        repositorioCliente.save(cliente);
    }

    // 🚨 Suma intentos fallidos al contador de fails
    public void incrementarIntento(Cliente cliente) {
        cliente.setIntentos(cliente.getIntentos() + 1);
        repositorioCliente.save(cliente);
    }

    // 🔁 Reinicia los intentos a 0 (cuando el compa se porta bien)
    public void reiniciarIntentos(Cliente cliente) {
        cliente.setIntentos(0);
        repositorioCliente.save(cliente);
    }

    // 🔒 Ban definitivo (por tonto o por hacker)
    public void bloquearCliente(Cliente cliente) {
        cliente.setBloqueado(true);
        repositorioCliente.save(cliente);
    }
}
