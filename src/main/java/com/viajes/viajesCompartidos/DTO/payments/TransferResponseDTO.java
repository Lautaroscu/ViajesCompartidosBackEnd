package com.viajes.viajesCompartidos.DTO.payments;

import java.io.Serializable;
import java.time.LocalDateTime;

public class TransferResponseDTO implements Serializable {
    private String numeroReferenciaBancaria;
    private LocalDateTime fechaOperacion;

    public TransferResponseDTO(String numeroReferenciaBancaria, LocalDateTime fechaOperacion) {
        this.numeroReferenciaBancaria = numeroReferenciaBancaria;
        this.fechaOperacion = fechaOperacion;
    }
    public String getNumeroReferenciaBancaria() {
        return numeroReferenciaBancaria;
    }
    public void setNumeroReferenciaBancaria(String numeroReferenciaBancaria) {
        this.numeroReferenciaBancaria = numeroReferenciaBancaria;
    }
    public LocalDateTime getFechaOperacion() {
        return fechaOperacion;
    }
    public void setFechaOperacion(LocalDateTime fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
        this.fechaOperacion = fechaOperacion;
    }
}
