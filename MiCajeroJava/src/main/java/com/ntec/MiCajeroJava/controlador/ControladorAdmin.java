package com.ntec.MiCajeroJava.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ntec.MiCajeroJava.identidad.Cliente;
import com.ntec.MiCajeroJava.identidad.Cuenta;
import com.ntec.MiCajeroJava.identidad.TipoCuenta;
import com.ntec.MiCajeroJava.servicios.ServicioCliente;
import com.ntec.MiCajeroJava.servicios.ServicioCuenta;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin") // Todo lo que empieza con /admin es territorio del Jefe Supremo 💼
public class ControladorAdmin {

    private final ServicioCliente servicioCliente;
    private final ServicioCuenta servicioCuenta;

    @GetMapping
    public String adminHome() {
        // Redirige al centro de mando del admin
        return "admin/index";
    }

    @GetMapping("/crear-cliente")
    public String mostrarFormularioCliente(Model model) {
        model.addAttribute("cliente", new Cliente()); // Cliente fresh salido del horno
        return "admin/crear-cliente";
    }

    @PostMapping("/crear-cliente")
    public String crearCliente(@ModelAttribute Cliente cliente) {
        servicioCliente.crearCliente(cliente); // Reclutando compas para el banco
        return "redirect:/admin";
    }

    @GetMapping("/crear-cuenta")
    public String mostrarFormularioCuenta(Model model) {
        model.addAttribute("cuenta", new Cuenta()); // Preparando cuenta mágica
        return "admin/crear-cuenta";
    }

    @PostMapping("/crear-cuenta")
    public String crearCuenta(
        @RequestParam String identificacion,
        @RequestParam String numero,
        @RequestParam TipoCuenta tipo,
        @RequestParam double saldo
    ) {
        Cliente cliente = servicioCliente
                .buscarPorIdentificacion(identificacion)
                .orElseThrow(() -> new RuntimeException("No se encontró al compa 😵"));

        servicioCuenta.crearCuenta(cliente, numero, tipo, saldo); // Cuenta creada con facha bancaria
        return "redirect:/admin";
    }

    @GetMapping("/desbloquear")
    public String mostrarDesbloqueo() {
        return "admin/desbloquear"; // Liberación de cuentas bloqueadas 🧙
    }

    @PostMapping("/desbloquear")
    public String desbloquearCuenta(
        @RequestParam String identificacion,
        @RequestParam String nuevoPIN
    ) {
        servicioCliente.desbloquearCliente(identificacion, nuevoPIN); // PIN reseteado con éxito
        return "redirect:/admin";
    }
}
