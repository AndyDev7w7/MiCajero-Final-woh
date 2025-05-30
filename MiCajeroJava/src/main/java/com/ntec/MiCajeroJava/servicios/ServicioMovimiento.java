package com.ntec.MiCajeroJava.servicios;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ntec.MiCajeroJava.identidad.Cuenta;
import com.ntec.MiCajeroJava.identidad.Movimiento;
import com.ntec.MiCajeroJava.identidad.TipoMovimiento;
import com.ntec.MiCajeroJava.repositorio.RepositorioCuenta;
import com.ntec.MiCajeroJava.repositorio.RepositorioMovimiento;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioMovimiento {

    private final RepositorioMovimiento repositorioMovimiento;
    private final RepositorioCuenta repositorioCuenta;

    // 📝 Registra cualquier movimiento: consignación, retiro, transferencia... TODO queda anotado
    public Movimiento registrarMovimiento(Cuenta cuenta, TipoMovimiento tipo, double monto) {
        Movimiento movimiento = Movimiento.builder()
            .cuenta(cuenta)
            .tipo(tipo)
            .monto(monto)
            .fecha(LocalDateTime.now())
            .build();

        return repositorioMovimiento.save(movimiento); // Guardado en el libro sagrado del dinero 📚💰
    }

    // 📜 Devuelve todos los movimientos de una cuenta
    public List<Movimiento> obtenerMovimientoPorCuenta(Cuenta cuenta, double monto) {
        return repositorioMovimiento.findByCuenta(cuenta);
    }

    // 💸 Realiza retiro solo si hay dinero (si no, RIP)
    public boolean realizarRetiro(Cuenta cuenta, double monto) {
        if (cuenta.getSaldo() >= monto) {
            cuenta.setSaldo(cuenta.getSaldo() - monto);
            repositorioCuenta.save(cuenta);
            registrarMovimiento(cuenta, TipoMovimiento.RETIRO, monto); // Registro de salida 💀
            return true;
        }
        return false; // "No hay money, bro" ❌
    }

    // 🔁 Transferencia entre dos cuentas (con registros dobles, como debe ser)
    public boolean realizarTransferencia(Cuenta origen, Cuenta destino, double monto) {
        if (origen.getSaldo() >= monto) {
            origen.setSaldo(origen.getSaldo() - monto);
            destino.setSaldo(destino.getSaldo() + monto);

            repositorioCuenta.save(origen);
            repositorioCuenta.save(destino);

            // Registro negativo en origen y positivo en destino 🧾
            registrarMovimiento(origen, TipoMovimiento.TRANSFERENCIA, -monto);
            registrarMovimiento(destino, TipoMovimiento.TRANSFERENCIA, monto);

            return true;
        }
        return false; // Saldo insuficiente para jugar a ser millonario 💸😞
    }

    // 🔍 Busca movimientos por número de cuenta y los ordena del más reciente al más oldie
    public List<Movimiento> buscarPorCuenta(String numeroCuenta) {
        Cuenta cuenta = repositorioCuenta.findByNumero(numeroCuenta)
            .orElseThrow(() -> new RuntimeException("Cuenta no encontrada, compita"));
        
        return repositorioMovimiento.findByCuenta_NumeroOrderByFechaDesc(cuenta);
    }

    // 💰 Realiza consignación si el monto es legal (> 0)
    public boolean realizarConsignacion(Cuenta cuenta, double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("Parcero, el monto tiene que ser mayor a cero 💀");
        }

        cuenta.setSaldo(cuenta.getSaldo() + monto);
        repositorioCuenta.save(cuenta);
        registrarMovimiento(cuenta, TipoMovimiento.CONSIGNACION, monto); // Dinero pa' dentro ✅
        return true;
    }
}
