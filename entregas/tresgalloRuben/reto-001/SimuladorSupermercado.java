public class SimuladorSupermercado {
    
    private static final int NUMERO_DE_CAJAS = 5;
    private static final int DURACION_JORNADA_MINUTOS = 12 * 60;
    private static final int PROBABILIDAD_LLEGADA_CLIENTE = 40;
    private static final int OBJETOS_MINIMOS = 5;
    private static final int OBJETOS_RANGO = 11;
    private static final int UMBRAL_ACTIVACION_CAJA_EXTRA = 15;

    private int[] clientesPorCaja;
    private int[] objetosPendientes;
    private boolean[] cajaLibre;
    private int clientesEnCola;
    private int minutosSinCola;
    private int totalObjetosVendidos;
    private boolean cajaExtraActiva;

    public SimuladorSupermercado() {
        clientesPorCaja = new int[NUMERO_DE_CAJAS];
        objetosPendientes = new int[NUMERO_DE_CAJAS];
        cajaLibre = new boolean[NUMERO_DE_CAJAS];
        
        inicializarCajas();
    }

    private void inicializarCajas() {
        for (int i = 0; i < NUMERO_DE_CAJAS; i++) {
            clientesPorCaja[i] = 0;
            objetosPendientes[i] = 0;
            cajaLibre[i] = true;
        }
        clientesEnCola = 0;
        minutosSinCola = 0;
        totalObjetosVendidos = 0;
        cajaExtraActiva = false;
    }

    public void ejecutarSimulacion() {
        for (int minutoActual = 1; minutoActual < DURACION_JORNADA_MINUTOS; minutoActual++) {
            boolean llegaCliente = procesarLlegadaCliente();
            procesarCajas();
            actualizarContadorColaSinClientes();
            mostrarEstadoMinuto(minutoActual, llegaCliente);
            actualizarEstadoCajaExtra();
        }
        mostrarResumenFinal();
    }

    private boolean procesarLlegadaCliente() {
        boolean llegaCliente = (Math.random() * 100) < PROBABILIDAD_LLEGADA_CLIENTE;
        
        if (llegaCliente) {
            clientesEnCola++;
            asignarClienteACajaLibre();
        }
        
        return llegaCliente;
    }

    private void asignarClienteACajaLibre() {
        for (int indiceCaja = 0; indiceCaja < NUMERO_DE_CAJAS; indiceCaja++) {
            boolean esCajaExtra = (indiceCaja == NUMERO_DE_CAJAS - 1);
            
            if (cajaLibre[indiceCaja] && (!esCajaExtra || cajaExtraActiva)) {
                clientesPorCaja[indiceCaja]++;
                cajaLibre[indiceCaja] = false;
                objetosPendientes[indiceCaja] = generarObjetosAleatorios();
                totalObjetosVendidos += objetosPendientes[indiceCaja];
                clientesEnCola--;
                break;
            }
        }
    }

    private int generarObjetosAleatorios() {
        return ((int) (Math.random() * 100)) % OBJETOS_RANGO + OBJETOS_MINIMOS;
    }

    private void procesarCajas() {
        for (int indiceCaja = 0; indiceCaja < NUMERO_DE_CAJAS; indiceCaja++) {
            if (objetosPendientes[indiceCaja] > 0) {
                objetosPendientes[indiceCaja]--;
                
                if (objetosPendientes[indiceCaja] == 0) {
                    cajaLibre[indiceCaja] = true;
                }
            }
        }
    }

    private void actualizarContadorColaSinClientes() {
        if (clientesEnCola == 0) {
            minutosSinCola++;
        }
    }

    private void actualizarEstadoCajaExtra() {
        cajaExtraActiva = (clientesEnCola >= UMBRAL_ACTIVACION_CAJA_EXTRA);
    }

    private void mostrarEstadoMinuto(int minuto, boolean llegaCliente) {
        System.out.println("----");
        System.out.print("MINUTO " + minuto);
        
        int personasLlegadas = llegaCliente ? 1 : 0;
        System.out.println(" - Llega " + personasLlegadas + " persona - En cola: " + clientesEnCola);
        
        mostrarEstadoCajas();
    }

    private void mostrarEstadoCajas() {
        StringBuilder estadoCajas = new StringBuilder();
        
        for (int i = 0; i < NUMERO_DE_CAJAS - 1; i++) {
            estadoCajas.append(" Caja").append(i + 1).append(":[").append(objetosPendientes[i]).append("]");
            if (i < NUMERO_DE_CAJAS - 2) {
                estadoCajas.append(" |");
            }
        }
        
        System.out.print(estadoCajas);
        
        int indiceCajaExtra = NUMERO_DE_CAJAS - 1;
        if (objetosPendientes[indiceCajaExtra] > 0 || cajaExtraActiva) {
            System.out.println(" Caja" + NUMERO_DE_CAJAS + ":[" + objetosPendientes[indiceCajaExtra] + "] ");
        } else {
            System.out.println();
        }
    }

    private void mostrarResumenFinal() {
        System.out.println("\n=== RESUMEN DEL DÍA ===");
        
        for (int i = 0; i < NUMERO_DE_CAJAS; i++) {
            System.out.println("Clientes totales que pasan por la caja " + (i + 1) + ": " + clientesPorCaja[i]);
        }
        
        int totalClientes = calcularTotalClientes();
        
        System.out.println("Personas que han pasado por la tienda: " + totalClientes);
        System.out.println("Hoy se han vendido " + totalObjetosVendidos + " productos");
        System.out.println("La cola ha estado vacía durante " + minutosSinCola + " minutos");
        System.out.println("Clientes en la cola al finalizar el día: " + clientesEnCola);
    }

    private int calcularTotalClientes() {
        int total = 0;
        for (int clientes : clientesPorCaja) {
            total += clientes;
        }
        return total;
    }

    public static void main(String[] args) {
        SimuladorSupermercado simulador = new SimuladorSupermercado();
        simulador.ejecutarSimulacion();
    }
}
