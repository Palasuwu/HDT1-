/**
 * author: Jose Sanchez
 * author: Jorge Palacios
 */




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Clase principal que representa la interfaz gráfica de una radio digital.
 */
public class RadioGUI extends JFrame {
    private Radio radio;
    private JButton btnEncenderApagar;
    private JButton btnCambiarAM;
    private JButton btnCambiarFM;
    private JButton btnSubirDial;
    private JButton btnBajarDial;
    private JButton btnGuardar;
    private JButton[] btnPresets;

    private JTextArea displayArea;

    /**
     * Constructor de la clase RadioGUI.
     */
    public RadioGUI() {
        radio = new Radio();
        initComponents();
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     */
    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Radio Digital");

        // Botones de control
        btnEncenderApagar = new JButton("Encender / Apagar");
        btnCambiarAM = new JButton("Cambiar a AM");
        btnCambiarFM = new JButton("Cambiar a FM");
        btnSubirDial = new JButton("Subir Dial");
        btnBajarDial = new JButton("Bajar Dial");
        btnGuardar = new JButton("Guardar");

        // Botones de presets
        btnPresets = new JButton[12];
        for (int i = 0; i < 12; i++) {
            btnPresets[i] = new JButton("P" + (i + 1));
            btnPresets[i].setFont(new Font("Arial", Font.PLAIN, 10)); // Fuente más pequeña
        }

        // Área de visualización
        displayArea = new JTextArea();
        displayArea.setEditable(false);

        // Panel principal
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de control
        JPanel controlPanel = new JPanel(new GridLayout(3, 2));
        controlPanel.add(btnEncenderApagar);
        controlPanel.add(btnCambiarAM);
        controlPanel.add(btnCambiarFM);
        controlPanel.add(btnSubirDial);
        controlPanel.add(btnBajarDial);
        controlPanel.add(btnGuardar);

        // Panel de presets
        JPanel presetsPanel = new JPanel(new GridLayout(2, 6));
        for (int i = 0; i < 12; i++) {
            presetsPanel.add(btnPresets[i]);
        }

        // Agregar componentes al panel principal
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(presetsPanel, BorderLayout.CENTER);
        panel.add(displayArea, BorderLayout.SOUTH);

        // Agregar el panel principal al JFrame
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel);

        pack();
        setLocationRelativeTo(null);

