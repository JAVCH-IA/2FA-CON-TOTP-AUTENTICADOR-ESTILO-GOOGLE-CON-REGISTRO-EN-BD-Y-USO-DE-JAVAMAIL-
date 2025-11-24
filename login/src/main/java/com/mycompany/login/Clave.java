package com.mycompany.login;

import com.formdev.flatlaf.FlatLightLaf;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.Random;

public class Clave extends JFrame {

    private static final String CARACTERES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final Random random = new Random();

    private JTextField correoField;
    private JButton enviarButton;
    private JTextArea statusArea;
    private BufferedImage fondoImagen;
    private String remitente;
    private String password;
    private String codigoVerificacionActual;

    public Clave() {
        setTitle("Registro - Enviar Código de Verificación");
        setSize(420, 280);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        cargarImagenFondo();
        cargarCredenciales();
        initComponents();
    }

    private void cargarImagenFondo() {
        try {
            fondoImagen = ImageIO.read(new File("fondo.jpg"));
        } catch (IOException e) {
            System.err.println("No se pudo cargar la imagen de fondo: " + e.getMessage());
            fondoImagen = null;
        }
    }

    private void cargarCredenciales() {
        try (BufferedReader br = new BufferedReader(new FileReader("credenciales.txt"))) {
            remitente = br.readLine();
            password = br.readLine();
            if (remitente != null && password != null) {
                System.out.println("Credenciales cargadas correctamente.");
            } else {
                System.err.println("Archivo credenciales.txt incompleto.");
            }
        } catch (IOException e) {
            System.err.println("Error al leer credenciales: " + e.getMessage());
        }
    }

    class PanelConFondo extends JPanel {
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (fondoImagen != null) {
                g.drawImage(fondoImagen, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    private void initComponents() {
        PanelConFondo panel = new PanelConFondo();
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        JPanel inputPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 150));
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                super.paintComponent(g);
            }
        };
        inputPanel.setOpaque(false);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE),
                "Ingrese su correo electrónico"));
        ((TitledBorder) inputPanel.getBorder()).setTitleColor(Color.WHITE);

        JLabel labelCorreo = new JLabel("Correo electrónico:");
        labelCorreo.setForeground(Color.WHITE);

        correoField = new JTextField();
        correoField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 28));
        correoField.setForeground(Color.WHITE);
        correoField.setOpaque(false);
        correoField.setBackground(new Color(0, 0, 0, 150));
        correoField.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));

        inputPanel.add(labelCorreo);
        inputPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        inputPanel.add(correoField);

        enviarButton = new JButton("Enviar Código");
        enviarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        enviarButton.setPreferredSize(new Dimension(140, 35));
        enviarButton.addActionListener(e -> enviarCodigo());

        statusArea = new JTextArea(7, 35);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);
        statusArea.setForeground(Color.WHITE);
        statusArea.setOpaque(false);
        statusArea.setBackground(new Color(0, 0, 0, 150));

        JScrollPane scroll = new JScrollPane(statusArea);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(enviarButton);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(scroll);

        setContentPane(panel);
    }

    public static String generarCodigo(int longitud) {
        StringBuilder codigo = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(CARACTERES.length());
            codigo.append(CARACTERES.charAt(indice));
        }
        return codigo.toString();
    }

    private void guardarCorreoEnArchivo(String correo) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("correos_registrados.txt", true))) {
            LocalDateTime ahora = LocalDateTime.now();
            DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String fechaHora = ahora.format(formato);
            writer.write(correo + " - Enviado el: " + fechaHora);
            writer.newLine();
            statusArea.append("Correo guardado en archivo con fecha y hora.\n");
        } catch (IOException e) {
            statusArea.append("Error al guardar el correo: " + e.getMessage() + "\n");
        }
    }

    public void enviarCorreo(String destinatario, String asunto, String mensajeTexto) {
        if (remitente == null || password == null || remitente.isEmpty() || password.isEmpty()) {
            statusArea.append("Credenciales no configuradas correctamente.\n");
            return;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(remitente, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(mensajeTexto);

            Transport.send(message);
            statusArea.append("Correo enviado correctamente a: " + destinatario + "\n");
            guardarCorreoEnArchivo(destinatario);
        } catch (MessagingException e) {
            statusArea.append("Error al enviar correo: " + e.getMessage() + "\n");
        }
    }

    private void enviarCodigo() {
        String correoDestino = correoField.getText().trim();
        if (correoDestino.isEmpty() || !correoDestino.contains("@")) {
            JOptionPane.showMessageDialog(this, "Por favor, ingresa un correo válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        codigoVerificacionActual = generarCodigo(8);
        String asunto = "Tu código de verificación";
        String mensaje = "Tu código de verificación es: " + codigoVerificacionActual;
        enviarCorreo(correoDestino, asunto, mensaje);

        String codigoIngresado = JOptionPane.showInputDialog(this, "Ingresa el código de verificación enviado a tu correo:");

        if (codigoIngresado != null && codigoIngresado.equals(codigoVerificacionActual)) {
            JOptionPane.showMessageDialog(this, "Código correcto. Acceso concedido.");

            // Cerrar ventana actual
            this.dispose();

            // Crear ventana de bienvenida con mismo fondo
            JFrame bienvenidaFrame = new JFrame("Bienvenido");
            bienvenidaFrame.setSize(420, 280);
            bienvenidaFrame.setLocationRelativeTo(null);
            bienvenidaFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            JPanel panelBienvenida = new JPanel() {
                BufferedImage img;
                {
                    try {
                        img = ImageIO.read(new File("fondo.jpg"));
                    } catch (IOException e) {
                        img = null;
                    }
                }
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    if (img != null) {
                        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                    }
                }
            };
            panelBienvenida.setLayout(new GridBagLayout());

            JLabel mensajeBienvenida = new JLabel("Bienvenido, has iniciado sesión correctamente.");
            mensajeBienvenida.setFont(new Font("Arial", Font.BOLD, 16));
            mensajeBienvenida.setForeground(Color.WHITE);
            panelBienvenida.add(mensajeBienvenida);

            bienvenidaFrame.setContentPane(panelBienvenida);
            bienvenidaFrame.setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this, "Código incorrecto. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("No se pudo aplicar FlatLaf: " + ex.getMessage());
        }
        SwingUtilities.invokeLater(() -> new Clave().setVisible(true));
    }
}
