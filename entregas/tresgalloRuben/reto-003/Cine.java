import java.time.LocalDate;

public class Cine {
    public Pelicula pelicula;
    public Ticket ticket;
    public Visitante visitante;
    public LocalDate apertura;
    public LocalDate cierre;

    public Cine(Pelicula pelicula, Visitante visitante, Ticket ticket, LocalDate apertura, LocalDate cierre) {
        this.pelicula = pelicula;
        this.visitante = visitante;
        this.ticket = ticket;
        this.apertura = apertura;
        this.cierre = cierre;
    }

    public boolean abierto() {
        LocalDate hoy = LocalDate.now();
        return !hoy.isBefore(apertura) && !hoy.isAfter(cierre);
    }
}