        // Agregar ActionListeners
        btnEncenderApagar.addActionListener(new ActionListener() {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e) {
                radio.toggleEncendidoApagado();
                actualizarDisplay();
            }
        });

        btnCambiarAM.addActionListener(new ActionListener() {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e) {
                radio.setFrecuencia(Radio.Frecuencia.AM);
                actualizarDisplay();
            }
        });

        btnCambiarFM.addActionListener(new ActionListener() {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e) {
                radio.setFrecuencia(Radio.Frecuencia.FM);
                actualizarDisplay();
            }
        });

        btnSubirDial.addActionListener(new ActionListener() {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e) {
                radio.subirDial();
                actualizarDisplay();
            }
        });

        btnBajarDial.addActionListener(new ActionListener() {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e) {
                radio.bajarDial();
                actualizarDisplay();
            }
        });

        btnGuardar.addActionListener(new ActionListener() {
            /**
             * {@inheritDoc}
             */
            public void actionPerformed(ActionEvent e) {
                String preset = JOptionPane.showInputDialog("Ingrese el número de botón (1-12):");
                try {
                    int numBoton = Integer.parseInt(preset);
                    radio.guardarEmisora(numBoton);
                    actualizarDisplay();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese un número válido.");
                }
            }
        });

        for (int i = 0; i < 12; i++) {
            final int numBoton = i + 1;
            btnPresets[i].addActionListener(new ActionListener() {
                /**
                 * {@inheritDoc}
                 */
                public void actionPerformed(ActionEvent e) {
                    radio.seleccionarEmisora(numBoton);
                    actualizarDisplay();
                }
            });
        }
    }

    /**
     * Actualiza el área de visualización con el estado actual de la radio.
     */
    private void actualizarDisplay() {
        String estado = "Estado actual de la radio:\n";
        estado += "Encendido: " + (radio.estaEncendida() ? "Sí" : "No") + "\n";
        if (radio.estaEncendida()) {
            estado += "Frecuencia: " + (radio.getFrecuencia() == Radio.Frecuencia.AM ? "AM" : "FM") + "\n";
            estado += "Emisora actual: " + radio.getEmisora() + "\n";
            estado += "Botones guardados: " + Arrays.toString(radio.getBotones()) + "\n";
        }
        displayArea.setText(estado);
    }

    /**
     * Método principal que inicia la aplicación.
     *
     * @param args Argumentos de la línea de comandos (no se utilizan).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            /**
             * {@inheritDoc}
             */
            public void run() {
                RadioGUI radioGUI = new RadioGUI();
                radioGUI.setVisible(true);
            }
        });
    }

    /**
     * Clase interna que representa la lógica de la radio.
     */
    static class Radio {
        enum Frecuencia { AM, FM }

        private boolean encendida;
        private Frecuencia frecuencia;
        private double emisora;
        private double[] botones;

        /**
         * Constructor de la clase Radio.
         */
        public Radio() {
            encendida = false;
            frecuencia = Frecuencia.AM;
            emisora = 530; // Emisora AM inicial
            botones = new double[12];
        }

        /**
         * Alterna el estado de encendido/apagado de la radio.
         */
        public void toggleEncendidoApagado() {
            encendida = !encendida;
            if (!encendida) {
                emisora = frecuencia == Frecuencia.AM ? 530 : 87.5; // Resetear emisora al apagar
            }
        }

        /**
         * Verifica si la radio está encendida.
         *
         * @return true si la radio está encendida, false de lo contrario.
         */
        public boolean estaEncendida() {
            return encendida;
        }

        /**
         * Establece la frecuencia de la radio (AM o FM).
         *
         * @param frecuencia La frecuencia a establecer.
         */
        public void setFrecuencia(Frecuencia frecuencia) {
            this.frecuencia = frecuencia;
        }

        /**
         * Obtiene la frecuencia actual de la radio.
         *
         * @return La frecuencia actual (AM o FM).
         */
        public Frecuencia getFrecuencia() {
            return frecuencia;
        }

        /**
         * Obtiene la emisora actual sintonizada en la radio.
         *
         * @return La emisora actual.
         */
        public double getEmisora() {
            return emisora;
        }

        /**
         * Sube el dial de la radio, cambiando la emisora.
         */
        public void subirDial() {
            emisora += (frecuencia == Frecuencia.AM) ? 10 : 0.2;
            if (emisora > ((frecuencia == Frecuencia.AM) ? 1700 : 108)) {
                emisora = (frecuencia == Frecuencia.AM) ? 530 : 87.5;
            }
        }

        /**
         * Baja el dial de la radio, cambiando la emisora.
         */
        public void bajarDial() {
            emisora -= (frecuencia == Frecuencia.AM) ? 10 : 0.2;
            if (emisora < ((frecuencia == Frecuencia.AM) ? 530 : 87.5)) {
                emisora = (frecuencia == Frecuencia.AM) ? 1700 : 108;
            }
        }

        /**
         * Guarda la emisora actual en el botón especificado.
         *
         * @param numBoton Número del botón donde se guardará la emisora.
         */
        public void guardarEmisora(int numBoton) {
            if (encendida) {
                botones[numBoton - 1] = emisora;
            }
        }

        /**
         * Selecciona la emisora guardada en el botón especificado.
         *
         * @param numBoton Número del botón desde el cual se seleccionará la emisora.
         */
        public void seleccionarEmisora(int numBoton) {
            if (encendida && botones[numBoton - 1] != 0) {
                emisora = botones[numBoton - 1];
            }
        }

        /**
         * Obtiene las emisoras guardadas en los botones.
         *
         * @return Un array con las emisoras guardadas.
         */
        public double[] getBotones() {
            return botones;
        }
    }
}
