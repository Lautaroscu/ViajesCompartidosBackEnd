package com.viajes.viajesCompartidos.DTO.payments;

import java.io.Serializable;

public class TransferRequest implements Serializable {

    private Origin origen;
    private Destination destino;
    private String importe;
    private String moneda;
    private String motivo;
    private String referencia;

    // Getters y setters
    public Origin getOrigen() {
        return origen;
    }

    public void setOrigen(Origin origen) {
        this.origen = origen;
    }

    public Destination getDestino() {
        return destino;
    }

    public void setDestino(Destination destino) {
        this.destino = destino;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    // Inner classes para origen y destino
    private static class Origin {
        private String cbu;

        // Getters y setters
        public String getCbu() {
            return cbu;
        }

        public void setCbu(String cbu) {
            this.cbu = cbu;
        }
    }

    private static class Destination {
        private String cbu;
        private String alias;

        private boolean esMismoTitular;

        // Getters y setters
        public String getCbu() {
            return cbu;
        }

        public void setCbu(String cbu) {
            this.cbu = cbu;
        }

        public String getAlias() {
            return alias;
        }

        public void setAlias(String alias) {
            this.alias = alias;
        }

        public boolean isEsMismoTitular() {
            return esMismoTitular;
        }

        public void setEsMismoTitular(boolean esMismoTitular) {
            this.esMismoTitular = esMismoTitular;
        }
    }
}
